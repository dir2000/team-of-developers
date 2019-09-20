package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Bill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PlainJdbcBillDAOIntegrationTest {

    @Autowired
    private BaseDAO<Bill> dao;

    private Bill entityToCreate = new Bill(1L, Date.valueOf("2020-11-03"));

    private Bill getUpdatedEntity(Bill entity) {

        Bill updatedBill = entity;
        updatedBill.setDocDate(Date.valueOf("2019-05-05"));

        return updatedBill;
    }

    @Test
    public void createBillTest() throws Exception {
        //when
        Bill entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getBillByIdTest() throws Exception {
        //when
        Bill createdEntity = dao.create(entityToCreate);
        Bill gottenBill = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenBill);
    }

    @Test
    public void getAllBillsTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Bill createdEntity = dao.create(entityToCreate);
        List<Bill> expected = Arrays.asList(createdEntity);
        List<Bill> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateBillTest() throws Exception {
        //when
        Bill createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Bill updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteBillTest() throws Exception {
        //when
        Bill createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Bill actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
