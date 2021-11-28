package com.obigo.hkmotors.common.network;


import com.obigo.hkmotors.module.Result_DrivingInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IHttpService {

    @GET("/poi/areascode")
    Call<Result_DrivingInfo> drivingInfo(@Query("count") String count, @Query("page") String page);

}
