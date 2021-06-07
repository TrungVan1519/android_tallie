package com.example.tallie.models;

public class Cart {
    private String time;
    private int qty;

    public Cart(String time, int qty) {
        this.time = time;
        this.qty = qty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
