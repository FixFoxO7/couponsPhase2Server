package com.eli.server.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @Column(name = "id", nullable = false)
    private long id;


    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "children_amount")
    private Integer childrenAmount;
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "customer")
    private List<Purchase>purchases;


    public Customer() {
    }

    public Customer(long id, User user, String address, String phoneNumber, Integer childrenAmount) {
        this.id = user.getId();
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.childrenAmount = childrenAmount;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", user=" + user +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", childrenAmount=" + childrenAmount +
                '}';
    }
}
