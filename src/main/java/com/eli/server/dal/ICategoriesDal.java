package com.eli.server.dal;

import com.eli.server.dto.CategoryDto;
import com.eli.server.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoriesDal extends CrudRepository<Category,Long> {

    Category findByName(String name);

}
