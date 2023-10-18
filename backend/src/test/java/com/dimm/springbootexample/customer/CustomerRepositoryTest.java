package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.AbstractTestcontainer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainer {

	@Autowired
	private CustomerRepository underTest;

	@Autowired
	ApplicationContext applicationContext;

	private Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		System.out.println(applicationContext.getBeanDefinitionCount());
	}

	@Test
	void existsCustomerByEmail() {
		Customer customer = createCustomer();
		underTest.save(customer);

		boolean actual = underTest.existsCustomerByEmail(customer.getEmail());
		assertThat(actual).isTrue();
	}

	@Test
	void returnFalseWhenEmailNotExist() {
		String email = "aaa@aaa";
		boolean actual = underTest.existsCustomerByEmail(email);
		assertThat(actual).isFalse();
	}

	private Customer createCustomer() {
		return Customer.builder()
				.name(faker.name().fullName())
				.email(faker.internet().emailAddress() + "-" + UUID.randomUUID())
				.age(faker.number().numberBetween(10,90))
				.gender(CustomerGender.MALE)
				.build();
	}
}