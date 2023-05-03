package com.eli.server.logic;

import com.eli.server.dal.ICategoriesDal;
import com.eli.server.dto.CategoryDto;
import com.eli.server.entities.Category;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoriesLogic {
    private ICategoriesDal iCategoriesDal;


    @Autowired
    public CategoriesLogic(ICategoriesDal iCategoriesDal) {
        this.iCategoriesDal = iCategoriesDal;
    }


    public void add(String authorization, Category category) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        categoryValidation(category);
        try {
            iCategoriesDal.save(category);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void update(String authorization, Category category) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        categoryValidation(category);
        try {
            iCategoriesDal.save(category);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void remove(String authorization, long categoryId) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        validateCategoryExist(categoryId);
        try {
            iCategoriesDal.deleteById(categoryId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public CategoryDto getById(long categoryId) throws ServerException {

        validateCategoryExist(categoryId);
        try {
            Category category = iCategoriesDal.findById(categoryId).get();
            String name = category.getName();
            return new CategoryDto(categoryId, name);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<CategoryDto> getAll() throws ServerException {

        List<CategoryDto> categoriesDto = new ArrayList<>();
        try {
            List<Category> categories = (List<Category>) iCategoriesDal.findAll();
            categories.forEach(category -> categoriesDto.add(new CategoryDto(category.getId(), category.getName())));
            return categoriesDto;
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private void categoryValidation(Category category) throws ServerException {

        String name = category.getName();

        if (!ValidatorUtils.isNameValid(name)) {
            throw new ServerException(ErrorType.INVALID_NAME_LENGTH, name);
        }
        if (iCategoriesDal.findByName(name) != null) {
            throw new ServerException(ErrorType.CATEGORY_ALREADY_EXISTS, name);
        }
    }

    void validateCategoryExist(long categoryId) throws ServerException {

        if (!iCategoriesDal.existsById(categoryId)) {
            throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "Category does NOT exist, id=" + categoryId);
        }
    }
}
