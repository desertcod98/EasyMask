package it.volta.ts.easymask.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class GraphicUtil
{
    public static int getScreenWidth(Activity activity)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    public static int getScreenHeight(Activity activity)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static void applySize(RelativeLayout layout, int width, int height)
    {
        layout.getLayoutParams().width = width;
        layout.getLayoutParams().height = height;
    }
}
