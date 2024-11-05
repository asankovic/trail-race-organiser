package com.asankovic.race.command.data.dtos.rest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRunnerData {

    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Size(max = 255, message = "Club name must not exceed 255 characters")
    private String club;

    @Size(max = 50, message = "Club code must not exceed 50 characters")
    private String distanceCode;
}
