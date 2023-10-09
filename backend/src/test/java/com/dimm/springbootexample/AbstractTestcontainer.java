package com.dimm.springbootexample;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainer {
	@Container
	protected static final PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:latest")
					.withDatabaseName("dimm-dao-unit-test")
					.withUsername("dimm")
					.withPassword("password");

	@DynamicPropertySource
	private static void registerDataSourceProperties (DynamicPropertyRegistry propertyRegistry){
		propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	private static DataSource getDataSource(){
		return DataSourceBuilder
				.create()
				.driverClassName(postgreSQLContainer.getDriverClassName())
				.url(postgreSQLContainer.getJdbcUrl())
				.username(postgreSQLContainer.getUsername())
				.password(postgreSQLContainer.getPassword()).build();
	}

	protected JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

	@BeforeAll
	static void setUpBeforeAll() {
		postgreSQLContainer.start();
		Flyway flyway = Flyway.configure().dataSource(postgreSQLContainer.getJdbcUrl(),
				postgreSQLContainer.getUsername(),
				postgreSQLContainer.getPassword()).load();
		flyway.migrate();
	}

	@AfterAll
	static void tearDownAfterAll() {
		postgreSQLContainer.stop();
	}
}
