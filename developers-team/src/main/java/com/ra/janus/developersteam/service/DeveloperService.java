package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.DeveloperDTO;
import com.ra.janus.developersteam.entity.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperService implements BaseService<DeveloperDTO> {
    transient private final BaseDAO<Developer> dao;

    @Autowired
    public DeveloperService(final BaseDAO<Developer> dao) {
        this.dao = dao;
    }

    public DeveloperDTO create(final DeveloperDTO developerDTO) {
        final Developer developer = dao.create(new Developer(developerDTO.getId(), developerDTO.getName()));
        return new DeveloperDTO(developer.getId(), developer.getName());
    }

    public List<DeveloperDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new DeveloperDTO(e.getId(), e.getName()))
                .collect(Collectors.toList());
    }

    public DeveloperDTO get(final long id) {
        final Developer developer = dao.get(id);
        return new DeveloperDTO(developer.getId(), developer.getName());
    }

    public boolean update(final DeveloperDTO developerDTO) {
        final Developer developer = new Developer(developerDTO.getId(), developerDTO.getName());
        return dao.update(developer);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }
}
