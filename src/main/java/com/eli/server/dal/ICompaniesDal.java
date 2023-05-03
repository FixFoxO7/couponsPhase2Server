package com.eli.server.dal;

import com.eli.server.entities.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICompaniesDal extends CrudRepository<Company,Long>  {

    List<Company> findAll(Pageable pageable);

    Company findByName(String name);

    Company getById(long companyId);
}
