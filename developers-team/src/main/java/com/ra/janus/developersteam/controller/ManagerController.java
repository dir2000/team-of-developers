package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.ManagerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.service.ManagerService;
import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final transient ManagerService managerService;
    private final transient ResponseService responseService;

    @Autowired
    public ManagerController(final ManagerService managerService, final ResponseService responseService) {
        this.managerService = managerService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<ManagerDTO>> getManagers() {
        return responseService.success("Successfully  read", managerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<ManagerDTO>> getManager(@PathVariable final long ident) {
        return responseService.success("Successfully read", managerService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<ManagerDTO>> createManager(@RequestBody @Valid final ManagerDTO managerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final ManagerDTO dto = managerService.create(managerDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<ManagerDTO>> updateManager(@RequestBody @Valid final ManagerDTO managerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (managerService.update(managerDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<ManagerDTO>> deleteManager(@PathVariable final long id) {
        if (managerService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/phone/{newPhone}")
    public ResponseEntity<ResponseDTO<ManagerDTO>> patchPhone(@PathVariable final long id, @PathVariable final String newPhone) {
        if (managerService.patchPhone(id, newPhone)) {
            return responseService.success("Updated to " + newPhone, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newPhone);
        }
    }
}
