# Vacation Scheduler – User Guide

This guide explains how to use the Vacation Scheduler app from a user's perspective.

---

## Getting Started

When you first open the app you will see the **Vacation Scheduler** welcome screen. Tap **Enter** to go to your vacation list.

---

## Screen Overview

| Screen | Purpose |
|---|---|
| Welcome | Entry point — tap Enter to begin |
| Vacation List | See all your vacations, search, and access the report |
| Vacation Details | Create or edit a vacation and manage its excursions |
| Excursion Details | Create or edit an excursion tied to a vacation |
| Report | View a full tabular report of all vacations |

---

## Managing Vacations

### Adding a Vacation

1. On the **Vacation List** screen, tap the **+** button in the bottom-right corner.
2. Fill in the following fields:
   - **Vacation Name** (required, max 100 characters)
   - **Hotel Name** (optional)
   - **Start Date** — tap the field to open a date picker
   - **End Date** — tap the field to open a date picker
3. Tap **Save**.

> The end date must be on or after the start date. Dates must be in MM/DD/YYYY format (the date picker handles this automatically).

### Editing a Vacation

1. Tap any vacation card on the **Vacation List** screen.
2. Update any fields.
3. Tap **Save** to apply changes.

### Deleting a Vacation

1. Open the vacation you want to delete.
2. Tap **Delete**.
3. A confirmation dialog will appear — tap **Delete** again to confirm.

> A vacation cannot be deleted if it still has excursions. Delete all excursions first, then delete the vacation.

---

## Managing Excursions

Excursions are day trips or activities that belong to a specific vacation. They must fall within the vacation's start and end dates.

### Adding an Excursion

1. Open a vacation (it must be saved first).
2. Scroll down to the **Excursions** section and tap **Add Excursion**.
3. Enter the **Excursion Title** (required, max 100 characters).
4. Tap the date field to select a date using the date picker.
5. Tap **Save**.

> The excursion date must fall within the vacation's start and end dates.

### Editing an Excursion

1. Tap any excursion card in the Vacation Details screen.
2. Update the title or date.
3. Tap **Save**.

### Deleting an Excursion

1. Open the excursion you want to delete.
2. Tap **Delete**.
3. Confirm the deletion in the dialog that appears.

---

## Searching Vacations

On the **Vacation List** screen, type in the **Search** bar at the top of the screen. The list filters in real time by:
- Vacation name
- Hotel name

Clear the search bar to show all vacations again.

If no vacations match the search (or no vacations exist yet), a message will appear in the center of the screen.

---

## Setting Alerts

Alerts send you a notification on the start and end dates of your vacation, or on the date of an excursion.

### Vacation Alert
1. Open a vacation and fill in both start and end dates.
2. Tap **Set Alert**.
3. You will receive a notification on the vacation's start date and another on the end date.

### Excursion Alert
1. Open an excursion and fill in its date.
2. Tap **Set Excursion Alert**.
3. You will receive a notification on the morning of that excursion.

> On Android 12 and above, the app may ask for permission to schedule exact alarms. Go to **Settings → Apps → Vacation Scheduler → Alarms & Reminders** and enable the permission to ensure alerts arrive at the correct time.

> On Android 13 and above, the app will request notification permission on first use. Tap **Allow** to enable alerts.

---

## Sharing a Vacation

1. Open any saved vacation.
2. Tap **Share**.
3. A share dialog will appear letting you send the vacation details (name, hotel, dates, and all excursions) via any installed app such as Messages, Email, or WhatsApp.

---

## Generating a Report

1. On the **Vacation List** screen, tap the **⋮** (three-dot menu) in the top-right corner.
2. Tap **Report**.
3. The Report screen shows a table with all your vacations including:
   - Vacation Name
   - Hotel
   - Start Date
   - End Date
   - Number of Excursions
4. The top of the report shows the date and time it was generated.

> The report is scrollable horizontally if the columns extend beyond the screen width.

---

## Validation Rules Summary

| Field | Rule |
|---|---|
| Vacation Name | Required, max 100 characters |
| Excursion Title | Required, max 100 characters |
| Start / End Date | Must be in MM/DD/YYYY format |
| End Date | Must be on or after Start Date |
| Excursion Date | Must fall within the vacation's Start and End dates |

---

## Tips

- Use the **back arrow** in the top-left of any screen to navigate to the previous screen without saving.
- The vacation list is sorted by ID (creation order) by default. Use the search bar to quickly find any vacation by name or hotel.
- Alerts are tied to the date — they fire at the time you set the alarm, not at a specific time of day. Set alerts a day in advance if you want a morning reminder.
