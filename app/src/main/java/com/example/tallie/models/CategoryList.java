package com.example.tallie.models;

import java.io.Serializable;
import java.util.List;

public class CategoryList implements Serializable {

    List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
