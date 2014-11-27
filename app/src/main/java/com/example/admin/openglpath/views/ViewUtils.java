package com.example.admin.openglpath.views;

import android.util.Log;
import android.view.View;

import com.example.admin.openglpath.data.DataHolder;

import static com.example.admin.openglpath.loopers.AnimationLoop.SECOND;
import static com.example.admin.openglpath.loopers.AnimationLoop.WAIT;

/**
 * Created by Mark Stanford on 11/19/14.
 */
public class ViewUtils {

    private static final String TAG = "ViewUtils";

    /**
     * Scales a touch event to openGL view.
     *
     * @param parentView View touch happened in
     * @param touchX X of the touch event from the View
     * @param touchY Y of the touch event from the View
     * @return float array with x[0] y[1]
     */
    public static float[] scaleTouchEvent(View parentView, float touchX, float touchY){
        int parentWidth = parentView.getWidth();
        int parentHeight = parentView.getHeight();

        float ratioToScreenX = (touchX / parentWidth);
        float ratioToScreenY = (parentHeight - touchY) / parentHeight;

        Log.d(TAG, "xRatioToScreen: " + ratioToScreenX);
        Log.d(TAG, "yRatioToScreen: " + ratioToScreenY);

        /**
         *
         * My math for this is:
         * (ratioTouch * (Zoom*2)) - zoom
         *
         * The ratio is making the touch from pixels into 0,0 bottom left and 1,1 top right.
         *
         * The zoom is -zoom to + zoom on the workspace, so we need * 2 the zoom.  This is becuase
         *  is counts from -zoom to +zoom which is zoom*2 units.
         *
         * We then subtract the zoom so make the number be scaled properly into the workspace. with 0,0 in the middle.
         *
         * Ex. ratioTouch = .5,.5
         * We want this to be 0,0 in a zoom of 1
         *
         * (.5 * (1*2)) - 1 = 0,0
         *
         * Ex. ratioTouch = 1,1,
         * We want this to be 2,2 in a zoom of 2
         * (1 * (2*2)) - 2 = 2,2
         *
         * Ex. ratioTouch = .25,.25
         * We want this to be -.5,-.5 in a zoom of 1
         * (.25 * (1*2)) - 1 = -.5,-.5
         *
         * We also need to incorporate the aspect ratio here.
         * We also need to add or subtract the offset for scrolling the workspace.
         */

        float aspectRatio = DataHolder.getInstance().getAspectRatio();
        float zoom = DataHolder.getInstance().getZoom();

        //Magic 2 makes it work because the zoom is -min and max which makes it doubel since it counts from - to +
        float xRatioWithZoom = (ratioToScreenX * (zoom*2)) - (zoom);
        float yRatioWithZoom = (ratioToScreenY * (zoom*2)) - (zoom);

        Log.d(TAG, "xRatioWithZoom: " + ratioToScreenX);
        Log.d(TAG, "yRatioWithZoom: " + ratioToScreenY);

        float ratioToScreenWithAspectX = (xRatioWithZoom * (aspectRatio)); //The two is because we split the screen at 0;

        Log.d(TAG, "ratioToScreenWithAspectX: " + ratioToScreenWithAspectX);


        /**
         * Put the x,y offset here.  It will just be to - or + the offset to the ratio
         */


        return new float[]{ratioToScreenWithAspectX,yRatioWithZoom};
    }

    /**
     * Scales a fling in pixels per second to a float per second
     * TODO: Fix this.  Also have it include the zoom factor and the left/right up/down offset
     * @param parentView
     * @param fling
     * @return
     */
    public static float scaleXPixelPerSecondToFloatPerFrame(View parentView, float fling){
        float parentWidth = parentView.getWidth();

        float waitsPerSecond = (SECOND/ WAIT);
        return (fling/waitsPerSecond) / (parentWidth);
    }

    /**
     * Scales a fling in pixels per second to a float per second
     * TODO: Fix this.  Also have it include the zoom factor and the left/right up/down offset
     * @param parentView
     * @param fling
     * @return
     */
    public static float scaleYPixelPerSecondToFloatPerFrame(View parentView, float fling){
        float parentHeight = parentView.getHeight();

        float waitsPerSecond = (SECOND / WAIT);
        return (fling/waitsPerSecond) / (parentHeight);
    }
}
