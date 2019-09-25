package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Customer {
    private long id;
    private String name;
    private String address;
    private String phone;

    public Customer(long id, Customer customer) {
        this(id, customer.getName(), customer.getAddress(), customer.getPhone());
    }
}
