# Chapter 21: Lifecycle Hooks, SSR Universal, Web Workers & Security (Questions 36–39)

This guide covers Questions 36 to 39 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
36. [What are Angular Lifecycle Hooks? Explain each one briefly.](#question-36-what-are-angular-lifecycle-hooks-explain-each-one-briefly)
37. [What is Angular Universal?](#question-37-what-is-angular-universal)
38. [What are Web Workers, and how do they help in Angular applications?](#question-38-what-are-web-workers-and-how-do-they-help-in-angular-applications)
39. [How do you implement Authentication & Authorization in Angular?](#question-39-how-do-you-implement-authentication--authorization-in-angular)

---

## Question 36: What are Angular Lifecycle Hooks? Explain each one briefly.

### 1. Previous Knowledge Required
- Component instantiation, DOM rendering lifecycle, and cleanup phases.

### 2. Logic & Theory (Deep-Dive)
When Angular creates, updates, and destroys components, it calls specific lifecycle hook methods on the component class if implemented:

```
[Constructor] -> Dependency Injection only; DOM and @Input() values are NOT ready!
     |
     v
[ngOnChanges] -> Called whenever one or more @Input() data-bound properties change.
     |           Receives SimpleChanges map containing previous and current values.
     v
[ngOnInit] -> Called ONCE after the first ngOnChanges. 
     |        @Input() properties are now resolved. Primary place for initial HTTP calls.
     v
[ngDoCheck] -> Called immediately after ngOnChanges and ngOnInit during every check cycle.
     |         Used to implement custom change detection algorithms.
     v
[ngAfterContentInit] -> Called ONCE after external content (<ng-content>) is projected.
     |
     v
[ngAfterContentChecked] -> Called after projected content has been checked.
     |
     v
[ngAfterViewInit] -> Called ONCE after the component's view and child views are fully initialized.
     |               Safe to interact with @ViewChild and canvas elements.
     v
[ngAfterViewChecked] -> Called after the component's view and child views have been checked.
     |
     v
[ngOnDestroy] -> Called ONCE right before the component is destroyed and removed from the DOM.
                 Mandatory place to unsubscribe observables and clear intervals.
```

### 3. How to Answer in an Interview
> *"Angular lifecycle hooks allow us to tap into critical moments in a component's existence.
>
> *The lifecycle starts with the constructor for dependency injection, followed by `ngOnChanges` whenever `@Input()` properties change, and `ngOnInit` for initial data fetching.
>
> *Once projected content is ready, `ngAfterContentInit` executes, followed by `ngAfterViewInit` once the component's template and child views are fully rendered—which is where we access `@ViewChild` references.
>
> *Finally, right before the component is destroyed, `ngOnDestroy` is triggered, where we must unsubscribe from Observables and clear intervals to prevent memory leaks."*

### 4. Three Practical Examples

#### Example 1: `ngOnChanges` Inspecting `SimpleChanges`
```typescript
@Component({
  selector: 'app-interest-display',
  template: `<p>Rate changed from {{ oldRate }}% to {{ currentRate }}%</p>`
})
export class InterestDisplayComponent implements OnChanges {
  @Input() rate: number = 8.5;
  oldRate = 0;
  currentRate = 0;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['rate']) {
      const rateChange = changes['rate'];
      this.oldRate = rateChange.previousValue;
      this.currentRate = rateChange.currentValue;
      if (!rateChange.firstChange) {
        console.log('Interest rate updated dynamically by parent!');
      }
    }
  }
}
```

#### Example 2: `ngAfterViewInit` Interacting with Child DOM Elements
```typescript
@Component({
  selector: 'app-document-viewer',
  template: `<canvas #pdfCanvas></canvas>`
})
export class DocumentViewerComponent implements AfterViewInit {
  @ViewChild('pdfCanvas') canvasRef!: ElementRef<HTMLCanvasElement>;

  ngAfterViewInit() {
    // In ngOnInit, canvasRef would be UNDEFINED because the template has not rendered!
    // ngAfterViewInit guarantees the canvas DOM element exists
    const ctx = this.canvasRef.nativeElement.getContext('2d');
    this.renderWatermark(ctx);
  }

  private renderWatermark(ctx: CanvasRenderingContext2D | null) { /* ... */ }
}
```

#### Example 3: `ngOnDestroy` Preventing Observable Memory Leaks
```typescript
@Component({...})
export class LiveTickerComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  tickerData: any;

  constructor(private tickerService: TickerService) {}

  ngOnInit() {
    this.tickerService.getLivePrices()
      .pipe(takeUntil(this.destroy$)) // Automatically completes when destroy$ emits!
      .subscribe(data => this.tickerData = data);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete(); // Cleanly tears down the subscription!
  }
}
```

### 5. Real-World Project Application
- In **Financial Applications**, failing to unsubscribe in `ngOnDestroy` when a user navigates between loan accounts causes duplicate WebSocket listeners to accumulate in memory, leaking megabytes of RAM and triggering duplicate browser alert popups.

---

## Question 37: What is Angular Universal?

### 1. Previous Knowledge Required
- Client-Side Rendering (CSR) vs Server-Side Rendering (SSR).
- Search Engine Optimization (SEO) web crawlers and web vitals (First Contentful Paint - FCP).

### 2. Logic & Theory (Deep-Dive)

#### The Problem with Client-Side Rendering (CSR):
When a user requests a standard Angular CSR page, the server returns an empty HTML file:
```html
<body><app-root></app-root><script src="main.js"></script></body>
```
The browser must download, parse, and execute the 2MB JavaScript bundle before rendering any UI.
- **Consequences**: Poor SEO (search crawlers and social media preview bots see an empty page) and sluggish First Contentful Paint on mobile networks.

#### The Angular Universal (SSR) Solution:
Angular Universal executes the Angular application **on a Node.js server**.
1. The server renders the full HTML markup with real data pre-populated.
2. The server sends the pre-rendered HTML and CSS directly to the browser.
3. The user sees the fully styled page **instantly** (blazing fast FCP).
4. Meanwhile, the client downloads the JavaScript bundle in the background and **hydrates** the static HTML into an interactive Angular app (Event listeners attached).
5. In Angular 16+, **Non-Destructive Hydration** eliminates page flickering by reusing the existing server-rendered DOM nodes rather than destroying and re-creating them.

#### The Big SSR Trap:
Node.js does **not** have browser globals like `window`, `document`, or `localStorage`! Calling `window.location.href` on the server causes Node.js to crash with `ReferenceError: window is not defined`. You must guard browser-only code using `isPlatformBrowser`:
```typescript
import { isPlatformBrowser } from '@angular/common';
if (isPlatformBrowser(this.platformId)) {
  localStorage.getItem('token');
}
```

### 3. How to Answer in an Interview
> *"Angular Universal is Angular's official Server-Side Rendering (SSR) technology. 
>
> *Instead of shipping an empty HTML shell that requires the browser to download and execute heavy JavaScript before rendering, Universal pre-renders the complete view on a Node.js server. 
>
> *This drastically improves SEO for search engine crawlers and social share cards, and delivers a near-instantaneous First Contentful Paint (FCP). 
> In modern Angular 16/17, it uses non-destructive hydration to make the server HTML interactive on the client without UI flickering."*

### 4. Three Practical Examples

#### Example 1: Guarding Browser Globals with `isPlatformBrowser`
```typescript
import { Component, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-theme-switcher',
  template: `<p>Current Theme: {{ theme }}</p>`
})
export class ThemeSwitcherComponent implements OnInit {
  theme = 'light';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit() {
    // Crucial: Node.js has no 'localStorage'! This check prevents server crashes.
    if (isPlatformBrowser(this.platformId)) {
      this.theme = localStorage.getItem('user-theme') || 'light';
    }
  }
}
```

#### Example 2: Dynamic SEO Meta Tags for Loan Products
```typescript
import { Component, OnInit } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';

@Component({
  selector: 'app-home-loan-seo',
  template: `<h1>Low Interest Home Loans</h1>`
})
export class HomeLoanSeoComponent implements OnInit {
  constructor(private title: Title, private meta: Meta) {}

  ngOnInit() {
    // Search engines reading server-rendered HTML will see these rich tags!
    this.title.setTitle('Instant Home Loans at 8.25% - State Bank');
    this.meta.updateTag({ name: 'description', content: 'Apply for fast personal and home loans with instant approval.' });
    this.meta.updateTag({ property: 'og:image', content: 'https://bank.com/assets/banner.png' });
  }
}
```

#### Example 3: Enabling SSR in Modern Angular 17+
```bash
# Add SSR with a single CLI command
ng add @angular/ssr
```
This automatically configures the Node.js Express server (`server.ts`) and sets up non-destructive hydration in `app.config.ts`:
```typescript
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration() // Angular 16+ non-destructive hydration!
  ]
};
```

### 5. Real-World Project Application
- In **Retail Banking Websites**, customer-facing loan interest comparison pages use Angular Universal so that Google search rankings place the bank on Page 1 and customers sharing loan calculator links on WhatsApp see rich thumbnail preview cards.

---

## Question 38: What are Web Workers, and how do they help in Angular applications?

### 1. Previous Knowledge Required
- The single-threaded JavaScript execution model (UI rendering and JavaScript share the same thread).
- Browser UI freezing / frame drops (jank).

### 2. Logic & Theory (Deep-Dive)

#### The Problem: UI Freezing on Heavy Computations
JavaScript in the browser runs on a **single main thread**.
If you execute a computationally heavy task on the main thread (e.g., parsing a 50MB CSV bank statement, encrypting biometric images, or calculating complex multi-decade amortization schedules), the browser cannot process user clicks or CSS animations. The entire UI freezes and appears unresponsive to the customer.

#### The Web Worker Solution:
A **Web Worker** runs JavaScript code in an entirely separate **background OS worker thread**, completely isolated from the main UI thread.
- The main thread communicates with the Web Worker using asynchronous message passing: `postMessage()` and `onmessage`.
- Because the worker runs in the background, the UI remains silky-smooth at 60 FPS while the computation runs.
- **CLI Command**: Angular CLI provides out-of-the-box Web Worker configuration:
  `ng generate web-worker features/amortization`

```
  [Main UI Thread]                                  [Web Worker Background Thread]
         |                                                         |
         | --- postMessage({ principal, rate, tenure }) ---------> |
         |                                                         | Heavy Mathematical
         | (UI remains completely responsive, smooth 60 FPS!)     | Amortization Calculation
         |                                                         | (No UI freeze!)
         | <--- postMessage({ monthlySchedules }) ---------------- |
         |                                                         |
  Renders Table
```

### 3. How to Answer in an Interview
> *"JavaScript in the browser is single-threaded. When heavy computational tasks run on the main thread, they block DOM rendering and user interaction, causing the UI to freeze.
>
> *Web Workers allow us to offload CPU-intensive operations (such as parsing huge Excel/CSV files, image processing, or complex cryptographic hashing) into a separate background thread. 
>
> *Angular CLI provides built-in support via `ng generate web-worker`. The main component posts data to the worker using `postMessage()` and listens for results via `onmessage`, keeping the user interface completely fluid."*

### 4. Three Practical Examples

#### Example 1: Generating and Creating a Web Worker via Angular CLI
```bash
ng generate web-worker services/heavy-calculation
```
Creates `heavy-calculation.worker.ts`:
```typescript
/// <reference lib="webworker" />

addEventListener('message', ({ data }) => {
  // Runs in background thread:
  const result = computeComplexAmortization(data.principal, data.rate, data.months);
  postMessage(result); // Send back to main UI thread
});

function computeComplexAmortization(p: number, r: number, m: number) {
  // Heavy computation simulating 200,000 iterations
  let total = 0;
  for (let i = 0; i < 200000000; i++) { total += (p * r) / m; }
  return { status: 'COMPLETED', calculatedTotal: total };
}
```

#### Example 2: Consuming the Web Worker in an Angular Component
```typescript
@Component({
  selector: 'app-heavy-processor',
  template: `
    <button (click)="runCalculation()">Start Calculation</button>
    <p>Status: {{ resultStatus }}</p>
  `
})
export class HeavyProcessorComponent implements OnInit, OnDestroy {
  private worker!: Worker;
  resultStatus = 'Idle';

  ngOnInit() {
    if (typeof Worker !== 'undefined') {
      // Instantiate worker
      this.worker = new Worker(new URL('./heavy-calculation.worker', import.meta.url));
      this.worker.onmessage = ({ data }) => {
        this.resultStatus = `Calculation Done: ${data.calculatedTotal}`;
      };
    }
  }

  runCalculation() {
    this.resultStatus = 'Calculating in background worker...';
    // Send data to worker thread
    this.worker.postMessage({ principal: 1000000, rate: 8.5, months: 240 });
  }

  ngOnDestroy() {
    this.worker?.terminate(); // Free background thread resources
  }
}
```

#### Example 3: Fallback when Web Worker is Unsupported
```typescript
executeTask(payload: any) {
  if (typeof Worker !== 'undefined') {
    this.worker.postMessage(payload);
  } else {
    // Graceful fallback on ancient legacy environments
    console.warn('Web Workers not supported. Executing on main thread.');
    const result = synchronousComputation(payload);
    this.handleResult(result);
  }
}
```

### 5. Real-World Project Application
- In **Banking Loan Underwriting**, underwriters frequently upload 100-page bank statements in PDF/Excel format containing 20,000 transaction rows. Web Workers parse and extract balance averages in the background without causing the browser tab to lag or crash.

---

## Question 39: How do you implement Authentication & Authorization in Angular?

### 1. Previous Knowledge Required
- JSON Web Tokens (JWT: Header, Payload, Signature).
- Angular HTTP Interceptors and Route Guards.

### 2. Logic & Theory (Deep-Dive)

#### The Complete Enterprise Architecture:
```
1. User Enters Credentials -> POST /api/v1/auth/login
2. Backend Returns:
   - Access Token (JWT, valid for 15 mins)
   - User Profile with Roles: ['ROLE_CREDIT_OFFICER']
   - Refresh Token stored in HttpOnly Secure Cookie
3. Frontend Stores:
   - Access Token stored in-memory (AuthService property)
   - Decoded user roles in BehaviorSubject
4. Navigation Attempt -> CanActivateFn (Route Guard):
   - Verifies user is authenticated.
   - Checks if user has required role for route.
5. Outgoing API Requests -> HttpInterceptorFn:
   - Clones request and adds Authorization: Bearer <accessToken>
6. Access Token Expired (401 Response) -> HttpInterceptorFn:
   - Catches 401 error.
   - Pauses failed request.
   - Calls POST /api/v1/auth/refresh to exchange cookie for new Access Token.
   - Retries the original request seamlessly!
```

### 3. How to Answer in an Interview
> *"We implement Authentication and Authorization through a multi-layered security architecture:
>
> 1. **Authentication State**: We use an `AuthService` with a `BehaviorSubject` to track current user profile and in-memory access tokens.
> 2. **Route Authorization**: We configure functional route guards (`CanActivateFn`) that inspect route `data: { roles: ['ADMIN'] }` to block unauthorized navigation.
> 3. **Token Propagation**: We use an `HttpInterceptorFn` that automatically attaches the `Authorization: Bearer <token>` header to all outgoing requests.
> 4. **Silent Token Refresh**: If the backend returns a 401, the interceptor intercepts the failure, uses a Refresh Token to fetch a new JWT, and seamlessly replays the failed request without logging out the user."*

### 4. Three Practical Examples

#### Example 1: The Modern Functional `AuthInterceptor` (Attaching Bearer Token & 401 Catch)
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getAccessToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Token expired: logout or refresh
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
```

#### Example 2: Role-Based Functional Route Guard (`roleGuard`)
```typescript
export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedRoles: string[] = route.data?.['roles'] || [];

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
  }

  if (expectedRoles.length > 0 && !authService.hasAnyRole(expectedRoles)) {
    return router.createUrlTree(['/unauthorized']); // Access Denied screen
  }

  return true;
};

// Route Configuration:
// { path: 'sanction-loan', component: SanctionComponent, canActivate: [roleGuard], data: { roles: ['ROLE_SANCTION_MANAGER'] } }
```

#### Example 3: Silent Refresh Token Flow in Interceptor
```typescript
private handle401Error(request: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> {
  if (!this.isRefreshing) {
    this.isRefreshing = true;
    this.refreshTokenSubject.next(null);

    return this.authService.refreshAccessToken().pipe(
      switchMap((newToken: string) => {
        this.isRefreshing = false;
        this.refreshTokenSubject.next(newToken);
        // Re-attempt original request with the new token
        return next(request.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } }));
      }),
      catchError(err => {
        this.isRefreshing = false;
        this.authService.logout();
        return throwError(() => err);
      })
    );
  } else {
    // Wait until token refresh completes, then resume
    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(jwt => next(request.clone({ setHeaders: { Authorization: `Bearer ${jwt}` } })))
    );
  }
}
```

### 5. Real-World Project Application
- In **Banking Loan Portals**, Role-Based Access Control (RBAC) guarantees that branch sales agents can only initiate applications, while only credit underwriters with `ROLE_SANCTION_OFFICER` can view and approve loan disbursements over $100,000.

---

*Back to [Master Index & Roadmap](file:///Users/apple/Workspace/Home/Javascript/Learning/00_Master_Index_and_Roadmap.md)*
