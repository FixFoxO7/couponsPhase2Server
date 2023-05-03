package com.eli.server.logic;


import com.eli.server.constants.Consts;
import com.eli.server.dal.ICustomersDal;
import com.eli.server.dto.CustomerDto;
import com.eli.server.entities.Customer;
import com.eli.server.entities.User;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersLogic {

    private UsersLogic userLogic;
    private ICustomersDal iCustomersDal;

    @Autowired
    public CustomersLogic(UsersLogic userLogic, ICustomersDal iCustomersDal) {
        this.userLogic = userLogic;
        this.iCustomersDal = iCustomersDal;
    }


    public void add(Customer customer) throws ServerException {

        validateCustomer(customer);
        userLogic.validateNameForCreate(customer.getUser());
        try {
            iCustomersDal.save(customer);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void update(String authorization, Customer customer) throws ServerException {

        validateCustomer(customer);
        userLogic.validateNameForUpdate(customer.getUser());
        userLogic.updateUser(authorization, customer.getUser());
        try {
            iCustomersDal.save(customer);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void remove(String authorization, long userId) throws ServerException {

        validateCustomerExist(userId);
        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        try {
            iCustomersDal.deleteById(userId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void selfRemove(String authorization) throws ServerException {

        long id = ValidatorUtils.validateToken(authorization);
        validateCustomerExist(id);
        ValidatorUtils.validateUserPermission(authorization, UserType.CUSTOMER);
        try {
            iCustomersDal.deleteById(id);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public CustomerDto getById(String authorization, long userId) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        validateCustomerExist(userId);
        try {
            Customer customer = findById(userId);
            return extractCustomerDto(customer);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public CustomerDto getMyDetails(String authorization) throws ServerException {

        long id = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.CUSTOMER);
        validateCustomerExist(id);
        try {
            Customer customer = findById(id);
            return extractCustomerDto(customer);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public List<CustomerDto> getAll(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iCustomersDal.getAllDto(paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    void validateCustomerExist(long userId) throws ServerException {

        User user = userLogic.getById(userId);

        if (user == null) {
            throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "user id=" + userId);
        }
        if (!isUserCustomer(user)) {
            throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "user Id=" + userId);
        }
    }


    private void validateCustomer(Customer customer) throws ServerException {

        String address = customer.getAddress();
        String phoneNumber = customer.getPhoneNumber();
        Integer childrenAmount = customer.getChildrenAmount();
        User user = customer.getUser();

        userLogic.validateUserDetails(user);

        if (!ValidatorUtils.isNameValid(address)) {
            throw new ServerException(ErrorType.INVALID_NAME_LENGTH, address);
        }
        if (!ValidatorUtils.isPhoneNumberValid(phoneNumber)) {
            throw new ServerException(ErrorType.INVALID_PHONE_NUMBER, phoneNumber);
        }
        if (childrenAmount != null && !ValidatorUtils.isNumberValid(childrenAmount)) {
            throw new ServerException(ErrorType.INVALID_VALUE_INPUT, " :" + childrenAmount);
        }
        if (!isUserCustomer(user)) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, String.valueOf(user.getUserType()));
        }
    }

    private boolean isUserCustomer(User user) {
        return user.getUserType() == UserType.CUSTOMER;
    }


    Customer findById(long customerId) throws ServerException {
        try {
            return iCustomersDal.findById(customerId).get();
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private CustomerDto extractCustomerDto(Customer customer) {

        String userName = customer.getUser().getUserName();
        UserType userType = customer.getUser().getUserType();
        String address = customer.getAddress();
        String phoneNumber = customer.getPhoneNumber();
        Integer childrenAmount = customer.getChildrenAmount();
        return new CustomerDto(userName, userType, address, phoneNumber, childrenAmount);
    }

}
