package com.example.protej.instanews.Detailsfirst_Activity



import retrofit2.http.GET
import retrofit2.http.Query



    interface ApiNewsInterface {

        @GET("everything")
        fun getNewsArticles(
            @Query("q") query: String,
            @Query("sortBy") sortBy: String = "popularity",
            @Query("apiKey") apiKey: String
        ):retrofit2.Call<News_Deta>
    }

