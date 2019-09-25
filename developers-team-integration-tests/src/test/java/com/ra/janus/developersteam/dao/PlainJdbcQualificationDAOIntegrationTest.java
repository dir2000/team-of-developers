package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.entity.Qualification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PlainJdbcQualificationDAOIntegrationTest {

    @Autowired
    private BaseDAO<Qualification> dao;

    private Qualification entityToCreate = new Qualification(1L, "Web Developer", "Front End");

    private Qualification getUpdatedEntity(Qualification entity) {

        Qualification updatedQualification = entity;

        updatedQualification.setName("Java Developer");
        updatedQualification.setResponsibility("Back End");

        return updatedQualification;
    }

    @Test
    public void createQualificationTest() throws Exception {
        //when
        Qualification entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getQualificationByIdTest() throws Exception {
        //when
        Qualification createdEntity = dao.create(entityToCreate);
        Qualification gottenQualification = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenQualification);
    }

    @Test
    public void getAllQualificationsTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Qualification createdEntity = dao.create(entityToCreate);
        List<Qualification> expected = Arrays.asList(createdEntity);
        List<Qualification> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateQualificationTest() throws Exception {
        //when
        Qualification createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Qualification updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteQualificationTest() throws Exception {
        //when
        Qualification createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Qualification actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
