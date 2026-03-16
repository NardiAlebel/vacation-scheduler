# Vacation Scheduler – Design Document

## 1. Application Overview

Vacation Scheduler is a native Android application that allows users to create, manage, and track vacations and their associated excursions. Users can set date-based alerts, share vacation details, search their vacation history, and generate a tabular report of all scheduled trips.

**Platform:** Android (API 26+)
**Language:** Java
**Architecture:** Repository Pattern with Room persistence library
**GitLab Repository:** https://gitlab.com/wgu-gitlab-environment/student-repos/NardosA/d424-software-engineering-capstone
**Branch / Commit:** `working-branch` @ `c1a022d`

---

## 2. Architecture Design Diagram

```mermaid
graph TD
    subgraph Presentation Layer
        A[MainActivity] --> B[VacationListActivity]
        B --> C[VacationDetailActivity]
        B --> F[ReportActivity]
        C --> D[ExcursionDetailActivity]
        C --> E[VacationAdapter]
        D --> G[ExcursionAdapter]
    end

    subgraph Abstract UI Layer
        H[BaseDetailActivity]
        C -.extends.-> H
        D -.extends.-> H
    end

    subgraph Domain Layer
        I[VacationRepository]
        B --> I
        C --> I
        D --> I
        F --> I
    end

    subgraph Persistence Layer
        J[VacationDatabase - Singleton]
        K[VacationDao]
        L[ExcursionDao]
        J --> K
        J --> L
        I --> K
        I --> L
    end

    subgraph Entity / Model Layer
        M[Vacation implements ScheduleItem]
        N[Excursion implements ScheduleItem]
        O[ScheduleItem - Interface]
        M -.implements.-> O
        N -.implements.-> O
        K --> M
        L --> N
    end

    subgraph System Services
        P[AlarmManager]
        Q[NotificationManager]
        R[VacationAlertReceiver]
        H --> P
        P --> R
        R --> Q
    end
```

---

## 3. Class Diagram

```mermaid
classDiagram
    class ScheduleItem {
        <<interface>>
        +getId() int
        +getDisplayTitle() String
        +getDisplayDate() String
    }

    class BaseDetailActivity {
        <<abstract>>
        #DATE_FORMAT String
        #dateFormat SimpleDateFormat
        #isValidDateFormat(String) boolean
        #showDatePicker(TextInputEditText) void
        #setAlert(long, String, String, int) void
    }

    class Vacation {
        -id int
        -name String
        -hotel String
        -startDate String
        -endDate String
        +Vacation(String, String, String, String)
        +getDisplayTitle() String
        +getDisplayDate() String
        +getId() int
        +getName() String
        +getHotel() String
        +getStartDate() String
        +getEndDate() String
    }

    class Excursion {
        -id int
        -title String
        -date String
        -vacationId int
        +Excursion(String, String, int)
        +getDisplayTitle() String
        +getDisplayDate() String
        +getId() int
        +getTitle() String
        +getDate() String
        +getVacationId() int
    }

    class VacationDao {
        <<interface>>
        +insert(Vacation) void
        +update(Vacation) void
        +delete(Vacation) void
        +getAllVacations() List~Vacation~
        +getVacationById(int) Vacation
        +searchVacations(String) List~Vacation~
    }

    class ExcursionDao {
        <<interface>>
        +insert(Excursion) void
        +update(Excursion) void
        +delete(Excursion) void
        +getExcursionsForVacation(int) List~Excursion~
        +getExcursionCountForVacation(int) int
        +getExcursionById(int) Excursion
    }

    class VacationDatabase {
        <<singleton>>
        -INSTANCE VacationDatabase
        +getDatabase(Context) VacationDatabase
        +vacationDao() VacationDao
        +excursionDao() ExcursionDao
    }

    class VacationRepository {
        -vacationDao VacationDao
        -excursionDao ExcursionDao
        -databaseExecutor ExecutorService
        +getAllVacations() List~Vacation~
        +searchVacations(String) List~Vacation~
        +insert(Vacation) void
        +update(Vacation) void
        +delete(Vacation) void
        +getExcursionsForVacation(int) List~Excursion~
        +getExcursionCountForVacation(int) int
        +insert(Excursion) void
        +update(Excursion) void
        +delete(Excursion) void
    }

    class MainActivity {
        +onCreate(Bundle) void
    }

    class VacationListActivity {
        -repository VacationRepository
        -adapter VacationAdapter
        -allVacations List~Vacation~
        -updateList(List~Vacation~) void
        +onCreateOptionsMenu(Menu) boolean
        +onOptionsItemSelected(MenuItem) boolean
    }

    class VacationDetailActivity {
        -editName TextInputEditText
        -editHotel TextInputEditText
        -editStartDate TextInputEditText
        -editEndDate TextInputEditText
        -repository VacationRepository
        -excursionAdapter ExcursionAdapter
        -vacationId int
        -isNewVacation boolean
        -saveVacation() void
        -confirmDeleteVacation() void
        -deleteVacation() void
        -setVacationAlerts() void
        -shareVacation() void
        -isEndDateAfterStartDate(String, String) boolean
    }

    class ExcursionDetailActivity {
        -editTitle TextInputEditText
        -editDate TextInputEditText
        -repository VacationRepository
        -excursionId int
        -vacationId int
        -vacationStartDate String
        -vacationEndDate String
        -saveExcursion() void
        -confirmDeleteExcursion() void
        -deleteExcursion() void
        -setExcursionAlert() void
        -isDateWithinVacation(String) boolean
    }

    class ReportActivity {
        -addHeaderRow(TableLayout) void
        -addDataRow(TableLayout, Vacation, int) void
        -makeCell(String, boolean) TextView
    }

    class VacationAdapter {
        -vacations List~Vacation~
        -context Context
        +setVacations(List~Vacation~) void
        +onCreateViewHolder(ViewGroup, int) VacationViewHolder
        +onBindViewHolder(VacationViewHolder, int) void
        +getItemCount() int
    }

    class ExcursionAdapter {
        -excursions List~Excursion~
        -context Context
        -vacationStartDate String
        -vacationEndDate String
        +setExcursions(List~Excursion~) void
        +setVacationDates(String, String) void
        +onCreateViewHolder(ViewGroup, int) ExcursionViewHolder
        +onBindViewHolder(ExcursionViewHolder, int) void
        +getItemCount() int
    }

    class VacationAlertReceiver {
        -CHANNEL_ID String
        +onReceive(Context, Intent) void
        -createNotificationChannel(Context) void
    }

    ScheduleItem <|.. Vacation
    ScheduleItem <|.. Excursion
    BaseDetailActivity <|-- VacationDetailActivity
    BaseDetailActivity <|-- ExcursionDetailActivity
    VacationDatabase --> VacationDao
    VacationDatabase --> ExcursionDao
    VacationRepository --> VacationDao
    VacationRepository --> ExcursionDao
    VacationListActivity --> VacationRepository
    VacationListActivity --> VacationAdapter
    VacationDetailActivity --> VacationRepository
    VacationDetailActivity --> ExcursionAdapter
    ExcursionDetailActivity --> VacationRepository
    ReportActivity --> VacationRepository
    VacationDao ..> Vacation
    ExcursionDao ..> Excursion
```

---

## 4. Database Schema

```
vacations
┌──────────────┬──────────┬────────────────────────────────────┐
│ Column       │ Type     │ Constraints                        │
├──────────────┼──────────┼────────────────────────────────────┤
│ id           │ INTEGER  │ PRIMARY KEY AUTOINCREMENT          │
│ name         │ TEXT     │ NOT NULL                           │
│ hotel        │ TEXT     │                                    │
│ startDate    │ TEXT     │                                    │
│ endDate      │ TEXT     │                                    │
└──────────────┴──────────┴────────────────────────────────────┘

excursions
┌──────────────┬──────────┬────────────────────────────────────────────────────┐
│ Column       │ Type     │ Constraints                                        │
├──────────────┼──────────┼────────────────────────────────────────────────────┤
│ id           │ INTEGER  │ PRIMARY KEY AUTOINCREMENT                          │
│ title        │ TEXT     │ NOT NULL                                           │
│ date         │ TEXT     │                                                    │
│ vacationId   │ INTEGER  │ NOT NULL, FOREIGN KEY → vacations(id), INDEXED     │
└──────────────┴──────────┴────────────────────────────────────────────────────┘
```

---

## 5. OOP Design Decisions

| Principle | Implementation |
|---|---|
| **Encapsulation** | All entity fields (`Vacation`, `Excursion`) are `private` with public getters/setters only |
| **Inheritance** | `VacationDetailActivity` and `ExcursionDetailActivity` both extend `BaseDetailActivity`, which extends `AppCompatActivity` |
| **Polymorphism** | Both `Vacation` and `Excursion` implement the `ScheduleItem` interface; overriding `getDisplayTitle()` and `getDisplayDate()` allows uniform handling across the app |
| **Abstraction** | `BaseDetailActivity` encapsulates shared date validation, date picker, and alarm logic so subclasses inherit behavior without duplication |
| **Repository Pattern** | `VacationRepository` abstracts all database access behind a single class, making the data source swappable without changing the UI layer |
| **Singleton Pattern** | `VacationDatabase` uses double-checked locking to ensure a single Room database instance across the application lifecycle |

---

## 6. Security Features

| Feature | Implementation |
|---|---|
| SQL Injection Prevention | Room ORM uses parameterized `@Query` bindings — no raw SQL string concatenation |
| Delete Confirmation | `AlertDialog` confirmation required before any destructive delete operation |
| Input Length Validation | Vacation name and excursion title capped at 100 characters |
| Date Validation | `SimpleDateFormat.setLenient(false)` enforces strict MM/dd/yyyy parsing |
| PendingIntent Security | All `PendingIntent` objects use `FLAG_IMMUTABLE` to prevent intent hijacking |
| Component Isolation | All activities and receivers declared `android:exported="false"` except the launcher |
| Code Obfuscation | R8 minification enabled (`isMinifyEnabled = true`) with ProGuard keep rules for Room entities |

---

## 7. Scalability Design

| Element | Scalability Benefit |
|---|---|
| Room Database + DAOs | Structured SQL layer that scales to large datasets with indexing on `vacationId` |
| RecyclerView + ViewHolder | Memory-efficient list rendering — only visible items are held in memory |
| Repository Pattern | Data source can be replaced (e.g., with a remote API) without changing any Activity |
| ExecutorService Thread Pool | 4-thread pool handles concurrent database operations without blocking the UI thread |
| ScheduleItem Interface | New schedulable entity types can be added without modifying existing adapters or activities |
| Package-based structure | Clear separation of `entities`, `dao`, `database`, `adapters`, `receivers`, and `ui` packages supports team scaling |

---

## 8. Repository Link

**GitLab:** https://gitlab.com/wgu-gitlab-environment/student-repos/NardosA/d424-software-engineering-capstone
**Branch:** `working-branch`
**Submission Commit:** `c1a022d`
**Direct link to commit:** https://gitlab.com/wgu-gitlab-environment/student-repos/NardosA/d424-software-engineering-capstone/-/commit/c1a022d

> **Note:** This is a native Android mobile application and is not hosted as a web application. The link to a hosted web app is not applicable.
