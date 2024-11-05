package com.asankovic.race.command.data.dtos.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRunnerMessageData extends BaseRunnerMessageData {

    private String runnerPublicID;

    public DeleteRunnerMessageData(final String runnerPublicID) {
        super(RunnerMessageType.DELETE);
        this.runnerPublicID = runnerPublicID;
    }
}
