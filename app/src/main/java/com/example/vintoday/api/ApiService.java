package com.example.vintoday.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ApiService {

    @GET("v2/everything")
    Call<NewsResponse> getAllNews(@Query("q") String q, @Query("apiKey") String apiKey);

//    @GET("api/users/{id}")
//    Call<NewsResponse> getUser(@Path("id") int id);

}
