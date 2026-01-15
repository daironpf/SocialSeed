package com.socialseed.socialuserservice.user.domain.model.valueobject;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing a user's vacation period.
 * Enforces domain rules: startDate must be before endDate.
 */
public record VacationPeriod(LocalDate startDate, LocalDate endDate, String note) {

    public VacationPeriod {
        Objects.requireNonNull(startDate, "startDate is required");
        Objects.requireNonNull(endDate, "endDate is required");

        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("startDate must be strictly before endDate");
        }
    }

    /**
     * Factory method to create a VacationPeriod without a note.
     */
    public static VacationPeriod of(LocalDate startDate, LocalDate endDate) {
        return new VacationPeriod(startDate, endDate, null);
    }
}
