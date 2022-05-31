package it.volta.ts.easymask.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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

public class MaskImage extends androidx.appcompat.widget.AppCompatImageView
{
    @ColorInt int drawColor  = 0xffffff00;
    @ColorInt int eraseColor = 0x00ffffff;
    int stroke;

    private OnMaskTouch onMaskTouch;
    private Bitmap eraseBitmap;

    List<List<FPoint>> points;
    List<FPoint>       track;
    int position = 0;
    List<FPoint>       trackToRedo;

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

    private void init()
    {
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
//        erasePaint.setColor(eraseColor);
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setOnTouchListener(onTouch);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width  = MeasureSpec.getSize(widthMeasureSpec );
        height = MeasureSpec.getSize(heightMeasureSpec);
        stroke = width * 5 / 100;

        drawPaint .setStrokeWidth(stroke);
        erasePaint.setStrokeWidth(stroke);
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

            MaskImage.this.invalidate();
            return true;

        }
    };

    public void erase(Bitmap bitmap){
        eraseBitmap = bitmap;
        erasePaint  = new Paint();
        MaskImage.this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
//        super.onDraw(canvas);

//        Bitmap base  = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
//        canvas.drawBitmap(base,0,0, new Paint());

        for (int tdx=0; tdx < points.size(); tdx++)
        {
            List<FPoint> track = points.get(tdx);

            if (track.size() > 1)
            {
                Path path = new Path();

                for (int idx = 0; idx < track.size(); idx++)
                {
                    if (idx == 0)
                         path.moveTo(track.get(idx).x, track.get(idx).y);
                    else path.lineTo(track.get(idx).x, track.get(idx).y);
                }

                canvas.drawPath(path, (tdx % 2 == 0 ? drawPaint : erasePaint));
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

    //----------------------------------------------------------------------------------------------

    public void undo () {
        if (position > 0) {
            trackToRedo = points.get(position-1);
            points.remove(position-1);
            position--;
            MaskImage.this.invalidate();
        }
    }

    public void redo () {
        points.add(trackToRedo);
        MaskImage.this.invalidate();
        position++;
    }
}
