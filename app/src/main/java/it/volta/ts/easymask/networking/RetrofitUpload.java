package it.volta.ts.easymask.networking;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUpload {
    private RequestBody action;
    private RequestBody code;
    private RetrofitServices services;

    public RetrofitUpload(String actionString, String codeString, File file){
        action = RequestBody.create(MediaType.parse("multipart/form-data"),actionString);
        code = RequestBody.create(MediaType.parse("multipart/form-data"),codeString);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageForm = MultipartBody.Part.createFormData("file",file.getName(),requestFile);
        services = new RetrofitServices() {
            @Override
            public Call<Response1> uploadData(RequestBody action, RequestBody code, MultipartBody.Part file) {
                return null;
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://vuo.elettra.eu/vuo/cgi-bin/")
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        services = retrofit.create(RetrofitServices.class);

        Call call = services.uploadData(action,code,imageForm);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

}
