package com.rdc.qrlogin.RetrofitClient;

import android.graphics.PostProcessor;

import com.rdc.qrlogin.Activities.LoginResponse;
import com.rdc.qrlogin.Activities.Response;

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
    @FormUrlEncoded
    @POST("cube/ft_rf7")
    Call<Response>rf(
            @Field("qr_key_id")String challan_no,
            @Field("sampling_loc")String str_sampling_loc,
            @Field("plant_slump")String str_plant_slum,
            @Field("sampling_time")String str_Sampling_time,
            @Field("sampling_slump")String str_sampling_slump,
            @Field("casting_time")String str_cast_time,
            @Field("casting_slump")String str_cast_slump,
            @Field("size")String str_Spiceman_size,
            @Field("addition")String str_water,
            @Field("admixture")String str_admix,
            @Field("placement")String str_placement
            );
}
