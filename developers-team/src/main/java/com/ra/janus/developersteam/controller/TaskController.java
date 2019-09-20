package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.TaskDTO;
import com.ra.janus.developersteam.service.ResponseService;
import com.ra.janus.developersteam.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final transient TaskService taskService;
    private final transient ResponseService responseService;

    @Autowired
    public TaskController(final TaskService taskService, final ResponseService responseService) {
        this.taskService = taskService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<TaskDTO>> getTasks() {
        return responseService.success("Successfully  read", taskService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<TaskDTO>> getTask(@PathVariable final long ident) {
        return responseService.success("Successfully read", taskService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<TaskDTO>> createTask(@RequestBody @Valid final TaskDTO taskDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final TaskDTO dto = taskService.create(taskDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<TaskDTO>> updateTask(@RequestBody @Valid final TaskDTO taskDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (taskService.update(taskDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<TaskDTO>> deleteTask(@PathVariable final long id) {
        if (taskService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/description/{newDescription}")
    public ResponseEntity<ResponseDTO<TaskDTO>> patchDescription(@PathVariable final long id, @PathVariable final String newDescription) {
        if (taskService.patchDescription(id, newDescription)) {
            return responseService.success("Updated to " + newDescription, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newDescription);
        }
    }
}
