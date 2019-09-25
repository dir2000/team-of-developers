package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Qualification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class QualificationDTO {
    private long id;
    @NotNull
    private String name;
    private String responsibility;

    public QualificationDTO(Qualification qualification) {
        this(qualification.getId(), qualification.getName(), qualification.getResponsibility());
    }
}


