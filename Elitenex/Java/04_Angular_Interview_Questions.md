# 🅰️ Top 20 Angular Framework Interview Questions & Answers

---

### Q1: What is Angular, and what are its core architectural building blocks?
**Answer:**
Angular is a TypeScript-based, open-source, component-driven client-side application framework developed and maintained by Google.

**Core Building Blocks:**
1. **Components:** The fundamental UI building block. Consists of a TypeScript class (logic), an HTML template (view), and CSS/SCSS styles.
2. **Templates & Data Binding:** HTML enriched with Angular directives, control flow, and property/event bindings.
3. **Directives:** Classes that add behavior or modify DOM elements (Structural, Attribute, and Component directives).
4. **Services & Dependency Injection:** Reusable singleton or scoped classes holding business logic, data access, and state, injected via Angular's hierarchical DI.
5. **Pipes:** Transform data for display directly inside templates (e.g., `date`, `currency`, `uppercase`, custom pipes).
6. **Routing (`@angular/router`):** Enables Single Page Application (SPA) client-side navigation, lazy loading, and route protection.

---

### Q2: What are Standalone Components, and how do they replace `NgModule`?
**Answer:**
Introduced in Angular 14 and made the default in Angular 17+, **Standalone Components** eliminate the need for `NgModule`. Components, pipes, and directives directly declare their own dependencies via the `imports` array in their decorator.

**Benefits:**
- Drastically reduced boilerplate code.
- Simplified mental model for developers.
- Direct lazy-loading of individual components via `loadComponent: () => import(...)`.
- Better tree-shaking and smaller bundle sizes.

```typescript
@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `<h1>{{ user.name }}</h1>`
})
export class UserProfileComponent {
  user = { name: 'Alice' };
}
```

---

### Q3: Walk through the Angular Component Lifecycle Hooks.
**Answer:**
Angular executes lifecycle hooks in a strict sequence:

1. **`constructor()`:** TypeScript class instantiation. Used only for basic dependency injection; never for heavy initialization.
2. **`ngOnChanges(changes: SimpleChanges)`:** Triggered before `ngOnInit()` and whenever `@Input()` data-bound properties change.
3. **`ngOnInit()`:** Invoked once after the first `ngOnChanges`. Ideal place for HTTP calls, event subscriptions, and component initialization.
4. **`ngDoCheck()`:** Invoked during every change detection cycle to detect custom changes that Angular missed.
5. **`ngAfterContentInit()`:** Called once after external content (`<ng-content>`) is projected into the component.
6. **`ngAfterContentChecked()`:** Called after every check of projected content.
7. **`ngAfterViewInit()`:** Called once after component view and child views have initialized. Safe place to access `@ViewChild` DOM elements.
8. **`ngAfterViewChecked()`:** Called after every check of component view and child views.
9. **`ngOnDestroy()`:** Invoked immediately before component destruction. Used to unsubscribe RxJS observables, detach listeners, and prevent memory leaks.
10. **`afterRender` / `afterNextRender` (Angular 16+):** Executes callback after Angular finishes rendering DOM; replaces manual DOM timing hacks.

---

### Q4: How does Change Detection work in Angular? Compare `Default` vs `OnPush`.
**Answer:**
Change Detection is Angular's mechanism to synchronize component state changes with the DOM.
- Traditionally powered by **Zone.js**, which monkey-patches asynchronous browser APIs (`setTimeout`, `fetch`, DOM clicks, Promises) and notifies Angular to run change detection from the root component down the entire component tree.

**Change Detection Strategies:**
| Strategy | Mechanism | When it Runs |
| :--- | :--- | :--- |
| **`ChangeDetectionStrategy.Default`** | Checks **every component in the entire tree** top-to-bottom on any browser event | Mouse click, timer, HTTP call anywhere in application |
| **`ChangeDetectionStrategy.OnPush`** | Skips the component subtree unless explicitly triggered | 1. An `@Input()` reference changes (immutable change)<br>2. An event handler inside the component fires<br>3. An `async` pipe receives a new emission<br>4. Signal updates read in template<br>5. Explicit `cdr.markForCheck()` |

**Best Practice:** Use `OnPush` across all components to eliminate redundant re-rendering cycles and maximize performance.

---

### Q5: What are Angular Signals (Angular 16+), and how do they enable Zoneless Angular?
**Answer:**
A **Signal** is a reactive wrapper around a value that notifies interested consumers when that value changes. Signals provide fine-grained, synchronous reactivity with dependency tracking.

**Three Primitive Types:**
1. **Writable Signal (`signal`):**
```typescript
const count = signal(0);
count.set(5);
count.update(n => n + 1);
```
2. **Computed Signal (`computed`):** Read-only memoized signal that recomputes only when its signal dependencies change.
```typescript
const doubleCount = computed(() => count() * 2);
```
3. **Effect (`effect`):** Runs side-effects (logging, analytics, localStorage) when dependent signals change.
```typescript
effect(() => console.log(`Count changed: ${count()}`));
```

**Zoneless Angular (Angular 18+):**
Signals inform Angular exactly *which specific node* changed in the DOM, allowing Angular to update only that DOM element without running a full top-down tree walk or requiring Zone.js.

---

### Q6: What are Signal-Based Component Inputs, Outputs, and Models (Angular 17.2+)?
**Answer:**
Modern Angular replaces decorator-based `@Input()` and `@Output()` with signal-based primitives:

```typescript
import { Component, input, output, model } from '@angular/core';

@Component({
  selector: 'app-counter',
  standalone: true,
  template: `
    <p>Title: {{ title() }}</p>
    <button (click)="increment()">Value: {{ count() }}</button>
  `
})
export class CounterComponent {
  // Required input signal
  title = input.required<string>();

  // Two-way binding model signal (replaces @Input + @Output pair)
  count = model<number>(0);

  // Modern Output emitter
  clicked = output<number>();

  increment() {
    this.count.update(c => c + 1);
    this.clicked.emit(this.count());
  }
}
```

---

### Q7: Explain the Built-in Control Flow Syntax in Angular 17+ vs Legacy Directives.
**Answer:**
Angular 17 introduced a compiler-native control flow block syntax that replaces structural directives (`*ngIf`, `*ngFor`, `*ngSwitch`), yielding cleaner templates, automatic type narrowing, and better performance.

**Comparison:**
```html
<!-- LEGACY STRUCTURAL DIRECTIVES -->
<div *ngIf="user; else guest">Hello {{ user.name }}</div>
<ng-template #guest>Please login</ng-template>

<ul>
  <li *ngFor="let item of items; trackBy: trackById">{{ item.name }}</li>
</ul>

<!-- MODERN BUILT-IN CONTROL FLOW (Angular 17+) -->
@if (user) {
  <div>Hello {{ user.name }}</div>
} @else {
  <div>Please login</div>
}

<ul>
  @for (item of items; track item.id) {
    <li>{{ item.name }}</li>
  } @empty {
    <li>No records found.</li>
  }
</ul>

@switch (status) {
  @case ('ACTIVE') { <span>Active</span> }
  @case ('PENDING') { <span>Pending</span> }
  @default { <span>Unknown</span> }
}
```

---

### Q8: How does Angular's Hierarchical Dependency Injection work, and what is the `inject()` function?
**Answer:**
Angular's DI system forms a hierarchical tree of injectors matching the component tree.

**Injector Hierarchy:**
1. **Platform Injector:** Shared by multiple Angular applications running in the same browser window.
2. **Root Injector (`providedIn: 'root'`):** Application-wide singleton service. Preferred for almost all services; enables dead-code tree-shaking.
3. **Environment Injector:** Provides dependencies for lazy-loaded route trees.
4. **Element/Component Injector:** Configured via `@Component({ providers: [...] })`. Creates an isolated instance for this component and its descendants.

**Modern `inject()` function (Angular 14+):**
Replaces constructor injection boilerplate:
```typescript
@Component({...})
export class UserComponent {
  private http = inject(HttpClient);
  private userService = inject(UserService);
  private route = inject(ActivatedRoute);
}
```

---

### Q9: Compare Observables, Promises, and Subjects in RxJS.
**Answer:**
| Dimension | Promise | Observable | Subject |
| :--- | :--- | :--- | :--- |
| **Emission** | Single value | Stream of 0 to infinite values | Stream (Multicast) |
| **Execution** | Eager (executes immediately upon creation) | Lazy (does not run until `.subscribe()`) | Eager/Multicast |
| **Cancellation** | Cannot cancel once started | Can cancel anytime via `subscription.unsubscribe()` | Can cancel subscriptions |
| **Operators** | Limited (`.then`, `.catch`) | Rich operator pipeline (`map`, `filter`, `switchMap`) | Uses all RxJS operators |

**RxJS Subject Types:**
- **`Subject`:** Multicast; new subscribers only receive values emitted *after* they subscribed.
- **`BehaviorSubject`:** Requires initial value; gives new subscribers the *latest* emitted value immediately.
- **`ReplaySubject`:** Buffers $N$ previous values and replays them to new subscribers.
- **`AsyncSubject`:** Emits only the final value, and only upon completion.

---

### Q10: Why should you prefer the `async` pipe, and how do you prevent RxJS memory leaks?
**Answer:**
**Why use `async` pipe:**
```html
<div>{{ user$ | async }}</div>
```
1. Automatically subscribes when the template renders.
2. Automatically unsubscribes when the component is destroyed (zero memory leak risk).
3. Automatically marks `OnPush` components for change detection upon new emissions.

**Preventing Memory Leaks in Manual Subscriptions:**
1. **`takeUntilDestroyed()` (Modern Angular 16+):**
```typescript
export class MyComponent {
  private destroyRef = inject(DestroyRef);

  constructor() {
    this.myService.getData()
      .pipe(takeUntilDestroyed()) // Auto-unsubscribes on component destruction!
      .subscribe(data => this.data = data);
  }
}
```
2. **Manual `Subscription.unsubscribe()` in `ngOnDestroy()`.**

---

### Q11: Explain key RxJS Flattening Operators (`switchMap`, `mergeMap`, `concatMap`, `exhaustMap`).
**Answer:**
Used when an outer observable emits values that trigger an inner observable (e.g., user typing triggering HTTP requests):

| Operator | Behavior | Best Used For |
| :--- | :--- | :--- |
| **`switchMap`** | Cancels previous in-flight inner observable if a new outer value arrives | Typeahead/Auto-complete search (ignore stale query results) |
| **`mergeMap`** | Runs all inner observables in parallel concurrently | Independent requests where order does not matter (e.g., batch delete) |
| **`concatMap`** | Queues inner observables in order, waiting for each to finish before starting next | Sequential tasks where order is critical (e.g., chat messages) |
| **`exhaustMap`** | Ignores new incoming values while current inner observable is running | Login / Submit buttons (prevents duplicate clicks/submissions) |

---

### Q12: How does Angular Routing work, and how do you implement Component Lazy Loading?
**Answer:**
Angular's Router maps browser URLs to views using declarative route configurations.

**Modern Standalone Component Lazy Loading:**
```typescript
export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.routes').then(m => m.ADMIN_ROUTES)
  },
  { path: 'user/:id', loadComponent: () => import('./user/user.component').then(m => m.UserComponent) },
  { path: '**', loadComponent: () => import('./not-found.component').then(m => m.NotFoundComponent) }
];
```

**Accessing Route Parameters:**
Enable `withComponentInputBinding()` in `appConfig.ts`:
```typescript
export class UserComponent {
  // Directly binds route parameter `:id` as an input signal!
  id = input.required<string>();
}
```

---

### Q13: What are Route Guards, and how do you write Functional Guards (`CanActivateFn`)?
**Answer:**
Guards control whether a user can navigate to or away from a route.
- `canActivate`: Controls if route can be accessed (authentication/roles).
- `canActivateChild`: Controls child routes.
- `canDeactivate`: Prevents user from leaving page with unsaved form changes.
- `resolve`: Pre-fetches required data before route transition.

**Modern Functional Guard (`CanActivateFn`):**
Class-based guards implementing `CanActivate` are deprecated. Use functional guards:
```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};

// Route definition:
{ path: 'orders', loadComponent: () => import('./orders.component'), canActivate: [authGuard] }
```

---

### Q14: Compare Template-Driven Forms vs Reactive Forms.
**Answer:**
| Dimension | Template-Driven Forms | Reactive Forms |
| :--- | :--- | :--- |
| **Setup** | Defined in HTML template via `ngModel` | Programmatically structured in TS class via `FormGroup`/`FormControl` |
| **Data Model** | Mutable, asynchronous | Immutable, synchronous reactive streams |
| **Form Tracking** | `NgForm`, `ngModel` | `FormGroup`, `FormControl`, `FormArray` |
| **Validation** | Directives in template (`required`, `minlength`) | Pure validator functions (`Validators.required`) |
| **Unit Testing** | Requires template rendering; difficult | Simple unit tests in TypeScript without DOM |
| **Best Used For** | Simple forms, login/contact forms | Complex, dynamic, enterprise enterprise forms |

**Custom Reactive Validator Example:**
```typescript
export function passwordStrengthValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    const hasUpperCase = /[A-Z]/.test(value);
    const hasNumber = /[0-9]/.test(value);
    return hasUpperCase && hasNumber ? null : { passwordWeak: true };
  };
}
```

---

### Q15: How do you configure Modern Functional HTTP Interceptors (`HttpInterceptorFn`)?
**Answer:**
Functional HTTP Interceptors intercept outgoing HTTP requests and incoming HTTP responses.

**JWT Auth & Error Interceptor Example:**
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(authReq).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        inject(Router).navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};

// Provide in app.config.ts:
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
```

---

### Q16: Explain Pure vs Impure Pipes, and why you should avoid calling methods in templates.
**Answer:**
- **Pure Pipe (Default):** Executed only when a **pure change** to the input value is detected (i.e., a change in a primitive value or a new object reference). Results are memoized/cached.
- **Impure Pipe (`pure: false`):** Executed during **every change detection cycle**, regardless of whether inputs changed. Incurs heavy performance penalties.

**Why avoid method calls in templates (`<div>{{ calculateTotal() }}</div>`):**
Angular cannot know if a class method returns a different value without calling it. Under default change detection, the method is re-evaluated **hundreds of times** during mouse movement or keystrokes!
- *Remedy:* Replace with a **Pure Pipe** or an **Angular Signal (`computed()`)**.

---

### Q17: What is Content Projection (`<ng-content>`) and `viewChild()` / `contentChild()`?
**Answer:**
- **Content Projection:** Allows a parent component to inject arbitrary HTML or components into placeholder slots `<ng-content>` inside a child component (multi-slot projection using `select="[header]"`).

**Signal Queries (Angular 17.2+):**
Replaces `@ViewChild` and `@ContentChild` with reactive signals:
```typescript
@Component({
  selector: 'app-video-player',
  standalone: true,
  template: `<video #player src="video.mp4"></video>`
})
export class VideoPlayerComponent {
  // Query element reference via signal
  player = viewChild.required<ElementRef<HTMLVideoElement>>('player');

  play() {
    this.player().nativeElement.play();
  }
}
```

---

### Q18: What are State Management strategies in Angular? When should you use NgRx?
**Answer:**
1. **Component State:** Managed locally with Angular Signals (`signal()`, `computed()`).
2. **Shared Service State (Lightweight):**
   - Injectable service using Signals or `BehaviorSubject`.
   - Ideal for 85% of standard CRUD applications without external libraries.
3. **NgRx Store (Global Redux Pattern):**
   - Implements Action $\rightarrow$ Reducer $\rightarrow$ Selector $\rightarrow$ Effects.
   - *When to use NgRx:*
     - Massive enterprise apps with high data complexity and many concurrent users.
     - State shared across distinct, non-hierarchical modules.
     - Need time-travel debugging (Redux DevTools) and deterministic state undo/redo.
4. **NgRx ComponentStore / SignalStore:** Modern, lightweight alternatives to full global NgRx for managing component-level async state.

---

### Q19: What are the key Performance Optimization techniques in Angular?
**Answer:**
1. **OnPush Change Detection:** Set `ChangeDetectionStrategy.OnPush` on all components.
2. **Deferrable Views (`@defer` in Angular 17+):**
   - Lazily loads components only when visible in viewport or on user interaction:
   ```html
   @defer (on viewport) {
     <app-heavy-chart [data]="chartData" />
   } @placeholder {
     <div>Scroll down to load chart...</div>
   }
   ```
3. **`NgOptimizedImage`:** Enforces responsive images, prevents layout shift (CLS), preloads priority hero images.
4. **Track Expression in `@for`:** Always supply unique identifier (`track item.id`) to avoid recreating DOM nodes.
5. **Route-Level Lazy Loading & Preloading Strategies:** Split JavaScript into chunks; preload in background after app is idle.
6. **Server-Side Rendering (SSR) & Hydration (Angular 17+):** Fast First Contentful Paint (FCP) and SEO with zero-flicker non-destructive hydration.

---

### Q20: How do you write Unit Tests in Angular using `TestBed` and `HttpTestingController`?
**Answer:**
Angular uses Jasmine/Karma or Vitest/Jest for unit testing.

```typescript
describe('UserService', () => {
  let service: UserService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(UserService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify(); // Asserts no outstanding HTTP requests
  });

  it('should fetch user by ID', () => {
    const mockUser = { id: '1', name: 'Bob' };

    service.getUserById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpTesting.expectOne('/api/users/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser); // Simulates response
  });
});
```

