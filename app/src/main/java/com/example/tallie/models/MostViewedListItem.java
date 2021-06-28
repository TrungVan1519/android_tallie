package com.example.tallie.models;

import java.io.Serializable;

public class MostViewedListItem implements Serializable {

    int count;
    Book product;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Book getProduct() {
        return product;
    }

    public void setProduct(Book product) {
        this.product = product;
    }
}
