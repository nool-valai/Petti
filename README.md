# 📦 Petti

### Your storage. Visualised.

**Petti is an open-source Android storage visualiser developed by [Nool Valai](https://github.com/nool-valai).** It aims to make understanding device storage simpler through a visual interface built around a Bento-style grid.

Instead of navigating through multiple system settings screens, Petti provides a dedicated place to explore storage information and understand how your device's available space is being used.

Built with **Kotlin, Jetpack Compose, and Material 3**, Petti is designed around a modern native Android experience.

---

## ✨ Features

### 📊 Visual Storage Overview

Explore your device's storage through a visual dashboard designed to make storage information easier to understand.

- Bento-style grid interface.
- Storage information presented through dedicated visual components.
- A consolidated view of available storage information.
- A foundation for expanding storage visualisation capabilities.

### 📁 Storage Analysis

Petti uses Android's storage APIs and permissions to access information about device storage.

Depending on Android's access restrictions and the data available on your device, the application can work with storage information associated with:

- Available and used device storage.
- Installed application storage statistics.
- Files and folders accessible to the application.
- Media indexed by Android's media database.

**Note:** Android restricts access to certain categories of storage information. Figures may be incomplete or differ from the totals shown in your device manufacturer's settings.

### 🎨 Modern Android Interface

Petti uses modern Android development technologies to provide a native application experience.

- Jetpack Compose for declarative UI.
- Material 3 components.
- Edge-to-edge application layout.
- Dedicated navigation structure.
- Persistent high-contrast preference.

### ♿ High-Contrast Mode

Petti includes a high-contrast setting that can be enabled from the application settings.

Your preference is stored locally using Android SharedPreferences, allowing the setting to persist between application sessions.

### ⚙️ Native Android Integration

Petti integrates with Android platform capabilities to support storage-related functionality.

- Storage permission handling.
- Usage statistics access.
- AndroidX lifecycle components.
- ViewModel-based UI state management.
- Kotlin Coroutines and Flow-compatible Android architecture.
- WorkManager dependency for background work.

The availability of individual features depends on the implementation, permissions granted, and Android version.

---

## 🖼️ Screenshots

Screenshots will be added here as the project develops.

To add your own screenshots, create an `assets/screenshots/` directory in the repository and place your images inside it.

For example:

```text
assets/
└── screenshots/
    ├── dashboard.png
    ├── storage-overview.png
    └── settings.png
```

Then reference them in this section:

```markdown
![Petti storage dashboard](assets/screenshots/dashboard.png)
```

Actual application screenshots are recommended so visitors can see Petti's interface before installing it.

---

## 🛠️ Tech Stack

Petti is developed using native Android technologies.

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | Declarative UI development |
| Material 3 | UI components and design system |
| AndroidX Activity Compose | Compose integration with Android activities |
| AndroidX Core KTX | Android platform extensions |
| AndroidX Lifecycle | Lifecycle-aware application components |
| ViewModel | UI state and lifecycle-aware logic |
| Kotlin Coroutines | Asynchronous programming support |
| Kotlin Flow | Reactive state and data streams |
| Navigation Compose | Navigation between application destinations |
| WorkManager | Background work infrastructure |
| DocumentFile | Document and file access abstractions |
| Gradle Kotlin DSL | Build configuration |
| JUnit | Unit testing |
| Espresso | Android UI testing support |

### Application details

| Property | Value |
|---|---|
| Application name | Petti |
| Package name | `com.noolvalai.petti` |
| Application ID | `com.noolvalai.petti` |
| Language | Kotlin |
| Minimum Android SDK | API 26 — Android 8.0 |
| Compile SDK | API 37, as configured in the project |
| Target SDK | API 37, as configured in the project |
| Initial version | `1.0` |
| Version code | `1` |
| UI framework | Jetpack Compose |

The configured SDK and dependency versions may require a recent Android Studio installation and compatible Android SDK components.

---

## 📋 Requirements

To build Petti from source, you will need:

- A computer running Windows, macOS, or Linux.
- Android Studio compatible with the project's Android Gradle Plugin.
- A compatible JDK and Gradle environment.
- Android SDK Platform 37, as configured in the project.
- An Android device or emulator running Android 8.0 or newer.
- An internet connection for the initial Gradle dependency download.

The project uses recent Android build tooling. If Gradle sync fails, check the Android Studio, JDK, Android SDK, and plugin compatibility before changing project versions.

---

## 🚀 Getting Started

### 1. Clone the repository

Open a terminal and run:

```bash
git clone https://github.com/nool-valai/Petti.git
```

Move into the project directory:

```bash
cd Petti
```

### 2. Open the project

1. Launch Android Studio.
2. Select **Open**.
3. Navigate to the cloned `Petti` directory.
4. Select the project root containing `settings.gradle.kts`.
5. Allow Android Studio to synchronise the Gradle project.

Wait for the initial dependency downloads and project indexing to finish.

### 3. Configure Android SDK components

If Android Studio reports that the required SDK platform or build tools are missing:

1. Open Android Studio's SDK Manager.
2. Install the SDK platform requested by the project.
3. Confirm that the installed Android Gradle Plugin is supported by your Android Studio version.
4. Synchronise the project again.

Avoid changing the configured SDK versions unless you understand the compatibility implications.

### 4. Run Petti

1. Connect an Android device with USB debugging enabled, or create an emulator.
2. Allow the device to appear in Android Studio.
3. Select the `app` run configuration.
4. Press **Run**.

Android may ask you to grant additional access before all storage-related functionality becomes available.

---

## 🏗️ Building from the Command Line

You can also build Petti without using Android Studio's Run button.

### Windows — PowerShell

Build a debug APK:

```powershell
.\gradlew.bat assembleDebug
```

Run local unit tests:

```powershell
.\gradlew.bat test
```

Run Android instrumented tests on a connected device or emulator:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

### macOS and Linux

Build a debug APK:

```bash
./gradlew assembleDebug
```

Run local unit tests:

```bash
./gradlew test
```

Run Android instrumented tests:

```bash
./gradlew connectedDebugAndroidTest
```

### Find the generated APK

After a successful debug build, the APK is normally located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

You can transfer the APK to a compatible Android device for testing.

**Build verification:** The commands above describe the configured Android Gradle project. A successful build has not been independently verified as part of this README preparation.

---

## Continue the README

Append the following sections to the same README, immediately after the build instructions.

:::writing{variant="document" id="84621" title="Petti README — Permissions, Architecture and Contributing"}
## 🔐 Permissions and Privacy

Petti requires access to certain Android capabilities to support its storage-related functionality.

Android places restrictions on application storage access, so some permissions must be granted manually through system settings.

### Permissions

| Permission | Purpose |
|---|---|
| `MANAGE_EXTERNAL_STORAGE` | Broad file access on supported Android versions |
| `PACKAGE_USAGE_STATS` | Access to application usage and storage statistics where permitted |
| `POST_NOTIFICATIONS` | Permission to display notifications on Android versions that require it |

The project also includes AndroidX WorkManager, which may use Android system services to schedule background work.

### All Files Access

The `MANAGE_EXTERNAL_STORAGE` permission is a special Android permission.

Granting it can allow an application to access a broad range of shared storage files, subject to Android's remaining restrictions.

Users should understand the scope of this permission before enabling it.

### Usage Statistics Access

Android restricts access to application usage statistics. The relevant access must be granted through the appropriate system settings.

The information available to Petti may vary depending on the Android version, device manufacturer, installed applications, and system restrictions.

### Privacy Principles

Petti is intended to help users understand their device storage.

When reviewing or contributing to the project, consider the following privacy principles:

- Request only the permissions necessary for implemented features.
- Explain why sensitive permissions are needed.
- Avoid collecting unnecessary personal information.
- Avoid transmitting file names or personal storage information without a clear, documented reason.
- Handle storage access failures gracefully.
- Never assume that granting a permission guarantees access to every file or storage statistic.

**Privacy statement:** This README does not claim that Petti has undergone an independent security audit. Review the source code and permission requirements before installing the application.

---

## 🧱 Project Architecture

Petti uses a Kotlin-based Android architecture with Jetpack Compose for its user interface.

The application entry point is:

```text
app/src/main/java/com/noolvalai/petti/MainActivity.kt
```

The main activity initialises the Compose interface, applies the application theme, and connects the activity-scoped ViewModel to the navigation graph.

### Architectural components

**MainActivity**

The Android activity responsible for initialising the application and setting up the Compose content.

**UI Layer**

Contains the Compose interface, screens, reusable components, theme definitions, and navigation.

**ViewModel**

Manages screen-related state and provides a lifecycle-aware boundary between UI components and application logic.

**Storage and Data Layer**

Supports the application's storage-related functionality through the relevant Android APIs and data-access implementations.

**Settings**

Uses Android SharedPreferences to persist the high-contrast preference locally.

### Simplified architecture

```text
Petti Android Application
          |
          v
     MainActivity
          |
          v
     Petti Theme
          |
          v
   Navigation Graph
          |
          v
    Compose Screens
          |
          v
       ViewModel
          |
          v
 Storage and Android APIs
```

This diagram represents the general application structure rather than every individual dependency or runtime interaction.

---

## 📂 Repository Structure

The main project structure is organised as follows:

```text
Petti/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/noolvalai/petti/
│   │   │   │       ├── MainActivity.kt
│   │   │   │       ├── data/
│   │   │   │       ├── domain/
│   │   │   │       ├── ui/
│   │   │   │       └── util/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle.properties
└── README.md
```

Some directories may contain additional files as development continues.

| Directory | Responsibility |
|---|---|
| `app/src/main/` | Application source code and resources |
| `data/` | Data-access and storage-related implementations |
| `domain/` | Domain models and application contracts |
| `ui/` | Compose screens, components, navigation, and theme |
| `util/` | Supporting utility functions |
| `app/src/test/` | Local unit tests |
| `app/src/androidTest/` | Instrumented Android tests |
| `gradle/` | Gradle wrapper and dependency version catalogue |

---

## 🧪 Testing

Petti includes the standard foundations for local unit testing and Android instrumented testing.

### Unit tests

Run:

```powershell
.\gradlew.bat test
```

These tests execute locally on the development machine.

### Instrumented tests

Run:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

A compatible Android device or emulator must be connected and available.

### Suggested testing priorities

Storage applications can behave differently across Android versions and device manufacturers. Useful test scenarios include:

- First launch without storage permissions.
- Granting and revoking permissions.
- Devices with very little free storage.
- Devices with large media libraries.
- Empty folders and inaccessible directories.
- Storage access errors.
- High-contrast preference persistence.
- Navigation between the main screen and settings.
- Background work when the application is not visible.

These are recommended test scenarios, not a claim that every scenario has already been tested.

---

## 🗺️ Roadmap

Potential improvements for future versions of Petti include:

- [ ] Add screenshots and a demonstration GIF to the README.
- [ ] Improve storage category visualisation.
- [ ] Expand folder-level storage analysis.
- [ ] Improve handling of inaccessible files and folders.
- [ ] Improve explanations of storage statistics and their limitations.
- [ ] Expand accessibility support.
- [ ] Add more unit tests and instrumented UI tests.
- [ ] Document Android-version-specific behaviour.
- [ ] Introduce automated build verification through GitHub Actions.
- [ ] Publish versioned releases with installation instructions.

This roadmap is intended to guide development. Items are not necessarily implemented in the current version.

---

## 🤝 Contributing

Contributions, bug reports, suggestions, and improvements are welcome.

Whether you are improving the UI, fixing a storage calculation, or making the application more accessible, contributions can help Petti become more useful.

### Reporting a bug

When reporting a problem, include:

- Your device model.
- Your Android version.
- The version of Petti you are using.
- Steps to reproduce the issue.
- The expected behaviour.
- The actual behaviour.
- Relevant logs or screenshots, with personal information removed.

Avoid including private file paths, personal documents, account details, or other sensitive information.

### Contributing code

1. Fork the repository.
2. Create a branch for your change.
3. Make your changes.
4. Test the application where possible.
5. Commit your changes with a descriptive message.
6. Push your branch to your fork.
7. Open a pull request describing the changes.

Example:

```bash
git checkout -b feature/storage-visualisation
```

After making your changes:

```bash
git add .
git commit -m "Improve storage visualisation"
git push origin feature/storage-visualisation
```

Then open a pull request against the main Petti repository.

### Contribution guidelines

- Keep changes focused and easy to review.
- Follow the existing Kotlin and Jetpack Compose conventions.
- Avoid introducing unnecessary dependencies.
- Handle permission failures and unavailable storage information gracefully.
- Document significant changes.
- Do not commit API keys, credentials, private files, or local machine configuration.
- Include tests where appropriate.

---

## 📜 Licence

Petti is intended to be distributed under the **MIT License**, subject to the licence included in the repository.

See the [`LICENSE`](LICENSE) file for the applicable terms.

Third-party dependencies, icons, fonts, and other bundled assets may have their own licence requirements. Check their respective licences before redistribution.

---

## 👨‍💻 About Nool Valai

**Petti is part of Nool Valai**, an independent open-source Android application development initiative.

Nool Valai focuses on building applications that combine practical functionality with thoughtful design and modern Android development technologies.

Explore the organisation's projects:

**[Nool Valai on GitHub](https://github.com/nool-valai)**

---

## ⭐ Support the Project

If you find Petti useful, consider:

- Giving the repository a star.
- Reporting bugs.
- Suggesting improvements.
- Contributing code or documentation.
- Sharing the project with other Android enthusiasts.

Every contribution helps improve the project.

---

<p align="center">
  <strong>Petti — Understand your storage at a glance.</strong>
  <br>
  Built with Kotlin and Jetpack Compose by Nool Valai.
</p>

### Before publishing

There are three things I recommend checking in your repository:

1. **Licence:** Make sure the root `LICENSE` file matches the licence you intend to use.
2. **Screenshots:** Add real screenshots from the running application to make the repository more engaging.
3. **Build configuration:** Verify that the project builds with its configured Android Gradle Plugin and SDK versions.

The README is now structured to serve as the main documentation for Petti on GitHub. It distinguishes between functionality supported by the project structure and features that still need verification, rather than claiming that every proposed capability is already complete.
