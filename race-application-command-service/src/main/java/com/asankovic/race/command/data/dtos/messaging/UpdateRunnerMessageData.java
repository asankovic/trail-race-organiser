package com.asankovic.race.command.data.dtos.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRunnerMessageData extends BaseRunnerMessageData {

    private String runnerPublicID;

    private String firstName;

    private String lastName;

    private String club;

    // TODO check naming consistency
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
