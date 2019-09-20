package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcQualificationDAO;
import com.ra.janus.developersteam.dto.QualificationDTO;
import com.ra.janus.developersteam.entity.Qualification;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class QualificationServiceMockTest {
    private static final PlainJdbcQualificationDAO MOCK_DAO = mock(PlainJdbcQualificationDAO.class);
    private static final QualificationService SERVICE = new QualificationService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Qualification TEST_QUALIFICATION = new Qualification(TEST_ID, "Web Developer", "Front End");
    private static final QualificationDTO TEST_QUALIFICATION_DTO = new QualificationDTO(TEST_QUALIFICATION);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_QUALIFICATION)).thenReturn(TEST_QUALIFICATION);

        //when
        QualificationDTO actual = SERVICE.create(TEST_QUALIFICATION_DTO);

        //then
        assertEquals(TEST_QUALIFICATION_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Qualification> testList = Arrays.asList(TEST_QUALIFICATION);
        List<QualificationDTO> testListDtO = Arrays.asList(TEST_QUALIFICATION_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<QualificationDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_QUALIFICATION);

        //wnen
        QualificationDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_QUALIFICATION_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Qualification.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_QUALIFICATION_DTO);

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
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_QUALIFICATION);
        when(MOCK_DAO.update(any(Qualification.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchResponsibility(TEST_ID, "Some new responsibility");

        //then
        assertEquals(true, isPatched);
    }
}