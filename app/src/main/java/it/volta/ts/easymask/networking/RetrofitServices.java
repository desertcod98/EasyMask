package it.volta.ts.easymask.networking;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitServices {
    @Multipart
    @POST("easymask.py?")
    Call<Response1> uploadData(
            @Part("action") RequestBody action,
            @Part("code") RequestBody code,
            @Part MultipartBody.Part file
    );

}
