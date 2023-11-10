package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.customer.dao.ICustomerDao;
import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.dimm.springbootexample.customer.entity.CustomerDTO;
import com.dimm.springbootexample.customer.service.CustomerDTOMapper;
import com.dimm.springbootexample.customer.service.CustomerService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	private CustomerService underTest;
	@Mock
	private ICustomerDao customerDao;
	@Mock
	PasswordEncoder passwordEncoder;

	private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

	@BeforeEach
	void setUp() {
		underTest = new CustomerService(customerDao, passwordEncoder, customerDTOMapper, authenticationManager);
	}

	@Test
	void canGetAllCustomers() {
		underTest.getAllCustomers();
		Mockito.verify(customerDao).findAll();
	}

	@Test
	void canGetCustomerById() {
		Long id = 1L;
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email("alex@gmail.com")
				.password("password")
				.age(22)
				.gender(CustomerGender.MALE)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		CustomerDTO expected = customerDTOMapper.apply(customer);
		CustomerDTO actual = underTest.getCustomerById(id);
		assertThat(actual).isEqualTo(expected);

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
				"Alex", email, 22, CustomerGender.MALE, "password");
		Mockito.when(customerDao.isExistsCustomerByEmail(email)).thenReturn(false);
		String passwordHash = "aqe%1313";
		Mockito.when(passwordEncoder.encode(any())).thenReturn(passwordHash);

		underTest.insertCustomer(customerRegistration);
		ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).insertCustomer(argumentCaptor.capture());
		Customer capturedCustomer = argumentCaptor.getValue();
		assertThat(capturedCustomer.getName()).isEqualTo(customerRegistration.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistration.getEmail());
		assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistration.getGender());
	}

	@Test
	void willThrowExceptionWhenEmailIsPresentWhileInsertingCustomer() {
		String email = "alex@gmail.com";
		Mockito.when(customerDao.isExistsCustomerByEmail(email)).thenReturn(true);
		assertThatThrownBy(() -> {
			underTest.insertCustomer(
					new CustomerRegistrationRequest("Alex", email, 22, CustomerGender.MALE,"password")
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
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", updatedEmail, 22, CustomerGender.MALE, "password");
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
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
		assertThat(capturedCustomer.getPassword()).isEqualTo(customerRegistration.getPassword());
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistration.getGender());

	}

	@Test
	void canUpdateCustomerByIdWhenNamesAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", null, null, null, null);
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customerRegistration.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
		assertThat(capturedCustomer.getPassword()).isEqualTo(customer.getPassword());
		assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
	}

	@Test
	void canUpdateCustomerByIdWhenEmailsAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		String updatedEmail = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(null, updatedEmail, null, null, null);
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
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
		assertThat(capturedCustomer.getPassword()).isEqualTo(customer.getPassword());
		assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
	}

	@Test
	void canUpdateCustomerByIdWhenAgesAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(null, null, 22, null, null);
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
		assertThat(capturedCustomer.getPassword()).isEqualTo(customer.getPassword());
		assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistration.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
	}

	@Test
	void canUpdateCustomerByIdWhenGendersAreDifferent() {
		Long id = 1L;
		String email = "alex@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest(null, null, null, CustomerGender.FEMALE, null);
		Customer customer = Customer.builder()
				.id(id)
				.name("Alex")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
		underTest.updateCustomerById(id, customerRegistration);

		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
		Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
		Customer capturedCustomer = customerArgumentCaptor.getValue();

		assertThat(capturedCustomer.getId()).isEqualTo(id);
		assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
		assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
		assertThat(capturedCustomer.getPassword()).isEqualTo(customer.getPassword());
		assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
		assertThat(capturedCustomer.getGender()).isEqualTo(customerRegistration.getGender());
	}

	@Test
	void willThrowExceptionWhenEmailIsPresentWhileUpdatingCustomerById() {
		Long id = 1L;
		String email = "alex@gmail.com";
		String updatedEmail = "joe@gmail.com";
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", updatedEmail, 22, CustomerGender.MALE, "newPassword");
		Customer customer = Customer.builder()
				.id(id)
				.name("Joe")
				.email(email)
				.password("password")
				.age(23)
				.gender(CustomerGender.MALE)
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
		CustomerRegistrationRequest customerRegistration = new CustomerRegistrationRequest("Alex", email, 22, CustomerGender.MALE, "newPassword");
		Customer customer = Customer.builder()
				.id(id)
				.name(customerRegistration.getName())
				.email(customerRegistration.getEmail())
				.password(customerRegistration.getPassword())
				.age(customerRegistration.getAge())
				.gender(CustomerGender.MALE)
				.build();
		Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

		assertThatThrownBy(() -> underTest.updateCustomerById(id, customerRegistration))
				.isInstanceOf(RequestValidationException.class)
				.hasMessage("no data changes found");

		Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());
	}
}