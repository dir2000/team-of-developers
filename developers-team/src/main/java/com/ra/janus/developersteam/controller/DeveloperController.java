package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.DeveloperDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.service.DeveloperService;
import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final transient DeveloperService developerService;
    private final transient ResponseService responseService;

    @Autowired
    public DeveloperController(final DeveloperService developerService, final ResponseService responseService) {
        this.developerService = developerService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<DeveloperDTO>> getDevelopers() {
        return responseService.success("Successfully  read", developerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<DeveloperDTO>> getDeveloper(@PathVariable final long ident) {
        return responseService.success("Successfully read", developerService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<DeveloperDTO>> createDeveloper(@RequestBody @Valid final DeveloperDTO developerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final DeveloperDTO dto = developerService.create(developerDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<DeveloperDTO>> updateDeveloper(@RequestBody @Valid final DeveloperDTO developerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (developerService.update(developerDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeveloperDTO>> deleteDeveloper(@PathVariable final long id) {
        if (developerService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }
}
