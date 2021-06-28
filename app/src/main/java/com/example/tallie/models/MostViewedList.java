package com.example.tallie.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MostViewedList implements Serializable {

    Meta _meta;
    ArrayList<MostViewedListItem> data;

    public Meta get_meta() {
        return _meta;
    }

    public void set_meta(Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<MostViewedListItem> getData() {
        return data;
    }

    public void setData(ArrayList<MostViewedListItem> data) {
        this.data = data;
    }
}
