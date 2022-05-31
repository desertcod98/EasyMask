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

import it.volta.ts.easymask.obj.FPoint;
import it.volta.ts.easymask.tools.ToolSelector;

/**
 * https://stackoverflow.com/questions/6650398/android-imageview-zoom-in-and-zoom-out
 */

public class MaskImage extends androidx.appcompat.widget.AppCompatImageView {
    @ColorInt
    int drawColor = 0xffffff00;

    int stroke;
    private OnMaskTouch onMaskTouch;

    List<List<FPoint>> points;
    List<FPoint> track;
    int position = 0;
    List<FPoint> trackToRedo;

    int fingerCounter = 0;
    boolean multiTouch = false;
    int width, height;
    float fromX, fromY, toX, toY;
    Paint drawPaint, erasePaint;

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
        stroke = width * 5 / 100;

        drawPaint.setStrokeWidth(stroke);
        erasePaint.setStrokeWidth(stroke);
    }

    OnTouchListener onTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    fingerCounter++;
                    System.out.println("DOWN " + fingerCounter);

                    if (fingerCounter == 1) {
                        fromX = x;
                        fromY = y;
                        track = new ArrayList<>();

                        points.add(track);
                        position++;

                        if (ToolSelector.toolState == 1)
                            track.add(new FPoint(x, y, false));
                        else track.add(new FPoint(x, y, true));

                        if (onMaskTouch != null)
                            onMaskTouch.onPoint(x, y);
                    } else {
                        multiTouch = true;
                        if (points.size() > 0)
                            points.remove(points.size() - 1);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    fingerCounter--;
                    if (multiTouch) {
                        multiTouch = false;
                        fingerCounter = 0;
                    }

                    System.out.println("UP   " + fingerCounter);

                    break;
                case MotionEvent.ACTION_MOVE:

                    System.out.println("MOVE " + fingerCounter);
                    if (!multiTouch) {
                        toX = x;
                        toY = y;

                        fromX = x;
                        fromY = y;
                        if (ToolSelector.toolState == 1)
                            track.add(new FPoint(x, y, false));
                        else track.add(new FPoint(x, y, true));
                        if (onMaskTouch != null)
                            onMaskTouch.onPoint(x, y);
                    }
                    break;
            }

            MaskImage.this.invalidate();
            return true;

        }
    };

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        for (int tdx = 0; tdx < points.size(); tdx++) {
            List<FPoint> track = points.get(tdx);

            if (track.size() > 1) {
                Path path = new Path();
                for (int idx = 0; idx < track.size(); idx++) {
                    if (idx == 0)
                        path.moveTo(track.get(idx).x, track.get(idx).y);
                    else path.lineTo(track.get(idx).x, track.get(idx).y);
                }
                canvas.drawPath(path, (points.get(tdx).get(0).eraser == false ? drawPaint : erasePaint));
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    public void setOnMaskTouch(OnMaskTouch onMaskTouch) {
        this.onMaskTouch = onMaskTouch;
    }

    public interface OnMaskTouch {
        void onPoint(float x, float y);
    }

    //----------------------------------------------------------------------------------------------

    public void undo() {
        if (position > 0) {
            trackToRedo = points.get(position - 1);
            points.remove(position - 1);
            position--;
            MaskImage.this.invalidate();
        }
    }

    public void redo() {
        points.add(trackToRedo);
        MaskImage.this.invalidate();
        position++;
    }
}
