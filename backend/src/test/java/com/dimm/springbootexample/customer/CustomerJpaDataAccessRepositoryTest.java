package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.customer.dao.impl.CustomerJpaDataAccessRepository;
import com.dimm.springbootexample.customer.dao.ICustomerRepository;
import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJpaDataAccessRepositoryTest {

	private CustomerJpaDataAccessRepository underTest;

	@Mock
	private ICustomerRepository ICustomerRepository;

	private AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJpaDataAccessRepository(ICustomerRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void findAll() {
		underTest.findAll();
		Mockito.verify(ICustomerRepository).findAll();
	}

	@Test
	void findCustomerById() {
		Long id = 1L;
		underTest.findCustomerById(id);
		Mockito.verify(ICustomerRepository).findById(id);
	}

	@Test
	void findCustomerByEmail() {
		String email = "jojo@momo.com";
		underTest.findCustomerByEmail(email);
		Mockito.verify(ICustomerRepository).findCustomerByEmail(email);
	}

	@Test
	void isExistsCustomerByEmail() {
		String email = new Faker().internet().emailAddress();
		underTest.isExistsCustomerByEmail(email);
		Mockito.verify(ICustomerRepository).existsCustomerByEmail(email);
	}

	@Test
	void insertCustomer() {
		Customer customer = Customer.builder().name("Dimm").email("dimm@gmail.com")
				.password("password")
				.age(10).gender(CustomerGender.MALE).build();
		underTest.insertCustomer(customer);
		Mockito.verify(ICustomerRepository).save(customer);
	}

	@Test
	void deleteCustomerById() {
		Long id = 1L;
		underTest.deleteCustomerById(id);
		Mockito.verify(ICustomerRepository).deleteById(id);
	}

	@Test
	void updateCustomer() {
		Customer customer = Customer.builder().id(1L).name("Dimm").email("dimm@gmail.com")
				.password("password")
				.age(10).gender(CustomerGender.MALE).build();
		underTest.updateCustomer(customer);
		Mockito.verify(ICustomerRepository).save(customer);
	}

	@Test
	void isExistsCustomerById() {
		Long id = 1L;
		underTest.isExistsCustomerById(id);
		Mockito.verify(ICustomerRepository).existsById(id);
	}
}