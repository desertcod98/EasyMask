package it.volta.ts.easymask.networking;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
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
        action = RequestBody.create(actionString,MediaType.parse("multipart/form-data"));
        code = RequestBody.create(codeString,MediaType.parse("multipart/form-data"));
        RequestBody requestFile = RequestBody.create(file ,MediaType.parse("multipart/form-data"));
        //TODO
        MultipartBody.Part imageForm = MultipartBody.Part.createFormData("file_upload",file.getName(),requestFile);
        services = new RetrofitServices() {
            @Override
            public Call<Response1> uploadData(RequestBody action, RequestBody code, MultipartBody.Part file) {
                return null;
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vuo.elettra.eu/vuo/cgi-bin/")
//                .baseUrl("https://webhook.site/")
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();
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
