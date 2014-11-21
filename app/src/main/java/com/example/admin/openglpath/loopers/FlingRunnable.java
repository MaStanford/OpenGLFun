package com.example.admin.openglpath.loopers;

import android.os.SystemClock;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.ViewUtils;

import static com.example.admin.openglpath.util.Constants.GRAVITY;
import static com.example.admin.openglpath.util.Constants.WAIT;

/**
 * TODO: This whole pattern can be improved and generalized for other animations
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class FlingRunnable implements Runnable {

    private static final String TAG = "FlingHandler";

    private float x,y,velX,velY;
    private Drawable drawable;
    private boolean doRun = true;

    public FlingRunnable(final Drawable drawable, final float x, final float y, final float velX, final float velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.drawable = drawable;
    }

    @Override
    public void run() {
        final DataHolder dh = DataHolder.getInstance();

        float velocityX = velX;
        float velocityY = velY;
        //Continue to fling until gravity makes velocity 0 or below
        while((Math.abs(velocityX) > GRAVITY*2 || Math.abs(velocityY) > GRAVITY*2) && doRun) {

            //Scale the velocity
            float velX = ViewUtils.scaleXPixelPerSecondToFloat(dh.getWorkspaceView(), velocityX, 2);
            float velY = ViewUtils.scaleYPixelPerSecondToFloat(dh.getWorkspaceView(), velocityY, 2);

            //Add velocity to the object
            drawable.setXYZ(x + velX, y - velY, 1);

            //Decrement the velocity by the gravity factor
            if (velocityX > GRAVITY*2) {
                velocityX = velocityX - GRAVITY;
            }else if (velocityX < -GRAVITY*2){
                velocityX = velocityX + GRAVITY;
            }

            if (velocityY > GRAVITY*2) {
                velocityY = velocityY - GRAVITY;
            }else if (velocityX < -GRAVITY*2){
                velocityY = velocityY + GRAVITY;
            }

            //Take a sleep for a bit
            SystemClock.sleep(WAIT);
        }
    }

    public void kill(){
        this.doRun = false;
    }
}
