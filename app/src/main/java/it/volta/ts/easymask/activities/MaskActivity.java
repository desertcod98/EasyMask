package it.volta.ts.easymask.activities;

import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.tools.ToolSelector;
import it.volta.ts.easymask.widgets.MaskImage;

public class MaskActivity extends AppCompatActivity
{
    ImageView downloadedImg, brush, eraser, btnUpload;
    MaskImage maskImage;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        downloadedImg = (ImageView) findViewById(R.id.imgSlot);
        Glide.with(this).load(url).into(downloadedImg);

        maskImage = findViewById(R.id.imgMask);
        maskImage.setOnMaskTouch(onMaskTouch);

        brush = findViewById(R.id.brush);
        eraser = findViewById(R.id.eraser);
        btnUpload = findViewById(R.id.btnUp);

        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 1;
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSelector.toolState = 0;
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




    MaskImage.OnMaskTouch onMaskTouch = new MaskImage.OnMaskTouch()
    {
        @Override
        public void onPoint(float x, float y) {
            System.out.println(x + ", " + y);
        }
    };

}
