package com.dimm.springbootexample.journey;

import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIT {

	@Autowired
	private WebTestClient webTestClient;
	private static final String AUTHENTICATION_PATH = "/api/v1/auth/";
	private static final String CUSTOMER_PATH = "/api/v1/customers";

	@Test
	void canRegisterACustomer() {
		//create registration request
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = firstName + "." + lastName + "@dimm.com";
		String password = "password";
		CustomerGender gender = CustomerGender.MALE;
		int age = faker.number().numberBetween(20, 70);

		CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.password(password)
				.age(age)
				.gender(gender)
				.build();

		//send a post request
		webTestClient.post()
				.uri(CUSTOMER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk()
				.returnResult(Void.class)
				.getResponseHeaders()
				.get(HttpHeaders.AUTHORIZATION)
				.get(0);}
