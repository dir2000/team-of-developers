package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Task;
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
public class PlainJdbcTaskDAOIntegrationTest {

    @Autowired
    private BaseDAO<Task> dao;

    private Task entityToCreate = new Task(1L, "Jan 40", "Integration tests for Dev Team");

    private Task getUpdatedEntity(Task entity) {

        Task updatedTask = entity;
        updatedTask.setTitle("Jan 6");
        updatedTask.setDescription("Project for Dev Team");

        return updatedTask;
    }

    @Test
    public void createTechnicalTaskTest() throws Exception {
        //when
        Task entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getTechnicalTaskByIdTest() throws Exception {
        //when
        Task createdEntity = dao.create(entityToCreate);
        Task gottenTechnicalTask = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenTechnicalTask);
    }

    @Test
    public void getAllTechnicalTasksTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Task createdEntity = dao.create(entityToCreate);
        List<Task> expected = Arrays.asList(createdEntity);
        List<Task> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateTechnicalTaskTest() throws Exception {
        //when
        Task createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Task updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteTechnicalTaskTest() throws Exception {
        //when
        Task createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Task actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
