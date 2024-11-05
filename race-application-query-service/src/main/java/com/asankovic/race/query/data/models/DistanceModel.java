package com.asankovic.race.query.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = DistanceModel.TABLE)
public class DistanceModel extends BaseModel {

    public static final String TABLE = "distances";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    private String name;

    public DistanceModel(final String name) {
        this.name = name;
    }
}
