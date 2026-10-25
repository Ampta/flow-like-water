# Chapter 16: Angular Core Fundamentals, CLI & Template Bindings (Questions 1–10)

This master guide covers Questions 1 to 10 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
1. [What are the main features introduced in Angular 7+?](#question-1-what-are-the-main-features-introduced-in-angular-7)
2. [Explain the difference between Angular and AngularJS.](#question-2-explain-the-difference-between-angular-and-angularjs)
3. [What is TypeScript, and why is it used in Angular?](#question-3-what-is-typescript-and-why-is-it-used-in-angular)
4. [Explain the purpose of Angular CLI.](#question-4-explain-the-purpose-of-angular-cli)
5. [What is the difference between `*ngIf`, `*ngFor`, and `[ngSwitch]`?](#question-5-what-is-the-difference-between-ngif-ngfor-and-ngswitch)
6. [How do you create a new Angular project using CLI?](#question-6-how-do-you-create-a-new-angular-project-using-cli)
7. [What are Components, Directives, and Services in Angular?](#question-7-what-are-components-directives-and-services-in-angular)
8. [Explain Interpolation and Property Binding in Angular.](#question-8-explain-interpolation-and-property-binding-in-angular)
9. [What is Two-way Data Binding? How does it work?](#question-9-what-is-two-way-data-binding-how-does-it-work)
10. [What are Event Binding and Attribute Binding?](#question-10-what-are-event-binding-and-attribute-binding)

---

## Question 1: What are the main features introduced in Angular 7+?

### 1. Previous Knowledge Required
- Single Page Application (SPA) architecture.
- Semantic versioning and Angular's 6-month release cadence.

### 2. Logic & Theory (Deep-Dive)
Angular releases major updates twice a year. Understanding key milestones demonstrates seniority:
- **Angular 7 & 8**: CLI prompts, Virtual Scrolling and Drag & Drop in CDK, Differential Loading (shipping modern ES2015+ bundles to modern browsers and ES5 polyfills to legacy browsers), dynamic imports for lazy loading (`import()`).
- **Angular 9 & 10**: The **Ivy Compiler & Runtime** by default. Drastically reduced bundle sizes, faster build times, localized compile errors, and improved debugging.
- **Angular 11–13**: Strict mode by default, View Engine deprecation, automatic inline Google Fonts, removal of IE11 support, streamlined APF (Angular Package Format).
- **Angular 14–15**: **Standalone Components** (eliminating the mandatory need for `NgModule`), Typed Reactive Forms, directive composition API.
- **Angular 16 & 17+**: **Angular Signals** (fine-grained reactivity without Zone.js overhead), **Built-in Control Flow syntax** (`@if`, `@for`, `@switch`), Hydration for SSR, deferrable views (`@defer`).

### 3. How to Answer in an Interview
> *"Angular 7+ marked a major evolution focused on build performance and developer ergonomics. 
> Key historical milestones include the introduction of the Ivy Compiler in Angular 9, which slashed bundle sizes and accelerated incremental builds. 
> In modern versions (v14–v17+), the architecture underwent a revolution with Standalone Components that eliminate `NgModule` boilerplate, Typed Reactive Forms, and in Angular 16/17, the introduction of Angular Signals for fine-grained reactivity and the new built-in control flow (`@if`, `@for`) which outperforms legacy structural directives."*

### 4. Three Practical Examples

#### Example 1: Virtual Scrolling (CDK introduced in Angular 7)
Rendering 10,000 DOM rows causes browser freezing. Virtual scrolling renders only the visible slice in the viewport.
```typescript
import { Component } from '@angular/core';
import { ScrollingModule } from '@angular/cdk/scrolling';

@Component({
  selector: 'app-virtual-scroll-demo',
  standalone: true,
  imports: [ScrollingModule],
  template: `
    <cdk-virtual-scroll-viewport itemSize="40" class="viewport">
      <div *cdkVirtualItem="let item of items" class="item-row">
        {{ item }}
      </div>
    </cdk-virtual-scroll-viewport>
  `,
  styles: [`.viewport { height: 200px; width: 100%; border: 1px solid #ccc; } .item-row { height: 40px; }`]
})
export class VirtualScrollDemoComponent {
  items = Array.from({ length: 10000 }).map((_, i) => `Transaction #${i + 1}`);
}
```

#### Example 2: Standalone Component & Typed Forms (Angular 14/15)
Eliminates `NgModule` and ensures compile-time type safety for form controls.
```typescript
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

interface LoanForm {
  applicantName: FormControl<string>;
  loanAmount: FormControl<number>;
}

@Component({
  selector: 'app-loan-apply',
  standalone: true, // No NgModule required!
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form">
      <input formControlName="applicantName" placeholder="Name" />
      <input formControlName="loanAmount" type="number" placeholder="Amount" />
    </form>
  `
})
export class LoanApplyComponent {
  form = new FormGroup<LoanForm>({
    applicantName: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    loanAmount: new FormControl(0, { nonNullable: true, validators: [Validators.min(1000)] })
  });
}
```

#### Example 3: Angular Signals & New Control Flow (Angular 16/17+)
Signals provide reactive state without Zone.js dirty checking.
```typescript
import { Component, signal, computed } from '@angular/core';

@Component({
  selector: 'app-signal-counter',
  standalone: true,
  template: `
    <h3>Borrower Credit Assessment</h3>
    <p>Base Score: {{ score() }}</p>
    <p>Risk Category: {{ riskLevel() }}</p>
    <button (click)="boostScore()">Add Verified Asset (+50)</button>

    @if (score() >= 750) {
      <div class="approved-badge">Instant Loan Approval Eligible</div>
    } @else {
      <div class="review-badge">Requires Manual Underwriter Review</div>
    }
  `
})
export class SignalCounterComponent {
  score = signal(720);
  riskLevel = computed(() => this.score() >= 750 ? 'LOW' : 'MEDIUM/HIGH');

  boostScore() {
    this.score.update(s => s + 50);
  }
}
```

### 5. Real-World Project Application
- In **Banking Loan Origination Systems (LOS)**, CDK Virtual Scrolling is used to render audit trails containing 50,000 ledger log entries smoothly without crashing mobile browsers.
- Typed Reactive Forms prevent runtime errors when passing financial amounts between components.

---

## Question 2: Explain the difference between Angular and AngularJS.

### 1. Previous Knowledge Required
- Architecture models: MVC (Model-View-Controller) vs Component-Based Architecture.
- JavaScript vs TypeScript.

### 2. Logic & Theory (Deep-Dive)
AngularJS (Angular 1.x) was created by Google in 2010. By 2016, modern web applications outgrew its architecture. Angular (v2+) was a complete rewrite from the ground up:
- **Architecture**: AngularJS used Controllers bound to `$scope`. Angular uses a Component-based tree where each component has its own encapsulated state and HTML template.
- **Language**: AngularJS was pure JavaScript (ES5). Angular is strictly built on TypeScript, offering compile-time type safety, decorators, and OOP.
- **Change Detection**: AngularJS used `$digest` cycles and `$watch` lists. When watches exceeded 2,000, performance collapsed. Angular uses unidirectional change detection guided by Zone.js and reactive Signals.
- **Mobile Support**: AngularJS was never designed for mobile devices. Angular was designed mobile-first with support for AOT, Tree-Shaking, and NativeScript/Ionic.

### 3. How to Answer in an Interview
> *"AngularJS is version 1.x, based on JavaScript and an MVC architecture relying on `$scope` and two-way dirty-checking digest cycles.
>
> *Angular (v2+) is a completely rewritten, modern framework written in TypeScript. It uses a component-based hierarchy, unidirectional data flow, hierarchical dependency injection, and AOT compilation. It is significantly faster, supports server-side rendering (SSR), and has complete mobile-first optimizations."*

### 4. Three Practical Examples

#### Example 1: Architecture Comparison (Code Structure)
- **AngularJS (Legacy $scope controller)**:
```javascript
// AngularJS 1.x
angular.module('app', []).controller('LoanCtrl', function($scope) {
  $scope.title = 'Personal Loan';
  $scope.calculateEmi = function() { /* ... */ };
});
```
- **Modern Angular (Component Class)**:
```typescript
// Angular Component
@Component({
  selector: 'app-loan',
  template: `<h1>{{ title }}</h1><button (click)="calculateEmi()">Calculate</button>`
})
export class LoanComponent {
  title: string = 'Personal Loan';
  calculateEmi(): void { /* ... */ }
}
```

#### Example 2: Change Detection / Event Loop
- **AngularJS**: Developers had to manually trigger `$scope.$apply()` when updating state inside `setTimeout` or raw AJAX callbacks.
- **Modern Angular**: Zone.js monkeys-patches all async APIs (DOM events, timers, XHR), automatically scheduling change detection without manual intervention.

#### Example 3: Dependency Injection
- **AngularJS**: String-based injection array `['$http', function($http){...}]` prone to breaking when code was minified without `ng-annotate`.
- **Modern Angular**: Class-token based DI resolved using TypeScript reflection:
```typescript
constructor(private http: HttpClient, private loanService: LoanService) {}
```

### 5. Real-World Project Application
- Enterprise banks migrated legacy AngularJS customer portals to modern Angular because AngularJS reached Official End-of-Life (EOL), presenting severe PCI-DSS security compliance vulnerabilities.

---

## Question 3: What is TypeScript, and why is it used in Angular?

### 1. Previous Knowledge Required
- JavaScript dynamic typing (`typeof x` changes at runtime).
- Transpilation (converting TS to browser-executable JS).

### 2. Logic & Theory (Deep-Dive)
TypeScript is an open-source, strongly typed superset of JavaScript developed by Microsoft. It compiles down to plain JavaScript.
**Why Angular chose TypeScript:**
1. **Compile-Time Type Checking**: Catches typos, undefined properties, and incorrect method arguments before code ever runs in a browser.
2. **Metadata & Decorators**: Decorators like `@Component`, `@Injectable`, `@Input`, and `@Directive` allow Angular to attach metadata to plain classes for DI and template compilation.
3. **Advanced OOP Constructs**: Native interfaces, abstract classes, generics, enums, and access modifiers (`private`, `protected`, `public`).
4. **Rich IDE Tooling**: Instant auto-completion, refactoring, and inline documentation in VS Code / IntelliJ.

### 3. How to Answer in an Interview
> *"TypeScript is a statically typed superset of JavaScript that compiles down to clean, standards-compliant JavaScript.
>
> *Angular uses TypeScript because it brings compile-time type safety, preventing common runtime bugs like 'undefined is not a function'. Crucially, TypeScript supports Decorators (`@Component`, `@Injectable`), which Angular uses to declare framework metadata and drive its Dependency Injection container."*

### 4. Three Practical Examples

#### Example 1: Type Safety & Interface Contracts
```typescript
export interface CustomerKyc {
  panNumber: string;
  monthlyIncome: number;
  isKycVerified: boolean;
}

export class KycService {
  processApplication(customer: CustomerKyc): string {
    // TypeScript will error at compile time if customer.monthlyIncome is assigned a string!
    if (customer.monthlyIncome > 50000 && customer.isKycVerified) {
      return 'APPROVED';
    }
    return 'REJECTED';
  }
}
```

#### Example 2: Decorator Metadata Pattern
```typescript
// TypeScript Decorators allow Angular to inspect class metadata at runtime
@Injectable({
  providedIn: 'root'
})
export class BankingTokenService {
  private secretToken = 'JWT-TOKEN-XYZ';
}
```

#### Example 3: Generics in HTTP REST Services
```typescript
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiClient {
  constructor(private http: HttpClient) {}

  // Generic method ensuring callers receive strictly-typed response models
  getRecord<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`/api/v1/${endpoint}`);
  }
}
```

### 5. Real-World Project Application
- In enterprise applications with 200+ developer teams, TypeScript interfaces act as contracts with backend Java Spring Boot DTOs, preventing API mismatch bugs.

---

## Question 4: Explain the purpose of Angular CLI.

### 1. Previous Knowledge Required
- Node.js, NPM, build tools like Webpack and Vite.

### 2. Logic & Theory (Deep-Dive)
The Angular CLI (`@angular/cli`) is a command-line interface tool used to initialize, develop, scaffold, test, and maintain Angular applications.
**Core Responsibilities:**
1. **Scaffolding (`ng generate`)**: Uses Angular Schematics to generate components, services, pipes, directives, and guards following official best practices.
2. **Build Automation & Bundling (`ng build`)**: Manages Webpack / Esbuild configurations under the hood, performing Tree-Shaking, AOT compilation, and minification.
3. **Local Dev Server (`ng serve`)**: Provides live-reloading and HMR (Hot Module Replacement) with in-memory compilation.
4. **Testing Harness (`ng test`, `ng e2e`)**: Configures Karma/Jasmine or Jest for unit testing.
5. **Automated Upgrades (`ng update`)**: Automatically updates dependencies and rewrites deprecated code using automated AST codemods.

### 3. How to Answer in an Interview
> *"The Angular CLI is the official command-line tool that automates the entire development lifecycle of an Angular application. 
>
> *It handles project initialization, standardizes architectural scaffolding via schematics (`ng g c`), orchestrates development servers with live reloading (`ng serve`), runs testing suites (`ng test`), and produces optimized, tree-shaken, AOT-compiled production bundles (`ng build --configuration production`). It also provides `ng update` for seamless framework migrations."*

### 4. Three Practical Examples

#### Example 1: Essential CLI Commands
```bash
# 1. Start local dev server with auto-open on port 4200
ng serve --open

# 2. Generate a Standalone component with inline template and SCSS
ng generate component modules/loan-summary --inline-template --style=scss --standalone

# 3. Create production bundle with source maps for APM error tracing
ng build --configuration production --source-map
```

#### Example 2: Custom Schematics in `angular.json`
Configuring defaults so all generated components use OnPush change detection and SCSS:
```json
{
  "schematics": {
    "@schematics/angular:component": {
      "style": "scss",
      "changeDetection": "OnPush",
      "skipTests": false
    }
  }
}
```

#### Example 3: Automated Migration with `ng update`
```bash
# Checks all outdated packages and runs official Angular automated code migrations
ng update @angular/core @angular/cli
```

### 5. Real-World Project Application
- In CI/CD pipelines (Jenkins / GitLab CI / GitHub Actions), `ng build --configuration production` is executed to build dockerized assets deployed to NGINX pods.

---

## Question 5: What is the difference between `*ngIf`, `*ngFor`, and `[ngSwitch]`?

### 1. Previous Knowledge Required
- Structural Directives (`*` prefix) that add, remove, or manipulate DOM elements.
- Angular Template syntax.

### 2. Logic & Theory (Deep-Dive)
All three are **Structural Directives** that alter the physical DOM layout:
- **`*ngIf`**: Conditionally creates or completely destroys a DOM subtree based on a truthy/falsy expression. *(Difference from CSS `display:none`: `*ngIf` completely removes the element and destroys its component lifecycle; `display:none` keeps the component in memory).*
- **`*ngFor`**: Iterates over an iterable collection (arrays, sets) and instantiates a template for each item in the collection.
- **`[ngSwitch]`**: Evaluates a switch expression and displays matching nested `*ngSwitchCase` elements, falling back to `*ngSwitchDefault`. Unlike `*ngIf`, it handles multiple conditional branches cleanly without nested `if-else` blocks.

### 3. How to Answer in an Interview
> *"All three are structural directives that manipulate the DOM structure.
>
> *`*ngIf` conditionally adds or destroys elements from the DOM based on a boolean condition. 
> `*ngFor` iterates over collections and stamps out template elements for each item, supporting `trackBy` for DOM recycling. 
> `[ngSwitch]` is an attribute directive combined with structural `*ngSwitchCase` elements to handle multi-branch conditions cleanly, analogous to a Java switch statement."*

### 4. Three Practical Examples

#### Example 1: Basic `*ngIf` with `else` block
```typescript
@Component({
  selector: 'app-loan-status',
  template: `
    <div *ngIf="isApproved; else pendingBlock" class="alert-success">
      Congratulations! Your loan has been approved.
    </div>
    <ng-template #pendingBlock>
      <div class="alert-warning">Application under review.</div>
    </ng-template>
  `
})
export class LoanStatusComponent {
  isApproved = true;
}
```

#### Example 2: High-Performance `*ngFor` with `trackBy`
Without `trackBy`, Angular re-renders the entire DOM list when array references change!
```typescript
@Component({
  selector: 'app-txn-list',
  template: `
    <ul>
      <li *ngFor="let tx of transactions; index as i; trackBy: trackByTxnId">
        {{ i + 1 }}. {{ tx.referenceNo }} - ${{ tx.amount }}
      </li>
    </ul>
  `
})
export class TxnListComponent {
  transactions = [
    { id: 101, referenceNo: 'TXN-909', amount: 500 },
    { id: 102, referenceNo: 'TXN-910', amount: 1200 }
  ];

  // Crucial for performance: Identifies unique elements by ID
  trackByTxnId(index: number, item: any): number {
    return item.id;
  }
}
```

#### Example 3: `[ngSwitch]` for Multi-State Workflows
```typescript
@Component({
  selector: 'app-kyc-stage',
  template: `
    <div [ngSwitch]="kycStage">
      <p *ngSwitchCase="'PAN_VERIFICATION'">Please enter your 10-digit PAN number.</p>
      <p *ngSwitchCase="'AADHAAR_OTP'">Enter the 6-digit OTP sent to your Aadhaar mobile.</p>
      <p *ngSwitchCase="'BIOMETRIC'">Visit the nearest branch for biometric verification.</p>
      <p *ngSwitchDefault>Application status unknown. Contact support.</p>
    </div>
  `
})
export class KycStageComponent {
  kycStage: 'PAN_VERIFICATION' | 'AADHAAR_OTP' | 'BIOMETRIC' = 'AADHAAR_OTP';
}
```

### 5. Real-World Project Application
- `*ngIf` guards sensitive financial data from rendering on the client until the user has passed multi-factor authentication.
- `trackBy` in `*ngFor` prevents UI flickering when updating real-time stock ticker or loan payment schedules.

---

## Question 6: How do you create a new Angular project using CLI?

### 1. Previous Knowledge Required
- Node.js LTS installed, NPM global package management.

### 2. Logic & Theory (Deep-Dive)
The `ng new` command triggers the Angular Schematic engine. It creates the project root directory, initializes a Git repository, generates configuration files (`angular.json`, `tsconfig.json`, `package.json`), installs NPM packages, and sets up a default root component.
**Key flags for enterprise projects:**
- `--routing=true`: Generates the routing module/routes.
- `--style=scss`: Configures Sass preprocessor.
- `--standalone=true`: Generates a modern standalone application (Angular 17 default).
- `--strict=true`: Enforces strict TypeScript compiler rules.

### 3. How to Answer in an Interview
> *"To create a new Angular project, we first ensure the CLI is installed globally using `npm install -g @angular/cli`. Then we run `ng new project-name`.
>
> *In production environments, we pass recommended flags such as `--routing --style=scss --strict` to configure routing, SCSS styling, and strict type checking. Finally, we navigate to the folder (`cd project-name`) and start the local development server with `ng serve`."*

### 4. Three Practical Examples

#### Example 1: Standard Interactive Project Creation
```bash
# 1. Install CLI globally
npm install -g @angular/cli

# 2. Run creation wizard (prompts for routing and stylesheet format)
ng new banking-app

# 3. Enter project and run
cd banking-app
ng serve
```

#### Example 2: Enterprise Non-Interactive Setup (Scripted)
```bash
# Automated CI/CD script setup with standalone architecture and SCSS
ng new loan-origination-ui \
  --routing=true \
  --style=scss \
  --standalone=true \
  --strict=true \
  --skip-git=false
```

#### Example 3: Multi-Project Monorepo Workspace
```bash
# Creates an empty workspace without an initial application (for micro-frontends/libraries)
ng new enterprise-bank-workspace --create-application=false
cd enterprise-bank-workspace

# Generate independent apps inside the same workspace
ng generate application customer-portal
ng generate application backoffice-admin
ng generate library shared-ui-components
```

### 5. Real-World Project Application
- Enterprise banks maintain a **Monorepo workspace** where `shared-ui-components` (buttons, currency formatters, KYC modals) are compiled as an internal library consumed by multiple separate frontend applications.

---

## Question 7: What are Components, Directives, and Services in Angular?

### 1. Previous Knowledge Required
- Separation of Concerns (Presentation vs Business Logic vs DOM Manipulation).

### 2. Logic & Theory (Deep-Dive)
These are the three fundamental building blocks of Angular:
1. **Component**: A TypeScript class decorated with `@Component({ selector, templateUrl, styleUrls })`. It controls a view (HTML) and holds presentation state. A component is technically a specialized Directive with a template.
2. **Directive**: A TypeScript class decorated with `@Directive({ selector })`. It attaches custom behavior or modifies the appearance and structure of existing DOM elements without owning an HTML template.
3. **Service**: A plain TypeScript class decorated with `@Injectable({ providedIn: 'root' })`. It encapsulates business logic, API communication, and state management, decoupled from any specific UI view.

### 3. How to Answer in an Interview
> *"A **Component** is the fundamental UI building block consisting of a TypeScript class, an HTML template, and CSS styles that controls a screen view.
>
> *A **Directive** is used to add custom behavior or transform the appearance and structure of existing DOM elements (e.g., auto-formatting currency or highlighting inputs).
>
> *A **Service** is a reusable class decorated with `@Injectable` that holds business logic, HTTP calls, and cross-component state, injected wherever needed via Angular's Dependency Injection system."*

### 4. Three Practical Examples

#### Example 1: Component (UI View & Controller)
```typescript
@Component({
  selector: 'app-account-balance',
  template: `<h3>Balance: {{ balance | currency:'INR' }}</h3>`
})
export class AccountBalanceComponent {
  balance = 125000;
}
```

#### Example 2: Directive (DOM Behavior Enhancement)
A custom directive that highlights an input field when focused:
```typescript
@Directive({
  selector: '[appInputHighlight]',
  standalone: true
})
export class InputHighlightDirective {
  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('focus') onFocus() {
    this.renderer.setStyle(this.el.nativeElement, 'border', '2px solid #0056b3');
  }

  @HostListener('blur') onBlur() {
    this.renderer.setStyle(this.el.nativeElement, 'border', '1px solid #ccc');
  }
}
```

#### Example 3: Service (Reusable HTTP Client & Business Logic)
```typescript
@Injectable({ providedIn: 'root' })
export class InterestRateService {
  constructor(private http: HttpClient) {}

  fetchCurrentHomeLoanRate(): Observable<number> {
    return this.http.get<{ rate: number }>('/api/v1/rates/home-loan').pipe(
      map(res => res.rate)
    );
  }
}
```

### 5. Real-World Project Application
- In an **E-Commerce Checkout flow**, the `CartComponent` renders items, the `appCreditCardFormat` Directive automatically adds spaces after every 4 digits typed, and the `PaymentService` communicates with the Stripe/Razorpay backend API.

---

## Question 8: Explain Interpolation and Property Binding in Angular.

### 1. Previous Knowledge Required
- One-Way Data Binding: Flowing data from the TypeScript component class to the HTML template.

### 2. Logic & Theory (Deep-Dive)
- **Interpolation (`{{ expression }}`)**: Embeds dynamic string representations into HTML text nodes. Angular evaluates the expression within the curly braces, converts the result to a string, and injects it into the DOM.
- **Property Binding (`[property]="expression"`)**: Directly binds an expression to a **DOM element property** (not HTML attribute!). This allows passing booleans, numbers, arrays, and complex objects directly into the DOM node or child component `@Input()`.
- **Key Difference**: Interpolation converts values to strings. Property binding preserves the native underlying data types (e.g., passing a boolean `false` to `[disabled]="isFormInvalid"`).

### 3. How to Answer in an Interview
> *"Both Interpolation and Property Binding are forms of one-way data binding from the component class to the view.
>
> *Interpolation uses double curly braces `{{ value }}` and is used to render string values directly inside HTML text nodes.
>
> *Property Binding uses square brackets `[property]="value"` and directly sets the property on the underlying DOM object. We must use property binding when passing non-string values like booleans (e.g., `[disabled]`), complex objects to child component `@Input()` properties, or image sources."*

### 4. Three Practical Examples

#### Example 1: Boolean Property Binding vs String Interpolation Trap
```html
<!-- ❌ WRONG: String "false" is truthy in JavaScript! Button remains DISABLED! -->
<button disabled="{{ isBtnDisabled }}">Submit</button>

<!-- ✅ CORRECT: Sets the actual DOM boolean property. Enabled when false! -->
<button [disabled]="isBtnDisabled">Submit</button>
```

#### Example 2: Property Binding Complex Objects to Child Components
```html
<!-- Passing an entire borrower object to child component @Input() -->
<app-borrower-card [borrowerData]="activeBorrower" [isKycDone]="true"></app-borrower-card>
```

#### Example 3: Dynamic Image Source Binding (Preventing 404s)
```html
<!-- ❌ Browser might evaluate {{ logoUrl }} before Angular compiles, causing a 404 console error -->
<img src="{{ logoUrl }}" />

<!-- ✅ Safe: Property binding waits until logoUrl is resolved by Angular -->
<img [src]="logoUrl" [alt]="companyName" />
```

### 5. Real-World Project Application
- Property binding `[disabled]="loanForm.invalid || isSubmitting"` ensures users cannot spam the "Submit Application" button and trigger duplicate loan disbursement transactions.

---

## Question 9: What is Two-way Data Binding? How does it work?

### 1. Previous Knowledge Required
- Property Binding (`[]` - Component to View).
- Event Binding (`()` - View to Component).
- Banana-in-a-box syntax `[()]`.

### 2. Logic & Theory (Deep-Dive)
Two-way data binding ensures that changes in the component class update the template UI immediately, and user changes in the template UI immediately update the component class property.
**How it works syntactically:**
The syntax is `[(ngModel)]="property"`, famously nicknamed the **"Banana in a Box"** `[()]`.
It is syntactic sugar combining:
1. Property Binding: `[ngModel]="customerName"`
2. Event Binding: `(ngModelChange)="customerName = $event"`

```
Component Class (TS)  <------- Event Binding (ngModelChange) --------  HTML Input (View)
Component Class (TS)  -------- Property Binding [ngModel] ---------->  HTML Input (View)
```

To use `[(ngModel)]`, you must import `FormsModule` from `@angular/forms`.

### 3. How to Answer in an Interview
> *"Two-way data binding synchronizes data between the component class and the template view in both directions simultaneously. 
>
> *Angular accomplishes this using the 'banana-in-a-box' syntax `[(ngModel)]`. Internally, it is syntactic sugar that combines property binding `[ngModel]` to push data to the input, and event binding `(ngModelChange)` to listen for user input and update the class property with `$event`."*

### 4. Three Practical Examples

#### Example 1: Standard `[(ngModel)]` Form Input
```typescript
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-loan-calc',
  standalone: true,
  imports: [FormsModule],
  template: `
    <label>Loan Amount: </label>
    <input [(ngModel)]="loanAmount" type="number" />
    <p>Requested Amount: {{ loanAmount }}</p>
  `
})
export class LoanCalcComponent {
  loanAmount: number = 250000;
}
```

#### Example 2: De-sugaring `[(ngModel)]` for Custom Side-Effects
When you need to trigger a calculation whenever the user types without adding an extra `(input)` listener:
```html
<input 
  [ngModel]="tenureMonths" 
  (ngModelChange)="onTenureChange($event)" 
  type="number" />
```
```typescript
onTenureChange(newTenure: number) {
  this.tenureMonths = newTenure;
  this.recalculateInterest(); // Immediate custom side-effect!
}
```

#### Example 3: Custom Two-Way Binding on Child Component
You can create your own two-way binding without `FormsModule` by pairing `@Input() property` with `@Output() propertyChange`:
```typescript
@Component({
  selector: 'app-counter',
  template: `
    <button (click)="decrement()">-</button>
    <span>{{ count }}</span>
    <button (click)="increment()">+</button>
  `
})
export class CounterComponent {
  @Input() count = 0;
  @Output() countChange = new EventEmitter<number>(); // Must match: <inputName>Change!

  increment() { this.countChange.emit(this.count + 1); }
  decrement() { this.countChange.emit(this.count - 1); }
}

// Parent Usage:
// <app-counter [(count)]="loanTenureYears"></app-counter>
```

### 5. Real-World Project Application
- Used extensively in interactive financial calculators (e.g., Loan EMI Calculator sliders) where dragging an interest rate slider immediately recalculates and re-renders monthly installments.

---

## Question 10: What are Event Binding and Attribute Binding?

### 1. Previous Knowledge Required
- DOM events (`click`, `keyup`, `submit`, `change`).
- HTML Attributes vs DOM Properties (`attr.colspan`, `aria-label`).

### 2. Logic & Theory (Deep-Dive)

#### 1. Event Binding (`(eventName)="handlerMethod($event)"`):
Flows data from the template to the component class in response to user actions. Angular uses parentheses `()`. The `$event` payload provides the native DOM event or custom emitted data.

#### 2. Attribute Binding (`[attr.attributeName]="expression"`):
- In HTML, attributes are defined in the source markup; properties exist on the instantiated DOM object.
- Most HTML attributes have 1-to-1 mappings with DOM properties (e.g., `id`, `value`).
- However, attributes like `colspan`, `rowspan`, `aria-*` (accessibility), and `svg` properties **do NOT exist as DOM properties!** Attempting `[colspan]="2"` causes Angular to throw a template error: *"Can't bind to 'colspan' since it isn't a known property"*.
- To target these HTML attributes directly, Angular provides Attribute Binding: `[attr.colspan]="val"`.

### 3. How to Answer in an Interview
> *"Event Binding captures user interactions (such as clicks, keystrokes, or custom child events) and triggers a method in the component class using parentheses `(click)="onClick($event)"`.
>
> *Attribute Binding is used when an HTML attribute does not have a corresponding DOM property mapping—such as `colspan`, `rowspan`, ARIA accessibility tags (`aria-label`), or SVG attributes. We bind to them using the `[attr.attribute-name]` syntax so Angular writes directly to the HTML attribute node."*

### 4. Three Practical Examples

#### Example 1: Event Binding with Event Filtering
```typescript
@Component({
  selector: 'app-search-box',
  template: `
    <!-- Captures only Enter keypress -->
    <input (keyup.enter)="onSearch(searchInput.value)" #searchInput placeholder="Press Enter to search..." />
    <button (click)="onSearch(searchInput.value)">Search</button>
  `
})
export class SearchBoxComponent {
  onSearch(query: string) {
    console.log('Searching for:', query);
  }
}
```

#### Example 2: Attribute Binding for Table `colspan` & `rowspan`
```html
<table class="table">
  <tr>
    <!-- [colspan]="2" will FAIL with an Angular compilation error! -->
    <!-- [attr.colspan]="2" binds directly to the HTML attribute -->
    <th [attr.colspan]="colSpanValue">Applicant KYC Verification Details</th>
  </tr>
  <tr>
    <td>PAN Number</td>
    <td>ABCDE1234F</td>
  </tr>
</table>
```

#### Example 3: Accessibility (ARIA) & SVG Attribute Binding
```html
<!-- Accessibility compliance for Screen Readers -->
<button [attr.aria-label]="isMuted ? 'Unmute microphone' : 'Mute microphone'"
        (click)="toggleAudio()">
  <svg>
    <!-- SVG attributes must be attribute-bound -->
    <circle [attr.cx]="50" [attr.cy]="50" [attr.r]="radius" />
  </svg>
</button>
```

### 5. Real-World Project Application
- In enterprise banking applications requiring **WCAG 2.1 AA Accessibility Compliance**, attribute binding (`[attr.aria-expanded]`, `[attr.aria-valuenow]`) ensures screen readers can announce loan approval progress to visually impaired users.

---

*Continue to [Chapter 17: Component Communication, Custom Directives & Pipes](file:///Users/apple/Workspace/Home/Javascript/Learning/17_Angular_Component_Communication_Directives_and_Pipes.md)*
