package com.dimm.springbootexample.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@RequiredArgsConstructor
public class CustomerJDBCDataAccessService implements ICustomerDao{

	private final JdbcTemplate jdbcTemplate;
	private final CustomerRowMapper customerRowMapper;

	@Override
	public List<Customer> findAll() {
		String sql = """
				Select id, name, email, age from customer
				""";
		return jdbcTemplate.query(sql, customerRowMapper);
	}

	@Override
	public Optional<Customer> findCustomerById(Long id) {
		String sql = """
				SELECT id, name, email, age FROM customer WHERE id = ?
				""";
		return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
	}

	@Override
	public boolean isExistsCustomerByEmail(String email) {
		String sql = """
				SELECT count(email) FROM customer WHERE email = ?
				""";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
		return count > 0;
	}

	@Override
	public boolean isExistsCustomerById(Long id) {
		String sql = """
				SELECT count(id) FROM customer WHERE id = ?
				""";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
		return count > 0;
	}

	@Override
	public void insertCustomer(Customer customer) {
		String sql = """
			INSERT INTO customer(name, email, age)
			VALUES(?,?,?)
			""";
		int result = jdbcTemplate.update(sql,
				customer.getName(),
				customer.getEmail(),
				customer.getAge());
	}

	@Override
	public void deleteCustomerById(Long id) {
		String sql = """
				DELETE FROM customer WHERE id = ?
				""";
		int result = jdbcTemplate.update(sql, id);
	}

	@Override
	public void updateCustomer(Customer customer) {
		if(customer.getName()!=null && customer.getEmail()!=null && customer.getAge()!=0){
			String sql = """
				UPDATE customer SET (name, email, age) = (?,?,?) WHERE id = ?
				""";
			int result = jdbcTemplate.update(sql,
					customer.getName(),
					customer.getEmail(),
					customer.getAge(),
					customer.getId());
		}
	}
}
