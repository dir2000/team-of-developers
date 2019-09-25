package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Qualification {

    private long id;
    private String name;
    private String responsibility;

    public Qualification(long id, Qualification qualification) {
        this(id, qualification.getName(), qualification.getResponsibility());
    }
}


