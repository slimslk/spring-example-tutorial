package com.dimm.springbootexample.customer;

import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.customer.entity.CustomerGender;
import com.dimm.springbootexample.customer.util.CustomerRowMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

	@Test
	void mapRow() throws SQLException {
		CustomerRowMapper underTest = new CustomerRowMapper();
		ResultSet resultSet = Mockito.mock(ResultSet.class);

		Mockito.when(resultSet.getLong("id")).thenReturn(1L);
		Mockito.when(resultSet.getString("name")).thenReturn("Alex");
		Mockito.when(resultSet.getString("email")).thenReturn("alex@gmail.com");
		Mockito.when(resultSet.getString("password")).thenReturn("password");
		Mockito.when(resultSet.getInt("age")).thenReturn(22);
		Mockito.when(resultSet.getString("gender")).thenReturn(String.valueOf(CustomerGender.MALE));

		Customer actual = underTest.mapRow(resultSet, 1);
		Customer expected = Customer.builder()
				.id(1L)
				.name("Alex")
				.email("alex@gmail.com")
				.password("password")
				.age(22)
				.gender(CustomerGender.MALE)
				.build();

		assertThat(actual).isEqualTo(expected);

	}
}