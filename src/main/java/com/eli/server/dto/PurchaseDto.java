package com.eli.server.dto;

import java.util.Date;

public class PurchaseDto {
    private long purchaseId;
    private String couponName;
    private String customer;
    private String company;
    private String companyEmployee;
    private float price;
    private int amount;

    private String category;

    private Date timestamp;

    public PurchaseDto(long purchaseId, String couponName, String customer, String company, String companyEmployee, float price, int amount, String category, Date timestamp) {
        this.purchaseId = purchaseId;
        this.couponName = couponName;
        this.customer = customer;
        this.company = company;
        this.companyEmployee = companyEmployee;
        this.price = price;
        this.amount = amount;
        this.category = category;
        this.timestamp = timestamp;
    }

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyEmployee() {
        return companyEmployee;
    }

    public void setCompanyEmployee(String companyEmployee) {
        this.companyEmployee = companyEmployee;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
