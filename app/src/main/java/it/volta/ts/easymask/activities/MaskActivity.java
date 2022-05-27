package it.volta.ts.easymask.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;

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
    ImageView downloadedImg, brush, eraser, btnUpload;
    MaskImage maskImage;
    MaskEraser maskEraser;
//    RelativeLayout rel;
    private int screenHeight;
    private int screenWidth;
    private String url;
    private int maxHeight;
    private int maxWidth;
    private int imgHeight;
    private int imgWidth;

    private final double maxHeightRatio = 2;
    private final double maxWidthRatio = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);
        screenHeight = GraphicUtil.getScreenHeight(this);
        screenWidth = GraphicUtil.getScreenWidth(this);
        maxHeight = (int)(screenHeight *maxHeightRatio);
        maxWidth = (int)(screenWidth*maxWidthRatio);
        Bundle b = getIntent().getExtras();
        url = b.getString("url");
        downloadedImg = findViewById(R.id.imgSlot);
        loadImage(downloadedImg, url);




        maskImage = findViewById(R.id.imgMask);
        maskImage.setOnMaskTouch(onMaskTouch);



        maskEraser = findViewById(R.id.imgEraser);
        maskEraser.setOnMaskTouch(onMaskEraserTouch);
        maskEraser.setMaskImage(maskImage);



        brush = findViewById(R.id.brush);
        eraser = findViewById(R.id.eraser);
        btnUpload = findViewById(R.id.btnUp);

        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 1;
                maskEraser.setEnabled(false);
                maskImage.setEnabled(true);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 0;
                maskEraser.setEnabled(true);
                maskImage.setEnabled(false);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });
    }

    private void setDimens(int imgWidth, int imgHeight){
        downloadedImg.getLayoutParams().height = (imgHeight*maxHeight)/screenHeight;
        downloadedImg.getLayoutParams().width = (imgWidth*maxWidth)/screenWidth;
        maskImage.getLayoutParams().height = (imgHeight*maxHeight)/screenHeight;
        maskImage.getLayoutParams().width = (imgWidth*maxWidth)/screenWidth;
        maskEraser.getLayoutParams().height = (imgHeight*maxHeight)/screenHeight;
        maskEraser.getLayoutParams().width = (imgWidth*maxWidth)/screenWidth;
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
