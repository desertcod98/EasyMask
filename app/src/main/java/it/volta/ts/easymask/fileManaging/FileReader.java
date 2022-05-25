package it.volta.ts.easymask.fileManaging;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileReader {
    final String tempFolderName = "temp";

    public void TEMPreadFromFile(Context context, String filename){
        File dir = new File(context.getFilesDir(), tempFolderName);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(new
                    File(dir+File.separator+"sus.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String read = null;
        StringBuilder builder = new StringBuilder("");

        while(true){
            try {
                if (!((read = bufferedReader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(read);
            //READ HERE
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
