package it.volta.ts.easymask.bean;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class Stats {

//    public static double getDimensStats (int baseWidth, int baseHeight, Bitmap overlay) {
//
//        int bmColoredPixels = calculateNonTraspPixels(overlay);
//
//        double value = ((bmColoredPixels) / (baseWidth * baseHeight)) * 100;
//
//        return value;
//    }

    public static int calculateNonTraspPixels (Bitmap b) {
        int pixelCounter = 0;

        int[] pixels = new int[b.getWidth() * b.getHeight()];
        b.getPixels(pixels, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());

        for (int idx = 0; idx < pixels.length; idx++){
            if (pixels[idx] != 0)
                pixelCounter++;
        }

        return pixelCounter;
    }

}
