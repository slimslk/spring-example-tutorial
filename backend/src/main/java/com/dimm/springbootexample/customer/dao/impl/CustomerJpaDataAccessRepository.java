package com.dimm.springbootexample.customer.dao.impl;

import com.dimm.springbootexample.customer.dao.ICustomerDao;
import com.dimm.springbootexample.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
@RequiredArgsConstructor
public class CustomerJpaDataAccessRepository implements ICustomerDao {

	private final com.dimm.springbootexample.customer.dao.ICustomerRepository ICustomerRepository;

	@Override
	public List<Customer> findAll() {
		return ICustomerRepository.findAll();
	}

	@Override
	public Optional<Customer> findCustomerById(Long id) {
		return ICustomerRepository.findById(id);
	}

	@Override
	public boolean isExistsCustomerByEmail(String email) {
		return ICustomerRepository.existsCustomerByEmail(email);
	}

	@Override
	public void insertCustomer(Customer customer) {
		ICustomerRepository.save(customer);
	}

	@Override
	public void deleteCustomerById(Long id) {
		ICustomerRepository.deleteById(id);
	}

	@Override
	public void updateCustomer(Customer customer) {
		ICustomerRepository.save(customer);
	}

	@Override
	public Optional<Customer> findCustomerByEmail(String email) {
		return ICustomerRepository.findCustomerByEmail(email);
	}

	@Override
	public boolean isExistsCustomerById(Long id) {
		return ICustomerRepository.existsById(id);
	}
}
