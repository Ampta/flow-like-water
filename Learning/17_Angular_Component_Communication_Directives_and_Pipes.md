# Chapter 17: Component Communication, Directives & Pipes (Questions 11–15)

This guide covers Questions 11 to 15 of the Angular Interview Series with deep-dive technical logic, interview scripts, 3 distinct examples per question, and real-world project applications.

---

## 📑 Question Index
11. [Explain the difference between `@Input()` and `@Output()` decorators.](#question-11-explain-the-difference-between-input-and-output-decorators)
12. [How can you pass data between parent and child components?](#question-12-how-can-you-pass-data-between-parent-and-child-components)
13. [What are Structural Directives and Attribute Directives?](#question-13-what-are-structural-directives-and-attribute-directives)
14. [What is the difference between `ngClass` and `ngStyle`?](#question-14-what-is-the-difference-between-ngclass-and-ngstyle)
15. [What are Pipes in Angular? Explain built-in pipes and how to create a custom pipe.](#question-15-what-are-pipes-in-angular-explain-built-in-pipes-and-how-to-create-a-custom-pipe)

---

## Question 11: Explain the difference between `@Input()` and `@Output()` decorators.

### 1. Previous Knowledge Required
- Unidirectional Data Flow (Data down, events up).
- RxJS `EventEmitter`.

### 2. Logic & Theory (Deep-Dive)
In Angular's component hierarchy, components must share data while remaining loosely coupled:
- **`@Input()`**: Marks a class field as an **input property**, meaning it can receive data passed down from its parent component template via property binding `[childProp]="parentData"`.
- **`@Output()`**: Marks a class field as an **output property**, instantiated as an `EventEmitter<T>`. It allows the child component to emit custom events up to the parent component via event binding `(customEvent)="parentMethod($event)"`.
- **The Golden Rule of Component Architecture**: *"Data down via `@Input()`, Events up via `@Output()`"*.

```
   +-----------------------------------------------+
   |               Parent Component                |
   +-----------------------------------------------+
         |                                   ^
         | [data]="parentValue"              | (onAction)="handleEvent($event)"
         v                                   |
   +-----------------------------------------------+
   |               Child Component                 |
   |   @Input() data;        @Output() onAction;   |
   +-----------------------------------------------+
```

### 3. How to Answer in an Interview
> *"In Angular, `@Input()` and `@Output()` facilitate communication between parent and child components adhering to unidirectional data flow.
>
> *`@Input()` allows a parent component to pass data downward into a child component via property binding. 
> `@Output()` uses an `EventEmitter` to dispatch events upward from the child to the parent in response to user actions or state changes. The parent listens using event binding and captures the emitted payload using `$event`."*

### 4. Three Practical Examples

#### Example 1: Basic Input and Output
```typescript
// Child Component
@Component({
  selector: 'app-kyc-badge',
  standalone: true,
  template: `
    <span [class.verified]="isVerified">{{ applicantName }}</span>
    <button (click)="requestReverification()">Re-verify</button>
  `
})
export class KycBadgeComponent {
  @Input() applicantName: string = '';
  @Input() isVerified: boolean = false;
  @Output() reVerify = new EventEmitter<string>();

  requestReverification() {
    this.reVerify.emit(this.applicantName); // Emits name to parent
  }
}

// Parent Template Usage:
// <app-kyc-badge [applicantName]="currentApplicant.name" 
//                [isVerified]="currentApplicant.verified" 
//                (reVerify)="onKycReVerify($event)">
// </app-kyc-badge>
```

#### Example 2: Intercepting `@Input()` Changes via Setter/Getter
You can execute logic whenever an input changes without needing `ngOnChanges`:
```typescript
@Component({
  selector: 'app-loan-amount-display',
  template: `<p>Sanctioned Amount: {{ formattedAmount }}</p>`
})
export class LoanAmountDisplayComponent {
  private _amount: number = 0;
  formattedAmount: string = '';

  @Input()
  set amount(val: number) {
    this._amount = val;
    this.formattedAmount = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val);
  }

  get amount(): number {
    return this._amount;
  }
}
```

#### Example 3: Modern Angular 17.1+ Signal Inputs & Outputs
Angular 17+ introduced signal-based inputs and outputs for improved type safety and performance:
```typescript
import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-credit-check',
  standalone: true,
  template: `
    <p>Credit Score: {{ score() }}</p>
    <button (click)="notifyApproved()">Approve</button>
  `
})
export class CreditCheckComponent {
  // Signal-based input: Read-only signal, required input
  score = input.required<number>();

  // Modern output function
  approved = output<{ score: number; timestamp: number }>();

  notifyApproved() {
    this.approved.emit({ score: this.score(), timestamp: Date.now() });
  }
}
```

### 5. Real-World Project Application
- In a **Banking Customer 360 Dashboard**, the parent dashboard passes the customer's account list to multiple child summary widgets via `@Input()`. When a user clicks "Freeze Card" in the card widget, it emits `@Output() cardFrozen` to notify the parent to refresh all balances.

---

## Question 12: How can you pass data between parent and child components?

### 1. Previous Knowledge Required
- Component hierarchy, DOM queries, Template reference variables (`#var`).

### 2. Logic & Theory (Deep-Dive)
There are **4 primary mechanisms** to pass data between parent and child components:
1. **`@Input()` and `@Output()`**: Standard mechanism for direct parent-child communication.
2. **Template Reference Variable (`#childRef`)**: In the parent template, assign a reference `#child` to the child component tag, allowing the parent template to directly access public child fields and methods.
3. **`@ViewChild()` / `@ViewChildren()`**: In the parent TypeScript class, query the child component instance to programmatically call its methods or read properties.
4. **Shared Service with RxJS `BehaviorSubject`**: When components are deeply nested, or when sibling/unrelated components need to communicate without passing props through 5 intermediate components (prop drilling).

### 3. How to Answer in an Interview
> *"We have four primary ways to communicate between parent and child:
> 1. `@Input()` and `@Output()` for standard property and event flow.
> 2. Template Reference Variables (`#childRef`) to call child methods directly from the parent HTML template.
> 3. `@ViewChild()` in the parent TypeScript class when we need programmatic control over child component instances after view initialization (`ngAfterViewInit`).
> 4. Shared Singleton Services backed by RxJS `BehaviorSubject` when communicating between deeply nested components or sibling components to avoid prop drilling."*

### 4. Three Practical Examples

#### Example 1: Template Reference Variable (`#child`)
```html
<!-- Parent Template -->
<app-disbursement-timer #timer></app-disbursement-timer>

<button (click)="timer.startCountdown()">Start Timer</button>
<button (click)="timer.resetCountdown()">Reset</button>
<p>Seconds Remaining: {{ timer.secondsLeft }}</p>
```

#### Example 2: `@ViewChild()` Programmatic Access
```typescript
@Component({
  selector: 'app-parent-audit',
  template: `
    <app-signature-pad #sigPad></app-signature-pad>
    <button (click)="exportSignature()">Save Application</button>
  `
})
export class ParentAuditComponent implements AfterViewInit {
  @ViewChild('sigPad') signaturePadComponent!: SignaturePadComponent;

  ngAfterViewInit() {
    // Child is guaranteed ready in ngAfterViewInit
    console.log('Signature pad initialized:', this.signaturePadComponent);
  }

  exportSignature() {
    const rawImage = this.signaturePadComponent.getEncodedBase64Image();
    this.saveToDatabase(rawImage);
  }
}
```

#### Example 3: Shared Service with `BehaviorSubject` (Decoupled Sibling / Deep Hierarchy)
```typescript
@Injectable({ providedIn: 'root' })
export class LoanContextService {
  // Holds current state and emits immediately to new subscribers
  private selectedLoanSubject = new BehaviorSubject<LoanDetails | null>(null);
  public selectedLoan$ = this.selectedLoanSubject.asObservable();

  setLoan(loan: LoanDetails) {
    this.selectedLoanSubject.next(loan);
  }
}
```

### 5. Real-World Project Application
- In **Loan Origination Systems (LOS)**, the `DocumentUploadComponent` uses `@ViewChild` on a `PdfViewerComponent` to jump directly to the specific page containing the applicant's signature when an underwriter clicks "Verify Signature".

---

## Question 13: What are Structural Directives and Attribute Directives?

### 1. Previous Knowledge Required
- HTML DOM Tree manipulation vs CSS/Attribute manipulation.
- `<ng-template>` element.

### 2. Logic & Theory (Deep-Dive)

| Criterion | Structural Directives | Attribute Directives |
| :--- | :--- | :--- |
| **DOM Impact** | **Changes DOM structure**: Adds, replaces, or completely destroys DOM elements. | **Changes DOM appearance or behavior**: Modifies style, classes, or event handlers without destroying the element. |
| **Syntax** | Prefixed with an asterisk (`*ngIf`, `*ngFor`). | Surrounded by brackets or used as plain attributes (`[ngClass]`, `[ngStyle]`, `appHighlight`). |
| **Templates** | De-sugars into an `<ng-template>` wrapper under the hood. | Binds directly to the host element via `ElementRef` and `Renderer2`. |
| **Quantity Rule**| **Only ONE structural directive** can be applied per element! | Multiple attribute directives can be placed on a single element. |

#### Why Only One Structural Directive Per Element?
Angular de-sugars `*directive` into `<ng-template [directive]>`. Having two structural directives (e.g., `*ngIf` and `*ngFor` on the same `<div>`) creates ambiguity about which template wraps which! To combine them, you must wrap one inside an `<ng-container>`:
```html
<ng-container *ngIf="loans.length > 0">
  <div *ngFor="let loan of loans">{{ loan.id }}</div>
</ng-container>
```

### 3. How to Answer in an Interview
> *"The fundamental difference is their impact on the DOM.
>
> *Structural Directives alter the actual DOM layout by adding, removing, or manipulating elements. They are designated with an asterisk (`*ngIf`, `*ngFor`), which Angular internally expands into an `<ng-template>`. You can only have one structural directive per host element.
>
> *Attribute Directives alter the appearance, styling, or behavior of an existing DOM element without altering the DOM tree (e.g., `ngClass`, `ngStyle`, or a custom drag-and-drop directive). You can place multiple attribute directives on a single element."*

### 4. Three Practical Examples

#### Example 1: Custom Attribute Directive (`appPermissionProtect`)
Disables and grays out buttons if the user lacks the required security role:
```typescript
@Directive({
  selector: '[appRoleProtect]',
  standalone: true
})
export class RoleProtectDirective implements OnInit {
  @Input('appRoleProtect') requiredRole!: string;

  constructor(private el: ElementRef, private authService: AuthService, private renderer: Renderer2) {}

  ngOnInit() {
    if (!this.authService.hasRole(this.requiredRole)) {
      this.renderer.setAttribute(this.el.nativeElement, 'disabled', 'true');
      this.renderer.setStyle(this.el.nativeElement, 'opacity', '0.4');
      this.renderer.setStyle(this.el.nativeElement, 'cursor', 'not-allowed');
    }
  }
}
// Usage: <button [appRoleProtect]="'ROLE_CREDIT_OFFICER'">Approve Loan</button>
```

#### Example 2: Custom Structural Directive (`*appRoleAuthorize`)
Removes the element from the DOM entirely if the user does not have permission:
```typescript
@Directive({
  selector: '[appRoleAuthorize]',
  standalone: true
})
export class RoleAuthorizeDirective {
  private hasView = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  @Input() set appRoleAuthorize(role: string) {
    const isAuthorized = this.authService.hasRole(role);

    if (isAuthorized && !this.hasView) {
      // Stamp out the template into the DOM
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.hasView = true;
    } else if (!isAuthorized && this.hasView) {
      // Destroy element from DOM
      this.viewContainer.clear();
      this.hasView = false;
    }
  }
}
// Usage: <div *appRoleAuthorize="'ROLE_ADMIN'">Admin Control Panel</div>
```

#### Example 3: Combining Directives via `<ng-container>`
```html
<!-- Clean pattern avoiding the "multiple structural directives on one element" compiler error -->
<ng-container *ngIf="isUserLoggedIn">
  <div *ngFor="let notification of notifications; trackBy: trackById"
       [ngClass]="{'unread': !notification.read}"
       appAutoDismiss>
    {{ notification.message }}
  </div>
</ng-container>
```

### 5. Real-World Project Application
- Attribute directives are used for **Credit Card / PAN Auto-Formatting**: As the user types, the directive formats text into `XXXX-XXXX-XXXX-XXXX` dynamically while maintaining cursor position.

---

## Question 14: What is the difference between `ngClass` and `ngStyle`?

### 1. Previous Knowledge Required
- CSS Class selectors (`.alert-danger`) vs Inline CSS styles (`style="color: red;"`).

### 2. Logic & Theory (Deep-Dive)
- **`ngClass`**: Dynamically adds or removes one or multiple **CSS classes** on an HTML element based on conditional expressions.
  - *Best Practice*: Use `ngClass` for maintainable design systems where styling rules live in external `.scss` files.
- **`ngStyle`**: Dynamically applies **inline CSS styles** directly to the DOM element's `style` attribute.
  - *Best Practice*: Use `ngStyle` for dynamic values calculated at runtime that cannot be predefined in CSS classes (e.g., dynamic progress bar width `width: 73%`, coordinate positioning, color pickers).

### 3. How to Answer in an Interview
> *"`ngClass` and `ngStyle` are both attribute directives used for dynamic styling.
>
> *`ngClass` toggles CSS classes on an element based on conditions. It accepts strings, arrays, or key-value object maps (e.g., `{'btn-success': isApproved, 'btn-danger': isRejected}`). This is preferred because styling remains centralized in CSS/SCSS stylesheets.
>
> *`ngStyle` applies inline styles directly to the element. It is best used for runtime dynamic calculations that cannot be hardcoded into CSS classes, such as computing dynamic progress bar percentages or element coordinates."*

### 4. Three Practical Examples

#### Example 1: `ngClass` Object Expression Syntax
```typescript
@Component({
  selector: 'app-cibil-indicator',
  template: `
    <div class="cibil-badge" [ngClass]="{
      'score-excellent': score >= 750,
      'score-average': score >= 650 && score < 750,
      'score-poor': score < 650
    }">
      CIBIL Score: {{ score }}
    </div>
  `,
  styles: [`
    .score-excellent { background-color: #28a745; color: white; }
    .score-average { background-color: #ffc107; color: black; }
    .score-poor { background-color: #dc3545; color: white; }
  `]
})
export class CibilIndicatorComponent {
  score = 780;
}
```

#### Example 2: `ngStyle` for Runtime Calculations (Progress Bar)
```typescript
@Component({
  selector: 'app-kyc-progress',
  template: `
    <div class="progress-container">
      <!-- Width is a dynamically calculated percentage at runtime -->
      <div class="progress-bar" [ngStyle]="{
        'width': completionPercentage + '%',
        'background-color': completionPercentage === 100 ? '#28a745' : '#007bff'
      }">
        {{ completionPercentage }}%
      </div>
    </div>
  `
})
export class KycProgressComponent {
  @Input() completionPercentage = 75; // Dynamic 0-100
}
```

#### Example 3: Modern Angular Single Class/Style Bindings
In modern Angular, plain class and style bindings can replace `ngClass` and `ngStyle` for simpler expressions without importing `CommonModule`:
```html
<!-- Modern Angular Class Binding -->
<button [class.is-loading]="isLoading" [class.btn-primary]="true">Submit</button>

<!-- Modern Angular Style Binding with Units -->
<div [style.font-size.px]="userFontSize" [style.color]="statusColor">Dynamic Text</div>
```

### 5. Real-World Project Application
- In a **Loan Origination System (LOS)**, `ngClass` applies color-coded risk status badges (`PENDING`, `SANCTIONED`, `DEFAULTED`) across loan queues, while `ngStyle` renders dynamic document-zoom percentages in the underwriting review screen.

---

## Question 15: What are Pipes in Angular? Explain built-in pipes and how to create a custom pipe.

### 1. Previous Knowledge Required
- Data transformation in templates without mutating component state.
- Pure Functions (functions with no side effects that return the same output for identical inputs).

### 2. Logic & Theory (Deep-Dive)
A **Pipe** is a TypeScript class decorated with `@Pipe({ name: 'pipeName' })` implementing the `PipeTransform` interface. It transforms raw model values into user-friendly formats directly inside templates using the pipe character `|`:
```html
{{ rawValue | pipeName:arg1:arg2 }}
```

#### Common Built-in Pipes:
- `DatePipe`: `{{ txDate | date:'dd-MMM-yyyy' }}`
- `CurrencyPipe`: `{{ amount | currency:'INR':'symbol':'1.2-2' }}`
- `DecimalPipe` & `PercentPipe`: Number formatting.
- `JsonPipe`: Debugging objects in template `{{ user | json }}`.
- `AsyncPipe`: Automatically subscribes to an RxJS `Observable` or `Promise`, unwraps the emitted value, triggers change detection, and **automatically unsubscribes on component destruction to prevent memory leaks!**

#### Pure vs Impure Pipes:
- **Pure Pipe (`pure: true` - Default)**: Angular executes the pipe **only when it detects a pure change** to the input value (i.e., a primitive change like `string`/`number`, or a new object reference). It does NOT execute on internal object mutations (e.g., `array.push()`). This makes pure pipes blazing fast.
- **Impure Pipe (`pure: false`)**: Angular executes the pipe on **every single change detection cycle**, regardless of whether the input changed. This can cause severe performance degradation if not used cautiously.

### 3. How to Answer in an Interview
> *"Pipes transform data for display in templates without altering the underlying component property.
>
> *Angular includes built-in pipes like `DatePipe`, `CurrencyPipe`, and the critically important `AsyncPipe`, which auto-subscribes to observables and auto-unsubscribes on destroy to prevent memory leaks.
>
> *To create a custom pipe, we implement `PipeTransform` and define the `transform()` method.
>
> *By default, pipes are **Pure**, meaning Angular executes them only when primitive values change or object references change. **Impure pipes** run on every change detection cycle, which can severely degrade performance."*

### 4. Three Practical Examples

#### Example 1: Built-in Pipes & Chaining
```html
<p>Sanction Date: {{ loan.approvedAt | date:'fullDate' | uppercase }}</p>
<p>EMI: {{ loan.monthlyEmi | currency:'INR':'symbol' }}</p>
```

#### Example 2: Custom Pure Pipe (Masking Sensitive Aadhaar / PAN Numbers)
```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'maskSensitive',
  standalone: true,
  pure: true // Default: Caches result until string reference changes
})
export class MaskSensitivePipe implements PipeTransform {
  transform(value: string | null | undefined, visibleDigits = 4): string {
    if (!value) return '';
    const cleanStr = value.trim();
    if (cleanStr.length <= visibleDigits) return cleanStr;

    const maskedLength = cleanStr.length - visibleDigits;
    const maskedSection = 'X'.repeat(maskedLength);
    const visibleSection = cleanStr.slice(-visibleDigits);

    return `${maskedSection}${visibleSection}`;
  }
}
// Usage: {{ '123456789012' | maskSensitive:4 }} ---> Output: XXXXXXXX9012
```

#### Example 3: The Power of `AsyncPipe` (Clean, Zero Memory Leaks)
```typescript
@Component({
  selector: 'app-active-loans',
  standalone: true,
  imports: [CommonModule],
  template: `
    <!-- AsyncPipe automatically subscribes and handles unsubscribing! -->
    <div *ngIf="loans$ | async as loans; else loading">
      <div *ngFor="let loan of loans">{{ loan.referenceNo }}</div>
    </div>
    <ng-template #loading>Loading active loans...</ng-template>
  `
})
export class ActiveLoansComponent {
  // No manual .subscribe() or ngOnDestroy() needed!
  loans$: Observable<LoanSummary[]> = inject(LoanService).getActiveLoans();
}
```

### 5. Real-World Project Application
- In **Banking Compliance**, custom masking pipes (`maskSensitive`) guarantee that customer Aadhaar numbers, PAN cards, and 16-digit credit card numbers are masked on client screens, satisfying PCI-DSS and RBI compliance.

---

*Continue to [Chapter 18: Services, Dependency Injection & Routing](file:///Users/apple/Workspace/Home/Javascript/Learning/18_Angular_Services_DI_and_Routing_Architecture.md)*
