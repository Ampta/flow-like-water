# Chapter 14: Angular Frontend, RxJS & Fullstack Integration

This guide covers real-world Angular and frontend integration interview questions asked across **Tansbank, In Solutions Global, Servosys, NeoSoft, Saint Gobain, and Echofeel**.

---

## 📑 Table of Contents
1. [Angular Architecture & Component Lifecycle Hooks](#1-angular-architecture--component-lifecycle-hooks)
2. [Component Communication: Parent-Child, ViewChild & Shared Services](#2-component-communication-parent-child-viewchild--shared-services)
3. [RxJS Observables vs Promises & Memory Leak Prevention](#3-rxjs-observables-vs-promises--memory-leak-prevention)
4. [Debouncing API Calls in Search Autocomplete (`debounceTime`)](#4-debouncing-api-calls-in-search-autocomplete-debouncetime)
5. [Angular HTTP Interceptors: Attaching JWT & Global Error Handling](#5-angular-http-interceptors-attaching-jwt--global-error-handling)
6. [AOT vs JIT Compilation & Angular Performance Optimization](#6-aot-vs-jit-compilation--angular-performance-optimization)
7. [Route Guards, Navigation State & Fullstack Spring Boot Integration](#7-route-guards-navigation-state--fullstack-spring-boot-integration)

---

## 1. Angular Architecture & Component Lifecycle Hooks

### 1. Previous Knowledge Required
- Single Page Applications (SPA), TypeScript, Document Object Model (DOM).

### 2. Logic & Theory (Deep-Dive)

```
      Constructor (Dependency Injection only, DOM not ready)
            |
            v
      ngOnChanges (Fires when @Input() properties change)
            |
            v
      ngOnInit (Component initialized, perfect for API calls)
            |
            v
      ngDoCheck (Custom change detection)
            |
            v
      ngAfterContentInit / ngAfterContentChecked
            |
            v
      ngAfterViewInit (Component's template and child views rendered)
            |
            v
      ngAfterViewChecked
            |
            v
      ngOnDestroy (Cleanup: Unsubscribe Observables, clear intervals)
```

| Lifecycle Hook | Purpose | Real-World Use Case |
| :--- | :--- | :--- |
| `ngOnInit()` | Component setup after `@Input()` values are initially resolved. | Triggering initial HTTP requests (`loanService.getApplications()`). |
| `ngOnChanges()` | Executes when `@Input()` data-bound properties change. | Recomputing loan EMI calculations when interest rate changes. |
| `ngAfterViewInit()`| Fires after the component template and child views are fully loaded. | Accessing child components via `@ViewChild` or initializing Chart.js / canvas. |
| `ngOnDestroy()` | Invoked right before Angular destroys the component. | Calling `subscription.unsubscribe()` or `destroy$.next()` to prevent memory leaks. |

### 3. How to Answer in an Interview
> *"Angular is an opinionated, enterprise-ready TypeScript framework providing dependency injection, two-way data binding, routing, and form validation out-of-the-box.*
>
> *A component's lifecycle starts with `ngOnChanges`, followed by `ngOnInit`, where we initiate backend REST calls. Once child views render, `ngAfterViewInit` fires, which is where we interact with `@ViewChild` references. Finally, `ngOnDestroy` is crucial in production for unsubscribing from RxJS streams to prevent memory leaks."*

---

## 2. Component Communication: Parent-Child, ViewChild & Shared Services

### 1. Previous Knowledge Required
- Component tree hierarchy, unidirectional data flow.

### 2. Logic & Theory (Deep-Dive)

#### 1. Parent to Child (`@Input`) & Child to Parent (`@Output` + `EventEmitter`)
Used when components have a direct parent-child relationship in the template hierarchy:
```typescript
// Child Component
@Component({
  selector: 'app-kyc-verify',
  template: `<button (click)="notifyParent()">Verify Aadhaar</button>`
})
export class KycVerifyComponent {
  @Input() aadhaarNumber: string = '';
  @Output() verificationStatus = new EventEmitter<boolean>();

  notifyParent() {
    this.verificationStatus.emit(true);
  }
}
```

#### 2. Unrelated / Sibling Components: Shared Service with RxJS `BehaviorSubject`
When two components share no direct parent-child relationship (e.g., Header navigation and Loan Dashboard):
```typescript
@Injectable({ providedIn: 'root' })
export class LoanEventBusService {
  // Holds current state and emits to new subscribers immediately
  private loanStatusSubject = new BehaviorSubject<string>('DRAFT');
  public loanStatus$ = this.loanStatusSubject.asObservable();

  updateStatus(newStatus: string) {
    this.loanStatusSubject.next(newStatus);
  }
}
```

#### 3. Accessing Child via `@ViewChild`
Allows a parent component to programmatically invoke public methods or inspect child component instances.

---

## 3. RxJS Observables vs Promises & Memory Leak Prevention

### 1. Previous Knowledge Required
- Asynchronous streams, Push vs Pull architectures.

### 2. Logic & Theory (Deep-Dive)

| Feature | Promise | RxJS Observable |
| :--- | :--- | :--- |
| **Emissions** | Resolves exactly **one single value** (or rejects). | Emits **zero, one, or multiple values over time**. |
| **Execution** | **Eager**: Starts executing immediately upon creation. | **Lazy**: Does not execute until `.subscribe()` is called! |
| **Cancelability** | **Cannot be cancelled** natively once started. | **Cancelable** via `.unsubscribe()` or `takeUntil()`. |
| **Operators** | Only `.then()`, `.catch()`, `.finally()`. | Rich ecosystem: `map`, `filter`, `switchMap`, `debounceTime`, `retry`. |

#### How to Avoid Memory Leaks in Angular:
Never leave subscriptions hanging. Use the `takeUntilDestroyed()` operator (Angular 16+) or `takeUntil`:
```typescript
@Component({...})
export class DashboardComponent {
  private destroyRef = inject(DestroyRef);
  private loanService = inject(LoanService);

  ngOnInit() {
    this.loanService.getActiveLoans()
      .pipe(takeUntilDestroyed(this.destroyRef)) // Automatically unsubscribes on destroy!
      .subscribe(data => this.loans = data);
  }
}
```

---

## 4. Debouncing API Calls in Search Autocomplete (`debounceTime`)

### 1. Interview Question (Tansbank):
*"A user is typing in a search box. If they type quickly, 20 API calls will hammer the backend. How do you implement debouncing in Angular?"*

### 2. Logic & Solution
Use RxJS `debounceTime(300)` combined with `distinctUntilChanged()` and `switchMap()`.
- `debounceTime(300)`: Waits 300ms of user silence before emitting the search term.
- `distinctUntilChanged()`: Prevents duplicate API calls if the user types and deletes the same character.
- `switchMap()`: Cancels any pending in-flight HTTP request if a new search term arrives!

```typescript
@Component({
  selector: 'app-customer-search',
  template: `<input [formControl]="searchControl" placeholder="Search customer by PAN..." />`
})
export class CustomerSearchComponent implements OnInit {
  searchControl = new FormControl('');
  results$!: Observable<CustomerDTO[]>;

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.results$ = this.searchControl.valueChanges.pipe(
      debounceTime(350), // Wait 350ms pause in typing
      distinctUntilChanged(), // Ignore if value is identical
      filter(term => (term ?? '').length >= 3), // Trigger only if 3+ chars
      switchMap(term => this.customerService.searchByPan(term!)) // Discard stale requests!
    );
  }
}
```

---

## 5. Angular HTTP Interceptors: Attaching JWT & Global Error Handling

### 1. Logic & Theory (Deep-Dive)
An `HttpInterceptor` acts as a middleware for all outgoing HTTP requests and incoming HTTP responses.
1. Intercepts outgoing requests to clone and append the `Authorization: Bearer <token>` header.
2. Intercepts incoming responses to handle `401 Unauthorized` by triggering a token refresh or redirecting to `/login`.

### 2. Practical Code Example (Modern Functional Interceptor in Angular 15+)
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getAccessToken();

  let clonedReq = req;
  if (token) {
    clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
```

---

## 6. AOT vs JIT Compilation & Angular Performance Optimization

### 1. AOT (Ahead-of-Time) vs JIT (Just-in-Time)
- **JIT**: The browser downloads the application TypeScript/HTML and the Angular compiler itself, compiling templates inside the browser at runtime. Slower initial page load and exposes source template vulnerabilities.
- **AOT**: Compiles templates and TypeScript into optimized JavaScript during the build process (`ng build`). The Angular compiler is NOT shipped to the browser, cutting bundle size by 50% and catching template errors at compile time.

### 2. How to Boost Angular Performance in Production:
1. **ChangeDetectionStrategy.OnPush**: Tells Angular to skip checking the component unless its `@Input()` reference changes or an Observable emits.
2. **Lazy Loading Routes**: Use `loadChildren` or `loadComponent` so modules are only downloaded when the user navigates to that screen.
3. **`trackBy` in `*ngFor`**: Prevents recreating entire DOM lists on updates by tracking unique IDs.
4. **Pure Pipes**: Caches output results for identical input parameters.

---

## 7. Route Guards, Navigation State & Fullstack Spring Boot Integration

### 1. Route Guard for Role-Based Access
```typescript
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.hasRole('ROLE_ADMIN')) {
    return true;
  }
  router.navigate(['/unauthorized']);
  return false;
};
```

### 2. Passing Data via Navigation State
```typescript
// Sending Component
this.router.navigate(['/loan-summary'], {
  state: { loanData: selectedLoan }
});

// Receiving Component (in constructor or ngOnInit)
constructor(private router: Router) {
  const navigation = this.router.getCurrentNavigation();
  this.loan = navigation?.extras.state?.['loanData'];
}
```

---

*Continue to [Chapter 15: Real-World LOS Project Architecture & Scenario Questions](file:///Users/apple/Workspace/Home/Javascript/Learning/15_Real_World_Project_LOS_and_Scenario_Interviews.md)*
