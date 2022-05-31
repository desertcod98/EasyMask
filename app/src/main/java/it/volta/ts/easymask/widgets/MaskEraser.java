package it.volta.ts.easymask.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.volta.ts.easymask.obj.FPoint;
import it.volta.ts.easymask.tools.ToolSelector;

public class MaskEraser extends androidx.appcompat.widget.AppCompatImageView
{

    private MaskImage maskImage;

    @ColorInt
    int eraseColor = Color.WHITE;
    int stroke;

    private OnMaskTouch onMaskTouch;

    List<List<FPoint>> points;
    List<FPoint>       track;
    int position = 0;
    List<FPoint>       trackToRedo;

    int width, height;
    float fromX, fromY, toX, toY;
    Paint paint;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setDrawingCacheEnabled(true);
        width  = MeasureSpec.getSize(widthMeasureSpec );
        height = MeasureSpec.getSize(heightMeasureSpec);
        stroke = width * 5 / 100;
        paint.setStrokeWidth(stroke);
    }

    OnTouchListener onTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event)
        {
            float x = event.getX();
            float y = event.getY();

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    fromX = x;
                    fromY = y;
                    track = new ArrayList<>();

                    points.add(track);
                    position++;

                    if (ToolSelector.toolState == 1)
                    {
                        track.add(new FPoint(x,y, false));
                    } else {
                        track.add(new FPoint(x,y, true));
                    }
                    if (onMaskTouch != null)
                        onMaskTouch.onPoint(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    if (ToolSelector.toolState != 1)
                    {

                    }
                    //show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    toX = x;
                    toY = y;

                    fromX = x;
                    fromY = y;
                    if (ToolSelector.toolState == 1)
                    {
                        track.add(new FPoint(x,y, false));
                    } else {
                        track.add(new FPoint(x,y, true));
                    }
                    if (onMaskTouch != null)
                        onMaskTouch.onPoint(x,y);

                    break;
            }

            MaskEraser.this.invalidate();
            return true;
        }
    };


    @Override
    protected void onDraw(Canvas canvas)
    {
        //super.onDraw(canvas);

        if (points != null) {
            for (List<FPoint> track : points) {
                if (track.size() > 1) {
                    for (int idx = 1; idx < track.size(); idx++) {
                        if (track.get(idx - 1).eraser || track.get(idx).eraser) {

                            paint.setColor(eraseColor);
                            canvas.drawLine(track.get(idx - 1).x, track.get(idx - 1).y,
                                    track.get(idx).x, track.get(idx).y,
                                    paint);
                        }
                    }
                } else {

                }
            }

//            try {
//                maskImage.erase(this.getDrawingCache());
//            } catch (Exception e) {
//                System.out.println("eccezione");
//            }
        }
    }

    //----------------------------------------------------------------------------------------------

    public void setOnMaskTouch(OnMaskTouch onMaskTouch) {
        this.onMaskTouch = onMaskTouch;
    }

    public interface OnMaskTouch
    {
        void onPoint(float x, float y);
    }

    //----------------------------------------------------------------------------------------------

    public MaskEraser(Context context) {
        super(context);
        init();
    }

    public MaskEraser(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskEraser(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        points = new ArrayList<>();

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        setBackgroundColor(0x00000000);
        setOnTouchListener(onTouch);
    }

    public void setMaskImage(MaskImage maskImage) {
        this.maskImage = maskImage;
    }

    //-----------------------------------------------------------------------------------------

    public void undo () {
        if (position > 0) {
            trackToRedo = points.get(position-1);
            points.remove(position-1);
            position--;
            MaskEraser.this.invalidate();
        }
    }

    public void redo () {
        points.add(trackToRedo);
        position++;
        MaskEraser.this.invalidate();
    }
}
