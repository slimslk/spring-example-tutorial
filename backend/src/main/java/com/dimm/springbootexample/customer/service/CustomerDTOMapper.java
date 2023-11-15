package com.dimm.springbootexample.customer.service;

import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {
	@Override
	public CustomerDTO apply(Customer customer) {
		return new CustomerDTO(
				customer.getId(),
				customer.getName(),
				customer.getEmail(),
				customer.getAge(),
				customer.getGender()
		);
	}
}
