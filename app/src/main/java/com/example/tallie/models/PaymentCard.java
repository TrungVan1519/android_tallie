package com.example.tallie.models;

import java.io.Serializable;
import java.util.Date;

public class PaymentCard implements Serializable {

    String card_number;
    String name;
    Date start_date;
    Date end_date;
    String cvc;

    public PaymentCard(String card_number, String name, Date start_date, Date end_date, String cvc) {
        this.card_number = card_number;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.cvc = cvc;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
