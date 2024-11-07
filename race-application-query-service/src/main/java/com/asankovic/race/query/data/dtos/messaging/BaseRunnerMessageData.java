package com.asankovic.race.query.data.dtos.messaging;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO move this and related classes to shared module/library
// TODO better infrastructure: topic per message type
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "messageType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateRunnerMessageData.class, name = RunnerMessageType.CREATE_VALUE),
        @JsonSubTypes.Type(value = UpdateRunnerMessageData.class, name = RunnerMessageType.PARTIAL_UPDATE_VALUE),
        @JsonSubTypes.Type(value = DeleteRunnerMessageData.class, name = RunnerMessageType.DELETE_VALUE),
})
public abstract class BaseRunnerMessageData {

    protected RunnerMessageType messageType;
}
