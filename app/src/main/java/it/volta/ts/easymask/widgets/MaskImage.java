package it.volta.ts.easymask.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.volta.ts.easymask.obj.FPoint;
import it.volta.ts.easymask.tools.ToolSelector;

public class MaskImage extends androidx.appcompat.widget.AppCompatImageView
{
    @ColorInt
    int drawColor = 0xffffff00;
    int stroke;

    private OnMaskTouch onMaskTouch;

    List<List<FPoint>> points;
    List<FPoint>       track;

    int width, height;
    float fromX, fromY, toX, toY;
    Paint paint;

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

    private void init()
    {
        points = new ArrayList<>();

        paint = new Paint();
        paint.setColor(drawColor);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        setBackgroundColor(0x00000000);
        setOnTouchListener(onTouch);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                    if (ToolSelector.toolState == 1)
                    {
                        fromX = x;
                        fromY = y;
                        track = new ArrayList<>();
                        track.add(new FPoint(x,y));
                        points.add(track);

                        if (onMaskTouch != null)
                            onMaskTouch.onPoint(x,y);
                    } else {

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (ToolSelector.toolState == 1)
                    {

                    } else {

                    }
                    //show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ToolSelector.toolState == 1)
                    {
                        toX = x;
                        toY = y;
                        track.add(new FPoint(x,y));
                        fromX = x;
                        fromY = y;

                        if (onMaskTouch != null)
                            onMaskTouch.onPoint(x,y);
                    } else {

                    }
                    break;
            }

            MaskImage.this.invalidate();
            return true;
        }
    };

    private void show()
    {
        System.out.println("--------------------");
        for (List<FPoint> track : points) {
            System.out.println("New track");
            for (FPoint point : track)
                System.out.println("    " + (int)point.x + ", " + (int)point.y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (List<FPoint> track : points)
        {
            if (track.size() > 1) {
                for (int idx = 1; idx < track.size(); idx++) {
                    canvas.drawLine(track.get(idx-1).x, track.get(idx-1).y,
                                    track.get(idx  ).x, track.get(idx  ).y,
                                    paint);
                }
            } else {

            }
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
}
