package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.exception.DuplicateResourceException;
import com.dimm.springbootexample.exception.RequestValidationException;
import com.dimm.springbootexample.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	private  CustomerService underTest;
	@Mock
	private ICustomerDao customerDao;

	@BeforeEach
	void setUp() {
		underTest = new CustomerService(customerDao);
	}

	@Test
	void canGetAllCustomers() {
		underTest.getAllCustomers();
		Mockito.verify(customerDao).findAll();
	}

	@Test
	void canGetCustomerById() {
		Long id = 1L;
		Customer customer = Customer.builder().id(id).name("Alex").email("alex@gmail.com").age(22).build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		Customer actual = underTest.getCustomerById(id);
		assertThat(actual).isEqualTo(customer);

	}

	@Test
	void willThrowExceptionWhenCustomerIsNotFoundById() {
		Long id = 1L;
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> underTest.getCustomerById(id))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage(String.format("Customer with [%d] id not found", id));
	}

	@Test
	void canInsertCustomer() {
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(
				"Alex", email, 22);
		Mockito.when(customerDao.isExistsCustomerByEmail(email)).thenReturn(false);
		Customer customer = Customer.builder()
				.id(null)
				.name(customerRegistration.getName())
				.email(customerRegistration.getEmail())
				.age(customerRegistration.getAge())
				.build();
		underTest.insertCustomer(customerRegistration);
		ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).insertCustomer(argumentCaptor.capture());
		Customer capturedCustomer = argumentCaptor.getValue();
		assertThat(capturedCustomer.getName()).isEqualTo(customerRegistration.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistration.getEmail());
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());
	}

	@Test
	void willThrowExceptionWhenEmailIsPresentWhileInsertingCustomer() {
		String email = "alex@gmail.com";
		Mockito.when(customerDao.isExistsCustomerByEmail(email)).thenReturn(true);
		assertThatThrownBy(() -> {
			underTest.insertCustomer(
					new CustomerRegistrationRequest("Alex", email, 22)
			);
		}).isInstanceOf(DuplicateResourceException.class).hasMessage("Email already taken");
		Mockito.verify(customerDao, Mockito.never()).insertCustomer(any());
	}

	@Test
	void deleteCustomerById() {
		Long id = 1L;
		Mockito.when(customerDao.isExistsCustomerById(id)).thenReturn(true);
		underTest.deleteCustomerById(id);
		Mockito.verify(customerDao).deleteCustomerById(id);

	}

	@Test
	void willThrowExceptionWhenDeleteAndIdIsNotPresent() {
		Long id = 1L;
		Mockito.when(customerDao.isExistsCustomerById(id)).thenReturn(false);
		assertThatThrownBy(() -> underTest.deleteCustomerById(id))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage(String.format("Customer with [%d] id not found", id));
		Mockito.verify(customerDao, Mockito.never()).deleteCustomerById(any());
	}

	@Test
	void canUpdateCustomerByIdWhenAllFieldsAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		String updatedEmail = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", updatedEmail, 22);
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.age(23)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		Mockito.when(customerDao.isExistsCustomerByEmail(updatedEmail)).thenReturn(false);
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();
		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customerRegistration.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistration.getEmail());
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());

	}

	@Test
	void canUpdateCustomerByIdWhenNamesAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", null, 23);
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.age(23)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customerRegistration.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
		assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
	}

	@Test
	void canUpdateCustomerByIdWhenEmailsAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		String updatedEmail = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(null, updatedEmail, 23);
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email(email)
				.age(23)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		Mockito.when(customerDao.isExistsCustomerByEmail(updatedEmail)).thenReturn(false);
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistration.getEmail());
		assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
	}

	@Test
	void canUpdateCustomerByIdWhenAgesAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(null, null, 22);
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email(email)
				.age(23)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());
	}

	@Test
	void willThrowExceptionWhenEmailIsPresentWhileUpdatingCustomerById() {
		Long id = 1L;
		String email = "alex@gmail.com";
		String updatedEmail = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", updatedEmail, 22);
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.age(23)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		Mockito.when(customerDao.isExistsCustomerByEmail(updatedEmail)).thenReturn(true);

		assertThatThrownBy(() ->
			underTest.updateCustomerById(id, customerRegistration))
				.isInstanceOf(DuplicateResourceException.class)
				.hasMessage("Email already taken");

		Mockito.verify(customerDao, Mockito.never()).updateCustomer(customer);
	}

	@Test
	void willThrowExceptionWhenIdIsNotPresentWhileUpdatingCustomerById() {
		Long id = 1L;
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> underTest.updateCustomerById(id, any()))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessage("Customer with [%d] id not found".formatted(id));

		Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());
	}

	@Test
	void canUpdateCustomerByIdWhenAllFieldsAreSame() {
		Long id = 1L;
		String email = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", email, 22);
		Customer customer = Customer.builder()
				.id(id)
				.name(customerRegistration.getName())
				.email(customerRegistration.getEmail())
				.age(customerRegistration.getAge())
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

		assertThatThrownBy(() -> underTest.updateCustomerById(id, customerRegistration))
				.isInstanceOf(RequestValidationException.class)
				.hasMessage("no data changes found");

		Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());
	}
}