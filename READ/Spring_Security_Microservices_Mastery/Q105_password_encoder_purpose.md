# Q105: What is the purpose of PasswordEncoder in Spring Security?

---

## PART 1: BACKEND (Spring Boot)

### 1. Real-World Project Scenario
A legacy e-commerce company suffered a database breach where the user table was dumped to a public forum. Because passwords were saved using unsalted MD5 hashes, hackers cracked 80% of 500,000 passwords within hours using pre-computed rainbow tables. The company had to urgently migrate to an adaptive, salted algorithm (`BCrypt` / `Argon2`). However, they could not force all 500,000 customers to reset their passwords on day one.

Using Spring Security's **`PasswordEncoder`** and **`DelegatingPasswordEncoder`** allowed the application to verify legacy passwords when users logged in, and transparently re-hash and upgrade them to `BCrypt` with a cost factor of 12 on the fly without user interruption.

---

### 2. Concept & Architecture

```
                  Raw Password (Plaintext from User)
                                │
                                ▼
┌─────────────────────── PasswordEncoder ───────────────────────┐
│                                                               │
│   String encode(CharSequence rawPassword);                    │
│   boolean matches(CharSequence rawPassword, String encoded);  │
│   boolean upgradeEncoding(String encodedPassword);            │
│                                                               │
└───────────────────────────────┬───────────────────────────────┘
                                │
        ┌───────────────────────┴───────────────────────┐
        ▼                                               ▼
┌──────────────────────────────┐        ┌──────────────────────────────┐
│    BCryptPasswordEncoder     │        │  DelegatingPasswordEncoder   │
│                              │        │                              │
│ • Adaptive Work Factor (10-14│        │ Formats hash with prefix:    │
│ • Automatic 128-bit Salt     │        │ {bcrypt}$2a$12$...           │
│ • Resistant to GPU attacks   │        │ {pbkdf2}e823...              │
│                              │        │ {argon2}$argon2id$...        │
└──────────────────────────────┘        └──────────────────────────────┘
```

#### Why Fast Hashes (MD5, SHA-256) Fail for Passwords:
Modern GPUs can compute over 100 billion SHA-256 hashes per second. A secure password hashing algorithm must be **computationally expensive** (Key Stretching) and **memory hard** (like Argon2id) to defeat ASIC/GPU attacks. `BCrypt` includes a configurable **work factor** (cost factor) where each increment doubles the verification time.

---

### 3. Production Code (Spring Boot 3.x)

#### 1. Delegating PasswordEncoder Bean (`PasswordEncoderConfig.java`)
```java
package com.enterprise.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        String defaultEncodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        
        // BCrypt with strength 12 (~250ms computation time on server)
        encoders.put("bcrypt", new BCryptPasswordEncoder(12));
        
        // Modern Argon2id for maximum GPU resistance
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        DelegatingPasswordEncoder delegatingPasswordEncoder = 
            new DelegatingPasswordEncoder(defaultEncodingId, encoders);
        
        // Fallback for legacy passwords without prefix
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder(10));
        return delegatingPasswordEncoder;
    }
}
```

#### 2. Automatic Hash Upgrader Service on Login (`AuthService.java`)
```java
package com.enterprise.security.service;

import com.enterprise.security.entity.UserAccount;
import com.enterprise.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void verifyAndUpgradePassword(String username, String rawPassword) {
        UserAccount user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        // 1. Verify credentials
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Bad credentials");
        }

        // 2. Check if algorithm or cost factor needs upgrading (e.g. from cost 10 to 12)
        if (passwordEncoder.upgradeEncoding(user.getPasswordHash())) {
            log.info("Upgrading password hash security for user: {}", username);
            String newHash = passwordEncoder.encode(rawPassword);
            user.setPasswordHash(newHash);
            userRepository.save(user);
        }
    }
}
```

---

## PART 2: FRONTEND (Angular Integration)

### 1. Practical Application & Architecture
While password hashing occurs strictly on the backend, the Angular frontend plays a critical role in password security:
1. **Never transmit passwords insecurely:** Enforce HTTPS and never log credentials to developer consoles or analytics.
2. **Entropy & Strength Estimation:** Guide the user in creating resilient passwords that resist dictionary attacks before form submission using dynamic Reactive Form validation.

---

### 2. Production Code (Angular 17+ Standalone)

#### 1. Custom Password Entropy Validator (`password-strength.validator.ts`)
```typescript
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function passwordStrengthValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value || '';
    if (!value) return null;

    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);
    const isLongEnough = value.length >= 10;

    const passwordValid = hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar && isLongEnough;

    if (!passwordValid) {
      return {
        passwordStrength: {
          hasUpperCase,
          hasLowerCase,
          hasNumeric,
          hasSpecialChar,
          isLongEnough
        }
      };
    }
    return null;
  };
}
```

#### 2. Password Strength Meter Component (`password-strength-meter.component.ts`)
```typescript
import { Component, Input, computed } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-password-strength-meter',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="strength-meter">
      <div class="progress-bar-container">
        <div class="progress-bar" [style.width.%]="scorePercentage()" [ngClass]="strengthClass()"></div>
      </div>
      <span class="strength-label">Strength: {{ strengthLabel() }}</span>
    </div>
  `,
  styles: [`
    .progress-bar-container { height: 6px; width: 100%; background: #e0e0e0; border-radius: 3px; margin-top: 4px; }
    .progress-bar { height: 100%; transition: width 0.3s ease; }
    .weak { background-color: #f44336; }
    .medium { background-color: #ff9800; }
    .strong { background-color: #4caf50; }
  `]
})
export class PasswordStrengthMeterComponent {
  @Input({ required: true }) password = '';

  private readonly score = computed(() => {
    const pwd = this.password;
    if (!pwd) return 0;
    let s = 0;
    if (pwd.length >= 10) s += 25;
    if (/[A-Z]/.test(pwd)) s += 25;
    if (/[0-9]/.test(pwd)) s += 25;
    if (/[^A-Za-z0-9]/.test(pwd)) s += 25;
    return s;
  });

  readonly scorePercentage = computed(() => this.score());
  readonly strengthClass = computed(() => {
    const s = this.score();
    if (s <= 50) return 'weak';
    if (s <= 75) return 'medium';
    return 'strong';
  });
  readonly strengthLabel = computed(() => {
    const s = this.score();
    if (s <= 50) return 'Weak';
    if (s <= 75) return 'Medium';
    return 'Strong (Compliant)';
  });
}
```
