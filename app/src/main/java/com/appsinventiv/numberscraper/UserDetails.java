package com.appsinventiv.numberscraper;

/**
 * Created by maliahmed on 11/16/2017.
 */

public class UserDetails {
    String username,email,password,phone,isDemo,code;

    public UserDetails(String username, String email, String password, String phone, String isDemo, String code) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isDemo=isDemo;
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserDetails() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(String isDemo) {
        this.isDemo = isDemo;
    }
}
