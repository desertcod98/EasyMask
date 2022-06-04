package it.volta.ts.easymask.networking;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import it.volta.ts.easymask.fileHandling.FileHandler;

public class ThreadRunner extends Thread{
    private Context context;
    private String imgCode;
    private FileHandler fileHandler;

    public ThreadRunner(Context context){
        this.context = context;
        fileHandler = new FileHandler(context);
    }
    @Override
    public void run() {
        try {
            File file = fileHandler.getImageByCode(imgCode);
            MultipartRequest multipartRequest;
            Map<String,String> map= new HashMap<>();
            map.put("code", imgCode);
            map.put("action","do_upload");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(new MultipartRequest(UrlHandler.getUrl(),
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    fileHandler.removeImageByCode(imgCode);
                }
            },file,map));
        } catch (Exception e) {
            e.printStackTrace();
        }

        }


    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }
}
