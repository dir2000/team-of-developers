package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ProjectDTO {

    private long id;
    @NotNull
    private String name;
    private String description;
    private String status;
    private Date eta;

    public ProjectDTO(Project project) {
        this(project.getId(), project.getName(), project.getDescription(), project.getStatus(), project.getEta());
    }
}
