package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class CustomerDTO {
    private long id;
    @NotNull
    private String name;
    private String address;
    private String phone;

    public CustomerDTO(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone());
    }
}
