package com.example.admin.openglpath.util;

import android.view.View;

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
}
