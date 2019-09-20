package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.BillDTO;
import com.ra.janus.developersteam.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService implements BaseService<BillDTO> {

    transient final private BaseDAO<Bill> dao;

    @Autowired
    public BillService(final BaseDAO<Bill> dao) {
        this.dao = dao;
    }

    public BillDTO create(final BillDTO billDTO) {
        final Bill bill = dao.create(new Bill(billDTO.getId(), billDTO.getDocDate()));
        return new BillDTO(bill.getId(), bill.getDocDate());
    }

    public List<BillDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new BillDTO(e.getId(), e.getDocDate()))
                .collect(Collectors.toList());
    }

    public BillDTO get(final long id) {
        final Bill bill = dao.get(id);
        return new BillDTO(bill.getId(), bill.getDocDate());
    }

    public boolean update(final BillDTO billDTO) {
        final Bill bill = new Bill(billDTO.getId(), billDTO.getDocDate());
        return dao.update(bill);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }
}
