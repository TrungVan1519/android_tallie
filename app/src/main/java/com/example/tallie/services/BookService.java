package com.example.tallie.services;

import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.Category;
import com.example.tallie.models.CategoryList;
import com.example.tallie.models.Review;
import com.example.tallie.models.ReviewList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService {

    @Headers({"Content-Type: application/json"})
    @GET("/api/products/most_viewed")
    Call<BookList> allMostViewed();

    @Headers({"Content-Type: application/json"})
    @GET("api/products/featured")
    Call<BookList> allFeaturedBooks();

    @Headers({"Content-Type: application/json"})
    @GET("api/categories")
    Call<CategoryList> allCategories();

    @Headers({"Content-Type: application/json"})
    @GET("api/categories/{id}/products")
    Call<BookList> getBooksByCategory(@Path("id") int categoryId);

    @Headers({"Content-Type: application/json"})
    @GET("api/products/{id}")
    Call<Book> getBookDetail(@Path("id") int bookId);

    @Headers({"Content-Type: application/json"})
    @GET("api/products/{id}/reviews")
    Call<ReviewList> allReviews(@Path("id") int bookId);
}
