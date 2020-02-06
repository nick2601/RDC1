package com.rdc.qrlogin.RetrofitClient;

import com.google.android.gms.common.api.Api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetClient {
    private static final String BASE_URL = "http://34.93.199.150/api/";
    private static RetClient minstance;
    private Retrofit retrofit;

    private RetClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetClient getInstance(){
        if (minstance ==null){
            minstance=new RetClient();
        }
        return minstance;
    }
    public apiInterface getApi(){
        return retrofit.create(apiInterface.class);
    }

}
