package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.exception.RequestValidationException;
import com.dimm.springbootexample.exception.DuplicateResourceException;
import com.dimm.springbootexample.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

	private final ICustomerDao customerRepository;

	public CustomerService(@Qualifier("jpa") ICustomerDao customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(long id) {
		return customerRepository.findCustomerById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format("Customer with [%d] id not found", id)));
	}

	public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
		String email = customerRegistrationRequest.getEmail();
		if (customerRepository.isExistsCustomerByEmail(email)) {
			throw new DuplicateResourceException("Email already taken");
		}
		Customer customer = Customer.builder()
				.name(customerRegistrationRequest.name)
				.email(customerRegistrationRequest.getEmail())
				.age(customerRegistrationRequest.getAge())
				.gender(customerRegistrationRequest.getGender())
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
		if (customer.getAge() != customerRegistrationRequest.getAge()) {
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
