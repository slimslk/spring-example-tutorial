package com.dimm.springbootexample.customer.dao;

import com.dimm.springbootexample.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsCustomerByEmail(String email);

    Optional<Customer> findCustomerByEmail(String email);

}
