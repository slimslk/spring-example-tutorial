package com.dimm.springbootexample.customer.dao;

import com.dimm.springbootexample.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerDao {
	List<Customer> findAll();
	Optional<Customer> findCustomerById(Long id);
	boolean isExistsCustomerByEmail(String email);
	boolean isExistsCustomerById(Long id);
	void insertCustomer(Customer customer);
	void deleteCustomerById(Long id);
	void updateCustomer(Customer customer);
	Optional<Customer> findCustomerByEmail(String email);
}
