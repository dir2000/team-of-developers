package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Project {

    private long id;
    private String name;
    private String description;
    private String status;
    private Date eta;

    public Project(long id, Project project) {
        this(id, project.getName(), project.getDescription(), project.getStatus(), project.getEta());
    }

}
