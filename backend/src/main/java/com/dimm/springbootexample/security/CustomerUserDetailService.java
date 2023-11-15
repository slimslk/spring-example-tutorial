package com.dimm.springbootexample.security;

import com.dimm.springbootexample.customer.dao.ICustomerDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService implements UserDetailsService {

	private final ICustomerDao customerDao;

	public CustomerUserDetailService(@Qualifier("jdbc") ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return customerDao.findCustomerByEmail(username).orElseThrow(() ->
				new UsernameNotFoundException(String.format("%s not found", username))
		);
	}
}
