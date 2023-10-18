package com.dimm.springbootexample.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessRepositoryTest {

	private CustomerJpaDataAccessRepository underTest;
	@Mock
	private CustomerRepository customerRepository;

	private AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJpaDataAccessRepository(customerRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void findAll() {
		underTest.findAll();
		Mockito.verify(customerRepository).findAll();
	}

	@Test
	void findCustomerById() {
		Long id = 1L;
		underTest.findCustomerById(id);
		Mockito.verify(customerRepository).findById(id);
	}

	@Test
	void isExistsCustomerByEmail() {
		String email = new Faker().internet().emailAddress();
		underTest.isExistsCustomerByEmail(email);
		Mockito.verify(customerRepository).existsCustomerByEmail(email);
	}

	@Test
	void insertCustomer() {
		Customer customer = Customer.builder().name("Dimm").email("dimm@gmail.com").age(10).gender(CustomerGender.MALE).build();
		underTest.insertCustomer(customer);
		Mockito.verify(customerRepository).save(customer);
	}

	@Test
	void deleteCustomerById() {
		Long id = 1L;
		underTest.deleteCustomerById(id);
		Mockito.verify(customerRepository).deleteById(id);
	}

	@Test
	void updateCustomer() {
		Customer customer = Customer.builder().id(1L).name("Dimm").email("dimm@gmail.com").age(10).gender(CustomerGender.MALE).build();
		underTest.updateCustomer(customer);
		Mockito.verify(customerRepository).save(customer);
	}

	@Test
	void isExistsCustomerById() {
		Long id = 1L;
		underTest.isExistsCustomerById(id);
		Mockito.verify(customerRepository).existsById(id);
	}
}