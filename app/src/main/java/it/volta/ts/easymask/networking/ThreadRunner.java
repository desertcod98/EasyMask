package it.volta.ts.easymask.networking;

import java.io.File;
import java.io.IOException;

public class ThreadRunner extends Thread{

    private File dir;
    @Override
    public void run() {
        try {
            //---
            String charset = "UTF-8";
            String requestURL = "https://vuo.elettra.eu/vuo/cgi-bin/easymask.py?action=upload&code=YlfZSqJnIY0wSzyTpot9ypbqxZTKdzt6";

            String filePath = dir+File.separator+"630658.png";
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);

            UploadManager multipart = null;
            try {
                multipart = new UploadManager(requestURL, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            multipart.setFormField("code", "YlfZSqJnIY0wSzyTpot9ypbqxZTKdzt6");
            multipart.setFormField("submit", "Start upload");
            multipart.setFormField("action", "do_upload");
            File file = new File(filePath);
            try {
                multipart.setFilePart("file", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String response = multipart.getResponse();
                System.out.println("::::::::::::"+response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //---
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDir(File dir) {
        this.dir = dir;
    }
}
