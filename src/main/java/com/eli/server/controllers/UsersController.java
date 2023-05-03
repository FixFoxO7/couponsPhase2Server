package com.eli.server.controllers;

import com.eli.server.dto.UserDto;
import com.eli.server.dto.UserLoginData;
import com.eli.server.entities.User;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.UsersLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private UsersLogic usersLogic;

    @Autowired
    public UsersController(UsersLogic usersLogic) {
        this.usersLogic = usersLogic;
    }

    @PostMapping
    public void add(@RequestBody User user) throws ServerException {
        usersLogic.addUser(user);
    }

    @PutMapping
    public void update(@RequestHeader String authorization, @RequestBody User user) throws ServerException {
        usersLogic.updateUser(authorization, user);
    }

    @DeleteMapping("{userId}")
    public void remove(@RequestHeader String authorization, @PathVariable("userId") long userId) throws ServerException {
        usersLogic.removeUser(authorization, userId);
    }

    @DeleteMapping
    public void selfRemove(@RequestHeader String authorization) throws ServerException {
        usersLogic.selfRemove(authorization);
    }

    @GetMapping("{userId}")
    public UserDto getById(@RequestHeader String authorization, @PathVariable("userId") long userId) throws ServerException {
        return usersLogic.getUserDtoById(authorization, userId);
    }

    @GetMapping("/myDetails")
    public UserDto getMyDetails(@RequestHeader String authorization) throws ServerException {
        return usersLogic.getMyDetails(authorization);
    }

    @GetMapping
    public List<UserDto> getAllDto(@RequestHeader String authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return usersLogic.getAll(authorization, pageNumber);
    }

    @GetMapping("/byType")
    public List<UserDto> getAllByType(@RequestHeader String authorization, @RequestParam("userType") String userType, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return usersLogic.getAllByType(authorization, userType, pageNumber);
    }

    @GetMapping("/byCompany/{companyId}")
    public List<UserDto> getAllByCompany(@RequestHeader String authorization, @PathVariable("companyId") long companyId, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return usersLogic.getAllByCompany(authorization, companyId, pageNumber);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginData userLoginData) throws ServerException {
        return usersLogic.login(userLoginData);
    }

}
