CREATE TABLE distances
(
    pk                 SERIAL PRIMARY KEY,
    name               VARCHAR(50)                         NOT NULL UNIQUE,
    creation_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE runners
(
    pk                 BIGSERIAL PRIMARY KEY,
    public_id          UUID                                NOT NULL UNIQUE,
    first_name         VARCHAR(255)                        NOT NULL,
    last_name          VARCHAR(255)                        NOT NULL,
    club               VARCHAR(255),
    distance_id        INTEGER                             NOT NULL,
    creation_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_distance FOREIGN KEY (distance_id) REFERENCES distances (pk)
);

CREATE UNIQUE INDEX idx_runners_public_id ON runners (public_id);