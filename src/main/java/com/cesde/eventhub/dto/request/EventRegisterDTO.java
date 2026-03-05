package com.cesde.eventhub.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cesde.eventhub.enums.Category;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRegisterDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date cannot be in the past")
    private LocalDate eventDate;

    private LocalTime startTime;
    private LocalTime openingTime;
    
    @NotNull(message = "Category is required")
    private Category category;
    private String imageUrl;

    private LocalDateTime salesStartDate;
    private LocalDateTime salesEndDate;

    @NotNull(message = "Zone ID is required")
    private Long zoneId;
}