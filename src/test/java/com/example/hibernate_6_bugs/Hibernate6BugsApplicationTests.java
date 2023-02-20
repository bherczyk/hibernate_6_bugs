package com.example.hibernate_6_bugs;

import com.example.hibernate_6_bugs.model.Company;
import com.example.hibernate_6_bugs.service.Service;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.Hibernate.isInitialized;

@SpringBootTest
@Testcontainers
class Hibernate6BugsApplicationTests {

	@Container
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

	@DynamicPropertySource
	static void registerMySQLProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
	}

	@Autowired
	Service service;

	@Test
	void customEntityGraph() {
		//given
		var id = service.create();

		//when
		var result = service.getCustomEntityGraph(id);

		//then
		assertThat(isInitialized(result.getFactory())).isTrue();
		assertThat(isInitialized(result.getFactory().getCarFactory())).isTrue();
		var car = result.getFactory().getCarFactory().getCars().iterator().next();
		assertThat(isInitialized(car)).isTrue();
		assertThat(isInitialized(car.getInfo())).isTrue();
		assertThat(car.getFeatures()).allMatch(Hibernate::isInitialized);
	}

	@Test
	void fetchGraphHint() {
		//given
		var id = service.create();

		//when
		var result = service.getFetchGraphHint(id);

		//then
		assertThat(isInitialized(result.getFactory())).isTrue();
		assertThat(isInitialized(result.getFactory().getCarFactory())).isTrue();
		var car = result.getFactory().getCarFactory().getCars().iterator().next();
		assertThat(isInitialized(car)).isTrue();
		assertThat(isInitialized(car.getInfo())).isTrue();
		assertThat(car.getFeatures()).allMatch(Hibernate::isInitialized);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void entityGraphInSecondQuery(boolean twoQueries) {
		//given
		var id = service.create();
		Function<Long, Company> method = twoQueries ? service::getTwoQueries : service::getTwoQueriesWithoutExplicitJoinFetch;

		//when
		var result = method.apply(id);

		//then
		assertThat(isInitialized(result.getFactory())).isTrue();
		assertThat(isInitialized(result.getFactory().getCarFactory())).isTrue();
		assertThat(isInitialized(result.getFactory().getCarFactory().getCars())).isTrue();
	}
}
