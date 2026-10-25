# Chapter 19: Forms, Validation & HTTP Client (Questions 23–28)

This guide covers Questions 23 to 28 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
23. [Explain the difference between Template-driven and Reactive Forms.](#question-23-explain-the-difference-between-template-driven-and-reactive-forms)
24. [How do you perform form validation in Angular?](#question-24-how-do-you-perform-form-validation-in-angular)
25. [How can you dynamically add form fields in a Reactive Form?](#question-25-how-can-you-dynamically-add-form-fields-in-a-reactive-form)
26. [How do you make an HTTP GET and POST request in Angular?](#question-26-how-do-you-make-an-http-get-and-post-request-in-angular)
27. [What is the difference between `HttpClientModule` and `HttpModule`?](#question-27-what-is-the-difference-between-httpclientmodule-and-httpmodule)
28. [How do you handle errors in HTTP requests in Angular?](#question-28-how-do-you-handle-errors-in-http-requests-in-angular)

---

## Question 23: Explain the difference between Template-driven and Reactive Forms.

### 1. Previous Knowledge Required
- Form validation, two-way data binding (`[(ngModel)]`) vs programmatic control model (`FormGroup`, `FormControl`).

### 2. Logic & Theory (Deep-Dive)

| Feature | Template-Driven Forms | Reactive Forms (Model-Driven) |
| :--- | :--- | :--- |
| **Setup & Source of Truth**| Logic defined in the **HTML Template** using directives (`ngModel`, `ngForm`). | Explicitly defined in the **TypeScript Class** via `FormGroup`, `FormControl`. |
| **Data Flow** | **Asynchronous**: relies on Angular template compilation and directives. | **Synchronous**: direct programmatic access to values and validation state. |
| **Form Model Mutation** | Two-way data binding mutates data model directly. | Immutable data model; changes emit functional event streams (`valueChanges`). |
| **Complex Validation** | Difficult (requires writing custom validator directives). | Clean, composable pure TypeScript functions passed in an array. |
| **Dynamic Controls** | Difficult to add/remove fields on the fly. | Native support via `FormArray`. |
| **Unit Testing** | Requires rendering the DOM template in tests. | Pure TypeScript unit tests without DOM rendering! |
| **Module** | `FormsModule` | `ReactiveFormsModule` |

### 3. How to Answer in an Interview
> *"The core difference is where the form model and validation logic reside.
>
> *Template-driven forms place the source of truth in the HTML template using directives like `ngModel`. They are asynchronous, simple to set up for basic forms, but difficult to unit test and scale for complex dynamic requirements.
>
> *Reactive Forms (Model-driven) define the form model programmatically in the TypeScript class using `FormGroup` and `FormControl`. They provide synchronous data access, reactive event streams via `valueChanges`, native support for `FormArray`, and can be unit tested without rendering the DOM. Reactive forms are the enterprise industry standard."*

### 4. Three Practical Examples

#### Example 1: Simple Template-Driven Form
```html
<form #loginForm="ngForm" (ngSubmit)="onLogin(loginForm.value)">
  <input name="email" ngModel required email #emailCtrl="ngModel" />
  <span *ngIf="emailCtrl.invalid && emailCtrl.touched">Valid email is required</span>

  <input name="password" ngModel required minlength="6" type="password" />
  <button type="submit" [disabled]="loginForm.invalid">Login</button>
</form>
```

#### Example 2: Enterprise Reactive Form (Clean & Testable)
```typescript
@Component({
  selector: 'app-loan-apply-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <form [formGroup]="loanForm" (ngSubmit)="submitLoan()">
      <input formControlName="panNumber" placeholder="ABCDE1234F" />
      <div *ngIf="panControl?.touched && panControl?.hasError('pattern')">
        Invalid PAN format! Must match 5 letters, 4 digits, 1 letter.
      </div>

      <input formControlName="requestedAmount" type="number" placeholder="Loan Amount" />
      <button [disabled]="loanForm.invalid">Submit</button>
    </form>
  `
})
export class LoanApplyFormComponent {
  private fb = inject(FormBuilder);

  loanForm = this.fb.group({
    panNumber: ['', [Validators.required, Validators.pattern(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/)]],
    requestedAmount: [50000, [Validators.required, Validators.min(10000), Validators.max(5000000)]]
  });

  get panControl() { return this.loanForm.get('panNumber'); }

  submitLoan() {
    if (this.loanForm.valid) {
      console.log('Sanction request payload:', this.loanForm.value);
    }
  }
}
```

#### Example 3: Modern Angular 14+ Strictly Typed Reactive Form
```typescript
interface KycFormStructure {
  fullName: FormControl<string>;
  monthlyIncome: FormControl<number>;
  isSalaried: FormControl<boolean>;
}

@Component({...})
export class TypedKycFormComponent {
  // Types are checked at compile-time: fullName can never be assigned a number!
  kycForm = new FormGroup<KycFormStructure>({
    fullName: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    monthlyIncome: new FormControl(0, { nonNullable: true, validators: [Validators.min(15000)] }),
    isSalaried: new FormControl(true, { nonNullable: true })
  });
}
```

### 5. Real-World Project Application
- In **Banking Applications**, Reactive Forms are mandatory because complex loan applications require cross-field validation (e.g., *loan tenure cannot exceed retirement age*), dynamic document upload arrays, and instant reactivity via `form.valueChanges`.

---

## Question 24: How do you perform form validation in Angular?

### 1. Previous Knowledge Required
- Synchronous vs Asynchronous Validators.
- Validation states (`pristine`, `dirty`, `touched`, `untouched`, `valid`, `invalid`).

### 2. Logic & Theory (Deep-Dive)
Validation ensures incoming user input conforms to business constraints before submission:
1. **Built-in Validators**: `Validators.required`, `Validators.minLength`, `Validators.maxLength`, `Validators.pattern`, `Validators.email`.
2. **Custom Synchronous Validators**: Pure functions taking an `AbstractControl` and returning `ValidationErrors | null` (`null` indicates valid).
3. **Cross-Field Validation**: Validators applied to the parent `FormGroup` to compare multiple fields (e.g., `password === confirmPassword`).
4. **Asynchronous Validators**: Return `Observable<ValidationErrors | null>`. Used when validation requires hitting an external backend API (e.g., verifying if a PAN number or Username is already registered).

### 3. How to Answer in an Interview
> *"In Angular Reactive Forms, validation is performed by passing validator functions to `FormControl` definitions.
>
> *We have three levels:
> 1. **Built-in Validators** like `required` and `pattern`.
> 2. **Custom Synchronous Validators** which take an `AbstractControl` and return a validation error object or `null`.
> 3. **Asynchronous Validators** which return an Observable or Promise to validate against backend services without blocking UI interaction (for instance, debouncing an API call to verify if an email is already taken).*
>
> *We display errors in templates conditionally when controls are both `invalid` and `touched` to avoid startling users before they have finished typing."*

### 4. Three Practical Examples

#### Example 1: Custom Synchronous Validator (Age Limit Check)
```typescript
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function minimumAgeValidator(minAge: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null; // Let 'required' validator handle empty values

    const birthDate = new Date(control.value);
    const ageDiffMs = Date.now() - birthDate.getTime();
    const ageDate = new Date(ageDiffMs);
    const calculatedAge = Math.abs(ageDate.getUTCFullYear() - 1970);

    return calculatedAge >= minAge ? null : { underAge: { requiredAge: minAge, actualAge: calculatedAge } };
  };
}
```

#### Example 2: Cross-Field Validator (Password & Confirm Password Match)
```typescript
export const passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;

  if (password && confirmPassword && password !== confirmPassword) {
    return { passwordMismatch: true };
  }
  return null;
};

// Usage on FormGroup:
// const form = new FormGroup({ password: new FormControl(''), confirmPassword: new FormControl('') }, { validators: passwordMatchValidator });
```

#### Example 3: Asynchronous Validator (Checking PAN Uniqueness with Backend API)
```typescript
import { AsyncValidatorFn } from '@angular/forms';
import { timer, of } from 'rxjs';
import { switchMap, map, catchError } from 'rxjs/operators';

export function panAvailabilityValidator(bureauService: BureauCheckService): AsyncValidatorFn {
  return (control: AbstractControl) => {
    if (!control.value) return of(null);

    // Debounce API calls by 400ms while user is typing
    return timer(400).pipe(
      switchMap(() => bureauService.checkPanRegistered(control.value)),
      map(isRegistered => (isRegistered ? { panAlreadyExists: true } : null)),
      catchError(() => of(null))
    );
  };
}
```

### 5. Real-World Project Application
- In a **Loan Origination System**, an asynchronous validator checks the customer's PAN card against existing active loans to prevent fraudulent multi-borrowing across branches in real time.

---

## Question 25: How can you dynamically add form fields in a Reactive Form?

### 1. Previous Knowledge Required
- `FormArray`, `FormGroup`, and dynamic DOM rendering with `*ngFor`.

### 2. Logic & Theory (Deep-Dive)
A `FormArray` is an alternative to `FormGroup` for managing any number of anonymous controls.
- While a `FormGroup` stores controls as **named key-value pairs** (`{ name: ctrl, age: ctrl }`), a `FormArray` stores controls as a **numerically indexed array** (`[ctrl0, ctrl1, ctrl2]`).
- It provides built-in methods: `.push()`, `.removeAt(index)`, `.insert(index)`, `.clear()`.
- Crucial for rendering dynamic sub-forms (e.g., adding multiple past employment histories, bank accounts, or nominee records).

### 3. How to Answer in an Interview
> *"We dynamically manage varying form fields using Angular's `FormArray`.
>
> *A `FormArray` tracks an indexed list of `FormControl`, `FormGroup`, or nested `FormArray` instances.
> In the TypeScript class, we define a getter that returns the `FormArray` using `this.form.get('items') as FormArray`. We can then dynamically call `.push()` to append a new control or `.removeAt(i)` to delete one.
>
> *In the HTML template, we bind `formArrayName="items"`, loop over `items.controls` using `*ngFor="let ctrl of items.controls; let i=index"`, and assign `[formGroupName]="i"`."*

### 4. Three Practical Examples

#### Example 1: Dynamic List of Bank Accounts (`FormArray` of `FormGroup`)
```typescript
@Component({
  selector: 'app-bank-accounts-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <form [formGroup]="customerForm">
      <div formArrayName="bankAccounts">
        <div *ngFor="let acct of bankAccounts.controls; let i = index" [formGroupName]="i" class="account-row">
          <h4>Account #{{ i + 1 }}</h4>
          <input formControlName="bankName" placeholder="Bank Name" />
          <input formControlName="accountNumber" placeholder="Account Number" />
          <input formControlName="ifscCode" placeholder="IFSC Code" />
          <button type="button" (click)="removeAccount(i)" *ngIf="bankAccounts.length > 1">Remove</button>
        </div>
      </div>
      <button type="button" (click)="addAccount()">+ Add Another Bank Account</button>
    </form>
  `
})
export class BankAccountsFormComponent {
  private fb = inject(FormBuilder);

  customerForm = this.fb.group({
    bankAccounts: this.fb.array([this.createAccountGroup()]) // Initialize with 1 account
  });

  get bankAccounts(): FormArray {
    return this.customerForm.get('bankAccounts') as FormArray;
  }

  createAccountGroup(): FormGroup {
    return this.fb.group({
      bankName: ['', Validators.required],
      accountNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9,18}$/)]],
      ifscCode: ['', [Validators.required, Validators.pattern(/^[A-Z]{4}0[A-Z0-9]{6}$/)]]
    });
  }

  addAccount() {
    this.bankAccounts.push(this.createAccountGroup());
  }

  removeAccount(index: number) {
    this.bankAccounts.removeAt(index);
  }
}
```

#### Example 2: Dynamic Primitive Control List (Adding Multiple Phone Numbers)
```typescript
export class ContactNumbersComponent {
  phoneNumbers = new FormArray([
    new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{10}$/)])
  ]);

  addPhone() {
    this.phoneNumbers.push(new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]));
  }
}
// Template: <input *ngFor="let c of phoneNumbers.controls; let i=index" [formControlName]="i" />
```

#### Example 3: Bulk Data Patching into `FormArray` (Editing Existing Application)
```typescript
populateExistingAccounts(accountsData: BankAccountDTO[]) {
  const formArray = this.customerForm.get('bankAccounts') as FormArray;
  formArray.clear(); // Clear existing

  accountsData.forEach(acct => {
    formArray.push(this.fb.group({
      bankName: [acct.bankName, Validators.required],
      accountNumber: [acct.accountNumber, Validators.required],
      ifscCode: [acct.ifscCode, Validators.required]
    }));
  });
}
```

### 5. Real-World Project Application
- In **Enterprise Loan Applications**, applicants frequently declare multiple co-borrowers or existing EMI liabilities. `FormArray` allows users to dynamically add 1 to 5 co-borrowers and dynamically validate each co-borrower's income and KYC documents.

---

## Question 26: How do you make an HTTP GET and POST request in Angular?

### 1. Previous Knowledge Required
- RESTful HTTP methods (`GET` for reading, `POST` for creating).
- `HttpClient` service and RxJS Observables.

### 2. Logic & Theory (Deep-Dive)
Angular provides the `HttpClient` service (`@angular/common/http`) to perform HTTP requests.
- **Automatic JSON Parsing**: Unlike `fetch()` in raw JavaScript, `HttpClient` automatically deserializes JSON response bodies into typed TypeScript objects!
- **Typed Responses**: You can pass a generic type: `http.get<CustomerDTO>('/api/v1/customer')`.
- **Immutability of Requests**: HTTP requests and headers in Angular are immutable. Modifying headers requires cloning (`req.clone()`).
- **Cold Observables**: Calling `http.get()` does **NOT** fire the network request until someone subscribes to the returned Observable (`.subscribe()` or `| async`)!

### 3. How to Answer in an Interview
> *"We perform HTTP requests using Angular's `HttpClient` service.
>
> *For a `GET` request, we invoke `http.get<T>(url, options)` where `T` is a TypeScript interface defining the expected response.
>
> *For a `POST` request, we invoke `http.post<T>(url, body, options)` passing the payload object as the second argument.
>
> *Crucially, Angular HTTP requests return cold RxJS Observables, meaning no HTTP request is dispatched over the network until a component subscribes to it."*

### 4. Three Practical Examples

#### Example 1: Basic Typed GET & POST Request
```typescript
export interface LoanApplicationDTO {
  id?: number;
  applicantName: string;
  amount: number;
}

@Injectable({ providedIn: 'root' })
export class LoanApiService {
  private http = inject(HttpClient);
  private baseUrl = 'https://api.bank.com/v1/loans';

  // GET Request
  getLoanById(id: number): Observable<LoanApplicationDTO> {
    return this.http.get<LoanApplicationDTO>(`${this.baseUrl}/${id}`);
  }

  // POST Request
  createLoanApplication(payload: LoanApplicationDTO): Observable<LoanApplicationDTO> {
    return this.http.post<LoanApplicationDTO>(this.baseUrl, payload);
  }
}
```

#### Example 2: GET with Query Parameters & Custom Headers
```typescript
getFilteredLoans(status: string, page: number): Observable<LoanPageResponse> {
  const params = new HttpParams()
    .set('status', status)
    .set('page', page.toString())
    .set('size', '20');

  const headers = new HttpHeaders()
    .set('X-Client-Version', '2.4.0');

  return this.http.get<LoanPageResponse>(this.baseUrl, { params, headers });
}
```

#### Example 3: POST with Progress Tracking (File Upload)
```typescript
uploadKycDocument(file: File, loanId: number): Observable<HttpEvent<any>> {
  const formData = new FormData();
  formData.append('document', file);
  formData.append('loanId', loanId.toString());

  return this.http.post('/api/v1/kyc/upload', formData, {
    reportProgress: true, // Enables progress events
    observe: 'events'     // Observe raw HTTP events (UploadProgress, Response)
  });
}
```

### 5. Real-World Project Application
- In **Loan Origination**, file uploads use `observe: 'events'` and `reportProgress: true` to display real-time percentage upload bars for 20MB scanned income tax returns.

---

## Question 27: What is the difference between `HttpClientModule` and `HttpModule`?

### 1. Previous Knowledge Required
- Angular 4.3 evolution (`@angular/http` vs `@angular/common/http`).
- Standalone providers in Angular 15+ (`provideHttpClient()`).

### 2. Logic & Theory (Deep-Dive)

| Criterion | Legacy `HttpModule` (Deprecated) | Modern `HttpClientModule` / `provideHttpClient()` |
| :--- | :--- | :--- |
| **Package** | `@angular/http` | `@angular/common/http` |
| **JSON Parsing** | Manual: Required calling `response.json()` explicitly on every call. | **Automatic**: Automatically parses JSON bodies directly into typed objects. |
| **Interceptors** | **Not supported**. Developers had to write convoluted wrapper classes. | **First-class native Interceptor support** via `HTTP_INTERCEPTORS` / `withInterceptors()`. |
| **Progress Events**| Inflexible. | Native upload and download progress event tracking. |
| **Testing** | `MockBackend` (complex to configure). | `HttpTestingController` via `provideHttpClientTesting()`. |
| **Status** | Completely removed in Angular 5+. | Current production standard. |

### 3. How to Answer in an Interview
> *"`HttpModule` was the original legacy HTTP client from Angular 2, which was deprecated in Angular 4.3 and removed in Angular 5. It required manually calling `.json()` to parse responses and lacked native interceptor support.
>
> *`HttpClientModule` (and modern `provideHttpClient()` in Angular 15+) is the modern replacement. It automatically deserializes JSON into typed objects, introduces first-class HTTP Interceptors for headers and error handling, supports progress event reporting, and provides clean testing via `HttpTestingController`."*

### 4. Three Practical Examples

#### Example 1: Legacy `HttpModule` (Historical Context)
```typescript
// ❌ Legacy Angular 2 syntax (Do NOT use!):
// import { Http } from '@angular/http';
// this.http.get(url).map(res => res.json()).subscribe(data => ...);
```

#### Example 2: Modern `provideHttpClient()` in Standalone Angular 15+
```typescript
// app.config.ts
import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors, withFetch } from '@angular/common/http';
import { authInterceptor } from './core/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // Modern provider replacing HttpClientModule
    provideHttpClient(
      withInterceptors([authInterceptor]),
      withFetch() // Uses modern browser Fetch API under the hood
    )
  ]
};
```

#### Example 3: Modern Unit Testing with `HttpTestingController`
```typescript
it('should fetch loan by ID', () => {
  const service = TestBed.inject(LoanApiService);
  const httpMock = TestBed.inject(HttpTestingController);

  service.getLoanById(101).subscribe(loan => {
    expect(loan.applicantName).toBe('Saikiran');
  });

  const req = httpMock.expectOne('https://api.bank.com/v1/loans/101');
  expect(req.request.method).toBe('GET');
  req.flush({ id: 101, applicantName: 'Saikiran', amount: 500000 }); // Mock response

  httpMock.verify();
});
```

---

## Question 28: How do you handle errors in HTTP requests in Angular?

### 1. Previous Knowledge Required
- HTTP Status Codes (400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 500 Internal Error).
- RxJS operators: `catchError`, `throwError`, `retry`.

### 2. Logic & Theory (Deep-Dive)
Errors occur in two forms:
1. **Client-side / Network Errors**: `ErrorEvent` (e.g., DNS resolution failure, network offline).
2. **Backend Server Errors**: `HttpErrorResponse` with HTTP status codes (e.g., 500 server exception, 401 token expired).

#### Enterprise Error Handling Architecture:
- **Centralized Interceptor**: Catch all errors in one global HTTP Interceptor.
  - `401 Unauthorized`: Automatically trigger token refresh or redirect to `/login`.
  - `403 Forbidden`: Display "Access Denied / Insufficient Roles" toast notification.
  - `500 Server Error`: Log error to centralized logging (Datadog/Sentry) and show a generic friendly alert.
- **Service-Level Fallbacks**: Use `catchError()` to return empty fallbacks or user-friendly domain exceptions.
- **Automatic Retries**: Use `retry({ count: 2, delay: 1000 })` for idempotent GET requests to tolerate transient network blips.

### 3. How to Answer in an Interview
> *"We handle HTTP errors through a two-tiered architecture: centralized handling via an HTTP Interceptor, and localized handling inside services.
>
> *In the HTTP Interceptor, we pipe the request through the RxJS `catchError` operator. We inspect the `HttpErrorResponse`: for 401s, we initiate token refresh or redirect to login; for 5xx errors, we display global notification banners and log to monitoring tools like Sentry.
>
> *For localized queries, we use `retry()` to recover from temporary network drops, and use `catchError` to re-throw user-friendly domain error messages to components."*

### 4. Three Practical Examples

#### Example 1: Global Functional HTTP Interceptor (Angular 15+)
```typescript
export const globalErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unexpected error occurred.';

      if (error.error instanceof ErrorEvent) {
        // Client-side network error
        errorMessage = `Network Error: ${error.error.message}`;
      } else {
        // Backend HTTP error
        switch (error.status) {
          case 401:
            toastService.showError('Session expired. Please login again.');
            router.navigate(['/login']);
            break;
          case 403:
            toastService.showError('You do not have permission to perform this action.');
            break;
          case 500:
            toastService.showError('Internal server error. Our engineering team has been notified.');
            break;
        }
      }
      return throwError(() => new Error(errorMessage));
    })
  );
};
```

#### Example 2: Transient Network Retry with Delay
```typescript
@Injectable({ providedIn: 'root' })
export class MarketRateService {
  private http = inject(HttpClient);

  fetchTreasuryRates(): Observable<RateDTO[]> {
    return this.http.get<RateDTO[]>('/api/v1/rates/treasury').pipe(
      // Retry 2 times with a 1-second delay before failing
      retry({ count: 2, delay: 1000 }),
      catchError(err => {
        console.error('Failed after 2 retries, returning cached rates', err);
        return of(FALLBACK_RATES); // Return graceful fallback data
      })
    );
  }
}
```

#### Example 3: Handling Form Validation Errors (400 Bad Request) in Component
```typescript
submitLoan() {
  this.loanService.submit(this.loanForm.value).subscribe({
    next: (res) => this.router.navigate(['/success', res.id]),
    error: (err) => {
      if (err.status === 400 && err.error?.fieldErrors) {
        // Map backend validation errors directly onto Reactive Form controls!
        Object.entries(err.error.fieldErrors).forEach(([field, msg]) => {
          this.loanForm.get(field)?.setErrors({ backendError: msg });
        });
      }
    }
  });
}
```

### 5. Real-World Project Application
- In **Banking APIs**, mapping backend 400 Bad Request error payloads (`fieldErrors: { panNumber: "Already linked to active loan #8401" }`) directly onto form controls provides instant feedback to bank branch operators without full page reloads.

---

*Continue to [Chapter 20: RxJS, Observables, State & Performance](file:///Users/apple/Workspace/Home/Javascript/Learning/20_Angular_RxJS_Observables_State_and_Performance.md)*
