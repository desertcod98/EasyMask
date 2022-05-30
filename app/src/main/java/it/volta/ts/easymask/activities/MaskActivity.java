package it.volta.ts.easymask.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.tools.ToolSelector;
import it.volta.ts.easymask.util.GraphicUtil;
import it.volta.ts.easymask.widgets.MaskEraser;
import it.volta.ts.easymask.widgets.MaskImage;

public class MaskActivity extends AppCompatActivity
{
    private ImageView downloadedImg, brush, eraser, btnUpload;
    private MaskImage maskImage;
    private MaskEraser maskEraser;
    RelativeLayout imageLayout;
    private int screenHeight;
    private int screenWidth;
    private String url;
    private int maxHeight, maxWidth;
    private int imgHeight, imgWidth;
    private int newHeight, newWidth;

    private final double maxHeightRatio = 0.6;
    private final double maxWidthRatio  = 0.9;
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
        downloadedImg = findViewById(R.id.imgSlot);

        imageLayout = findViewById(R.id.image_layout);

        maskImage = findViewById(R.id.imgMask);
        maskImage.setOnMaskTouch(onMaskTouch);

        maskEraser = findViewById(R.id.imgEraser);
        maskEraser.setOnMaskTouch(onMaskEraserTouch);
        maskEraser.setMaskImage(maskImage);

        loadImage(downloadedImg, url);


        brush     = findViewById(R.id.brush );
        eraser    = findViewById(R.id.eraser);
        btnUpload = findViewById(R.id.btnUp );

        brush.setOnClickListener(view -> {
            ToolSelector.toolState = 1;
            maskEraser.setEnabled(false);
            maskImage.setEnabled(true);
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        });

        eraser.setOnClickListener(view -> {
            ToolSelector.toolState = 0;
            maskEraser.setEnabled(true);
            maskImage.setEnabled(false);
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });
    }

    private void setDimens(RelativeLayout imageLayout, int imgWidth, int imgHeight)
    {
        newHeight = imgHeight;
        newWidth  = imgWidth;

        if (imgWidth > imgHeight) {
            float ratio = (float) maxWidth / (float) imgWidth;
            newWidth  = (int)((float) imgWidth  * ratio);
            newHeight = (int)((float) imgHeight * ratio);
        } else {
            float ratio = (float) maxHeight / (float) imgHeight;
            newWidth  = (int)((float) imgWidth  * ratio);
            newHeight = (int)((float) imgHeight * ratio);
        }

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
                        imgWidth  = bitmap.getWidth();
                        imgHeight = bitmap.getHeight();
                        view.setImageBitmap(bitmap);

                        setDimens(imageLayout, imgWidth,imgHeight);
                        //TODO Error: maskImage == null nella seconda scansione del qr
                        Bitmap transBmp = Bitmap.createBitmap(imgWidth,imgHeight,Bitmap.Config.ARGB_8888);
                        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                        drawable.setAlpha(100);
                        maskImage.setImageDrawable(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    //-----------------------------------------------------------------------------------------


    MaskEraser.OnMaskTouch onMaskEraserTouch = new MaskEraser.OnMaskTouch() {
        @Override
        public void onPoint(float x, float y) {

        }
    };


    MaskImage.OnMaskTouch onMaskTouch = new MaskImage.OnMaskTouch()
    {
        @Override
        public void onPoint(float x, float y) {
            System.out.println(x + ", " + y);
        }
    };



}
