package com.example.admin.openglpath.loopers;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.ViewUtils;

import static com.example.admin.openglpath.util.Constants.*;

/**
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class FlingAnimation implements Animation {

    private static final String TAG = "FlingAnimation";

    private float x,y, velocityX, velocityY;
    private Drawable drawable;
    private boolean doRun = true;
    private DataHolder dh = DataHolder.getInstance();

    public FlingAnimation(Drawable drawable, float x, float y, float velocityX, float velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
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

            //Add velocity to the object
            drawable.setXYZ(x + velX*2, y - velY, 1);

            //Decrement the velocity by the gravity factor
            if (Math.abs(velocityX) > MIN_SPEED) {
                velocityX *= GRAVITY;
            }

            if (Math.abs(velocityX) > MIN_SPEED) {
                velocityY *= GRAVITY;
            }

            return true;
        }else{
            return false;
        }
    }
}
