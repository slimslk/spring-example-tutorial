package com.dimm.springbootexample.journey;

import com.dimm.springbootexample.customer.Customer;
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
import java.util.UUID;

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
		String name = faker.name().firstName();
		String email = name+"_"+ UUID.randomUUID()+"@mymail.com";
		int age = faker.number().numberBetween(20,70);

		CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
				.name(name)
				.email(email)
				.age(age)
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
				.name(name)
				.email(email)
				.age(age)
				.build();
		assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expected);
		//get customer by id

		assert customers != null;
		Long id = customers.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		expected.setId(id);
		webTestClient.get()
				.uri(CUSTOMER_URI+"/{customerId}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(new ParameterizedTypeReference<Customer>() {})
				.isEqualTo(expected);
	}
}
