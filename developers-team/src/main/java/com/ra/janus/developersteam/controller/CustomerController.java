package com.ra.janus.developersteam.controller;

import com.ra.janus.developersteam.dto.CustomerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.service.CustomerService;
import com.ra.janus.developersteam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final transient CustomerService customerService;
    private final transient ResponseService responseService;

    @Autowired
    public CustomerController(final CustomerService customerService, final ResponseService responseService) {
        this.customerService = customerService;
        this.responseService = responseService;
    }

    @GetMapping()
    public ResponseEntity<ResponseListDTO<CustomerDTO>> getCustomers() {
        return responseService.success("Successfully  read", customerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{ident}")
    public ResponseEntity<ResponseDTO<CustomerDTO>> getCustomer(@PathVariable final long ident) {
        return responseService.success("Successfully read", customerService.get(ident), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<CustomerDTO>> createCustomer(@RequestBody @Valid final CustomerDTO customerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        final CustomerDTO dto = customerService.create(customerDTO);
        return responseService.success("Successfully created", dto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ResponseDTO<CustomerDTO>> updateCustomer(@RequestBody @Valid final CustomerDTO customerDTO, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseService.validationError(bindingResult.getAllErrors().toString());
        }
        if (customerService.update(customerDTO)) {
            return responseService.success("Updated", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<CustomerDTO>> deleteCustomer(@PathVariable final long id) {
        if (customerService.delete(id)) {
            return responseService.success("Deleted", HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't deleted");
        }
    }

    @PatchMapping("/{id}/address/{newAddress}")
    public ResponseEntity<ResponseDTO<CustomerDTO>> patchCustomerAddress(@PathVariable final long id, @PathVariable final String newAddress) {

        if (customerService.patchAddress(id, newAddress)) {
            return responseService.success("Was successfully updated to " + newAddress, HttpStatus.OK);
        } else {
            return responseService.persistenceError("Wasn't updated to " + newAddress);
        }
    }
}
