package com.dimm.springbootexample.customer.entity;

import lombok.Builder;

@Builder
public record CustomerDTO(Long id, String name, String email, int age, CustomerGender gender) {
}
