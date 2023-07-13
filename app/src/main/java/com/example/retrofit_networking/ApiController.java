package com.example.retrofit_networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
    private static final String url="https://0177-49-36-209-47.ngrok-free.app";
    private static ApiController apiController;
    private static Retrofit retrofit;

    ApiController(){
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized ApiController getInstance(){
        if(apiController==null){
            apiController=new ApiController();
        }
        return apiController;
    }
    ApiSet getApi()
    {
        return retrofit.create(ApiSet.class);
    }
    ApiSet postData(){
        return retrofit.create(ApiSet.class);
    }

    ApiSet updateData(){return retrofit.create(ApiSet.class);}
    ApiSet deleteData(){return retrofit.create(ApiSet.class);}
}
