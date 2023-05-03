package com.eli.server.dto;

public class BasicCouponDto {
    private long couponId;

    private String name;
    private float price;
    private String endDate;
    private String imgSrc;


    public BasicCouponDto(long couponId, String name, float price, String endDate,String imgSrc) {
        this.couponId = couponId;
        this.name = name;
        this.price = price;
        this.endDate = endDate;
        this.imgSrc = imgSrc;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
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

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
