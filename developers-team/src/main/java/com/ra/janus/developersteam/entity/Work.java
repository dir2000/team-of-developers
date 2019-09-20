package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Work {
    private long id;
    private String name;
    private BigDecimal price;

    public Work(long id, Work work) {
        this(id, work.getName(), work.getPrice());
    }

}
