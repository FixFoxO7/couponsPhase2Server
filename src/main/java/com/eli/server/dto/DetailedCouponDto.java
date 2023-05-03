package com.eli.server.dto;

public class DetailedCouponDto {

    private long couponId;
    private String category;
    private String name;
    private float price;
    private String endDate;
    private String startDate;
    private String description;
    private String companyName;
    private String companyEmployee;
    private String imgSrc;

    public DetailedCouponDto(long couponId, String category, String name, float price, String endDate, String startDate, String description, String companyName, String companyEmployee, String imgSrc) {
        this.couponId = couponId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.endDate = endDate;
        this.startDate = startDate;
        this.description = description;
        this.companyName = companyName;
        this.companyEmployee = companyEmployee;
        this.imgSrc = imgSrc;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmployee() {
        return companyEmployee;
    }

    public void setCompanyEmployee(String companyEmployee) {
        this.companyEmployee = companyEmployee;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
