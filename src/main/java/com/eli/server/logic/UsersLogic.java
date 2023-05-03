package com.eli.server.logic;


import com.eli.server.constants.Consts;
import com.eli.server.dal.IUsersDal;
import com.eli.server.dto.SuccessfulLoginDetails;
import com.eli.server.dto.UserDto;
import com.eli.server.dto.UserLoginData;
import com.eli.server.entities.User;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.JWTUtils;
import com.eli.server.utils.ValidatorUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsersLogic {

    private IUsersDal iUsersDal;

    @Autowired
    public UsersLogic(IUsersDal iUsersDal) {
        this.iUsersDal = iUsersDal;
    }


    public void addUser(User user) throws ServerException {

        validateUserDetails(user);
        validateNameForCreate(user);
        try {
            iUsersDal.save(user);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void updateUser(String authorization, User user) throws ServerException {

        validateRequestPermission(authorization, user);
        validateUserDetails(user);
        validateNameForUpdate(user);
        validateUserExist(user.getId());
        try {
            iUsersDal.save(user);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void removeUser(String authorization, long userId) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        try {
            iUsersDal.deleteById(userId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void selfRemove(String authorization) throws ServerException {

        long userId = ValidatorUtils.validateToken(authorization);

        try {
            iUsersDal.deleteById(userId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public UserDto getUserDtoById(String authorization, long userId) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        User user = getById(userId);
        return extractUserDto(user);
    }

    public UserDto getMyDetails(String authorization) throws ServerException {

        long userId = ValidatorUtils.validateToken(authorization);
        User user = getById(userId);
        return extractUserDto(user);
    }


    public List<UserDto> getAll(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);

        List<UserDto> usersDto = new ArrayList<>();
        try {
            Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
            List<User> users = iUsersDal.findAll(paging);
            users.forEach(user -> usersDto.add(extractUserDto(user)));
            return usersDto;
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<UserDto> getAllByType(String authorization, String userType, int pageNumber) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        List<UserDto> usersDto = new ArrayList<>();
        try {
            List<User> users = iUsersDal.findAllByUserType(userType, paging);
            users.forEach(user -> usersDto.add(extractUserDto(user)));
            return usersDto;
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public List<UserDto> getAllByCompany(String authorization, long companyId, int pageNumber) throws ServerException {

        long userId = ValidatorUtils.validateToken(authorization);
        validatePermission(userId, companyId);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        List<UserDto> usersDto = new ArrayList<>();
        try {
            List<User> users = iUsersDal.findAllByCompany(companyId, paging);
            users.forEach(user -> usersDto.add(extractUserDto(user)));
            return usersDto;
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public String login(UserLoginData userLoginDetails) throws ServerException {

        String userName = userLoginDetails.getUserName();
        String password = userLoginDetails.getPassword();

        SuccessfulLoginDetails userData = iUsersDal.login(userName, password);
        if (userData == null) {
            throw new ServerException(ErrorType.USER_LOGIN_FAILED);
        }
        try {
            return JWTUtils.createJWT(userData);
        } catch (Exception e) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

    }

    User getById(long userId) throws ServerException {

        validateUserExist(userId);
        return iUsersDal.findById(userId).get();
    }

    private void validatePermission(long userId, long companyId) throws ServerException {

        User user = getById(userId);
        if (user.getUserType() == UserType.CUSTOMER) {
            throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
        }
        if (user.getUserType() == UserType.COMPANY) {
            if (user.getCompany().getId() != companyId) {
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        }
    }

    private UserDto extractUserDto(User user) {

        String userName = user.getUserName();
        String userType = String.valueOf(user.getUserType());
        String companyName = null;
        if (user.getCompany() != null) {
            companyName = user.getCompany().getName();
        }
        return new UserDto(userName, userType, companyName);
    }

    void validateNameForCreate(User user) throws ServerException {

        String userName = user.getUserName();
        if (iUsersDal.existsByUserName(userName)) {
            throw new ServerException(ErrorType.USERNAME_ALREADY_EXISTS, "userName=" + userName);
        }
    }

    void validateNameForUpdate(User user) throws ServerException {

        String name = user.getUserName();
        User user1 = iUsersDal.findIdByUserName(name);
        long id = user.getId();
        if (user1 != null && id != user1.getId()) {
            throw new ServerException(ErrorType.USERNAME_ALREADY_EXISTS, "user name=" + name);
        }
    }

    void validateUserExist(long userId) throws ServerException {

        if (iUsersDal.findById(userId).isEmpty()) {
            throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "user id: " + userId);
        }
    }

    void validateUserDetails(User user) throws ServerException {
        String userName = user.getUserName();

        validateUserName(userName);
        String password = user.getPassword();
        validatePassword(password);
    }

    private void validatePassword(String password) throws ServerException {

        if (!ValidatorUtils.isNameValid(password)) {
            throw new ServerException(ErrorType.INVALID_INPUT_LENGTH, password);
        }
        if (password.length() < 4) {
            throw new ServerException(ErrorType.INVALID_PASSWORD_LENGTH, password);
        }
        if (!isPatternValid(password)) {
            throw new ServerException(ErrorType.INVALID_PASSWORD_PATTERN, password);
        }
    }

    private void validateUserName(String email) throws ServerException {
        final String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ServerException(ErrorType.INVALID_USER_NAME, "UserName=" + email);
        }
    }

    private boolean isPatternValid(String password) {
        int digitCount = 0;
        int letterCount = 0;
        char[] chars = password.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
            if (c <= 'a' || c >= 'z') {
                letterCount++;
            }
            if (c <= 'A' || c >= 'Z') {
                letterCount++;
            }
        }
        return digitCount >= 2 && letterCount >= 2;
    }

    private void validateRequestPermission(String authorization, User user) throws ServerException {

        long id = ValidatorUtils.validateToken(authorization);
        User u = getById(id);
        if (u.getUserType() == UserType.CUSTOMER || u.getUserType() == UserType.COMPANY) {
            if (id != user.getId()) {
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        }
    }

}
