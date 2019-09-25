package com.ra.janus.developersteam.dto;

import com.ra.janus.developersteam.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class TaskDTO {
    private long id;
    @NotNull
    private String title;
    private String description;

    public TaskDTO(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription());
    }
}
