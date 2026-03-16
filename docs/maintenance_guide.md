# Vacation Scheduler – Maintenance & Setup Guide

This guide covers everything a developer needs to set up, build, run, and maintain the Vacation Scheduler Android application.

---

## Prerequisites

| Tool | Minimum Version | Notes |
|---|---|---|
| Android Studio | Hedgehog (2023.1.1) or newer | Download from developer.android.com |
| JDK | 11 | Bundled with Android Studio |
| Android SDK | API 26 (Android 8.0) minimum | API 36 compile SDK required |
| Gradle | 8.x | Managed by the Gradle wrapper — no separate install needed |
| Git | Any recent version | For cloning the repository |

---

## 1. Cloning the Repository

```bash
git clone https://gitlab.com/wgu-gitlab-environment/student-repos/NardosA/d424-software-engineering-capstone.git
cd d424-software-engineering-capstone
git checkout working-branch
```

---

## 2. Opening in Android Studio

1. Launch Android Studio.
2. Select **File → Open** (or **Open** from the Welcome screen).
3. Navigate to the cloned folder and click **OK**.
4. Android Studio will detect the Gradle project and begin syncing. Wait for the sync to complete (watch the status bar at the bottom).
5. If prompted to upgrade the Gradle plugin, choose **Don't remind me again for this project** unless you intend to upgrade.

---

## 3. Project Structure

```
app/
├── src/main/
│   ├── java/com/example/vacation_sceduler/
│   │   ├── ui/                         # BaseDetailActivity (abstract)
│   │   ├── entities/                   # Vacation, Excursion, ScheduleItem
│   │   ├── dao/                        # VacationDao, ExcursionDao
│   │   ├── database/                   # VacationDatabase, VacationRepository
│   │   ├── adapters/                   # VacationAdapter, ExcursionAdapter
│   │   ├── receivers/                  # VacationAlertReceiver
│   │   ├── MainActivity.java
│   │   ├── VacationListActivity.java
│   │   ├── VacationDetailActivity.java
│   │   ├── ExcursionDetailActivity.java
│   │   └── ReportActivity.java
│   ├── res/
│   │   ├── layout/                     # XML layouts for each screen
│   │   ├── menu/                       # Options menu XML
│   │   ├── drawable/                   # Icons and shape drawables
│   │   └── values/                     # strings.xml, colors.xml, themes.xml
│   └── AndroidManifest.xml
├── proguard-rules.pro
└── build.gradle.kts
docs/
├── design_document.md
├── maintenance_guide.md
├── user_guide.md
└── storyboard.md
```

---

## 4. Building the Application

### Debug Build (for development and testing)

In Android Studio, click the green **Run** button (▶) or use:

```
Build → Make Project   (Ctrl+F9)
Run → Run 'app'        (Shift+F10)
```

### Release Build (for submission or distribution)

1. Go to **Build → Generate Signed Bundle / APK**.
2. Select **APK**.
3. Create or select a keystore file.
4. Choose the `release` build variant.
5. Click **Finish**. The signed APK will be placed in `app/release/`.

> **Note:** The release build has R8 minification enabled (`isMinifyEnabled = true`). ProGuard keep rules in `proguard-rules.pro` protect all Room entity and DAO classes from being stripped.

---

## 5. Running on a Device or Emulator

### Physical Device
1. Enable **Developer Options** on the Android device (Settings → About Phone → tap Build Number 7 times).
2. Enable **USB Debugging** under Developer Options.
3. Connect the device via USB.
4. Select the device from the target dropdown in Android Studio and click Run.

### Emulator
1. Open **Device Manager** (Tools → Device Manager).
2. Click **Create Device**, select a phone profile (e.g., Pixel 6), and choose a system image at API 26 or higher.
3. Start the emulator and select it as the run target.

---

## 6. Database

The app uses **Room** (SQLite wrapper). The database file is named `vacation_database` and is stored in the app's internal storage:

```
/data/data/com.example.vacation_sceduler/databases/vacation_database
```

This location is only accessible on rooted devices or via Android Studio's **App Inspection** tool:

1. Open **View → Tool Windows → App Inspection**.
2. Select the running process.
3. Navigate to **Database Inspector** to view, query, and modify the `vacations` and `excursions` tables live.

### Schema Changes
The database version is set to `1` in `VacationDatabase.java`. If you change the schema (add/remove columns or tables):

1. Increment `version = 1` to the next integer.
2. Provide a `Migration` object, or keep `fallbackToDestructiveMigration()` (current setting, which drops and recreates the database on version mismatch — acceptable for development, not production).

---

## 7. Key Dependencies

All dependencies are declared in `app/build.gradle.kts`:

| Library | Purpose |
|---|---|
| `androidx.room:room-runtime` | SQLite ORM for structured database access |
| `androidx.room:room-compiler` | Annotation processor that generates DAO implementations |
| `com.google.android.material:material` | Material Design 3 UI components |
| `androidx.recyclerview:recyclerview` | Efficient scrolling list views |
| `androidx.constraintlayout:constraintlayout` | Flexible UI layout system |
| `androidx.appcompat:appcompat` | Backward-compatible Activity and UI support |

To add a new dependency, edit `app/build.gradle.kts` under `dependencies { }` and sync Gradle.

---

## 8. Notifications and Alarms

The app uses `AlarmManager` to schedule exact alarms and `NotificationCompat` to display them via `VacationAlertReceiver`.

**Permissions required (declared in `AndroidManifest.xml`):**
- `POST_NOTIFICATIONS` — required on Android 13+ (API 33) for showing notifications
- `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` — required for exact alarm scheduling

On Android 12+ (API 31+), the app checks `alarmManager.canScheduleExactAlarms()` before scheduling. If the user has not granted exact alarm permission, the app falls back to an inexact alarm.

---

## 9. Running Tests

```
Run → Run 'All Tests'
```

Or via Gradle:

```bash
./gradlew test                  # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests (requires device/emulator)
```

Test files are located at:
- `app/src/test/` — unit tests
- `app/src/androidTest/` — instrumented (on-device) tests

---

## 10. Common Maintenance Tasks

| Task | How To |
|---|---|
| Add a new field to Vacation | Edit `Vacation.java`, update the DAO query and Repository, increment DB version, add migration |
| Add a new screen | Create a new Activity class, add its layout XML, register it in `AndroidManifest.xml` |
| Change the notification channel | Edit `VacationAlertReceiver.CHANNEL_ID` and `createNotificationChannel()` |
| Update string resources | Edit `app/src/main/res/values/strings.xml` |
| Change the app theme/colors | Edit `app/src/main/res/values/themes.xml` and `colors.xml` |
| Update ProGuard rules | Edit `app/proguard-rules.pro` |
