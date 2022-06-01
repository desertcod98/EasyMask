package it.volta.ts.easymask.bean;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class Stats {

    public static int getDimensStats (int baseWidth, int baseHeight, ImageView overlay) {
        Bitmap b = ((BitmapDrawable)overlay.getDrawable()).getBitmap();

        int bmColoredPixels = calculateNonTraspPixels(b);

        return ((bmColoredPixels) / (baseWidth * baseHeight)) * 100;
    }

    private static int calculateNonTraspPixels (Bitmap b) {
        int pixelCounter = 0;

        for (int idxW = 0; idxW < b.getWidth(); idxW++){
            for (int idxH = 0; idxH < b.getHeight(); idxH++) {
                if (b.getPixel(idxW, idxH) == 0x50ff0000)
                    pixelCounter++;
            }
        }

        return pixelCounter;
    }

}
