package com.example.admin.openglpath.loopers;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.ViewUtils;

/**
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class FlingAnimation implements Animation {

    private static final String TAG = "FlingAnimation";

    //Minimum speed in which to show fling
    public static final float MIN_SPEED = 100f;

    //Gravity is how much the velocity decrements per loop
    public static final float GRAVITY = 1.01f;

    //The initial resistence to movement
    public static final float SLUDGE = 2f;

    private float x,y, velocityX, velocityY;
    private Drawable drawable;
    private boolean doRun = true;
    private DataHolder dh = DataHolder.getInstance();

    public FlingAnimation(Drawable drawable, float x, float y, float velocityX, float velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX/SLUDGE;
        this.velocityY = velocityY/SLUDGE;
        this.drawable = drawable;
    }

    @Override
    public void stop(){
        this.doRun = false;
    }

    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * Call this to animate the next step.
     *
     * Returns true is loop is still valid, return false if loop is over.
     *
     * @return
     */
    @Override
    public boolean doNext() {
        //Continue to fling until gravity makes velocity less than min velocity
        if(((Math.abs(velocityX) > MIN_SPEED) || (Math.abs(velocityX) > MIN_SPEED)) && doRun) {

            //Scale the velocity
            float velX = ViewUtils.scaleXPixelPerSecondToFloatPerFrame(dh.getWorkspaceView(), velocityX);
            float velY = ViewUtils.scaleYPixelPerSecondToFloatPerFrame(dh.getWorkspaceView(), velocityY);

            //Set the new values
            x = x + velX;
            y = y - velY;

            //Add velocity to the object
            drawable.setXYZ(x, y, 1);

            //Decrement the velocity by the gravity factor
            if (Math.abs(velocityX) > MIN_SPEED) {
                velocityX = velocityX / GRAVITY;
            }

            if (Math.abs(velocityX) > MIN_SPEED) {
                velocityY = velocityY / GRAVITY;
            }

            return true;
        }else{
            return false;
        }
    }
}
