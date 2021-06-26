package com.example.tallie.models;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {

    int id;
    int user_id;
    int product_id;
    int star;
    String overview;
    String content;
    boolean prevent_spoiler;
    String started_reading;
    String finished_reading;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPrevent_spoiler() {
        return prevent_spoiler;
    }

    public void setPrevent_spoiler(boolean prevent_spoiler) {
        this.prevent_spoiler = prevent_spoiler;
    }

    public String getStarted_reading() {
        return started_reading;
    }

    public void setStarted_reading(String started_reading) {
        this.started_reading = started_reading;
    }

    public String getFinished_reading() {
        return finished_reading;
    }

    public void setFinished_reading(String finished_reading) {
        this.finished_reading = finished_reading;
    }
}
