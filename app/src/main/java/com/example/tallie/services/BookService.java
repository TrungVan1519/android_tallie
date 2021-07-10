package com.example.tallie.services;

import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.CategoryList;
import com.example.tallie.models.MostViewedList;
import com.example.tallie.models.Order;
import com.example.tallie.models.Review;
import com.example.tallie.models.ReviewList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookService {

    @Headers({"Content-Type: application/json"})
    @GET("api/products/featured")
    Call<BookList> allFeaturedBooks();

    @Headers({"Content-Type: application/json"})
    @GET("/api/products/most_viewed")
    Call<MostViewedList> allMostViewed();

    @Headers({"Content-Type: application/json"})
    @GET("api/products/{id}")
    Call<Book> getBookDetail(@Path("id") int bookId);

    @Headers({"Content-Type: application/json"})
    @GET("api/products/{id}/reviews")
    Call<ReviewList> allReviews(@Path("id") int bookId);

    @Headers({"Content-Type: application/json"})
    @POST("api/products/{id}/reviews")
    Call<Review> addReview(@Header("X-Access-Token") String jwt, @Path("id") int bookId, @Body Review review);

    @Headers({"Content-Type: application/json"})
    @PUT("api/products/{id}/reviews")
    Call<Review> updateReview(@Header("X-Access-Token") String jwt, @Path("id") int bookId, @Body Review review);

    @Headers({"Content-Type: application/json"})
    @DELETE("api/products/{id}/reviews")
    Call<String> deleteReview(@Header("X-Access-Token") String jwt, @Path("id") int bookId);

    @Headers({"Content-Type: application/json"})
    @GET("api/categories")
    Call<CategoryList> allCategories();

    @Headers({"Content-Type: application/json"})
    @GET("api/categories/{id}/products")
    Call<BookList> getBooksByCategory(@Path("id") int categoryId);

    @Headers({"Content-Type: application/json"})
    @POST("api/wishlist")
    Call<String> addToWishList(@Header("X-Access-Token") String jwt, @Body Book book);

    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "api/wishlist", hasBody = true)
    Call<String> deleteFromWishList(@Header("X-Access-Token") String jwt, @Body Book book);

    @Headers({"Content-Type: application/json"})
    @GET("api/wishlist")
    Call<BookList> getWishList(@Header("X-Access-Token") String jwt);

    @Headers({"Content-Type: application/json"})
    @POST("api/seen")
    Call<String> addToSeenList(@Header("X-Access-Token") String jwt, @Body Book book);

    @Headers({"Content-Type: application/json"})
    @GET("api/seen")
    Call<BookList> getSeenList(@Header("X-Access-Token") String jwt);

    @Headers({"Content-Type: application/json"})
    @POST("api/orders")
    Call<Order> orderBook(@Header("X-Access-Token") String jwt, @Body Order order);
}
