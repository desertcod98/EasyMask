package it.volta.ts.easymask.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import it.volta.ts.easymask.obj.FPoint;
import it.volta.ts.easymask.obj.Track;
import it.volta.ts.easymask.tools.ToolSelector;

/**
 * https://stackoverflow.com/questions/6650398/android-imageview-zoom-in-and-zoom-out
 */

public class MaskImage extends androidx.appcompat.widget.AppCompatImageView {
    @ColorInt
    private int drawColor = 0xffffff00;

    private float stroke;

    private List<Track> points;
    private Track track;


    private int position = 0;
    private Stack<Track> pointsToRedo;
    private Track trackToRedo;

    private int width, height;
    private float fromX, fromY, toX, toY;
    private Paint drawPaint, erasePaint;
    private int fingerCount;

    public MaskImage(Context context) {
        super(context);
        init();
    }

    public MaskImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        points = new ArrayList<>();
        pointsToRedo = new Stack<>();

        drawPaint = new Paint();
        drawPaint.setAntiAlias(false);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(drawColor);

        erasePaint = new Paint();
        erasePaint.setAntiAlias(false);
        erasePaint.setStrokeCap(Paint.Cap.ROUND);
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setOnTouchListener(onTouch);

//        drawable = new BitmapDrawable(getResources(), this.getDrawingCache());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if(drawPaint.getStrokeWidth() == 0 || erasePaint.getStrokeWidth() == 0){
            stroke = width * 5 / 100;

            drawPaint.setStrokeWidth(stroke);
            erasePaint.setStrokeWidth(stroke);
        }
    }

    OnTouchListener onTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getPointerCount() == 1) {
                        fromX = x;
                        fromY = y;
                        track = new Track();

                        points.add(track);
                        position++;

                        if (ToolSelector.toolState == 1)
                            track.setEraser(false);
                        else track.setEraser(true);
                        track.getTrackList().add(new FPoint(x, y));

                    }
                    fingerCount = event.getPointerCount();
                    break;
                case MotionEvent.ACTION_UP:


                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        toX = x;
                        toY = y;

                        fromX = x;
                        fromY = y;
                        if (ToolSelector.toolState == 1){
                            track.setEraser(false);
                        }
                        else {
                            track.setEraser(true);
                        }
                        track.getTrackList().add(new FPoint(x, y));
                    }
                    else{
                        if(fingerCount == 1){
                            fingerCount = 2;
                            points.remove(points.size()-1);
                            position--;
                        }

                    }
                    MaskImage.this.invalidate();


            }
            return true;
        }
    };

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        for (int tdx = 0; tdx < points.size(); tdx++) {
            Track track = new Track();
            track.setTrackList(points.get(tdx).getTrackList());

            if (track != null && track.getTrackList().size() > 1) {
                Path path = new Path();
                for (int idx = 0; idx < track.getTrackList().size(); idx++) {
                    if (idx == 0)
                        path.moveTo(track.getTrackList().get(idx).x, track.getTrackList().get(idx).y);
                    else path.lineTo(track.getTrackList().get(idx).x, track.getTrackList().get(idx).y);
                }
                canvas.drawPath(path, (points.get(tdx).isEraser() ? erasePaint : drawPaint));
            }
        }
    }

    public void undo() {
        if (position > 0) {
            trackToRedo = points.get(position - 1);
            pointsToRedo.push(trackToRedo);
            points.remove(position - 1);
            position--;
            MaskImage.this.invalidate();
        }
    }

    public void setStrokeWidth(float strokeWidth) {
        erasePaint.setStrokeWidth(stroke);
        drawPaint.setStrokeWidth(stroke);
    }

    public float getStrokeWidthConst(){
        return stroke;
    }

    public void redo() {
        if(!(pointsToRedo.isEmpty())){
            points.add(pointsToRedo.pop());
            position++;
        }
        MaskImage.this.invalidate();

    }

}

