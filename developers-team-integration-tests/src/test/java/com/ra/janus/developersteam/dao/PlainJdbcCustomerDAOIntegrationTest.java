package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PlainJdbcCustomerDAOIntegrationTest {
    @Autowired
    private BaseDAO<Customer> dao;

    private Customer entityToCreate = new Customer(1L, "John", "Home", "12345");

    private Customer getUpdatedEntity(Customer entity) {

        Customer updatedCustomer = entity;
        updatedCustomer.setName("Jack");
        updatedCustomer.setAddress("Somewhere");
        updatedCustomer.setPhone("54321");

        return updatedCustomer;
    }

    @Test
    public void createCustomerTest() throws Exception {
        //when
        Customer entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        //when
        Customer createdEntity = dao.create(entityToCreate);
        Customer gottenCustomer = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenCustomer);
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Customer createdEntity = dao.create(entityToCreate);
        List<Customer> expected = Arrays.asList(createdEntity);
        List<Customer> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateCustomerTest() throws Exception {
        //when
        Customer createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Customer updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        //when
        Customer createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Customer actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
