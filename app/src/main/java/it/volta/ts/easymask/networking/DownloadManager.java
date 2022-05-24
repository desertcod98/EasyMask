package it.volta.ts.easymask.networking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadManager {
    private final String urlKeyword = "code=";
    private final String imgExtension = ".png";

    private ReadableByteChannel getUrlByteChannel(String url){
        try {
            ReadableByteChannel urlByteChannel = Channels.newChannel(new URL(url).openStream());
            return urlByteChannel;
        } catch (IOException e) {
            return null;
        }
    }

    public int saveImgToFile(String url){
        ReadableByteChannel urlByteChannel = getUrlByteChannel(url);
        if (urlByteChannel == null){
            return -1;
        }
        String filename = (url.substring(url.indexOf(urlKeyword)+urlKeyword.length()))+imgExtension;
        FileOutputStream fileOutputStream = null;
        try {
            urlByteChannel = Channels.newChannel(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace(); //TODO implement exception management
        }
        try {
            fileOutputStream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            new File(filename);
            saveImgToFile(url);
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
