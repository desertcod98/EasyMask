package it.volta.ts.easymask.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.slider.Slider;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.tools.ToolSelector;
import it.volta.ts.easymask.util.GraphicUtil;
import it.volta.ts.easymask.widgets.MaskImage;

public class MaskActivity extends AppCompatActivity
{
    private ImageView downloadedImg, brush, eraser, undo, redo, btnUpload;
    private MaskImage maskImage;
    private RelativeLayout imageLayout;
    private ImageView zoomIn, zoomOut;
    private BitmapDrawable sourceImage;
    private Slider slider;

    private int screenHeight;
    private int screenWidth;
    private String url;
    private int    maxHeight, maxWidth;
    private int    imgHeight, imgWidth;
    private int    newHeight, newWidth;

    private final double maxHeightRatio = 0.6;
    private final double maxWidthRatio  = 0.95;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        screenHeight = GraphicUtil.getScreenHeight(this);
        screenWidth  = GraphicUtil.getScreenWidth(this);
        maxHeight    = (int)(screenHeight * maxHeightRatio);
        maxWidth     = (int)(screenWidth  * maxWidthRatio );

        Bundle b = getIntent().getExtras();
        url = b.getString("url");
        downloadedImg = findViewById(R.id.imgSlot     );
        imageLayout   = findViewById(R.id.image_layout);

        maskImage = findViewById(R.id.imgMask);
        maskImage.setFocusable(true);

        slider = findViewById(R.id.strokeSlider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                maskImage.setStroke(value * 50);
            }
        });

        loadImage(downloadedImg, url);

        brush     = findViewById(R.id.brush );
        eraser    = findViewById(R.id.eraser);
        undo      = findViewById(R.id.undo);
        redo      = findViewById(R.id.redo);
        btnUpload = findViewById(R.id.btnUp );

        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 1;
                brush.setColorFilter(0x50000000);
                eraser.clearColorFilter();
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 0;
                eraser.setColorFilter(0x50000000);
                brush.clearColorFilter();
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        undo.setOnClickListener(view -> {
            maskImage.undo();
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        });

        redo.setOnClickListener(view -> {
            maskImage.redo();
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        zoomIn  = findViewById(R.id.zoom_in );
        zoomOut = findViewById(R.id.zoom_out);
        zoomIn .setOnClickListener(onZoom);
        zoomOut.setOnClickListener(onZoom);
    }

    private void setDimens(int imgWidth, int imgHeight)
    {
        newHeight = imgHeight;
        newWidth  = imgWidth;
        float ratio;

        if (imgWidth > imgHeight) {
            ratio = (float) maxWidth / (float) imgWidth;

        } else if (imgHeight > imgWidth){
            ratio = (float) maxHeight / (float) imgHeight;
        }else{
            if(maxWidth>maxWidth)
                 ratio = (float) maxHeight / (float) imgHeight;
            else ratio = (float) maxWidth / (float) imgWidth;
        }

        newWidth  = (int)((float) imgWidth  * ratio);
        newHeight = (int)((float) imgHeight * ratio);

        GraphicUtil.applySize(imageLayout, newWidth, newHeight);
    }


    @Override
    protected void onResume() {
        super.onResume();

        int width  = downloadedImg.getWidth();
        int height = downloadedImg.getHeight();
    }

    private void loadImage(ImageView view, String url)
    {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        imgWidth = bitmap.getWidth();
                        imgHeight = bitmap.getHeight();
                        view.setImageBitmap(bitmap);

                        setDimens(imgWidth,imgHeight);
                        Bitmap transBmp = Bitmap.createBitmap(imgWidth,imgHeight,Bitmap.Config.ARGB_8888);
                        sourceImage = new BitmapDrawable(getResources(), bitmap);
                        sourceImage.setAlpha(100);
                        maskImage.setImageDrawable(sourceImage);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    //-----------------------------------------------------------------------------------------


    View.OnClickListener onZoom = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            float scale = downloadedImg.getScaleX();

            switch (v.getId()) {
                case R.id.zoom_in:
                    scale += 0.1f;
                    break;
                case R.id.zoom_out:
                    if (downloadedImg.getScaleX() > 1f)
                        scale -= 0.1f;
                    break;
            }

//            System.out.println(maskImage.getStrokeWidthConst());
//            maskImage.setStrokeWidth((1-(scale-1))*maskImage.getStrokeWidthConst());
            downloadedImg.setScaleX(scale);
            downloadedImg.setScaleY(scale);
            maskImage    .setScaleX(scale);
            maskImage    .setScaleY(scale);
        }
    };
}
