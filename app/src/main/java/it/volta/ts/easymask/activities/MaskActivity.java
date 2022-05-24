package it.volta.ts.easymask.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import it.volta.ts.easymask.R;
import it.volta.ts.easymask.widgets.MaskImage;

/**
 *  https://vuo.elettra.eu/vuo/cgi-bin/easymask.py?action=get_map&code=S7O61G6Gr5qKDl0XlEy9vnx8pVvttnS5
 */


public class MaskActivity extends AppCompatActivity
{
    ImageView downloadedImg;
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
    }


    MaskImage.OnMaskTouch onMaskTouch = new MaskImage.OnMaskTouch()
    {
        @Override
        public void onPoint(float x, float y) {
            System.out.println(x + ", " + y);
        }
    };

}
