package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcManagerDAO;
import com.ra.janus.developersteam.dto.ManagerDTO;
import com.ra.janus.developersteam.entity.Manager;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ManagerServiceMockTest {
    private static final PlainJdbcManagerDAO MOCK_DAO = mock(PlainJdbcManagerDAO.class);
    private static final ManagerService SERVICE = new ManagerService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Manager TEST_MANAGER = new Manager(TEST_ID, "John", "box@mail.com", "911");
    private static final ManagerDTO TEST_MANAGER_DTO = new ManagerDTO(TEST_MANAGER);

    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_MANAGER)).thenReturn(TEST_MANAGER);

        //when
        ManagerDTO actual = SERVICE.create(TEST_MANAGER_DTO);

        //then
        assertEquals(TEST_MANAGER_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Manager> testList = Arrays.asList(TEST_MANAGER);
        List<ManagerDTO> testListDtO = Arrays.asList(TEST_MANAGER_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<ManagerDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_MANAGER);

        //wnen
        ManagerDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_MANAGER_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Manager.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_MANAGER_DTO);

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
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_MANAGER);
        when(MOCK_DAO.update(any(Manager.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchPhone(TEST_ID, "Some new phone");

        //then
        assertEquals(true, isPatched);
    }
}