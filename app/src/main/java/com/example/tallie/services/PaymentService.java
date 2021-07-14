package com.example.tallie.services;

import com.example.tallie.models.PaymentCard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PaymentService {

    @Headers({"Content-Type: application/json"})
    @POST("api/payments")
    Call<String> registerPaymentCard(@Header("X-Access-Token") String jwt, @Body PaymentCard paymentCard);

    @Headers({"Content-Type: application/json"})
    @DELETE("api/payments")
    Call<String> deregisterPaymentCard(@Header("X-Access-Token") String jwt);
}
