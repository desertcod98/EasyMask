package it.volta.ts.easymask.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import it.volta.ts.easymask.R;

public class MaskActivity extends AppCompatActivity {
    ImageView downloadedImg;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        downloadedImg = (ImageView) findViewById(R.id.imgSlot);
        Glide.with(this).load(url).into(downloadedImg);
    }
}
