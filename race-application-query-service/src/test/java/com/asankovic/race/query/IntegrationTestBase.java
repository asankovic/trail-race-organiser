package com.asankovic.race.query;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

//TODO add dedicated migration scripts for tests
//TODO move more utility methods here
public abstract class IntegrationTestBase {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");
}
