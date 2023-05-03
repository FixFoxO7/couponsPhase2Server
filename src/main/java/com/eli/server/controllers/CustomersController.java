package com.eli.server.controllers;

import com.eli.server.dto.CustomerDto;
import com.eli.server.entities.Customer;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.CustomersLogic;
import com.eli.server.logic.UsersLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    private CustomersLogic customersLogic;
    private UsersLogic usersLogic;

    @Autowired
    public CustomersController(CustomersLogic customersLogic, UsersLogic usersLogic) {
        this.customersLogic = customersLogic;
        this.usersLogic = usersLogic;
    }


    @PostMapping
    public void add(@RequestBody Customer customer) throws ServerException {
        customersLogic.add(customer);
    }

    @GetMapping("{customerId}")
    public CustomerDto getById(@RequestHeader String authorization,@PathVariable("customerId") long customerId) throws ServerException {
        return customersLogic.getById(authorization,customerId);
    }
    @GetMapping("/myDetails")
    public CustomerDto getById(@RequestHeader String authorization) throws ServerException {
        return customersLogic.getMyDetails(authorization);
    }

    @PutMapping
    public void update(@RequestHeader String authorization, @RequestBody Customer customer) throws ServerException {
        customersLogic.update(authorization, customer);
    }

    @DeleteMapping("{customerId}")
    public void delete(@RequestHeader String authorization, @PathVariable("customerId") long customerId) throws ServerException {
        customersLogic.remove(authorization, customerId);
    }

    @DeleteMapping
    public void selfRemove(@RequestHeader String authorization) throws ServerException {
        customersLogic.selfRemove(authorization);
    }

    @GetMapping
    public List<CustomerDto> getAll(@RequestHeader String authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return customersLogic.getAll(authorization, pageNumber);
    }

}
