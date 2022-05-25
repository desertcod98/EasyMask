package it.volta.ts.easymask;



import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.IOException;

import it.volta.ts.easymask.activities.AnyOrientationCaptureActivity;
import it.volta.ts.easymask.activities.MaskActivity;
import it.volta.ts.easymask.networking.ThreadRunner;
import it.volta.ts.easymask.networking.UploadManager;

public class MainActivity extends AppCompatActivity {

    ThreadRunner threadRunner = new ThreadRunner();

    private ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);

        //----------
        File dir = new File(getApplicationContext().getFilesDir(),"mydir");
        System.out.println("----------------------------"+dir);
        if(!dir.exists()){
            dir.mkdir();
        }

        threadRunner.setDir(dir);
        threadRunner.start();

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
                /*downloadManager = new DownloadManager();
                downloadManager.saveImgToFile(intentResult.getContents());
                messageText.setText(intentResult.getContents());
                */
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}