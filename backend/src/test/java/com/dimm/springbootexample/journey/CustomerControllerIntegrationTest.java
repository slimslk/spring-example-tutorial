package com.dimm.springbootexample.journey;

import com.dimm.springbootexample.customer.Customer;
import com.dimm.springbootexample.customer.CustomerGender;
import com.dimm.springbootexample.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;
	private static final String CUSTOMER_URI = "/api/v1/customers";

	@Test
	void canRegisterACustomer() {
		//create registration request
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = firstName + "." + lastName + "@dimm.com";
		CustomerGender gender = CustomerGender.MALE;
		int age = faker.number().numberBetween(20, 70);

		CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.age(age)
				.gender(gender)
				.build();

		//send a post request
		webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk();

		//get all customers
		List<Customer> customers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {
				}).returnResult()
				.getResponseBody();

		//make sure that customer is present
		Customer expected = Customer.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.age(age)
				.gender(gender)
				.build();

		Customer actual = customers.stream().filter(cus ->
				cus.getName().equals(expected.getName()) &&
				cus.getAge() == expected.getAge() &&
				cus.getEmail().equals(expected.getEmail()) &&
				cus.getGender().equals(expected.getGender())).findFirst().orElseThrow();

		assertThat(actual.getName()).isEqualTo(expected.getName());
		assertThat(actual.getAge()).isEqualTo(expected.getAge());
		assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
		assertThat(actual.getGender()).isEqualTo(expected.getGender());
		//get customer by id

		Long id = customers.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		expected.setId(id);
		webTestClient.get()
				.uri(CUSTOMER_URI + "/{customerId}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(new ParameterizedTypeReference<Customer>() {
				})
				.isEqualTo(expected);
	}
}
