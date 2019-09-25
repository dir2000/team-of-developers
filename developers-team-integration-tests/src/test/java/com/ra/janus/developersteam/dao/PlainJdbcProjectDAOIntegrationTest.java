package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PlainJdbcProjectDAOIntegrationTest {

    @Autowired
    private BaseDAO<Project> dao;

    private Project entityToCreate = new Project(1L, "Integration Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30"));

    private Project getUpdatedEntity(Project entity) {

        Project updatedProject = (Project) entity;
        updatedProject.setName("Developers Team");
        updatedProject.setDescription("first project");
        updatedProject.setStatus("WIP");
        updatedProject.setEta(Date.valueOf("2019-08-01"));

        return updatedProject;
    }

    @Test
    public void createProjectTest() throws Exception {
        //when
        Project entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getProjectByIdTest() throws Exception {
        //when
        Project createdEntity = dao.create(entityToCreate);
        Project gottenProject = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenProject);
    }

    @Test
    public void getAllProjectsTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Project createdEntity = dao.create(entityToCreate);
        List<Project> expected = Arrays.asList(createdEntity);
        List<Project> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateProjectTest() throws Exception {
        //when
        Project createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Project updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteProjectTest() throws Exception {
        //when
        Project createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Project actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
