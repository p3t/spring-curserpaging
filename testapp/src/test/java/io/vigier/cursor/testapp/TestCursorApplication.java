package io.vigier.cursor.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration( proxyBeanMethods = false )
public class TestCursorApplication {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer( DockerImageName.parse( "mongo:latest" ) );
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>( DockerImageName.parse( "postgres:latest" ) );
    }

    public static void main( String[] args ) {
        SpringApplication.from( TestApplication::main ).with( TestCursorApplication.class ).run( args );
    }

}
