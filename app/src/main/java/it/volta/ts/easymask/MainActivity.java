package it.volta.ts.easymask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import it.volta.ts.easymask.activities.QrCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imgLogo, imgScritta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgLogo = (ImageView)findViewById(R.id.ImageLogo);
        imgLogo.setImageResource(R.drawable.logo);

        imgScritta = (ImageView)findViewById(R.id.ImageScritta);
        imgScritta.setImageResource(R.drawable.scrittamain);

        Intent intent = new Intent(this, QrCaptureActivity.class);
        startActivity(intent);

    }
}