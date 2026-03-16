# Vacation Scheduler App - Storyboard

## Application Flow Diagram

```
+------------------+
|   HOME SCREEN    |
|   (MainActivity) |
|                  |
|  [Vacation       |
|   Scheduler]     |
|                  |
|  [Enter Button]  |
+--------+---------+
         |
         v
+------------------+
| VACATION LIST    |
|(VacationList     |
|     Activity)    |
|                  |
| +-------------+  |
| | Vacation 1  |--+------+
| +-------------+  |      |
| | Vacation 2  |  |      |
| +-------------+  |      |
| | Vacation 3  |  |      |
| +-------------+  |      |
|                  |      |
|  [+ FAB Button]--+--+   |
+------------------+  |   |
                      |   |
         +------------+   |
         |                |
         v                v
+------------------+    +------------------+
| VACATION DETAIL  |    | VACATION DETAIL  |
| (New Vacation)   |    | (Edit Vacation)  |
|(VacationDetail   |    |(VacationDetail   |
|     Activity)    |    |     Activity)    |
|                  |    |                  |
| Name: [_______]  |    | Name: [Trip___]  |
| Hotel:[_______]  |    | Hotel:[Hotel__]  |
| Start:[__/__/__] |    | Start:[01/15/26] |
| End:  [__/__/__] |    | End:  [01/22/26] |
|                  |    |                  |
| [Save] [Delete]  |    | [Save] [Delete]  |
| [Alert] [Share]  |    | [Alert] [Share]  |
|                  |    |                  |
| -- Excursions -- |    | -- Excursions -- |
| (empty)          |    | +-------------+  |
|                  |    | | Excursion 1 |--+--+
| [Add Excursion]  |    | +-------------+  |  |
+------------------+    | | Excursion 2 |  |  |
                        | +-------------+  |  |
                        |                  |  |
                        | [Add Excursion]--+--+--+
                        +------------------+  |  |
                                              |  |
                        +---------------------+  |
                        |                        |
                        v                        v
                   +------------------+    +------------------+
                   | EXCURSION DETAIL |    | EXCURSION DETAIL |
                   | (New Excursion)  |    | (Edit Excursion) |
                   |(ExcursionDetail  |    |(ExcursionDetail  |
                   |     Activity)    |    |     Activity)    |
                   |                  |    |                  |
                   | Title:[_______]  |    | Title:[Hiking_]  |
                   | Date: [__/__/__] |    | Date: [01/17/26] |
                   |                  |    |                  |
                   | [Save] [Delete]  |    | [Save] [Delete]  |
                   | [Set Alert]      |    | [Set Alert]      |
                   +------------------+    +------------------+
                            |                       |
                            v                       v
                   +------------------+    +------------------+
                   | Actions:         |    | Actions:         |
                   | - Save: saves    |    | - Save: updates  |
                   |   new excursion  |    |   excursion      |
                   | - Delete: remove |    | - Delete: remove |
                   |   excursion      |    |   excursion from |
                   | - Set Alert:     |    |   database       |
                   |   schedule       |    | - Set Alert:     |
                   |   notification   |    |   schedule       |
                   |   for date       |    |   notification   |
                   +------------------+    +------------------+
```

## Excursion Detail Screen - Detailed View

```
+--------------------------------+
|      EXCURSION DETAIL          |
|      (ExcursionDetailActivity) |
+--------------------------------+
|                                |
|  Excursion Title:              |
|  +---------------------------+ |
|  | [User enters title here]  | |
|  +---------------------------+ |
|                                |
|  Excursion Date:               |
|  +---------------------------+ |
|  | [MM/DD/YYYY]    [Calendar]| |
|  +---------------------------+ |
|  * Must be within vacation     |
|    start and end dates         |
|                                |
+--------------------------------+
|  +----------+   +----------+   |
|  |   SAVE   |   |  DELETE  |   |
|  +----------+   +----------+   |
|                                |
|  +---------------------------+ |
|  |       SET ALERT           | |
|  +---------------------------+ |
|  * Schedules notification for  |
|    the excursion date          |
+--------------------------------+
```

## Screen Descriptions

### 1. Home Screen (MainActivity)
- **Purpose**: Entry point to the application
- **Elements**:
  - App title "Vacation Scheduler"
  - "Enter" button to navigate to vacation list
- **Navigation**: Enter button -> Vacation List

### 2. Vacation List (VacationListActivity)
- **Purpose**: Display all vacations
- **Elements**:
  - RecyclerView with vacation cards (name, dates)
  - Floating Action Button (+) to add new vacation
- **Navigation**:
  - FAB -> New Vacation Detail
  - Tap vacation -> Edit Vacation Detail
  - Back -> Home Screen

### 3. Vacation Detail (VacationDetailActivity)
- **Purpose**: Add/Edit vacation and view excursions
- **Elements**:
  - Vacation Name input field
  - Hotel Name input field
  - Start Date picker (MM/DD/YYYY)
  - End Date picker (MM/DD/YYYY)
  - Save button
  - Delete button (validates no excursions exist)
  - Set Alert button (notifications for start/end dates)
  - Share button (share via email/SMS)
  - Excursions list (RecyclerView)
  - Add Excursion button
- **Validation**:
  - Date format validation
  - End date must be after start date
  - Cannot delete if excursions exist
- **Navigation**:
  - Save -> Back to Vacation List
  - Delete -> Back to Vacation List
  - Add Excursion -> New Excursion Detail
  - Tap excursion -> Edit Excursion Detail
  - Back -> Vacation List

### 4. Excursion Detail (ExcursionDetailActivity)
- **Purpose**: Add new excursion or edit existing excursion for a vacation
- **Elements**:
  - Excursion Title input field (required)
  - Excursion Date picker (MM/DD/YYYY format)
  - **Save button** - Saves the excursion to the database
  - **Delete button** - Removes the excursion from the database (enabled for existing excursions)
  - **Set Alert button** - Schedules a notification for the excursion date
- **Validation**:
  - Date format validation (must be MM/DD/YYYY)
  - Excursion date must fall within the vacation start and end dates
  - Alert shown if date is outside vacation period
- **Navigation**:
  - Save -> Saves excursion and returns to Vacation Detail
  - Delete -> Deletes excursion and returns to Vacation Detail
  - Back -> Vacation Detail (discards unsaved changes)

## User Flow Examples

### Flow 1: Add a New Vacation
1. Open app -> Home Screen
2. Tap "Enter" -> Vacation List (empty)
3. Tap FAB (+) -> Vacation Detail (new)
4. Fill in: Name, Hotel, Start Date, End Date
5. Tap "Save" -> Returns to Vacation List (shows new vacation)

### Flow 2: Add Excursion to Vacation
1. From Vacation List, tap a vacation -> Vacation Detail
2. Tap "Add Excursion" -> Excursion Detail (new)
3. Fill in: Title, Date
4. Tap "Save" -> Returns to Vacation Detail (shows excursion in list)

### Flow 3: Set Alerts
1. From Vacation Detail, tap "Set Alert"
2. System schedules notifications for start and end dates
3. Toast confirms "Alerts set for vacation start and end dates"

### Flow 4: Share Vacation
1. From Vacation Detail, tap "Share"
2. System opens share dialog (email, SMS, etc.)
3. Pre-populated with vacation details and excursions

### Flow 5: Delete Vacation (with validation)
1. From Vacation Detail (with excursions), tap "Delete"
2. Toast shows "Cannot delete vacation with excursions"
3. User must delete excursions first
4. After excursions deleted, tap "Delete" -> Success

### Flow 6: Set Excursion Alert
1. From Vacation Detail, tap an excursion -> Excursion Detail
2. Tap "Set Alert" button
3. System schedules notification for the excursion date
4. Toast confirms "Alert set for excursion date"
5. On the excursion date, user receives notification reminder

### Flow 7: Delete Excursion
1. From Vacation Detail, tap an excursion -> Excursion Detail
2. Tap "Delete" button
3. Excursion is removed from the database
4. Returns to Vacation Detail (excursion no longer in list)
5. Toast confirms "Excursion deleted"

## File Structure Reference

```
app/src/main/
├── java/com/example/vacation_sceduler/
│   ├── MainActivity.java
│   ├── VacationListActivity.java
│   ├── VacationDetailActivity.java
│   ├── ExcursionDetailActivity.java
│   ├── adapters/
│   │   ├── VacationAdapter.java
│   │   └── ExcursionAdapter.java
│   ├── database/
│   │   ├── VacationDatabase.java
│   │   └── VacationRepository.java
│   ├── dao/
│   │   ├── VacationDao.java
│   │   └── ExcursionDao.java
│   ├── entities/
│   │   ├── Vacation.java
│   │   └── Excursion.java
│   └── receivers/
│       └── VacationAlertReceiver.java
└── res/layout/
    ├── activity_main.xml
    ├── activity_vacation_list.xml
    ├── activity_vacation_detail.xml
    ├── activity_excursion_detail.xml
    ├── item_vacation.xml
    └── item_excursion.xml
```
