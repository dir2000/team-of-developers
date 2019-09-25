package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Manager {
    private long id;
    private String name;
    private String email;
    private String phone;


    public Manager(Long id, Manager manager) {
        this.id = id;
        this.name = manager.getName();
        this.email = manager.getEmail();
        this.phone = manager.getPhone();
    }
}
