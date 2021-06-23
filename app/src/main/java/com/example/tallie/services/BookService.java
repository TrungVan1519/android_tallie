package com.example.tallie.services;

import com.example.tallie.models.BookList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface BookService {

    @Headers({"Content-Type: application/json"})
    @GET("/api/products/most_viewed")
    Call<BookList> allMostViewed();

    @Headers({"Content-Type: application/json"})
    @GET("api/products/featured")
    Call<BookList> allFeaturedBooks();
}
