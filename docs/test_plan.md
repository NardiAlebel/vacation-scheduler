# Vacation Scheduler – Test Plan and Results

## 1. Test Plan Overview

| Item | Detail |
|---|---|
| Application | Vacation Scheduler (Android) |
| Test Type | Unit Tests (JUnit 4) |
| Test File | `VacationValidationTest.java` |
| Framework | JUnit 4 via `androidx.test` |
| Execution | `./gradlew testDebugUnitTest` |

---

## 2. Test Categories and Objectives

### Category 1 – Date Format Validation
Verify that `isValidDateFormat()` correctly accepts and rejects date strings.

| Test ID | Test Name | Input | Expected Result |
|---|---|---|---|
| T01 | `validDate_returnsTrue` | `"06/15/2025"` | true |
| T02 | `invalidDateFormat_returnsFalse` | `"2025-06-15"` | false |
| T03 | `emptyDate_returnsFalse` | `""` | false |
| T04 | `nullDate_returnsFalse` | `null` | false |
| T05 | `impossibleDate_returnsFalse` | `"13/40/2025"` | false |

---

### Category 2 – Date Range Validation
Verify that end date / start date ordering is enforced correctly.

| Test ID | Test Name | Start | End | Expected Result |
|---|---|---|---|---|
| T06 | `endDateAfterStartDate_returnsTrue` | `06/01/2025` | `06/15/2025` | true |
| T07 | `endDateEqualToStartDate_returnsTrue` | `06/01/2025` | `06/01/2025` | true |
| T08 | `endDateBeforeStartDate_returnsFalse` | `06/15/2025` | `06/01/2025` | false |

---

### Category 3 – Excursion Date Boundary Validation
Verify that excursion dates must fall within the vacation period.

| Test ID | Test Name | Excursion Date | Vacation Start | Vacation End | Expected Result |
|---|---|---|---|---|---|
| T09 | `excursionDateWithinVacation_returnsTrue` | `06/10/2025` | `06/01/2025` | `06/30/2025` | true |
| T10 | `excursionDateOnVacationStartDate_returnsTrue` | `06/01/2025` | `06/01/2025` | `06/30/2025` | true |
| T11 | `excursionDateOnVacationEndDate_returnsTrue` | `06/30/2025` | `06/01/2025` | `06/30/2025` | true |
| T12 | `excursionDateBeforeVacation_returnsFalse` | `05/31/2025` | `06/01/2025` | `06/30/2025` | false |
| T13 | `excursionDateAfterVacation_returnsFalse` | `07/01/2025` | `06/01/2025` | `06/30/2025` | false |
| T14 | `excursionDateWithEmptyVacationDates_returnsTrue` | `06/10/2025` | `""` | `""` | true |

---

### Category 4 – Input Length Validation
Verify that name and title length constraints are enforced.

| Test ID | Test Name | Input | Expected Result |
|---|---|---|---|
| T15 | `vacationName_notEmpty_isValid` | `"Hawaii Trip"` | valid (not empty) |
| T16 | `vacationName_empty_isInvalid` | `"   "` | invalid (empty after trim) |
| T17 | `vacationName_withinLengthLimit_isValid` | 11-char string | true (≤ 100) |
| T18 | `vacationName_exceedsLengthLimit_isInvalid` | 101-char string | false (> 100) |

---

### Category 5 – OOP: Encapsulation
Verify that private fields are accessible only through public getters and setters.

| Test ID | Test Name | Object | Expected Result |
|---|---|---|---|
| T19 | `vacation_encapsulation_setAndGetId` | Vacation, setId(42) | getId() == 42 |
| T20 | `excursion_encapsulation_setAndGetVacationId` | Excursion, setVacationId(99) | getVacationId() == 99 |

---

### Category 6 – OOP: Polymorphism and Inheritance
Verify that `Vacation` and `Excursion` correctly implement the `ScheduleItem` interface and override its methods polymorphically.

| Test ID | Test Name | Expected Result |
|---|---|---|
| T21 | `vacation_getDisplayTitle_returnsName` | `"Beach Trip"` |
| T22 | `vacation_getDisplayDate_returnsCombinedDates` | `"07/01/2025 - 07/10/2025"` |
| T23 | `excursion_getDisplayTitle_returnsTitle` | `"Snorkeling"` |
| T24 | `excursion_getDisplayDate_returnsDate` | `"07/05/2025"` |
| T25 | `vacation_implementsScheduleItem` | instanceof ScheduleItem == true |
| T26 | `excursion_implementsScheduleItem` | instanceof ScheduleItem == true |
| T27 | `scheduleItem_polymorphicDisplayTitle_vacation` | `"Mountain Hike"` via ScheduleItem reference |
| T28 | `scheduleItem_polymorphicDisplayTitle_excursion` | `"Zip Line"` via ScheduleItem reference |

---

## 3. Test Results

**Total Tests: 28 | Passed: 28 | Failed: 0 | Skipped: 0**

All 28 unit tests passed on the first run.

> Screenshot of the Android Studio test results panel and the Gradle HTML report
> (`app/build/reports/tests/testDebugUnitTest/index.html`) should be captured here.

### Running the tests in Android Studio (for screenshots):
1. Open the project in Android Studio.
2. Navigate to `app/src/test/java/com/example/vacation_sceduler/VacationValidationTest.java`.
3. Right-click the file → **Run 'VacationValidationTest'**.
4. Screenshot the green checkmarks in the **Run** panel at the bottom.
5. Alternatively, after running `./gradlew testDebugUnitTest`, open `app/build/reports/tests/testDebugUnitTest/index.html` in a browser and screenshot the results page.

---

## 4. Summaries of Changes Resulting from Completed Tests

During the implementation of tests, the following issues were identified and resolved:

| Issue Found | Change Made |
|---|---|
| `build.gradle.kts` root file had a comment and `plugins {` merged onto the same line, breaking Gradle compilation | Fixed by separating the comment from the `plugins {` block |
| `settings.gradle.kts` was missing from the project root, preventing command-line Gradle execution | Created `settings.gradle.kts` with correct `pluginManagement` and `dependencyResolutionManagement` repository configuration |
| `isEndDateAfterStartDate()` in `VacationDetailActivity` had an unchecked potential null dereference on parsed dates | Added null checks before calling `.before()` on parsed Date objects |
| `setExcursionAlert()` in `ExcursionDetailActivity` had an unchecked null dereference on the parsed date | Wrapped in a null check before calling `setAlert()` |
