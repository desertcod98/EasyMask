package it.volta.ts.easymask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import it.volta.ts.easymask.activities.AnyOrientationCaptureActivity;
import it.volta.ts.easymask.activities.MaskActivity;
import it.volta.ts.easymask.widgets.MaskImage;

public class MainActivity extends AppCompatActivity {

    private ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        intentIntegrator.setPrompt("Scan a barcode or a QR Code");
        intentIntegrator.setOrientationLocked(false);


        btn = findViewById(R.id.btnAct);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, MaskActivity.class);
                intent.putExtra("url", intentResult.getContents());
                startActivity(intent);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}