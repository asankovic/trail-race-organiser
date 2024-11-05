package com.asankovic.race.query.data.dtos.messaging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRunnerMessageData extends BaseRunnerMessageData {

    @NotBlank(message = "Public ID is required for updating the runner data")
    private String runnerPublicID;

    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Size(max = 255, message = "Club name must not exceed 255 characters")
    private String club;

    @Size(max = 50, message = "Club code must not exceed 50 characters")
    private String distanceCode;

    public UpdateRunnerMessageData(final String runnerPublicID,
                                   final String firstName,
                                   final String lastName,
                                   final String club,
                                   final String distanceCode) {
        super(RunnerMessageType.PARTIAL_UPDATE);
        this.runnerPublicID = runnerPublicID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.distanceCode = distanceCode;
    }
}
