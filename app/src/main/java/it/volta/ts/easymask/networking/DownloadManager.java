package it.volta.ts.easymask.networking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadManager {
    private ReadableByteChannel urlByteChannel;
    private String url;
    private final String urlKeyword = "code=";
    private final String imgExtension = ".png";

    public int setUrlByteChannel(String url){
        try {
            urlByteChannel = Channels.newChannel(new URL(url).openStream());
            this.url = url;
            return 0;
        } catch (IOException e) {
            return -1;
        }
    }

    public int saveImgToFile(){
        String filename = (url.substring(url.indexOf(urlKeyword)+urlKeyword.length()))+imgExtension;
        System.out.println(filename);
        FileOutputStream fileOutputStream = null;
        try {
            urlByteChannel = Channels.newChannel(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace(); //TODO implement exception management
        }
        try {
            fileOutputStream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.getChannel()
                    .transferFrom(urlByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
