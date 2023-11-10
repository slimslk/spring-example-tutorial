package com.dimm.springbootexample.customer.service;

import com.dimm.springbootexample.auth.AuthenticationRequest;
import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.dimm.springbootexample.customer.dao.ICustomerDao;
import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerDTO;
import com.dimm.springbootexample.exception.AuthenticateValidationException;
import com.dimm.springbootexample.exception.RequestValidationException;
import com.dimm.springbootexample.exception.DuplicateResourceException;
import com.dimm.springbootexample.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

	private final ICustomerDao customerRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomerDTOMapper customerDTOMapper;
	private final AuthenticationManager authenticationManager;

	public CustomerService(@Qualifier("jpa") ICustomerDao customerRepository, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper, AuthenticationManager authenticationManager) {
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
		this.customerDTOMapper = customerDTOMapper;
		this.authenticationManager = authenticationManager;
	}

	public List<CustomerDTO> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		return customers.stream().map(customerDTOMapper).collect(Collectors.toList());
	}

	public CustomerDTO getCustomerById(long id) {
		return customerRepository.findCustomerById(id)
				.map(customerDTOMapper)
				.orElseThrow(
				() -> new ResourceNotFoundException(String.format("Customer with [%d] id not found", id)));
	}

	public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
		String email = customerRegistrationRequest.getEmail();
		if (customerRepository.isExistsCustomerByEmail(email)) {
			throw new DuplicateResourceException("Email already taken");
		}
		Customer customer = Customer.builder()
				.name(customerRegistrationRequest.getName())
				.email(customerRegistrationRequest.getEmail())
				.age(customerRegistrationRequest.getAge())
				.gender(customerRegistrationRequest.getGender())
				.password(passwordEncoder.encode(customerRegistrationRequest.getPassword()))
				.build();
		customerRepository.insertCustomer(customer);
	}

	public void deleteCustomerById(Long id) {
		if (!customerRepository.isExistsCustomerById(id)) {
			throw new ResourceNotFoundException("Customer with [%d] id not found".formatted(id));
		}
		customerRepository.deleteCustomerById(id);
	}

	public void updateCustomerById(Long id, CustomerRegistrationRequest customerRegistrationRequest) {
		Customer customer = customerRepository.findCustomerById(id).orElseThrow(
				() -> new ResourceNotFoundException("Customer with [%d] id not found".formatted(id)));

		boolean isChanged = false;
		if (customerRegistrationRequest.getName() != null && !customer.getName().equals(customerRegistrationRequest.getName())) {
			customer.setName(customerRegistrationRequest.getName());
			isChanged = true;
		}
		if (customerRegistrationRequest.getEmail() != null && !customer.getEmail().equals(customerRegistrationRequest.getEmail())) {
			if (customerRepository.isExistsCustomerByEmail(customerRegistrationRequest.getEmail())) {
				throw new DuplicateResourceException("Email already taken");
			}
			customer.setEmail(customerRegistrationRequest.getEmail());
			isChanged = true;
		}
		if (customerRegistrationRequest.getAge() != null && customer.getAge() != customerRegistrationRequest.getAge()) {
			customer.setAge(customerRegistrationRequest.getAge());
			isChanged = true;
		}
		if (customerRegistrationRequest.getGender() != null && !customer.getGender().equals(customerRegistrationRequest.getGender())) {
			customer.setGender(customerRegistrationRequest.getGender());
			isChanged = true;
		}
		if (!isChanged) {
			throw new RequestValidationException("no data changes found");
		}
		customerRepository.updateCustomer(customer);
	}
}
