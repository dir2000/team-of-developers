package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.BaseDAO;
import com.ra.janus.developersteam.dto.CustomerDTO;
import com.ra.janus.developersteam.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements BaseService<CustomerDTO> {

    transient private final BaseDAO<Customer> dao;

    @Autowired
    public CustomerService(final BaseDAO<Customer> dao) {
        this.dao = dao;
    }

    public CustomerDTO create(final CustomerDTO customerDTO) {
        final Customer customer = dao.create(new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getPhone()));
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone());
    }

    public List<CustomerDTO> getAll() {
        return dao.getAll()
                .stream()
                .map(e -> new CustomerDTO(e.getId(), e.getName(), e.getAddress(), e.getPhone()))
                .collect(Collectors.toList());
    }

    public CustomerDTO get(final long id) {
        final Customer customer = dao.get(id);
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone());
    }

    public boolean update(final CustomerDTO customerDTO) {
        final Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getPhone());
        return dao.update(customer);
    }

    public boolean delete(final long id) {
        return dao.delete(id);
    }

    public boolean patchAddress(final long id, final String address) {
        final Customer customer = dao.get(id);
        customer.setAddress(address);
        return dao.update(customer);
    }
}
