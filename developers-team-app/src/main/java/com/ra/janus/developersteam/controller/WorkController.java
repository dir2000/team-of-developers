package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.WorkDTO;
import com.ra.janus.developersteam.service.ResponseService;
import com.ra.janus.developersteam.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/works")
public class WorkController {

    private final transient WorkService workService;
    private final transient ResponseService responseService;

    @Autowired
    public WorkController(final WorkService workService, final ResponseService responseService) {
        this.workService = workService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<WorkDTO>> getWorks() {
        return responseService.success("Successfully  read", workService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<WorkDTO>> getWork(@PathVariable final long ident) {
        return responseService.success("Successfully read", workService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<WorkDTO>> createWork(@RequestBody @Valid final WorkDTO workDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final WorkDTO dto = workService.create(workDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<WorkDTO>> updateWork(@RequestBody @Valid final WorkDTO workDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (workService.update(workDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<WorkDTO>> deleteWork(@PathVariable final long id) {
        if (workService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/price/{newPrice}")
    public ResponseEntity<ResponseDTO<WorkDTO>> patchPrice(@PathVariable final long id, @PathVariable final BigDecimal newPrice) {
        if (workService.patchPrice(id, newPrice)) {
            return responseService.success("Updated to " + newPrice, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newPrice);
        }
    }
}
