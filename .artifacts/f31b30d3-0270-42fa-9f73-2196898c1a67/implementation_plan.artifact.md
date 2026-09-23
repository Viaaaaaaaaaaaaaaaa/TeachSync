# Firebase Backend & Authentication Integration

This plan integrates Firebase as the primary backend for TeachSync, providing real authentication, user management, and email-based OTP sessions via Google's secure infrastructure.

## User Review Required

> [!IMPORTANT]
> To finish the setup, you will need to:
> 1.  Download `google-services.json` from your Firebase Console.
> 2.  Place it in the `app/` directory of your project.
> 3.  Enable **Email/Password** and **Google** sign-in providers in the Firebase Console under the "Authentication" section.

## Proposed Changes

### [Core Dependencies]

#### [MODIFY] [build.gradle.kts](file:///C:/Users/USER/StudioProjects/AssistAi2/app/build.gradle.kts)
- Add Firebase BOM, Firebase Auth, and Firebase Firestore dependencies.
- Add Google Services plugin.

#### [MODIFY] [build.gradle.kts (project)](file:///C:/Users/USER/StudioProjects/AssistAi2/build.gradle.kts)
- Add Google Services classpath.

---

### [Authentication & Validation]

#### [MODIFY] [SignUpActivity.kt](file:///C:/Users/USER/StudioProjects/AssistAi2/app/src/main/java/com/example/assistai/SignUpActivity.kt)
- Integrate `FirebaseAuth` to create real accounts.
- Implement strong validations (e.g., password complexity, real-time email check).
- Store additional user data (like Full Name) in Firestore.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/USER/StudioProjects/AssistAi2/app/src/main/java/com/example/assistai/MainActivity.kt)
- Integrate `FirebaseAuth` for secure sign-in.
- Check for existing sessions on app launch to skip login if the user is already authenticated.

#### [MODIFY] [ForgotPasswordActivity.kt](file:///C:/Users/USER/StudioProjects/AssistAi2/app/src/main/java/com/example/assistai/ForgotPasswordActivity.kt)
- Use `Firebase.auth.sendPasswordResetEmail(email)` to send **real** Gmail-based recovery sessions.

---

### [Session Management]

#### [MODIFY] [EmailMonitoringActivity.kt](file:///C:/Users/USER/StudioProjects/AssistAi2/app/src/main/java/com/example/assistai/EmailMonitoringActivity.kt)
- Use Firestore to manage and persist the "Linked Gmail" state across sessions.
- Replace mock "Connect" logic with real Firebase User data.

#### [MODIFY] [DashboardActivity.kt](file:///C:/Users/USER/StudioProjects/AssistAi2/app/src/main/java/com/example/assistai/DashboardActivity.kt)
- Wire up logout functionality to `FirebaseAuth.signOut()`.

## Verification Plan

### Automated Tests
- Run `app:testDebugUnitTest` to verify that the new validation logic handles edge cases correctly.

### Manual Verification
- Deploy to a device/emulator.
- Perform a sign-up and check the Firebase Console to see the user created.
- Trigger a "Forgot Password" flow and verify a real email is received in the Gmail inbox.
- Test persistent sessions by closing and reopening the app.
