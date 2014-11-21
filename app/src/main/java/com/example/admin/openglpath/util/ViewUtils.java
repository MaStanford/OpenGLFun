package com.example.admin.openglpath.util;

import android.view.View;

import static com.example.admin.openglpath.util.Constants.*;

/**
 * Created by Mark Stanford on 11/19/14.
 */
public class ViewUtils {

    /**
     * Scales a touch event to openGL view.
     *
     * @param parentView View touch happened in
     * @param touchX X of the touch event from the View
     * @param touchY Y of the touch event from the View
     * @param scaleOver How many units to scale the event over
     * @return float array with x[0] y[1]
     */
    public static float[] scaleTouchEvent(View parentView, float touchX, float touchY, int scaleOver){
        int parentWidth = parentView.getWidth();
        int parentHeight = parentView.getHeight();

        float scaledX = (touchX / (parentWidth/scaleOver)) -  (scaleOver/2);
        float scaledY = ((parentHeight - touchY) / (parentHeight/scaleOver)) - (scaleOver/2);

        return new float[]{scaledX,scaledY};
    }

    /**
     * Scales a fling in pixels per second to a float per second
     * @param parentView
     * @param fling
     * @param scale How many units are in the workspace. I.E -1 to 1 is a scale of 2
     * @return
     */
    public static float scaleXPixelPerSecondToFloat(View parentView, float fling, int scale){
        int parentWidth = parentView.getWidth();

        int waitsPerSecond = (SECOND/ WAIT);
        return (fling/waitsPerSecond) / (parentWidth/scale);
    }

    /**
     * Scales a fling in pixels per second to a float per second
     *
     * @param parentView
     * @param fling
     * @param scale How many units are in the workspace . I.E -1 to 1 is a scale of 2
     * @return
     */
    public static float scaleYPixelPerSecondToFloat(View parentView, float fling, int scale){
        int parentHeight = -parentView.getHeight();

        int waitsPerSecond = (SECOND/ WAIT);
        return (fling/waitsPerSecond) / (parentHeight/scale);
    }
}
