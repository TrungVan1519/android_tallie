package com.example.tallie.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderList implements Serializable {

    Meta _meta;
    ArrayList<Order> orders;

    public Meta get_meta() {
        return _meta;
    }

    public void set_meta(Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrderList{" +
                "_meta=" + _meta +
                ", orders=" + orders +
                '}';
    }
}
