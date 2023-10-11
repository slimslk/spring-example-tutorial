package com.dimm.springbootexample.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers(){
       return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable(value = "customerId") Long id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public void addCustomer(@RequestBody CustomerRegistrationRequest customerDTO){
        customerService.insertCustomer(customerDTO);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomerById(@PathVariable(value = "customerId") Long id){
        customerService.deleteCustomerById(id);
    }

    @PutMapping("/{customerId}")
    public void updateCustomerById(
            @PathVariable(value = "customerId") Long id,
            @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.updateCustomerById(id, customerRegistrationRequest);
    }
}
