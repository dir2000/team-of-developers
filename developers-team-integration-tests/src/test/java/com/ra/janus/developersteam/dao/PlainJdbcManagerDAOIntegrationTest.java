package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Manager;
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
public class PlainJdbcManagerDAOIntegrationTest {

    @Autowired
    private BaseDAO<Manager> dao;

    private Manager entityToCreate = new Manager(1L, "John", "manager@gmail.com", "050-000-11-22");

    private Manager getUpdatedEntity(Manager entity) {

        Manager updatedManager = entity;
        updatedManager.setName("Jack");
        updatedManager.setEmail("jach@gmail.com");
        updatedManager.setPhone("050-222-33-44");

        return updatedManager;
    }

    @Test
    public void createManagerTest() throws Exception {
        //when
        Manager entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getManagerByIdTest() throws Exception {
        //when
        Manager createdEntity = dao.create(entityToCreate);
        Manager gottenManager = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenManager);
    }

    @Test
    public void getAllManagersTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Manager createdEntity = dao.create(entityToCreate);
        List<Manager> expected = Arrays.asList(createdEntity);
        List<Manager> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateManagerTest() throws Exception {
        //when
        Manager createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Manager updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteManagerTest() throws Exception {
        //when
        Manager createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Manager actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
