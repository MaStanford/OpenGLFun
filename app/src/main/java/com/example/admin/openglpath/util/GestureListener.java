package com.example.admin.openglpath.util;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.loopers.FlingRunnable;
import com.example.admin.openglpath.shapes.Card;
import com.example.admin.openglpath.shapes.Drawable;

import java.util.Random;

/**
 * Use this to create or modify object, select objects or w/e
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    public static final String TAG = "GestureListener";

    //Maybe we will need a context, who knows
    Context mContext;

    //The previously made object in case we have a double tap or long
    Drawable mcurrentDrawable;

    /**
     * Weird issue where onDown gets called after onDoubleTap
     */
    boolean isDoubleLastCommand = false;

    public GestureListener(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        DataHolder dh = DataHolder.getInstance();
        dh.clearCurrentSelectedDrawable();
        Log.d(TAG, "onSingleTapUp detected");
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onLongPress detected: " + x + ":" + y);

        dh.getDrawableList().clear();
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            Log.d(TAG, "onScroll detected: " + x + ":" + y);
            DataHolder.getInstance().getCurrentSelectedDrawable().setXYZ(x, y, 1);
        }

        //Check to see if we have an up gesture
        if(e2.getAction() == MotionEvent.ACTION_UP){
            Log.d(TAG, "onScroll action up == " + (e2.getAction() == MotionEvent.ACTION_UP));
            dh.clearCurrentSelectedDrawable();
        }

        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            Log.d(TAG, "onFling detected: " + x + ":" + y);
            dh.getCurrentSelectedDrawable().setXYZ(x, y , 1);
        }

        //Check to see if we have an up gesture and then calculate the fling
        if(e2.getAction() == MotionEvent.ACTION_UP){
            //Make sure we have a drawable under our touch
            if(dh.isCurrentlySelectedDrawable()){
                Log.d(TAG, "onFlingUp detected: " + velocityX + ":" + velocityY);
                float x, y;
                float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
                x = scaled[0];
                y = scaled[1];

                //Fling the object!
                FlingRunnable fling = new FlingRunnable(dh.getCurrentSelectedDrawable(), x, y, velocityY, velocityY);
                dh.getFlingMap().put(dh.getCurrentSelectedDrawable(), fling);
                dh.getFlingQueue().add(fling);
            }
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        if(!isDoubleLastCommand) {
            DataHolder dh = DataHolder.getInstance();
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            Log.d(TAG, "onDown detected: " + x + ":" + y);

            //Check to see if we intersect an object here.  If not then create one.
            Drawable drawable = dh.getIntersectingDrawable(x, y);
            if (drawable != null) {
                dh.setCurrentSelectedDrawable(drawable);
                mcurrentDrawable = drawable;

                //Check to see if we are flinging.  If we are then we need to kill it.
                if(dh.getFlingMap().containsKey(drawable)){
                    dh.getFlingMap().get(drawable).kill();
                }

            } else { //TODO: we need to do a check to see what we are supposed to draw here
                Card newCard = new Card(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                dh.setCurrentSelectedDrawable(newCard);
                dh.addDrawable(newCard);
                mcurrentDrawable = newCard;
            }
        }
        isDoubleLastCommand = false;
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        isDoubleLastCommand = true;

        DataHolder dh = DataHolder.getInstance();

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];

        Log.d(TAG, "onDoubleTap detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dh.getIntersectingDrawable(x,y);
        if(drawable != null) {
            if(mcurrentDrawable != null) {
                dh.removeDrawable(mcurrentDrawable);
                mcurrentDrawable = null;
            }

            dh.removeDrawable(drawable);
            dh.clearCurrentSelectedDrawable();
        }else{
            dh.removeDrawable(mcurrentDrawable);
            mcurrentDrawable = null;
            dh.clearCurrentSelectedDrawable();
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }
}
