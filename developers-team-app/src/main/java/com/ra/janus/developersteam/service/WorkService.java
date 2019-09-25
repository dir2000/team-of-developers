package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.WorkDTO;
import com.ra.janus.developersteam.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkService implements BaseService<WorkDTO> {
    transient private final BaseDAO<Work> dao;

    @Autowired
    public WorkService(final BaseDAO<Work> dao) {
        this.dao = dao;
    }

    public WorkDTO create(final WorkDTO workDTO) {
        final Work work = dao.create(new Work(workDTO.getId(), workDTO.getName(), workDTO.getPrice()));
        return new WorkDTO(work.getId(), work.getName(), work.getPrice());
    }

    public List<WorkDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new WorkDTO(e.getId(), e.getName(), e.getPrice()))
                .collect(Collectors.toList());
    }

    public WorkDTO get(final long id) {
        final Work work = dao.get(id);
        return new WorkDTO(work.getId(), work.getName(), work.getPrice());
    }

    public boolean update(final WorkDTO workDTO) {
        final Work work = new Work(workDTO.getId(), workDTO.getName(), workDTO.getPrice());
        return dao.update(work);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchPrice(final long id, final BigDecimal price) {
        final Work work = dao.get(id);
        work.setPrice(price);
        return dao.update(work);
    }
}
