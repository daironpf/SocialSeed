package com.socialseed.socialuserservice.user.entry.rest.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record StartVacationRequestDTO(
        @NotNull(message = "{user.id.required}")
        UUID userId,

        @NotNull(message = "{vacation.startDate.required}")
        @FutureOrPresent(message = "{vacation.startDate.futureOrPresent}")
        LocalDate startDate,

        @NotNull(message = "{vacation.endDate.required}")
        LocalDate endDate,

        @Size(max = 255, message = "{vacation.note.size}")
        String note
) {
}
