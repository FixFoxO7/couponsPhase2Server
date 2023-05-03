package com.eli.server.controllers;

import com.eli.server.dto.CompanyDto;
import com.eli.server.entities.Company;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.CompaniesLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private CompaniesLogic companiesLogic;

    @Autowired
    public CompaniesController(CompaniesLogic companiesLogic) {
        this.companiesLogic = companiesLogic;
    }


    @PostMapping
    public void add(@RequestHeader String authorization,@RequestBody Company company) throws ServerException {
        companiesLogic.add(authorization,company);
    }

    @GetMapping("{companyId}")
    public CompanyDto getById(@PathVariable("companyId") long companyId) throws ServerException {
        return companiesLogic.getById(companyId);
    }

    @PutMapping
    public void update(@RequestHeader String authorization,@RequestBody Company company) throws ServerException {
        companiesLogic.update(authorization,company);
    }

    @DeleteMapping("{companyId}")
    public void delete(@RequestHeader String authorization,@PathVariable("companyId") long companyId) throws ServerException {
        companiesLogic.remove(authorization,companyId);
    }

    @GetMapping
    public List<CompanyDto> getAll(@RequestParam("pageNumber") int pageNumber) throws ServerException {
        return companiesLogic.getAll(pageNumber);
    }
}
