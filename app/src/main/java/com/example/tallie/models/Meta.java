package com.example.tallie.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Meta implements Serializable {
    Integer next_page;
    int page;
    int per_page;
    Integer prev_page;
    int total_items;
    int total_pages;

    public Integer getNext_page() {
        return next_page;
    }

    public void setNext_page(Integer next_page) {
        this.next_page = next_page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Integer getPrev_page() {
        return prev_page;
    }

    public void setPrev_page(Integer prev_page) {
        this.prev_page = prev_page;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    @NonNull
    @Override
    public String toString() {
        return "Meta{" +
                "next_page='" + next_page + '\'' +
                ", page=" + page +
                ", per_page=" + per_page +
                ", prev_page='" + prev_page + '\'' +
                ", total_items=" + total_items +
                ", total_pages=" + total_pages +
                '}';
    }
}
