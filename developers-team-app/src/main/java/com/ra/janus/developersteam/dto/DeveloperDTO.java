package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Developer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class DeveloperDTO {
    long id;
    @NotNull
    String name;

    public DeveloperDTO(Developer developer) {
        this(developer.getId(), developer.getName());
    }
}