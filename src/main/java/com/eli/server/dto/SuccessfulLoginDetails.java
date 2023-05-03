package com.eli.server.dto;

import com.eli.server.enums.UserType;

public class SuccessfulLoginDetails {
    private long id;
    private Long companyId;
    private UserType userType;

    public SuccessfulLoginDetails(long id, Long companyId, UserType userType) {
        this.id = id;
        this.companyId = companyId;
        this.userType = userType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
