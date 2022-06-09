package it.volta.ts.easymask.fileHandling;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {
    private final String dirName = "imgs";
    private final String defaultExtension = ".png";
    private Context context;
    private File dir;

    public FileHandler(Context context){
        this.context = context;
        dir = new File(context.getFilesDir(),dirName);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public void writeImageToFile(Bitmap bitmap, String fileName){
        File f = new File(dir+File.separator+fileName+defaultExtension);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getImageByCode(String imgCode){
        return new File(dir+File.separator+imgCode+defaultExtension);
    }

    public void removeImageByCode(String imgCode) {
        File image = new File(dir+File.separator+imgCode+defaultExtension);
        image.delete();
    }
}
