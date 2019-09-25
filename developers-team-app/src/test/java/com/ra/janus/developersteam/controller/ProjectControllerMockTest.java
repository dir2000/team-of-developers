package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.ProjectDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.service.ProjectService;
import com.ra.janus.developersteam.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerMockTest {
    private final ProjectService projectServiceMock = mock(ProjectService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final ProjectController projectController = new ProjectController(projectServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testProjectId = 1L;
    private final Project testProject = new Project(testProjectId, "Great project", "Big", "New", new Date(1L));
    private final ProjectDTO testProjectDTO = new ProjectDTO(testProject);
    private final List<ProjectDTO> testListDTO = Arrays.asList(testProjectDTO);
    private final int responseEntityFieldCount = 3;
    private final String testProjectName = testProject.getName();
    private final ResponseEntity<ResponseListDTO<ProjectDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<ProjectDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public ProjectControllerMockTest() {
        ResponseListDTO<ProjectDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<ProjectDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testProjectDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(ProjectDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(ProjectDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

        testResponseDTO = new ResponseDTO<>(ResponseService.VALIDATION_ERROR, null, null);
        validationErrorEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.BAD_REQUEST);
        doReturn(validationErrorEntity).when(responseServiceMock).validationError(anyString());

        testResponseDTO = new ResponseDTO<>(ResponseService.PERSISTENCE_ERROR, null, null);
        persistenceErrorEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        doReturn(persistenceErrorEntity).when(responseServiceMock).persistenceError(anyString());
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(projectController)
                .build();
    }

    @Test
    void whenGetProjectsShouldReturnProjectsJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testProject.getId()));
    }

    @Test
    void whenGetProjectShouldReturnProjectJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/projects/" + testProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testProject.getId()));
    }

    @Test
    void whenCreateAProjectShouldReturnProjectJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testProjectDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testProject.getId()));
    }

    @Test
    void whenCreateAProjectShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testProjectDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testProjectDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testProjectDTO.setName(testProjectName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateProjectShouldReturnProjectJsonRepresentation() throws Exception {
        //given
        when(projectServiceMock.update(testProjectDTO)).thenReturn(true);

        String testName = "Jill";
        testProjectDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testProjectDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testProjectDTO.setName(testProjectName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateProjectShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(projectServiceMock.update(testProjectDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testProjectDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateProjectShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testProjectDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testProjectDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testProjectDTO.setName(testProjectName);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteProjectShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(projectServiceMock.delete(testProjectId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/projects/" + testProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteProjectShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(projectServiceMock.delete(testProjectId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/projects/" + testProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchProjectShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        String newDescription = "777-77-77";
        when(projectServiceMock.patchDescription(testProjectId, newDescription)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/projects/" + testProjectId + "/description/" + newDescription)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchProjectShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        String newDescription = "777-77-77";
        when(projectServiceMock.patchDescription(testProjectId, newDescription)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/projects/" + testProjectId + "/description/" + newDescription)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}