package com.abhishek.retrofitv2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    String apikey = "04d8f69895e546d6adf1dd290740f391";

    @GET("/v2/top-headlines")
    Call<NewsModel> getNews(@Query("country") String country,
                            @Query("apiKey") String apiKey,
                            @Query("pageSize") int pageSize,
                            @Query("category") String category
    );
}
