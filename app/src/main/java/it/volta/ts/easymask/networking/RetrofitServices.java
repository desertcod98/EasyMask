package it.volta.ts.easymask.networking;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitServices {
    @Multipart
    @Headers({
            "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36"
    })
    @POST("easymask.py?")
//    @POST("b8b5ae3a-44b1-48ad-9baa-c99fbe48d589")
    Call<Response1> uploadData(
            @Part("action") RequestBody action,
            @Part("code") RequestBody code,
            @Part MultipartBody.Part file
    );

}
