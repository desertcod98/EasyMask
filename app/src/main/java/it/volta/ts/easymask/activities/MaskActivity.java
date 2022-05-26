package it.volta.ts.easymask.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.tools.ToolSelector;
import it.volta.ts.easymask.widgets.MaskEraser;
import it.volta.ts.easymask.widgets.MaskImage;

import android.widget.RelativeLayout.LayoutParams;
import android.view.View;

public class MaskActivity extends AppCompatActivity
{
    ImageView downloadedImg, brush, eraser, btnUpload;
    MaskImage maskImage;
    MaskEraser maskEraser;
    RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        downloadedImg = findViewById(R.id.imgSlot);
        Glide.with(this).load(url).into(downloadedImg);

        rel = findViewById(R.id.layout);

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        LayoutParams params = new LayoutParams(w, h);
                        rel.setLayoutParams(params);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        maskImage = findViewById(R.id.imgMask);
        maskImage.setOnMaskTouch(onMaskTouch);
        maskEraser = findViewById(R.id.imgMaskEraser);
        maskEraser.setOnMaskTouch(onMaskEraserTouch);

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
