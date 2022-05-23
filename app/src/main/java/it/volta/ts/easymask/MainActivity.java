package it.volta.ts.easymask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import it.volta.ts.easymask.activities.QrCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, QrCaptureActivity.class);

        btn = (ImageView)findViewById(R.id.btnAct);
        btn.setImageResource(R.drawable.btnqr);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }
}