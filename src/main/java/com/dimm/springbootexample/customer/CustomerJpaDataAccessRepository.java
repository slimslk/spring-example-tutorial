package com.dimm.springbootexample.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
@RequiredArgsConstructor
public class CustomerJpaDataAccessRepository implements ICustomerDao{

	private final CustomerRepository customerRepository;

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> findCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public boolean isExistsCustomerByEmail(String email) {
		return customerRepository.existsCustomerByEmail(email);
	}

	@Override
	public void insertCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public void deleteCustomerById(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	public void updateCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public boolean isExistsCustomerById(Long id) {
		return customerRepository.existsById(id);
	}
}
