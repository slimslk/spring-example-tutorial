package com.dimm.springbootexample.customer.entity;

import com.dimm.springbootexample.customer.entity.CustomerGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRegistrationRequest {
    String name;
    String email;
    Integer age;
    CustomerGender gender;
    String password;
}
