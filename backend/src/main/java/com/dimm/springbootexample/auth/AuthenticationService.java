package com.dimm.springbootexample.auth;

import com.dimm.springbootexample.customer.entity.Customer;
import com.dimm.springbootexample.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public AuthenticationResponse login (AuthenticationRequest request) {
		Customer customer = (Customer) authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(
						request.username(),
						request.password()
				)).getPrincipal();
		String token = jwtUtil.issueToken(customer.getUsername(), customer.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));

		return new AuthenticationResponse(token);
	}

}
