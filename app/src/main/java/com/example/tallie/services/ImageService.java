package com.example.tallie.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {

    @GET("images/{img_id}")
    Call<ResponseBody> downloadImage(@Path("img_id") int img_id);
}
