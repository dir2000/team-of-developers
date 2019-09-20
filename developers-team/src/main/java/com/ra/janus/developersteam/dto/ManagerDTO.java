package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ManagerDTO {
    private long id;
    @NotNull
    private String name;
    private String email;
    private String phone;

    public ManagerDTO(Manager manager) {
        this(manager.getId(), manager.getName(), manager.getEmail(), manager.getPhone());
    }
}
