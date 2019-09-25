package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcProjectDAO;
import com.ra.janus.developersteam.dto.ProjectDTO;
import com.ra.janus.developersteam.entity.Project;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ProjectServiceMockTest {
    private static final PlainJdbcProjectDAO MOCK_DAO = mock(PlainJdbcProjectDAO.class);
    private static final ProjectService SERVICE = new ProjectService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Project TEST_PROJECT = new Project(TEST_ID, "Mock Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30"));
    private static final ProjectDTO TEST_PROJECT_DTO = new ProjectDTO(TEST_PROJECT);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_PROJECT)).thenReturn(TEST_PROJECT);

        //when
        ProjectDTO actual = SERVICE.create(TEST_PROJECT_DTO);

        //then
        assertEquals(TEST_PROJECT_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Project> testList = Arrays.asList(TEST_PROJECT);
        List<ProjectDTO> testListDtO = Arrays.asList(TEST_PROJECT_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<ProjectDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_PROJECT);

        //wnen
        ProjectDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_PROJECT_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Project.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_PROJECT_DTO);

        //then
        assertEquals(true, isUpdated);
    }

    @Test
    void whenDeleteShouldDeleteIt() {
        //given
        when(MOCK_DAO.delete(TEST_ID)).thenReturn(true);

        //wnen
        boolean isDeleted = SERVICE.delete(TEST_ID);

        //then
        assertEquals(true, isDeleted);
    }


    @Test
    void whenPatchShouldPatchIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_PROJECT);
        when(MOCK_DAO.update(any(Project.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchDescription(TEST_ID, "Some new description");

        //then
        assertEquals(true, isPatched);
    }
}