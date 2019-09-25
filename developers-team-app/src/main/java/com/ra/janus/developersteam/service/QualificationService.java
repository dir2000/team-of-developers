package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.QualificationDTO;
import com.ra.janus.developersteam.entity.Qualification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QualificationService implements BaseService<QualificationDTO> {
    transient private final BaseDAO<Qualification> dao;

    @Autowired
    public QualificationService(final BaseDAO<Qualification> dao) {
        this.dao = dao;
    }

    public QualificationDTO create(final QualificationDTO qualificationDTO) {
        final Qualification qualification = dao.create(new Qualification(qualificationDTO.getId(), qualificationDTO.getName(), qualificationDTO.getResponsibility()));
        return new QualificationDTO(qualification.getId(), qualification.getName(), qualification.getResponsibility());
    }

    public List<QualificationDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new QualificationDTO(e.getId(), e.getName(), e.getResponsibility()))
                .collect(Collectors.toList());
    }

    public QualificationDTO get(final long id) {
        final Qualification qualification = dao.get(id);
        return new QualificationDTO(qualification.getId(), qualification.getName(), qualification.getResponsibility());
    }

    public boolean update(final QualificationDTO qualificationDTO) {
        final Qualification qualification = new Qualification(qualificationDTO.getId(), qualificationDTO.getName(), qualificationDTO.getResponsibility());
        return dao.update(qualification);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchResponsibility(final long id, final String responsibility) {
        final Qualification qualification = dao.get(id);
        qualification.setResponsibility(responsibility);
        return dao.update(qualification);
    }
}
