package com.zte.zshop.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:helloboy
 * Date:2021-06-23 10:54
 * Description:<描述>
 */
public class Customer implements Serializable {

    private Integer id;

    private String name;

    private String loginName;

    private String password;

    private String phone;

    private String address;



    private Integer isValid;

    private Date registDate;

    private String image;

    public Customer() {
    }

    public Customer(Integer id, String name, String loginName, String password, String phone, String address, Integer isValid, Date registDate, String image) {
        this.id = id;
        this.name = name;
        this.loginName = loginName;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.isValid = isValid;
        this.registDate = registDate;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
