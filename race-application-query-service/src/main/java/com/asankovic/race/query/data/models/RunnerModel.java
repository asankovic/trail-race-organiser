package com.asankovic.race.query.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = RunnerModel.TABLE)
public class RunnerModel extends BaseModel {

    public static final String TABLE = "runners";
    public static final String TYPE_CODE = "RunnerModel";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    //TODO add separate table for club
    private String club;

    @ManyToOne
    @JoinColumn(name = "distance_id", nullable = false)
    private DistanceModel distance;

    public RunnerModel(final String firstName, final String lastName, final String club, final DistanceModel distance) {
        this.publicId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.distance = distance;
    }
}
