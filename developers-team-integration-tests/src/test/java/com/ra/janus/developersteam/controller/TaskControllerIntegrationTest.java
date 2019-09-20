package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.TaskDTO;
import com.ra.janus.developersteam.entity.Task;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class TaskControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final TaskDTO testTaskDTO = new TaskDTO(new Task(1L, "Jan 40", "Integration tests for Dev Team"));
    private MockMvc mockMvc;
    @Autowired
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }

    @Test
    void whenGetTaskDTOsShouldReturnTaskDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<TaskDTO>> listResponseEntity = taskController.getTasks();
        for (TaskDTO dto : listResponseEntity.getBody().getResponse()) {
            taskController.deleteTask(dto.getId());
        }

        TaskDTO created = createDTO(testTaskDTO);
        List<TaskDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<TaskDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<TaskDTO>>() {
                        });

        List<TaskDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetTaskDTOShouldReturnTaskDTO() throws Exception {
        //given
        TaskDTO expected = createDTO(testTaskDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/tasks/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        TaskDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateTaskShouldReturnTaskDTO() throws Exception {
        //when
        TaskDTO created = createDTO(testTaskDTO);

        //then
        assertTrue(created instanceof TaskDTO);
    }

    @Test
    void whenUpdateTaskShouldReturnConfirmation() throws Exception {
        //given
        TaskDTO dto = createDTO(testTaskDTO);
        dto.setTitle("Some");

        //when
        MvcResult result = mockMvc.perform(
                put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<TaskDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteTaskShouldReturnConfirmation() throws Exception {
        //given
        TaskDTO dto = createDTO(testTaskDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/tasks/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<TaskDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchTaskShouldReturnConfirmation() throws Exception {
        //given
        TaskDTO dto = createDTO(testTaskDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/tasks/{id}/description/{newDescription}", dto.getId(), "Some new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<TaskDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private TaskDTO createDTO(TaskDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private TaskDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<TaskDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<TaskDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<TaskDTO>>() {
                });
    }
}