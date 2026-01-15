package com.socialseed.socialuserservice.domain;

import com.socialseed.socialuserservice.user.domain.model.valueobject.VacationPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VacationPeriodTest {

    @Test
    void should_create_vacation_period_when_data_is_valid() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);
        String note = "Family trip";

        VacationPeriod vacationPeriod = new VacationPeriod(start, end, note);

        assertEquals(start, vacationPeriod.startDate());
        assertEquals(end, vacationPeriod.endDate());
        assertEquals(note, vacationPeriod.note());
    }

    @Test
    void should_throw_exception_when_start_date_is_after_end_date() {
        LocalDate start = LocalDate.now().plusDays(5);
        LocalDate end = LocalDate.now();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> new VacationPeriod(start, end, "Invalid"));
        assertEquals("startDate must be strictly before endDate", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_start_date_is_equal_to_end_date() {
        LocalDate today = LocalDate.now();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> new VacationPeriod(today, today, "Invalid"));
        assertEquals("startDate must be strictly before endDate", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_dates_are_null() {
        assertThrows(NullPointerException.class, () -> new VacationPeriod(null, LocalDate.now(), "Note"));
        assertThrows(NullPointerException.class, () -> new VacationPeriod(LocalDate.now(), null, "Note"));
    }

    @Test
    void should_create_vacation_period_without_note() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(1);

        VacationPeriod vacationPeriod = VacationPeriod.of(start, end);

        assertNull(vacationPeriod.note());
        assertEquals(start, vacationPeriod.startDate());
        assertEquals(end, vacationPeriod.endDate());
    }
}
