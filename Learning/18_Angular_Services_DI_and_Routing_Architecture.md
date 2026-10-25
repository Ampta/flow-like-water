# Chapter 18: Services, Dependency Injection & Routing Architecture (Questions 16–22)

This guide covers Questions 16 to 22 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
16. [What is a Service in Angular? Why is it used?](#question-16-what-is-a-service-in-angular-why-is-it-used)
17. [Explain the concept of Dependency Injection (DI) in Angular.](#question-17-explain-the-concept-of-dependency-injection-di-in-angular)
18. [How do you create and use a service in Angular?](#question-18-how-do-you-create-and-use-a-service-in-angular)
19. [How do you configure routing in Angular?](#question-19-how-do-you-configure-routing-in-angular)
20. [What is the difference between `RouterLink` and `href`?](#question-20-what-is-the-difference-between-routerlink-and-href)
21. [How can you pass parameters in Angular routes?](#question-21-how-can-you-pass-parameters-in-angular-routes)
22. [What are Route Guards? Name different types of route guards.](#question-22-what-are-route-guards-name-different-types-of-route-guards)

---

## Question 16: What is a Service in Angular? Why is it used?

### 1. Previous Knowledge Required
- Separation of Concerns, Single Responsibility Principle (SRP).
- Reusable stateful vs stateless classes.

### 2. Logic & Theory (Deep-Dive)
A **Service** in Angular is a broad category encompassing any value, function, or feature that an application needs. It is typically a TypeScript class decorated with `@Injectable()` that contains:
1. **Business Logic & Calculations**: Financial formulas, tax rates, data normalization.
2. **Backend API Communication**: Encapsulating HTTP requests (`HttpClient`).
3. **Cross-Component State Management**: Storing logged-in user details, shopping carts, or multi-step wizard state.
4. **Why NOT put logic inside Components?** Components should be thin controllers dedicated to rendering the view and user interactions. Putting HTTP calls or business algorithms inside components bloats them, makes unit testing painful, and prevents reusing that logic across other views.

### 3. How to Answer in an Interview
> *"A Service is a plain TypeScript class decorated with `@Injectable` that encapsulates reusable business logic, external API calls, and cross-component state management.
>
> *We use services to adhere to the Single Responsibility Principle. Components should strictly handle the presentation layer and user interactions. Moving data-fetching and domain logic into services keeps components clean, facilitates code reuse across multiple views, and allows us to easily mock services during unit tests."*

### 4. Three Practical Examples

#### Example 1: Financial Calculation Service (Stateless Domain Logic)
```typescript
@Injectable({ providedIn: 'root' })
export class EmiCalculatorService {
  calculateMonthlyEmi(principal: number, annualRate: number, tenureMonths: number): number {
    const monthlyRate = annualRate / (12 * 100);
    const emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) /
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);
    return Math.round(emi);
  }
}
```

#### Example 2: API Client Service (Stateless HTTP Communication)
```typescript
@Injectable({ providedIn: 'root' })
export class BureauCheckService {
  constructor(private http: HttpClient) {}

  checkCibilScore(panNumber: string): Observable<CibilResponse> {
    return this.http.post<CibilResponse>('/api/v1/bureau/cibil-inquiry', { panNumber });
  }
}
```

#### Example 3: Application State Service (Stateful with BehaviorSubject)
```typescript
@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  setLoggedInUser(user: UserProfile) {
    this.currentUserSubject.next(user);
  }

  logout() {
    this.currentUserSubject.next(null);
  }
}
```

### 5. Real-World Project Application
- In a **Loan Origination System (LOS)**, the `LoanApplicationService` stores the applicant's multi-step progress (Personal Details -> Income Proof -> KYC Docs -> Bank Statements), allowing the user to navigate back and forth between wizard screens without losing form data.

---

## Question 17: Explain the concept of Dependency Injection (DI) in Angular.

### 1. Previous Knowledge Required
- Inversion of Control (IoC).
- Instantiating objects via `new Service()` vs receiving dependencies from a container.

### 2. Logic & Theory (Deep-Dive)
Dependency Injection is a design pattern in which a class requests dependencies from external sources rather than creating them itself.
Angular has a **Hierarchical Dependency Injection** system. When a component requests a service:
1. Angular looks for a provider in the component's own `providers: []` array.
2. If not found, it bubbles up to the parent component injector.
3. If not found, it checks the route / environment injector (`provideRouter`, lazy-loaded module).
4. If not found, it checks the `RootInjector` (configured via `@Injectable({ providedIn: 'root' })`).
5. If not found anywhere, it throws a `NullInjectorError: No provider for MyService!`.

```
                      [Platform Injector] (DOM, Zone.js)
                               |
                               v
                        [Root Injector]  (@Injectable({ providedIn: 'root' }))
                               |
                               v
                     [Environment Injector] (Routes / Modules)
                               |
                               v
                     [Parent Component Injector] (providers: [MyService])
                               |
                               v
                     [Child Component Injector]
```

#### Tree-Shaking with `providedIn: 'root'`:
Declaring `@Injectable({ providedIn: 'root' })` makes the service **Tree-Shakable**. If your application never actually imports or uses the service, Webpack/Esbuild will completely remove it from the final production bundle, saving bandwidth!

### 3. How to Answer in an Interview
> *"Angular features a Hierarchical Dependency Injection framework. Rather than manually instantiating classes with the `new` keyword, components declare dependencies in their constructor or via `inject()`, and Angular resolves them.
>
> *Angular's injector tree is hierarchical: it checks the local component injector first, then bubbles up through parent components, environment route injectors, and finally the Root Injector.*
>
> *By using `@Injectable({ providedIn: 'root' })`, we register the service as a singleton at the application root, which also enables automatic tree-shaking so unused services are stripped from the production bundle."*

### 4. Three Practical Examples

#### Example 1: Root Singleton Injection (App-Wide Instance)
```typescript
@Injectable({
  providedIn: 'root' // Exactly ONE shared instance across the entire application
})
export class ConfigService {
  public apiUrl = 'https://api.bank.com/v1';
}
```

#### Example 2: Component-Level Injection (Isolated Instance Per Component)
When you want each component to have its own independent instance of a service (not shared):
```typescript
@Component({
  selector: 'app-loan-wizard-step',
  providers: [StepValidatorService], // Creates a UNIQUE instance for each instance of this component!
  template: `...`
})
export class LoanWizardStepComponent {
  constructor(private validator: StepValidatorService) {}
}
```

#### Example 3: Value Providers & Injection Tokens
Injecting runtime configuration or constants:
```typescript
// 1. Define InjectionToken
export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL');

// 2. Provide in app.config.ts / main.ts
export const appConfig: ApplicationConfig = {
  providers: [
    { provide: API_BASE_URL, useValue: 'https://secure.bank.com/api' }
  ]
};

// 3. Inject in Service
@Injectable({ providedIn: 'root' })
export class SecureClient {
  constructor(@Inject(API_BASE_URL) private baseUrl: string) {}
}
```

### 5. Real-World Project Application
- In **multi-tab loan comparisons**, component-level `providers: [LoanCalculatorService]` ensures that changing interest sliders in Tab 1 does not mutate calculations in Tab 2.

---

## Question 18: How do you create and use a service in Angular?

### 1. Previous Knowledge Required
- Angular CLI schematics (`ng g s`).
- Constructor injection vs modern `inject()` function (Angular 14+).

### 2. Logic & Theory (Deep-Dive)
- **Step 1: Creation**: Generated using CLI `ng generate service services/loan` or written manually with the `@Injectable()` decorator.
- **Step 2: Consumption**:
  - **Traditional Way**: Constructor Parameter Injection `constructor(private loanService: LoanService) {}`.
  - **Modern Way (Angular 14+)**: The `inject()` function `private loanService = inject(LoanService);`.
- **Why `inject()` is becoming the standard**:
  1. No constructor boilerplate needed.
  2. Works in factory functions, router guards, and custom interceptors where constructors are unavailable.
  3. Much easier inheritance (subclasses don't need to pass parent dependencies via `super(dep1, dep2, dep3)`).

### 3. How to Answer in an Interview
> *"To create a service, we use the Angular CLI command `ng g s services/loan`. This generates a TypeScript class with the `@Injectable({ providedIn: 'root' })` decorator.
>
> *To use the service in a component, we can either inject it via the constructor (`constructor(private loanService: LoanService) {}`) or use the modern `inject()` function introduced in Angular 14 (`private loanService = inject(LoanService)`). The `inject()` function simplifies class inheritance and allows injecting dependencies directly inside functional route guards."*

### 4. Three Practical Examples

#### Example 1: Classic Constructor Injection
```typescript
@Component({
  selector: 'app-loan-list',
  template: `<div *ngFor="let l of loans">{{ l.id }}</div>`
})
export class LoanListComponent implements OnInit {
  loans: Loan[] = [];

  constructor(private loanService: LoanService) {}

  ngOnInit() {
    this.loanService.getLoans().subscribe(data => this.loans = data);
  }
}
```

#### Example 2: Modern `inject()` Function (Angular 14+)
```typescript
@Component({
  selector: 'app-modern-loan-list',
  standalone: true,
  template: `<div *ngFor="let l of loans">{{ l.id }}</div>`
})
export class ModernLoanListComponent implements OnInit {
  // Clean, no constructor needed!
  private loanService = inject(LoanService);
  loans: Loan[] = [];

  ngOnInit() {
    this.loanService.getLoans().subscribe(data => this.loans = data);
  }
}
```

#### Example 3: Using `inject()` Inside a Functional Guard
```typescript
// Functional Route Guard using inject() directly without a class
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }
  return router.parseUrl('/login');
};
```

### 5. Real-World Project Application
- Functional guards and interceptors in modern Angular applications use `inject(AuthService)` and `inject(Router)` to protect routes without the boilerplate of registering multi-provider classes.

---

## Question 19: How do you configure routing in Angular?

### 1. Previous Knowledge Required
- Single Page Application (SPA) client-side routing.
- HTML5 History API (`pushState`, `popState`).

### 2. Logic & Theory (Deep-Dive)
Routing allows users to navigate between views without full-page browser reloads:
1. **Route Definitions**: An array of `Route` objects mapping URL paths to components: `const routes: Routes = [{ path: 'loans', component: LoanComponent }];`.
2. **Configuration**:
   - Modern Angular: Configured in `app.config.ts` using `provideRouter(routes)`.
   - Legacy Angular: Configured in `app-routing.module.ts` using `RouterModule.forRoot(routes)`.
3. **Router Outlet (`<router-outlet></router-outlet>`)**: A placeholder directive in the shell template where Angular dynamically renders the active routed component.
4. **Wildcard & Redirect Routes**: `{ path: '', redirectTo: '/dashboard', pathMatch: 'full' }` and `{ path: '**', component: PageNotFoundComponent }`.

### 3. How to Answer in an Interview
> *"We configure routing by defining an array of `Route` objects, where each route maps a URL path string to a Component.
>
> *In modern standalone applications, we register this array in `app.config.ts` using `provideRouter(routes, withComponentInputBinding())`. 
>
> *In the main shell template (`app.component.html`), we place the `<router-outlet></router-outlet>` directive, which serves as the dynamic container where Angular stamps out matched route components based on the browser URL."*

### 4. Three Practical Examples

#### Example 1: Modern Standalone Route Configuration (`app.routes.ts`)
```typescript
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { 
    path: 'dashboard', 
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) 
  },
  { 
    path: 'loans/:id', 
    loadComponent: () => import('./features/loan-detail/loan-detail.component').then(m => m.LoanDetailComponent) 
  },
  { 
    path: '**', 
    loadComponent: () => import('./core/not-found/not-found.component').then(m => m.NotFoundComponent) 
  }
];
```

#### Example 2: Registering Router in `app.config.ts`
```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    // withComponentInputBinding allows route params to be injected directly as @Input()!
    provideRouter(routes, withComponentInputBinding())
  ]
};
```

#### Example 3: Nested / Child Routes (Multi-Tab Underwriting Screen)
```typescript
export const underwritingRoutes: Routes = [
  {
    path: 'underwriting/:loanId',
    component: UnderwritingShellComponent,
    children: [
      { path: '', redirectTo: 'applicant-info', pathMatch: 'full' },
      { path: 'applicant-info', component: ApplicantInfoTabComponent },
      { path: 'cibil-report', component: CibilReportTabComponent },
      { path: 'documents', component: DocumentsTabComponent }
    ]
  }
];
```

### 5. Real-World Project Application
- In **Banking Portals**, nested child routes power multi-tab underwriting screens where the left sidebar displays applicant summary info permanently while the sub-views render different audit documents dynamically.

---

## Question 20: What is the difference between `RouterLink` and `href`?

### 1. Previous Knowledge Required
- Browser full page refresh vs SPA client-side history navigation.

### 2. Logic & Theory (Deep-Dive)

| Criterion | `<a href="/loans">` | `<a [routerLink]="['/loans']">` |
| :--- | :--- | :--- |
| **Browser Action** | **Full Page Reload**: Browser re-requests the entire HTML, CSS, and JS bundles from the server. | **Client-Side SPA Navigation**: Intercepts click event, updates URL via HTML5 `pushState`, and swaps components in `<router-outlet>`. |
| **Application State** | **Completely wiped out!** In-memory variables, tokens, and active form data are destroyed. | **Preserved!** In-memory state, user sessions, and cache in services remain intact. |
| **Performance** | Slow (requires re-downloading bundles, re-initializing Angular runtime). | Blazing fast (instantaneous DOM swap in milliseconds). |
| **External Links** | Appropriate for navigating to external 3rd-party websites (e.g., `href="https://uidai.gov.in"`). | Exclusively for internal application routes. |

### 3. How to Answer in an Interview
> *"The difference is full-page browser reload versus client-side Single Page Application navigation.
>
> *`href` is a native HTML attribute that triggers a full page refresh, causing the browser to re-download all scripts and wiping out the entire in-memory application state and cached services.
>
> *`routerLink` is an Angular directive that intercepts the anchor click, uses the HTML5 History API (`pushState`) to update the URL without reloading the browser, and renders the matched component inside `<router-outlet>` while preserving all application state."*

### 4. Three Practical Examples

#### Example 1: Basic `routerLink` with Active Styling
```html
<nav>
  <!-- routerLinkActive automatically applies the 'active-tab' CSS class when URL matches! -->
  <a [routerLink]="['/dashboard']" routerLinkActive="active-tab">Dashboard</a>
  <a [routerLink]="['/applications']" routerLinkActive="active-tab">Applications</a>

  <!-- External link MUST use href -->
  <a href="https://cibil.com" target="_blank" rel="noopener">External Bureau Portal</a>
</nav>
```

#### Example 2: `routerLink` Passing Route Parameters & Query Params
```html
<a [routerLink]="['/loans', loan.id]" 
   [queryParams]="{ stage: 'underwriting', ref: 'email' }">
  Review Loan #{{ loan.id }}
</a>
<!-- Produces: /loans/10492?stage=underwriting&ref=email -->
```

#### Example 3: Programmatic Navigation via Router Service (Instead of template link)
```typescript
@Component({...})
export class PaymentActionComponent {
  private router = inject(Router);

  onPaymentSuccess(txnId: string) {
    // Equivalent to routerLink, but executed from TypeScript logic
    this.router.navigate(['/receipt', txnId], {
      queryParams: { status: 'SUCCESS' }
    });
  }
}
```

### 5. Real-World Project Application
- Using `href` instead of `routerLink` in a single navigation link inside an internal dashboard is a classic beginner bug that accidentally logs the user out by wiping the in-memory JWT authentication token!

---

## Question 21: How can you pass parameters in Angular routes?

### 1. Previous Knowledge Required
- URL anatomy: Path parameters (`/path/:id`), Query parameters (`?key=value`), and Browser `history.state`.

### 2. Logic & Theory (Deep-Dive)
Angular provides **4 distinct ways** to pass data between routes:
1. **Required Route Parameters (Path Params)**: Part of the URL path (`/loans/:id`). Best for unique resource identifiers. Read via `ActivatedRoute.snapshot.paramMap.get('id')` or Observable `paramMap`.
2. **Optional Query Parameters**: Key-value pairs appended after `?` (`/loans?status=APPROVED&page=2`). Best for filtering, sorting, and pagination. Read via `queryParamMap`.
3. **Matrix Parameters**: Semicolon-separated pairs (`/loans;status=APPROVED;city=Mumbai`).
4. **Router State (Hidden Navigation State)**: Passing complex JavaScript objects without exposing them in the browser URL bar via `router.navigate(['/path'], { state: { data: myObject } })`.

### 3. How to Answer in an Interview
> *"Angular supports multiple mechanisms for passing route parameters:
> 1. **Path Parameters** for mandatory identifiers defined in route patterns like `/loans/:id`, accessed via `ActivatedRoute.snapshot.paramMap`.
> 2. **Query Parameters** for optional filters like `?stage=KYC&sort=asc`, accessed via `route.queryParamMap`.
> 3. **Navigation State** (`history.state`) for passing complex objects securely without exposing sensitive fields in the browser address bar.
> 4. In Angular 16+, with `withComponentInputBinding()`, path and query parameters can be injected directly into component `@Input()` properties without referencing `ActivatedRoute`!"*

### 4. Three Practical Examples

#### Example 1: Traditional `ActivatedRoute` (Path and Query Params)
```typescript
@Component({
  selector: 'app-loan-detail',
  template: `<h3>Loan ID: {{ loanId }}, Mode: {{ mode }}</h3>`
})
export class LoanDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  loanId!: string | null;
  mode!: string | null;

  ngOnInit() {
    // 1. Path Param (/loans/:id)
    this.loanId = this.route.snapshot.paramMap.get('id');

    // 2. Query Param (/loans/101?mode=readonly)
    this.mode = this.route.snapshot.queryParamMap.get('mode');
  }
}
```

#### Example 2: Modern Angular 16+ `withComponentInputBinding`
Route parameters map directly to `@Input()` properties with zero boilerplate:
```typescript
// Route config: { path: 'borrower/:borrowerId', component: BorrowerComponent }

@Component({
  selector: 'app-borrower',
  template: `<h2>Borrower ID: {{ borrowerId }}</h2>`
})
export class BorrowerComponent {
  // Angular 16+ automatically binds the route param :borrowerId here!
  @Input() borrowerId!: string;
}
```

#### Example 3: Passing State Objects via `NavigationExtras`
Passing an entire object without cluttering the URL:
```typescript
// Sender Component:
this.router.navigate(['/sanction-letter'], {
  state: { applicantData: this.currentApplicant, termsAccepted: true }
});

// Receiver Component:
@Component({...})
export class SanctionLetterComponent {
  private router = inject(Router);
  applicantData: any;

  constructor() {
    const nav = this.router.getCurrentNavigation();
    this.applicantData = nav?.extras.state?.['applicantData'] ?? history.state?.['applicantData'];
  }
}
```

### 5. Real-World Project Application
- In **Banking KYC flows**, passing large document metadata via `history.state` ensures sensitive customer data does not appear in the browser URL bar or proxy server access logs.

---

## Question 22: What are Route Guards? Name different types of route guards.

### 1. Previous Knowledge Required
- Middleware, authorization checkpoints, and URL navigation cancelation.

### 2. Logic & Theory (Deep-Dive)
Route Guards are interfaces or functions that the Angular Router executes before navigating to or away from a route. They return a `boolean`, `UrlTree`, or an `Observable/Promise<boolean | UrlTree>` to allow, redirect, or abort navigation.

#### 5 Types of Route Guards:
1. **`CanActivate`**: Checks if a user is permitted to visit a route (e.g., checking if user is logged in).
2. **`CanActivateChild`**: Checks if a user is permitted to visit child routes of a module.
3. **`CanDeactivate`**: Warns a user if they attempt to navigate away with unsaved form changes.
4. **`CanMatch` (Replaces `CanLoad`)**: Controls whether a route (and its lazy-loaded bundle) can even be matched. If false, Angular skips this route and checks subsequent routes.
5. **`Resolve`**: Pre-fetches necessary data before the route component activates, preventing blank-screen rendering delays.

### 3. How to Answer in an Interview
> *"Route Guards act as navigation middleware that can grant, redirect, or block access to routes.
>
> *The primary types are:
> - `CanActivate` to verify authentication/roles before loading a route.
> - `CanDeactivate` to prevent users from accidentally leaving unsaved forms.
> - `CanMatch` to prevent downloading lazy-loaded bundles unless authorized.
> - `Resolve` to pre-fetch API data before rendering the route.
>
> *In modern Angular (v15+), class-based guards implementing interfaces are deprecated in favor of lightweight, composable **Functional Guards** like `CanActivateFn` using `inject()`."*

### 4. Three Practical Examples

#### Example 1: Modern Functional `CanActivateFn` (Authentication Check)
```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true; // Navigation allowed
  }

  // Redirect to login with returnUrl
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
```

#### Example 2: `CanDeactivateFn` (Preventing Data Loss on Unsaved Forms)
```typescript
export interface HasUnsavedChanges {
  hasUnsavedChanges(): boolean;
}

export const unsavedChangesGuard: CanDeactivateFn<HasUnsavedChanges> = (component) => {
  if (component.hasUnsavedChanges()) {
    return confirm('You have unsaved loan application changes. Are you sure you want to discard them?');
  }
  return true;
};
```

#### Example 3: `ResolveFn` (Pre-fetching Loan Data Before Component Loads)
```typescript
export const loanDataResolver: ResolveFn<LoanDetails> = (route) => {
  const loanService = inject(LoanService);
  const loanId = route.paramMap.get('id')!;
  return loanService.fetchLoanById(loanId);
};

// Route config:
// { path: 'loans/:id', component: LoanComponent, resolve: { loanData: loanDataResolver } }
```

### 5. Real-World Project Application
- In **FinTech Loan Sanction Portals**, `CanActivate` ensures unauthorized users cannot view approved sanction letters, and `CanDeactivate` prevents underwriters from losing complex notes if they accidentally click the back button.

---

*Continue to [Chapter 19: Forms, Validation & HTTP Client](file:///Users/apple/Workspace/Home/Javascript/Learning/19_Angular_Forms_Validation_and_HTTP_Client.md)*
