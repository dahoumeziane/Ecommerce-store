package com.mezdah.mystore.model;

import java.util.ArrayList;

public class Order {
    ArrayList<Product> products;
    String full_name , address,phone_number,wilaya,commune ,user_id;
    int total_price;
    int state; // 0: waiting , 1: Accepted , 2:Refused , 3:Completed
    String orderId;

    public String getOrderId() {
        return orderId;
    }

    public Order(ArrayList<Product> products, String full_name, String address, String phone_number, String wilaya, String commune, String user_id, int total_price, int state, String orderId) {
        this.products = products;
        this.full_name = full_name;
        this.address = address;
        this.phone_number = phone_number;
        this.wilaya = wilaya;
        this.commune = commune;
        this.user_id = user_id;
        this.total_price = total_price;
        this.state = state;
        this.orderId = orderId;
    }

    public Order() {
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
