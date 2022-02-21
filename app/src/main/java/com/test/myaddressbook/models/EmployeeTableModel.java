package com.test.myaddressbook.models;

public class EmployeeTableModel {
    private int employeeId;
    private String name;
    private String city;
    private String phone;
    private String email;
    private String picture;

    public EmployeeTableModel(int employeeId, String name, String city, String phone, String email, String picture) {
        this.employeeId = employeeId;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.picture = picture;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
