package com.dimm.springbootexample;

import com.dimm.springbootexample.customer.Customer;
import com.dimm.springbootexample.customer.CustomerRepository;
import com.dimm.springbootexample.customer.ICustomerDao;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringBootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(@Qualifier("jdbc") ICustomerDao customerRepository) {
//		return args -> {
//			Faker faker = new Faker();
//				String name = faker.name().fullName();
//				String email = faker.internet().emailAddress();
//				while (customerRepository.isExistsCustomerByEmail(email)) {
//					email = faker.internet().emailAddress();
//				}
//				customerRepository.insertCustomer(
//						Customer.builder()
//								.name(name)
//								.email(email)
//								.age(faker.number().numberBetween(10,80))
//								.build()
//				);
//		};
//	}
}
