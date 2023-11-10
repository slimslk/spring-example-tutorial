package com.dimm.springbootexample.customer.controller;

import com.dimm.springbootexample.customer.entity.CustomerRegistrationRequest;
import com.dimm.springbootexample.customer.entity.CustomerDTO;
import com.dimm.springbootexample.customer.service.CustomerService;
import com.dimm.springbootexample.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable(value = "customerId") Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRegistrationRequest customerDTO) {
        customerService.insertCustomer(customerDTO);
        String token = jwtUtil.issueToken(customerDTO.getEmail(), "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomerById(@PathVariable(value = "customerId") Long id) {
        customerService.deleteCustomerById(id);
    }

    @PutMapping("/{customerId}")
    public void updateCustomerById(
            @PathVariable(value = "customerId") Long id,
            @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.updateCustomerById(id, customerRegistrationRequest);
    }
}
