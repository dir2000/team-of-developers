package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Developer {
    long id;
    String name;

    public Developer(long id, Developer developer) {
        this.id = id;
        this.name = developer.getName();
    }
}