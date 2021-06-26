package com.example.tallie.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class BookList implements Serializable {

    Meta _meta;
    ArrayList<Book> data;
    ArrayList<Book> products;

    public BookList(Meta _meta, ArrayList<Book> data) {
        this._meta = _meta;
        this.data = data;
    }

    public Meta get_meta() {
        return _meta;
    }

    public void set_meta(Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Book> getData() {
        return data;
    }

    public void setData(ArrayList<Book> data) {
        this.data = data;
    }

    public ArrayList<Book> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Book> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookList{" +
                "_meta=" + _meta +
                ", data=" + data +
                ", products=" + products +
                '}';
    }
}
