package com.example.tallie.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Meta implements Serializable {
    Integer next_page, nextPage; // are the same
    int page;
    int per_page, perPage; // are the same
    Integer prev_page, prevPage; // are the same
    int total_items, totalItems; // are the same
    int total_pages, totalPages; // are the same

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

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public Integer getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @NonNull
    @Override
    public String toString() {
        return "Meta{" +
                "next_page=" + next_page +
                ", nextPage=" + nextPage +
                ", page=" + page +
                ", per_page=" + per_page +
                ", perPage=" + perPage +
                ", prev_page=" + prev_page +
                ", prevPage=" + prevPage +
                ", total_items=" + total_items +
                ", totalItems=" + totalItems +
                ", total_pages=" + total_pages +
                ", totalPages=" + totalPages +
                '}';
    }
}
