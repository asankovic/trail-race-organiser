package com.asankovic.race.query.data.models;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseModel {

    @CreationTimestamp(source = SourceType.DB)
    private Instant creationTime;

    @CreationTimestamp(source = SourceType.DB)
    private Instant lastModifiedTime;
}
