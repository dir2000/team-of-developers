package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.ProjectDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.service.ProjectService;
import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final transient ProjectService projectService;
    private final transient ResponseService responseService;

    @Autowired
    public ProjectController(final ProjectService projectService, final ResponseService responseService) {
        this.projectService = projectService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<ProjectDTO>> getProjects() {
        return responseService.success("Successfully  read", projectService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<ProjectDTO>> getProject(@PathVariable final long ident) {
        return responseService.success("Successfully read", projectService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<ProjectDTO>> createProject(@RequestBody @Valid final ProjectDTO projectDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final ProjectDTO dto = projectService.create(projectDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<ProjectDTO>> updateProject(@RequestBody @Valid final ProjectDTO projectDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (projectService.update(projectDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<ProjectDTO>> deleteProject(@PathVariable final long id) {
        if (projectService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/description/{newDescription}")
    public ResponseEntity<ResponseDTO<ProjectDTO>> patchDescription(@PathVariable final long id, @PathVariable final String newDescription) {
        if (projectService.patchDescription(id, newDescription)) {
            return responseService.success("Updated to " + newDescription, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newDescription);
        }
    }
}
