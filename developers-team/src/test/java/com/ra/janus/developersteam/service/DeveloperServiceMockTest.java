package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcDeveloperDAO;
import com.ra.janus.developersteam.dto.DeveloperDTO;
import com.ra.janus.developersteam.entity.Developer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DeveloperServiceMockTest {
    private static final PlainJdbcDeveloperDAO MOCK_DAO = mock(PlainJdbcDeveloperDAO.class);
    private static final DeveloperService SERVICE = new DeveloperService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Developer TEST_DEVELOPER = new Developer(TEST_ID, "Nick");
    private static final DeveloperDTO TEST_DEVELOPER_DTO = new DeveloperDTO(TEST_DEVELOPER);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_DEVELOPER)).thenReturn(TEST_DEVELOPER);

        //when
        DeveloperDTO actual = SERVICE.create(TEST_DEVELOPER_DTO);

        //then
        assertEquals(TEST_DEVELOPER_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Developer> testList = Arrays.asList(TEST_DEVELOPER);
        List<DeveloperDTO> testListDtO = Arrays.asList(TEST_DEVELOPER_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<DeveloperDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_DEVELOPER);

        //wnen
        DeveloperDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_DEVELOPER_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Developer.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_DEVELOPER_DTO);

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
}