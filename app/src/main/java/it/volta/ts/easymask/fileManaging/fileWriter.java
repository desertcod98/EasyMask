package it.volta.ts.easymask.fileManaging;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

public class fileWriter {
    private final String dirName = "imgs";
    private String dirPath;
    private Context context;
    private File dir;

    public fileWriter(Context context){
        this.context = context;
        dir = new File(context.getFilesDir(),dirName);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    //TODO: passare png non  bitmap (BufferedImage?), usare un'altra classe per conversioni ecc
    public void writeImageToFile(Bitmap bitmap, String fileName){
        File image = new File(dir+File.separator+fileName);

    }

}
