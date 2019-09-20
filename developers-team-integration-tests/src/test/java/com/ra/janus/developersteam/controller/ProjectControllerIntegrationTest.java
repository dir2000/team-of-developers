package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.ProjectDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class ProjectControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProjectDTO testProjectDTO = new ProjectDTO(new Project(1L, "Integration Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30")));
    private MockMvc mockMvc;
    @Autowired
    private ProjectController projectController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(projectController)
                .build();
    }

    @Test
    void whenGetProjectDTOsShouldReturnProjectDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<ProjectDTO>> listResponseEntity = projectController.getProjects();
        for (ProjectDTO dto : listResponseEntity.getBody().getResponse()) {
            projectController.deleteProject(dto.getId());
        }

        ProjectDTO created = createDTO(testProjectDTO);
        List<ProjectDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<ProjectDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<ProjectDTO>>() {
                        });

        List<ProjectDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetProjectDTOShouldReturnProjectDTO() throws Exception {
        //given
        ProjectDTO expected = createDTO(testProjectDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/projects/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        ProjectDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateProjectShouldReturnProjectDTO() throws Exception {
        //when
        ProjectDTO created = createDTO(testProjectDTO);

        //then
        assertTrue(created instanceof ProjectDTO);
    }

    @Test
    void whenUpdateProjectShouldReturnConfirmation() throws Exception {
        //given
        ProjectDTO dto = createDTO(testProjectDTO);
        dto.setName("Failed");

        //when
        MvcResult result = mockMvc.perform(
                put("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ProjectDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteProjectShouldReturnConfirmation() throws Exception {
        //given
        ProjectDTO dto = createDTO(testProjectDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/projects/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ProjectDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchProjectShouldReturnConfirmation() throws Exception {
        //given
        ProjectDTO dto = createDTO(testProjectDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/projects/{id}/description/{newDescription}", dto.getId(), "Sad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ProjectDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private ProjectDTO createDTO(ProjectDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private ProjectDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<ProjectDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<ProjectDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<ProjectDTO>>() {
                });
    }
}