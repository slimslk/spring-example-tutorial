package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.AbstractTestcontainer;
import com.dimm.springbootexample.customer.dao.ICustomerRepository;
import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ICustomerRepositoryTest extends AbstractTestcontainer {

	@Autowired
	private ICustomerRepository underTest;

	@Autowired
	ApplicationContext applicationContext;

	private Faker faker = new Faker();

	@BeforeEach
	void setUp() {
		System.out.println(applicationContext.getBeanDefinitionCount());
	}

	@Test
	void isExistsCustomerByEmail() {
		Customer customer = createCustomer();
		underTest.save(customer);

		boolean actual = underTest.existsCustomerByEmail(customer.getEmail());
		assertThat(actual).isTrue();
	}

	@Test
	void canFindCustomerByEmail() {
		Customer customer = createCustomer();
		underTest.save(customer);
		Optional<Customer> actual = underTest.findCustomerByEmail(customer.getEmail());
		assertThat(actual.get()).isEqualTo(customer);
	}

	@Test
	void returnFalseWhenEmailNotExist() {
		String email = "aaa@aaa";
		boolean actual = underTest.existsCustomerByEmail(email);
		assertThat(actual).isFalse();
	}

	@Test
	void returnEmptyWhenEmailNotFound() {
		String email = "aaa@aaa";
		Optional<Customer> actual = underTest.findCustomerByEmail(email);
		assertThat(actual).isEmpty();
	}

	private Customer createCustomer() {
		return Customer.builder()
				.name(faker.name().fullName())
				.email(faker.internet().emailAddress() + "-" + UUID.randomUUID())
				.password("password")
				.age(faker.number().numberBetween(10,90))
				.gender(CustomerGender.MALE)
				.build();
	}
}