package com.example.tallie.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderList implements Serializable {

    Meta _meta;

    @SerializedName("orders")
    ArrayList<Order> orders;

    public Meta get_meta() {
        return _meta;
    }

    public void set_meta(Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Order> getOrderedBooks() {
        return orders;
    }

    public void setOrderedBooks(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrderedBookList{" +
                "_meta=" + _meta +
                ", orderedBooks=" + orders +
                '}';
    }
}
