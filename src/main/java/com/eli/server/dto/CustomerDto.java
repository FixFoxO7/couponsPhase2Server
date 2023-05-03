package com.eli.server.dto;

import com.eli.server.enums.UserType;

public class CustomerDto {

    private String userName;
    private UserType userType;
    private String address;
    private String phoneNumber;
    private Integer childrenAmount;

    public CustomerDto(String userName, UserType userType, String address, String phoneNumber, Integer childrenAmount) {

        this.userName = userName;
        this.userType = userType;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.childrenAmount = childrenAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getChildrenAmount() {
        return childrenAmount;
    }

    public void setChildrenAmount(Integer childrenAmount) {
        this.childrenAmount = childrenAmount;
    }
}
