package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcWorkDAO;
import com.ra.janus.developersteam.dto.WorkDTO;
import com.ra.janus.developersteam.entity.Work;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class WorkServiceMockTest {
    private static final PlainJdbcWorkDAO MOCK_DAO = mock(PlainJdbcWorkDAO.class);
    private static final WorkService SERVICE = new WorkService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Work TEST_WORK = new Work(1L, "name", new BigDecimal(7));
    private static final WorkDTO TEST_WORK_DTO = new WorkDTO(TEST_WORK);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_WORK)).thenReturn(TEST_WORK);

        //when
        WorkDTO actual = SERVICE.create(TEST_WORK_DTO);

        //then
        assertEquals(TEST_WORK_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Work> testList = Arrays.asList(TEST_WORK);
        List<WorkDTO> testListDtO = Arrays.asList(TEST_WORK_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<WorkDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_WORK);

        //wnen
        WorkDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_WORK_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Work.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_WORK_DTO);

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
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_WORK);
        when(MOCK_DAO.update(any(Work.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchPrice(TEST_ID, new BigDecimal(8));

        //then
        assertEquals(true, isPatched);
    }
}