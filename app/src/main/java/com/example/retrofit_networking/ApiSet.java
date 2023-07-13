package com.example.retrofit_networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiSet {

    @GET("display")
    Call<List<Post_model>> getdata();
    @POST("add")
    Call<Post_model> postData(@Body Post_model post_model);

    @POST("update")
    Call<Post_model> updateData(@Body Post_model post_model);

    @POST("delete")
    Call<Post_model> deleteData(@Body Post_model post_model);

}
