package it.volta.ts.easymask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import it.volta.ts.easymask.R;

public class PopupActivity extends AppCompatActivity {

    TextView cov, imgSize, outOf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);

        cov = findViewById(R.id.coverage);
        imgSize = findViewById(R.id.imageDimens);
        outOf = findViewById(R.id.pixelsOutOf);

        Bundle b = getIntent().getExtras();

        System.out.println(b.getString("covStr"));

        cov.setText(b.getString("covStr"));
        imgSize.setText(b.getString("sizeStr"));
        outOf.setText(b.getString("pixStr"));
    }

    public boolean onTouchEvent(MotionEvent event){
        this.finish();
        return true;
    }
}