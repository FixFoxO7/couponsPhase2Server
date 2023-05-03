package com.eli.server.dto;

public class UserDto {


    private String userName;
    private String userType;
    private String company;

    public UserDto(String userName, String userType, String company) {

        this.userName = userName;
        this.userType = userType;
        this.company = company;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
