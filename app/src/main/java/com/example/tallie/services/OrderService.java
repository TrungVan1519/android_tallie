package com.example.tallie.services;

import com.example.tallie.models.Order;
import com.example.tallie.models.OrderList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface OrderService {

    // for https://tallie-shipping.herokuapp.com/
    @Headers({"Content-Type: application/json"})
    @GET("api/orders")
    Call<OrderList> allOrders(@Header("X-Auth-Token") String jwt);

    @Headers({"Content-Type: application/json"})
    @DELETE("api/orders/{id}")
    Call<Order> deleteOrder(@Header("X-Auth-Token") String jwt, @Path("id") String orderId);
}
