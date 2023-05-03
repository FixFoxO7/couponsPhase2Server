package com.eli.server.controllers;
import com.eli.server.dto.CategoryDto;
import com.eli.server.entities.Category;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.CategoriesLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoriesLogic categoriesLogic;

    @Autowired
    public CategoryController(CategoriesLogic categoriesLogic) {
        this.categoriesLogic = categoriesLogic;
    }

    @PostMapping
    public void add(@RequestHeader String authorization,@RequestBody Category category) throws ServerException {
        categoriesLogic.add(authorization,category);
    }

    @GetMapping("{id}")
    public CategoryDto get(@PathVariable("id") long id) throws ServerException {
        return categoriesLogic.getById(id);
    }

    @GetMapping
    public List<CategoryDto> getAll() throws ServerException {
        return categoriesLogic.getAll();
    }

    @PutMapping
    public void update(@RequestHeader String authorization,@RequestBody Category category) throws ServerException {
        categoriesLogic.update(authorization,category);
    }

    @DeleteMapping("{id}")
    public void delete(@RequestHeader String authorization,@PathVariable("id") long id) throws ServerException {
        categoriesLogic.remove(authorization,id);
    }
}
