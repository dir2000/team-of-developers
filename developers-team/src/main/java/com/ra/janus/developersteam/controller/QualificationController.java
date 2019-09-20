package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.QualificationDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.service.QualificationService;
import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/qualifications")
public class QualificationController {

    private final transient QualificationService qualService;
    private final transient ResponseService responseService;

    @Autowired
    public QualificationController(final QualificationService qualService, final ResponseService responseService) {
        this.qualService = qualService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<QualificationDTO>> getQualifications() {
        return responseService.success("Successfully  read", qualService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<QualificationDTO>> getQualification(@PathVariable final long ident) {
        return responseService.success("Successfully read", qualService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<QualificationDTO>> createQualification(@RequestBody @Valid final QualificationDTO qualificationDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final QualificationDTO dto = qualService.create(qualificationDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<QualificationDTO>> updateQualification(@RequestBody @Valid final QualificationDTO qualificationDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (qualService.update(qualificationDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<QualificationDTO>> deleteQualification(@PathVariable final long id) {
        if (qualService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/responsibility/{newResponsibility}")
    public ResponseEntity<ResponseDTO<QualificationDTO>> patchResponsibility(@PathVariable final long id, @PathVariable final String newResponsibility) {
        if (qualService.patchResponsibility(id, newResponsibility)) {
            return responseService.success("Was successfully updated to " + newResponsibility, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newResponsibility);
        }
    }
}
