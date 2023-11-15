package com.dimm.springbootexample;

import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.dimm.springbootexample.customer.service.CustomerService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class InsertRandomCustomer {

	private final CustomerService customerService;

	@GetMapping("/random")
	public void insertRandomCustomerToDB() throws ExecutionException, InterruptedException {

		CompletableFuture<RandomUser> future = jsonBodyAsMap(URI.create("https://randomuser.me/api/?inc=gender,name"));
		RandomUser randomUser = future.get();
		String firstName = randomUser.name().get("first");
		String lastName = randomUser.name().get("last");
		CustomerGender gender = randomUser.gender().equals("male")?CustomerGender.MALE:CustomerGender.FEMALE;
		CustomerRegistrationRequest customer = CustomerRegistrationRequest.builder()
				.name(firstName + " " + lastName)
				.age(new Faker().number().numberBetween(18, 70))
				.email(firstName + "." + lastName +"@dimm.com")
				.gender(gender)
				.build();
		customerService.insertCustomer(customer);
	}

	private CompletableFuture<RandomUser> jsonBodyAsMap(URI uri) {
		UncheckedObjectMapper objectMapper = new UncheckedObjectMapper();
		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Accept", "application/json")
				.build();

		return HttpClient.newHttpClient()
				.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenApply(objectMapper::readValue);
	}
}

class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
	/**
	 * Parses the given JSON string into a Map.
	 */
	RandomUser readValue(String content) {
		try {
			System.out.println(content);
			int index1 = content.indexOf("{\"gender\"");
			int index2 = content.indexOf("],\"info\":{");
			String json = content.substring(index1, index2);
			return this.readValue(json, RandomUser.class);
		} catch (IOException ioe) {
			throw new CompletionException(ioe);
		}
	}
}

record RandomUser(String gender, Map<String, String> name, Map<String, String> picture) {
}
