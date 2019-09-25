package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.TaskDTO;
import com.ra.janus.developersteam.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService implements BaseService<TaskDTO> {
    transient private final BaseDAO<Task> dao;

    @Autowired
    public TaskService(final BaseDAO<Task> dao) {
        this.dao = dao;
    }

    public TaskDTO create(final TaskDTO taskDTO) {
        final Task task = dao.create(new Task(taskDTO.getId(), taskDTO.getTitle(), taskDTO.getDescription()));
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription());
    }

    public List<TaskDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new TaskDTO(e.getId(), e.getTitle(), e.getDescription()))
                .collect(Collectors.toList());
    }

    public TaskDTO get(final long id) {
        final Task task = dao.get(id);
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription());
    }

    public boolean update(final TaskDTO taskDTO) {
        final Task task = new Task(taskDTO.getId(), taskDTO.getTitle(), taskDTO.getDescription());
        return dao.update(task);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchDescription(final long id, final String description) {
        final Task task = dao.get(id);
        task.setDescription(description);
        return dao.update(task);
    }
}
