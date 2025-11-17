package com.movietime.movie_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
class MovieServiceApplicationTests {

    // Force an in-memory H2 datasource (Postgres mode) just for tests
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        r.add("spring.datasource.url", () ->
                "jdbc:h2:mem:moviesvc_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH");
        r.add("spring.datasource.username", () -> "sa");
        r.add("spring.datasource.password", () -> "");
        r.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        r.add("spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
        // If you have data.sql in main/resources, make sure tests don't try to run it:
        r.add("spring.sql.init.mode", () -> "never");
    }

    @Test
    void contextLoads() { }
}
