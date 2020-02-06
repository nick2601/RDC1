package com.rdc.qrlogin.RetrofitClient;

import com.rdc.qrlogin.Activities.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface apiInterface {
    @FormUrlEncoded
    @POST("master/ft_login")
    Call<LoginResponse>login(
            @Field("ft_login")String sname,
            @Field("ft_password")String spass
    );
}
