package com.obigo.hkmotors.common.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpService {
    //서버 주소
    public static final String URL = "http://10.40.104.98:8181/";
    public IHttpService httpService = null;

    private HttpService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        httpService = retrofit.create(IHttpService.class);
        
    }

    private static class Singleton{
        private static final HttpService instance = new HttpService();
    }

    public static HttpService getInstance(){
        return Singleton.instance;
    }


}
