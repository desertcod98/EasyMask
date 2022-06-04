package it.volta.ts.easymask.networking;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ThreadRunner extends Thread{
    Context context;

    private File dir;
    @Override
    public void run() {
        try {
            File file = new File(dir + File.separator + "line.png");
            MultipartRequest multipartRequest;
            Map<String,String> map= new HashMap<>();
            map.put("code", "S7O61G6Gr5qKDl0XlEy9vnx8pVvttnS5");
            map.put("action","do_upload");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(new MultipartRequest("https://vuo.elettra.eu/vuo/cgi-bin/easymask.py?",
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            },file,map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
