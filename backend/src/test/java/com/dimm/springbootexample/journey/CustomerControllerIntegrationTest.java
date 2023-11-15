package com.dimm.springbootexample.journey;

import com.dimm.springbootexample.customer.entity.CustomerDTO;
import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.dimm.springbootexample.exception.ResourceNotFoundException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

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
		String jwtToken = webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isCreated()
				.returnResult(Void.class)
				.getResponseHeaders()
				.get(HttpHeaders.AUTHORIZATION)
				.get(0);

		//get all customers
		List<CustomerDTO> customers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
				}).returnResult()
				.getResponseBody();

		Long id = customers.stream()
				.filter(c -> c.email().equals(email))
				.map(CustomerDTO::id)
				.findFirst()
				.orElseThrow();

		//make sure that customer is present
		CustomerDTO expected = new CustomerDTO(
				id,
				firstName + " " + lastName,
				email,
				age,
				gender);

		CustomerDTO actual = customers.stream().filter(cus ->
				cus.name().equals(expected.name()) &&
				cus.age() == expected.age() &&
				cus.email().equals(expected.email()) &&
				cus.gender().equals(expected.gender())).findFirst().orElseThrow();

		assertThat(actual.name()).isEqualTo(expected.name());
		assertThat(actual.age()).isEqualTo(expected.age());
		assertThat(actual.email()).isEqualTo(expected.email());
		assertThat(actual.gender()).isEqualTo(expected.gender());

		//get customer by id
		webTestClient.get()
				.uri(CUSTOMER_URI + "/{customerId}", id)
				.accept(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(new ParameterizedTypeReference<CustomerDTO>() {
				})
				.isEqualTo(expected);
	}

	@Test
	public void canDeleteCustomer() {
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = firstName + "." + lastName + "@dimm.com";
		String password = "password";
		CustomerGender gender = CustomerGender.MALE;
		int age = faker.number().numberBetween(20, 70);

		CustomerRegistrationRequest customerRequest1 = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.password(password)
				.age(age)
				.gender(gender)
				.build();
		//create customer 1
		webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customerRequest1), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isCreated();
		//create customer 2 and remember auth token

		firstName = faker.name().firstName();
		lastName = faker.name().lastName();
		email = firstName + "." + lastName + "@dimm.com";
		password = "password";
		gender = CustomerGender.FEMALE;
		age = faker.number().numberBetween(20, 70);

		CustomerRegistrationRequest customerRequest2 = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.password(password)
				.age(age)
				.gender(gender)
				.build();

		String jwtToken = webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customerRequest2), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isCreated()
				.returnResult(Void.class)
				.getResponseHeaders()
				.get(HttpHeaders.AUTHORIZATION)
				.get(0);

		//find all customers and get first customer id
		List<CustomerDTO> customers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
				}).returnResult()
				.getResponseBody();

		Long id = customers.stream().filter(customer -> customer.email()
				.equals(customerRequest1.getEmail()))
				.map(CustomerDTO::id)
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Customer with not found"));
		//delete customer 1
		webTestClient.delete()
				.uri(CUSTOMER_URI+"/%s".formatted(id))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus().isOk();
		//check if customer 1 is present
		webTestClient.get()
				.uri(CUSTOMER_URI + "/%s".formatted(id))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isNotFound();
	}

	@Test
	public void canUpdateCustomer() {
		//create customer
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = firstName + "." + lastName + "@dimm.com";
		String password = "password";
		CustomerGender gender = CustomerGender.MALE;
		int age = faker.number().numberBetween(20, 70);

		CustomerRegistrationRequest customer = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.email(email)
				.password(password)
				.age(age)
				.gender(gender)
				.build();

		String jwtToken = webTestClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(customer), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isCreated()
				.returnResult(Void.class)
				.getResponseHeaders()
				.get(HttpHeaders.AUTHORIZATION)
				.get(0);

		//find all customers and get customer id
		List<CustomerDTO> customers = webTestClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
				}).returnResult()
				.getResponseBody();

		Long id = customers.stream().filter(cus -> cus.email()
						.equals(customer.getEmail()))
				.map(CustomerDTO::id)
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Customer with not found"));

		//update customer
		CustomerRegistrationRequest updatedCustomer = CustomerRegistrationRequest.builder()
				.age(25)
				.name("Calon Wong")
				.build();

		webTestClient.put()
				.uri(CUSTOMER_URI + "/%s".formatted(id))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.body(Mono.just(updatedCustomer), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus()
				.isOk();

		//get updated customer by id
		CustomerDTO actual = webTestClient.get()
				.uri(CUSTOMER_URI + "/%s".formatted(id))
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(new ParameterizedTypeReference<CustomerDTO>(){})
				.returnResult()
				.getResponseBody();

		// check is fields are updated
		assertThat(actual.age()).isEqualTo(updatedCustomer.getAge());
		assertThat(actual.name()).isEqualTo(updatedCustomer.getName());
	}
}
