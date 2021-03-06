package com.example.tallie.services;

import com.example.tallie.models.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    @Multipart
    @POST("")
    Call<String> uploadAvatar(@Part MultipartBody.Part image);

    @Headers({"Content-Type: application/json"})
    @POST("api/users")
    Call<User> registerUser(@Body User user);

    @Headers({"Content-Type: application/json"})
    @POST("api/auth")
    Call<String> loginUser(@Body User user);

    @Headers({"Content-Type: application/json"})
    @GET("api/users/me")
    Call<User> getUserProfile(@Header("X-Access-Token") String jwt);

    @Headers({"Content-Type: application/json"})
    @PUT("api/users/me")
    Call<User> updateUserProfile(@Header("X-Access-Token") String jwt, @Body User user);

    @Headers({"Content-Type: application/json"})
    @GET("api/users/{id}")
    Call<User> lookupUser(@Path("id") int userId);
}
