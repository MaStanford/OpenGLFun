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

    private static final String TAG = "FlingAnimate";

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
        //Continue to fling until gravity makes velocity 0 or below
        if((Math.abs(velocityX) > GRAVITY*2 || Math.abs(velocityY) > GRAVITY*2) && doRun) {

            //Scale the velocity
            float velX = ViewUtils.scaleXPixelPerSecondToFloat(dh.getWorkspaceView(), velocityX);
            float velY = ViewUtils.scaleYPixelPerSecondToFloat(dh.getWorkspaceView(), velocityY);

            velX *= SLUDGE;
            velY *= SLUDGE;

            //Add velocity to the object
            drawable.setXYZ(x + velX, y - velY, 1);

            //Decrement the velocity by the gravity factor
            if (velocityX > GRAVITY * 2) {
                velocityX = velocityX - GRAVITY;
            } else if (velocityX < -GRAVITY * 2) {
                velocityX = velocityX + GRAVITY;
            }

            if (velocityY > GRAVITY * 2) {
                velocityY = velocityY - GRAVITY;
            } else if (velocityX < -GRAVITY * 2) {
                velocityY = velocityY + GRAVITY;
            }

            return true;
        }else{
            return false;
        }


    }
}
