package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcTechnicalTaskDAO;
import com.ra.janus.developersteam.dto.TaskDTO;
import com.ra.janus.developersteam.entity.Task;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TaskServiceMockTest {
    private static final PlainJdbcTechnicalTaskDAO MOCK_DAO = mock(PlainJdbcTechnicalTaskDAO.class);
    private static final TaskService SERVICE = new TaskService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Task TEST_TASK = new Task(TEST_ID, "title", "description");
    private static final TaskDTO TEST_TASK_DTO = new TaskDTO(TEST_TASK);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_TASK)).thenReturn(TEST_TASK);

        //when
        TaskDTO actual = SERVICE.create(TEST_TASK_DTO);

        //then
        assertEquals(TEST_TASK_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Task> testList = Arrays.asList(TEST_TASK);
        List<TaskDTO> testListDtO = Arrays.asList(TEST_TASK_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<TaskDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_TASK);

        //wnen
        TaskDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_TASK_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Task.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_TASK_DTO);

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
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_TASK);
        when(MOCK_DAO.update(any(Task.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchDescription(TEST_ID, "Some new description");

        //then
        assertEquals(true, isPatched);
    }
}