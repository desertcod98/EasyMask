package it.volta.ts.easymask.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.slider.Slider;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.bean.Stats;
import it.volta.ts.easymask.fileHandling.FileHandler;
import it.volta.ts.easymask.networking.ThreadRunner;
import it.volta.ts.easymask.networking.UrlHandler;
import it.volta.ts.easymask.tools.ToolSelector;
import it.volta.ts.easymask.util.GraphicUtil;
import it.volta.ts.easymask.util.MathUtil;
import it.volta.ts.easymask.widgets.MaskImage;

public class MaskActivity extends AppCompatActivity
{
    private ThreadRunner threadRunner;

    private FileHandler fileHandler;

    private ImageView downloadedImg, brush, eraser, undo, redo, uploadBtn, btnStats;
    private MaskImage maskImage;
    private RelativeLayout imageLayout;
    private ImageView zoomIn, zoomOut;
    private BitmapDrawable sourceImage;
    private Slider slider;

    private Bitmap drawingBitmap;
    private Bitmap resizedBitmap;
    private int screenHeight;
    private int screenWidth;
    private String url;
    private int    maxHeight, maxWidth;
    private int    imgHeight, imgWidth;
    private int    newHeight, newWidth;

    private final double maxHeightRatio = 0.6;
    private final double maxWidthRatio  = 0.95;
    private final String urlKey = "url"; //TODO duplicate, dovrebbe essere solo in urlHandler, rivedi responsabilità classi

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        screenHeight = GraphicUtil.getScreenHeight(this);
        screenWidth  = GraphicUtil.getScreenWidth(this);
        maxHeight    = (int)(screenHeight * maxHeightRatio);
        maxWidth     = (int)(screenWidth  * maxWidthRatio );

        fileHandler = new FileHandler(this);


        Bundle b = getIntent().getExtras();
        url = b.getString(urlKey);
        downloadedImg = findViewById(R.id.imgSlot     );
        imageLayout   = findViewById(R.id.image_layout);

        maskImage = findViewById(R.id.imgMask);
        maskImage.setFocusable(true);
        maskImage.setDownloadedImg(downloadedImg);

        uploadBtn = findViewById(R.id.btnUp);
        uploadBtn.setOnClickListener(onUploadBtnClick);
        slider = findViewById(R.id.strokeSlider);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                maskImage.setStrokeWidth(value * 50);
            }
        });

        loadImage(downloadedImg, url);

        brush     = findViewById(R.id.brush );
        eraser    = findViewById(R.id.eraser);
        undo      = findViewById(R.id.undo);
        redo      = findViewById(R.id.redo);
        btnStats  = findViewById(R.id.btnSt);

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



        btnStats.setOnClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            float px = (float)Stats.calculateNonTraspPixels(maskImage.getDrawingBitmap());
            float w = maskImage.getDrawingBitmap().getWidth();
            float h = maskImage.getDrawingBitmap().getHeight();
            float perc = MathUtil.roundDown((px / (w*h)) * 100, 1);
            Toast.makeText(this, ("Coverage: " + perc + "%"), Toast.LENGTH_SHORT).show();

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            dimBehind(popupWindow);
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
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
            scale = MathUtil.roundDown(scale,1);


            if(v.getId() == R.id.zoom_in){
                scale += 0.1f;
            }else if(v.getId() == R.id.zoom_out){
                if (scale > 1f)
                    scale -= 0.1f;
            }

            downloadedImg.setScaleX(scale);
            downloadedImg.setScaleY(scale);
            maskImage    .setScaleX(scale);
            maskImage    .setScaleY(scale);
        }
    };

    View.OnClickListener onUploadBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            //getBitmap
            drawingBitmap = maskImage.getDrawingBitmap();
            //resize bitmap
            resizedBitmap = Bitmap.createScaledBitmap(
                    drawingBitmap, imgWidth, imgHeight, false);
            String imgCode = UrlHandler.getCodeFromUrl(url);
            fileHandler.writeImageToFile(resizedBitmap, imgCode);

            threadRunner = new ThreadRunner(MaskActivity.this);
            threadRunner.setImgCode(imgCode);
            threadRunner.start();
        }
    };

    private void dimBehind(PopupWindow popupWindow) {
        View container;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            container = (View) popupWindow.getContentView().getParent();
        } else {
            container = popupWindow.getContentView();
        }
        if (popupWindow.getBackground() != null) {
            container = (View) container.getParent();
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }


}

