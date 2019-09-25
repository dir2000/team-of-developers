package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.TaskDTO;
import com.ra.janus.developersteam.entity.Task;
import com.ra.janus.developersteam.service.ResponseService;
import com.ra.janus.developersteam.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerMockTest {
    private final TaskService taskServiceMock = mock(TaskService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final TaskController taskController = new TaskController(taskServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testTaskId = 1L;
    private final Task testTask = new Task(testTaskId, "Some", "Little");
    private final TaskDTO testTaskDTO = new TaskDTO(testTask);
    private final List<TaskDTO> testListDTO = Arrays.asList(testTaskDTO);
    private final int responseEntityFieldCount = 3;
    private final String testTaskTitle = testTask.getTitle();
    private final ResponseEntity<ResponseListDTO<TaskDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<TaskDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public TaskControllerMockTest() {
        ResponseListDTO<TaskDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<TaskDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testTaskDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(TaskDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(TaskDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(taskController)
                .build();
    }

    @Test
    void whenGetTasksShouldReturnTasksJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testTask.getId()));
    }

    @Test
    void whenGetTaskShouldReturnTaskJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/tasks/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testTask.getId()));
    }

    @Test
    void whenCreateATaskShouldReturnTaskJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testTaskDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testTask.getId()));
    }

    @Test
    void whenCreateATaskShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testTaskDTO.setTitle(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testTaskDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testTaskDTO.setTitle(testTaskTitle);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateTaskShouldReturnTaskJsonRepresentation() throws Exception {
        //given
        when(taskServiceMock.update(testTaskDTO)).thenReturn(true);

        String testTitle = "Another";
        testTaskDTO.setTitle(testTitle);

        //when
        final ResultActions result = mockMvc.perform(
                put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testTaskDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testTaskDTO.setDescription(testTaskTitle);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.title").value(testTitle));
    }

    @Test
    void whenUpdateTaskShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(taskServiceMock.update(testTaskDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testTaskDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateTaskShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testTaskDTO.setTitle(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testTaskDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testTaskDTO.setTitle(testTaskTitle);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteTaskShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(taskServiceMock.delete(testTaskId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/tasks/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteTaskShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(taskServiceMock.delete(testTaskId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/tasks/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchTaskShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        String newDescription = "big";
        when(taskServiceMock.patchDescription(testTaskId, newDescription)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/tasks/" + testTaskId + "/description/" + newDescription)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchTaskShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        String newDescription = "big";
        when(taskServiceMock.patchDescription(testTaskId, newDescription)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/tasks/" + testTaskId + "/description/" + newDescription)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}