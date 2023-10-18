package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.AbstractTestcontainer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainer {

	private final Faker faker = new Faker();

	private CustomerJDBCDataAccessService underTest;
	private final CustomerRowMapper rowMapper = new CustomerRowMapper();

	@BeforeEach
	void setUp() {
		underTest = new CustomerJDBCDataAccessService(
				getJdbcTemplate(),
				rowMapper);
	}

	@Test
	void findAll() {
		Customer customer = createNewCustomer();
		underTest.insertCustomer(customer);
		List<Customer> actual = underTest.findAll();
		assertThat(actual).isNotEmpty();
	}

	@Test
	void findCustomerById() {
		Customer customer = createNewCustomer();
		String email = customer.getEmail();
		underTest.insertCustomer(customer);
		Long id = getCustomerIdFromDatabaseByEmail(email);
		Optional<Customer> actual = underTest.findCustomerById(id);

		assertThat(actual).isNotEmpty().hasValueSatisfying(c-> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
			assertThat(c.getGender()).isEqualTo(customer.getGender());
		});
	}

	@Test
	void willReturnEmptyWhenSelectCustomerById(){
		Long id =-1L;
		Optional<Customer> actual = underTest.findCustomerById(id);
		assertThat(actual).isEmpty();
	}

	@Test
	void isExistsCustomerByEmail() {
		Customer customer = createNewCustomer();
		String email = customer.getEmail();
		underTest.insertCustomer(customer);
		boolean actual = underTest.isExistsCustomerByEmail(email);
		assertThat(actual).isTrue();
	}

	@Test
	void isExistsCustomerById() {
		Customer customer = createNewCustomer();
		String email =  customer.getEmail();
		underTest.insertCustomer(customer);
		Long id =getCustomerIdFromDatabaseByEmail(email);
		boolean actual = underTest.isExistsCustomerById(id);
		assertThat(actual).isTrue();
	}

	@Test
	void insertCustomer() {
		Customer customer = createNewCustomer();
		String email = customer.getEmail();
		underTest.insertCustomer(customer);
		Customer actual = underTest.findAll().stream().filter(c -> c.getEmail().equals(email)).findFirst().orElseThrow();
		assertThat(actual).matches(c -> {
			return c.getName().equals(customer.getName()) &&
					c.getEmail().equals(customer.getEmail()) &&
					c.getAge() == customer.getAge() &&
					c.getGender() == customer.getGender();
		});
	}

	@Test
	void deleteCustomerById() {
		Customer customer = createNewCustomer();
		String email = customer.getEmail();
		underTest.insertCustomer(customer);
		Long id = getCustomerIdFromDatabaseByEmail(email);

		underTest.deleteCustomerById(id);

		Optional<Customer> actual = underTest.findCustomerById(id);
		assertThat(actual).isEmpty();
	}

	@Test
	void updateCustomer() {
		Customer customer = createNewCustomer();
		String email = customer.getEmail();
		underTest.insertCustomer(customer);

		Long id = getCustomerIdFromDatabaseByEmail(email);

		Customer updatedCustomer = new Customer();
		updatedCustomer.setId(id);
		updatedCustomer.setName(customer.getName());
		updatedCustomer.setEmail(customer.getEmail());
		updatedCustomer.setAge(5);
		updatedCustomer.setGender(CustomerGender.MALE);
		underTest.updateCustomer(updatedCustomer);

		Optional<Customer> actual = underTest.findCustomerById(id);
		assertThat(actual).isNotEmpty().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getName()).isEqualTo(updatedCustomer.getName());
			assertThat(c.getEmail()).isEqualTo(updatedCustomer.getEmail());
			assertThat(c.getAge()).isEqualTo(updatedCustomer.getAge());
			assertThat(c.getGender()).isEqualTo(updatedCustomer.getGender());
		});
	}

	private Long getCustomerIdFromDatabaseByEmail(String email){
		return underTest.findAll().stream()
				.filter(c -> c.getEmail().equals(email)).map(Customer::getId)
				.findFirst().orElseThrow();
	}

	private Customer createNewCustomer() {
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		return Customer.builder()
				.name(firstName + " " + lastName)
				.email(firstName + "." + lastName + "@dimm.com")
				.age(faker.number().numberBetween(10,90))
				.gender(CustomerGender.MALE)
				.build();
	}
}