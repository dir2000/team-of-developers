package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Work;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PlainJdbcWorkDAOIntegrationTest {

    @Autowired
    private BaseDAO<Work> dao;

    private Work entityToCreate = new Work(1L, "Developer", new BigDecimal("4000.00"));

    private Work getUpdatedEntity(Work entity) {

        Work updatedWork = entity;
        updatedWork.setName("Tester");
        updatedWork.setPrice(new BigDecimal("2000.00"));

        return updatedWork;
    }

    @Test
    public void createWorkTest() throws Exception {
        //when
        Work entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getWorkByIdTest() throws Exception {
        //when
        Work createdEntity = dao.create(entityToCreate);
        Work gottenWork = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenWork);
    }

    @Test
    public void getAllWorksTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Work createdEntity = dao.create(entityToCreate);
        List<Work> expected = Arrays.asList(createdEntity);
        List<Work> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateWorkTest() throws Exception {
        //when
        Work createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Work updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteWorkTest() throws Exception {
        //when
        Work createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Work actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }

}
