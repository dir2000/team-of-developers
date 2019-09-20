package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Work;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class WorkDTO {
    private long id;
    @NotNull
    private String name;
    private BigDecimal price;

    public WorkDTO(Work work) {
        this(work.getId(), work.getName(), work.getPrice());
    }
}
