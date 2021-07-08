package com.example.tallie.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    String id, _id; // are the same
    int user_id, userId; // are the same
    int product_id, productId; // are the same
    String created_at, createdAt; // are the same
    Date estimating_date_arrival;
    boolean has_taken;
    boolean is_delivering;
    boolean is_delivered;
    int quantity;
    String card_number;
    String recipient_name;
    String recipient_phone;
    String deliver_to;

    public Order(int product_id, int quantity, String card_number, String recipient_name, String recipient_phone, String deliver_to) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.card_number = card_number;
        this.recipient_name = recipient_name;
        this.recipient_phone = recipient_phone;
        this.deliver_to = deliver_to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Date getEstimating_date_arrival() {
        return estimating_date_arrival;
    }

    public void setEstimating_date_arrival(Date estimating_date_arrival) {
        this.estimating_date_arrival = estimating_date_arrival;
    }

    public boolean isHas_taken() {
        return has_taken;
    }

    public void setHas_taken(boolean has_taken) {
        this.has_taken = has_taken;
    }

    public boolean isIs_delivering() {
        return is_delivering;
    }

    public void setIs_delivering(boolean is_delivering) {
        this.is_delivering = is_delivering;
    }

    public boolean isIs_delivered() {
        return is_delivered;
    }

    public void setIs_delivered(boolean is_delivered) {
        this.is_delivered = is_delivered;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public String getDeliver_to() {
        return deliver_to;
    }

    public void setDeliver_to(String deliver_to) {
        this.deliver_to = deliver_to;
    }

    @NonNull
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", _id='" + _id + '\'' +
                ", user_id=" + user_id +
                ", userId=" + userId +
                ", product_id=" + product_id +
                ", productId=" + productId +
                ", created_at='" + created_at + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", estimating_date_arrival=" + estimating_date_arrival +
                ", has_taken=" + has_taken +
                ", is_delivering=" + is_delivering +
                ", is_delivered=" + is_delivered +
                ", quantity=" + quantity +
                ", card_number='" + card_number + '\'' +
                ", recipient_name='" + recipient_name + '\'' +
                ", recipient_phone='" + recipient_phone + '\'' +
                ", deliver_to='" + deliver_to + '\'' +
                '}';
    }
}
