package com.example.tallie.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewList implements Serializable {

    Meta _meta;
    ArrayList<Review> reviews;

    public Meta get_meta() {
        return _meta;
    }

    public void set_meta(Meta _meta) {
        this._meta = _meta;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
