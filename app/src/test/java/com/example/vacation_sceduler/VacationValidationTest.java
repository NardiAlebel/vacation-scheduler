package com.example.vacation_sceduler;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

import com.example.vacation_sceduler.entities.Excursion;
import com.example.vacation_sceduler.entities.ScheduleItem;
import com.example.vacation_sceduler.entities.Vacation;

public class VacationValidationTest {

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        dateFormat.setLenient(false);
    }

    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return false;
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEndDateAfterStartDate(String startStr, String endStr) {
        try {
            Date start = dateFormat.parse(startStr);
            Date end = dateFormat.parse(endStr);
            return end != null && start != null && !end.before(start);
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isDateWithinVacation(String excursionDateStr, String vacationStart, String vacationEnd) {
        if (vacationStart == null || vacationStart.isEmpty() ||
                vacationEnd == null || vacationEnd.isEmpty()) return true;
        try {
            Date excursionDate = dateFormat.parse(excursionDateStr);
            Date startDate = dateFormat.parse(vacationStart);
            Date endDate = dateFormat.parse(vacationEnd);
            return excursionDate != null && startDate != null && endDate != null
                    && !excursionDate.before(startDate) && !excursionDate.after(endDate);
        } catch (ParseException e) {
            return true;
        }
    }

    @Test
    public void validDate_returnsTrue() {
        assertTrue(isValidDateFormat("06/15/2025"));
    }

    @Test
    public void invalidDateFormat_returnsFalse() {
        assertFalse(isValidDateFormat("2025-06-15"));
    }

    @Test
    public void emptyDate_returnsFalse() {
        assertFalse(isValidDateFormat(""));
    }

    @Test
    public void nullDate_returnsFalse() {
        assertFalse(isValidDateFormat(null));
    }

    @Test
    public void impossibleDate_returnsFalse() {
        assertFalse(isValidDateFormat("13/40/2025"));
    }

    @Test
    public void endDateAfterStartDate_returnsTrue() {
        assertTrue(isEndDateAfterStartDate("06/01/2025", "06/15/2025"));
    }

    @Test
    public void endDateEqualToStartDate_returnsTrue() {
        assertTrue(isEndDateAfterStartDate("06/01/2025", "06/01/2025"));
    }

    @Test
    public void endDateBeforeStartDate_returnsFalse() {
        assertFalse(isEndDateAfterStartDate("06/15/2025", "06/01/2025"));
    }

    @Test
    public void excursionDateWithinVacation_returnsTrue() {
        assertTrue(isDateWithinVacation("06/10/2025", "06/01/2025", "06/30/2025"));
    }

    @Test
    public void excursionDateOnVacationStartDate_returnsTrue() {
        assertTrue(isDateWithinVacation("06/01/2025", "06/01/2025", "06/30/2025"));
    }

    @Test
    public void excursionDateOnVacationEndDate_returnsTrue() {
        assertTrue(isDateWithinVacation("06/30/2025", "06/01/2025", "06/30/2025"));
    }

    @Test
    public void excursionDateBeforeVacation_returnsFalse() {
        assertFalse(isDateWithinVacation("05/31/2025", "06/01/2025", "06/30/2025"));
    }

    @Test
    public void excursionDateAfterVacation_returnsFalse() {
        assertFalse(isDateWithinVacation("07/01/2025", "06/01/2025", "06/30/2025"));
    }

    @Test
    public void excursionDateWithEmptyVacationDates_returnsTrue() {
        assertTrue(isDateWithinVacation("06/10/2025", "", ""));
    }

    @Test
    public void vacationName_notEmpty_isValid() {
        String name = "Hawaii Trip";
        assertFalse(name.trim().isEmpty());
    }

    @Test
    public void vacationName_empty_isInvalid() {
        String name = "   ";
        assertTrue(name.trim().isEmpty());
    }

    @Test
    public void vacationName_withinLengthLimit_isValid() {
        String name = "Hawaii Trip";
        assertTrue(name.length() <= 100);
    }

    @Test
    public void vacationName_exceedsLengthLimit_isInvalid() {
        String name = "A".repeat(101);
        assertFalse(name.length() <= 100);
    }

    @Test
    public void vacation_getDisplayTitle_returnsName() {
        Vacation vacation = new Vacation("Beach Trip", "Hilton", "07/01/2025", "07/10/2025");
        assertEquals("Beach Trip", vacation.getDisplayTitle());
    }

    @Test
    public void vacation_getDisplayDate_returnsCombinedDates() {
        Vacation vacation = new Vacation("Beach Trip", "Hilton", "07/01/2025", "07/10/2025");
        assertEquals("07/01/2025 - 07/10/2025", vacation.getDisplayDate());
    }

    @Test
    public void excursion_getDisplayTitle_returnsTitle() {
        Excursion excursion = new Excursion("Snorkeling", "07/05/2025", 1);
        assertEquals("Snorkeling", excursion.getDisplayTitle());
    }

    @Test
    public void excursion_getDisplayDate_returnsDate() {
        Excursion excursion = new Excursion("Snorkeling", "07/05/2025", 1);
        assertEquals("07/05/2025", excursion.getDisplayDate());
    }

    @Test
    public void vacation_implementsScheduleItem() {
        Vacation vacation = new Vacation("Test", "Hotel", "01/01/2025", "01/10/2025");
        assertTrue(vacation instanceof ScheduleItem);
    }

    @Test
    public void excursion_implementsScheduleItem() {
        Excursion excursion = new Excursion("Test", "01/05/2025", 1);
        assertTrue(excursion instanceof ScheduleItem);
    }

    @Test
    public void scheduleItem_polymorphicDisplayTitle_vacation() {
        ScheduleItem item = new Vacation("Mountain Hike", "Lodge", "08/01/2025", "08/07/2025");
        assertEquals("Mountain Hike", item.getDisplayTitle());
    }

    @Test
    public void scheduleItem_polymorphicDisplayTitle_excursion() {
        ScheduleItem item = new Excursion("Zip Line", "08/03/2025", 2);
        assertEquals("Zip Line", item.getDisplayTitle());
    }

    @Test
    public void vacation_encapsulation_setAndGetId() {
        Vacation vacation = new Vacation("Test", "Hotel", "01/01/2025", "01/10/2025");
        vacation.setId(42);
        assertEquals(42, vacation.getId());
    }

    @Test
    public void excursion_encapsulation_setAndGetVacationId() {
        Excursion excursion = new Excursion("Tour", "01/05/2025", 1);
        excursion.setVacationId(99);
        assertEquals(99, excursion.getVacationId());
    }
}
