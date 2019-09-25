package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.ManagerDTO;
import com.ra.janus.developersteam.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService implements BaseService<ManagerDTO> {
    transient private final BaseDAO<Manager> dao;

    @Autowired
    public ManagerService(final BaseDAO<Manager> dao) {
        this.dao = dao;
    }

    public ManagerDTO create(final ManagerDTO managerDTO) {
        final Manager manager = dao.create(new Manager(managerDTO.getId(), managerDTO.getName(), managerDTO.getEmail(), managerDTO.getPhone()));
        return new ManagerDTO(manager.getId(), manager.getName(), manager.getEmail(), manager.getPhone());
    }

    public List<ManagerDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new ManagerDTO(e.getId(), e.getName(), e.getEmail(), e.getPhone()))
                .collect(Collectors.toList());
    }

    public ManagerDTO get(final long id) {
        final Manager manager = dao.get(id);
        return new ManagerDTO(manager.getId(), manager.getName(), manager.getEmail(), manager.getPhone());
    }

    public boolean update(final ManagerDTO managerDTO) {
        final Manager manager = new Manager(managerDTO.getId(), managerDTO.getName(), managerDTO.getEmail(), managerDTO.getPhone());
        return dao.update(manager);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchPhone(final long id, final String phone) {
        final Manager manager = dao.get(id);
        manager.setPhone(phone);
        return dao.update(manager);
    }
}
