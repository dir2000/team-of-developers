package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.ProjectDTO;
import com.ra.janus.developersteam.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements BaseService<ProjectDTO> {
    transient private final BaseDAO<Project> dao;

    @Autowired
    public ProjectService(final BaseDAO<Project> dao) {
        this.dao = dao;
    }

    public ProjectDTO create(final ProjectDTO projectDTO) {
        final Project project = dao.create(new Project(projectDTO.getId(), projectDTO.getName(), projectDTO.getDescription(), projectDTO.getStatus(), projectDTO.getEta()));
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getStatus(), project.getEta());
    }

    public List<ProjectDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new ProjectDTO(e.getId(), e.getName(), e.getDescription(), e.getStatus(), e.getEta()))
                .collect(Collectors.toList());
    }

    public ProjectDTO get(final long id) {
        final Project project = dao.get(id);
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription(), project.getStatus(), project.getEta());
    }

    public boolean update(final ProjectDTO projectDTO) {
        final Project project = new Project(projectDTO.getId(), projectDTO.getName(), projectDTO.getDescription(), projectDTO.getStatus(), projectDTO.getEta());
        return dao.update(project);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchDescription(final long id, final String description) {
        final Project project = dao.get(id);
        project.setDescription(description);
        return dao.update(project);
    }
}
