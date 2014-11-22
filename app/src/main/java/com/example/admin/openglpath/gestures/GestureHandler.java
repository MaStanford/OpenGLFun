package com.example.admin.openglpath.gestures;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.loopers.FlingAnimation;
import com.example.admin.openglpath.shapes.Card;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.shapes.Stroke;
import com.example.admin.openglpath.shapes.StrokePoints;
import com.example.admin.openglpath.util.ViewUtils;

import java.util.Random;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class GestureHandler {

    public static final String TAG = "GestureHandler";

    //Dataholder
    DataHolder dh = DataHolder.getInstance();

    //The previously made object in case we have a double tap or long
    Drawable mPreviouslyMadeObject;

    //Maintian state of strokes
    private int mCurrentObject          = -1;

    //Maintain state of gestures
    private int mLastCommand            = NONE;

    /**
     * Drawable currently selected or last made
     */
    public static final int CARD               = 4;
    public static final int STROKE             = 5;
    public static final int STROKE_POINTS      = 6;

    /**
     * Command last used
     */
    public static final int NONE        = 122;
    public static final int DOWN        = 123;
    public static final int DOUBLE_TAP  = 124;
    public static final int SCROLL      = 125;
    public static final int SCALE_START = 126;
    public static final int SCALE_STOP  = 127;
    public static final int FLING       = 128;

    /**
     * Private constructor for our singleton pattern
     */
    private GestureHandler(){
    }

    /**
     * Instance holder for our safe lazy instantiation pattern
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class instanceHolder{
        private static final GestureHandler INSTANCE = new GestureHandler();
    }

    /**
     * Returns the singleton
     * @return The GestureHandler singleton reference
     */
    public static GestureHandler getInstance(){
        return instanceHolder.INSTANCE;
    }

    public void onScale(ScaleGestureDetector detector) {
        dh.clearCurrentSelectedDrawable();
        Log.d(TAG, "onScale");
    }

    public void onScaleBegin(ScaleGestureDetector detector) {

        //Delete the created object from the onDown
        if(mLastCommand == DOWN) {
            dh.removeDrawable(mPreviouslyMadeObject);
            dh.clearCurrentSelectedDrawable();
            mPreviouslyMadeObject = null;
            mLastCommand = SCALE_START;
        }

        Log.d(TAG, "onScaleBegin");
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        mLastCommand = SCALE_STOP;
        Log.d(TAG, "onScaleEnd");
    }

    public void onSingleTapUp(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        dh.clearCurrentSelectedDrawable();
        Log.d(TAG, "onSingleTapUp detected");
    }

    public void onLongPress(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onLongPress detected: " + x + ":" + y);

        //For debugging purposes
        dh.getDrawableList().clear();
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = SCROLL;
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
        x = scaled[0];
        y = scaled[1];

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable() && e2.getAction() != MotionEvent.ACTION_UP){
            //We have a currently selected drawable. We need to change it's x,y;
            Log.d(TAG, "onScroll detected: " + x + ":" + y);
            DataHolder.getInstance().getCurrentSelectedDrawable().setXYZ(x, y, 1);
        }

        //Check to see if we have an up gesture
        if(e2.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onScroll action up == " + (e2.getAction() == MotionEvent.ACTION_UP));
            dh.clearCurrentSelectedDrawable();
            mLastCommand = NONE;
        }
    }

    public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = FLING;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onFling detected: " + x + ":" + y);


        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            dh.getCurrentSelectedDrawable().setXYZ(x, y , 1);
        }

        //Check to see if we have an up gesture and then calculate the fling
        if(e2.getAction() == MotionEvent.ACTION_UP){
            //Make sure we have a drawable under our touch
            if(dh.isCurrentlySelectedDrawable() && mCurrentObject == CARD){
                //Fling the object!
                FlingAnimation fling = new FlingAnimation(dh.getCurrentSelectedDrawable(), x, y, velocityX, velocityY);
                dh.getAnimationMap().put(dh.getCurrentSelectedDrawable(), fling);
                dh.clearCurrentSelectedDrawable();
            }
        }
    }

    public void onShowPress(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        Log.d(TAG, "onShowPress detected!");
    }

    public void onDown(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        //Maintain handler state
        mLastCommand = DOWN;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onDown detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dh.getIntersectingDrawable(x, y);
        if (drawable != null) {
            dh.setCurrentSelectedDrawable(drawable);

            //Check to see if we are flinging.  If we are then we need to kill it.
            if(dh.getAnimationMap().containsKey(drawable)){
                dh.getAnimationMap().get(drawable).stop();
            }

        } else {
            //Draw the shape
            Drawable newObj;
            switch(dh.getmCurrentShapeToDraw()){
                case CARD:
                    newObj = new Card(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                    mPreviouslyMadeObject = dh.getCurrentSelectedDrawable();
                    dh.setCurrentSelectedDrawable(newObj);
                    dh.addDrawable(newObj);
                    mCurrentObject = CARD;
                    break;
                case STROKE:
                    newObj  = new Stroke(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                    mPreviouslyMadeObject = dh.getCurrentSelectedDrawable();
                    dh.setCurrentSelectedDrawable(newObj);
                    dh.addDrawable(newObj);
                    mCurrentObject = STROKE;
                    break;
                case STROKE_POINTS:
                    newObj = new StrokePoints(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                    mPreviouslyMadeObject = dh.getCurrentSelectedDrawable();
                    dh.setCurrentSelectedDrawable(newObj);
                    dh.addDrawable(newObj);
                    mCurrentObject = STROKE;
                    break;
            }
        }

        mLastCommand = DOWN;
    }

    public void onDoubleTap(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = DOUBLE_TAP;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];

        Log.d(TAG, "onDoubleTap detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dh.getIntersectingDrawable(x,y);
        if(drawable != null) {
            if(mPreviouslyMadeObject != null) {
                dh.removeDrawable(mPreviouslyMadeObject);
                mPreviouslyMadeObject = null;
            }

            dh.removeDrawable(drawable);
            dh.clearCurrentSelectedDrawable();
        }else{
            dh.removeDrawable(mPreviouslyMadeObject);
            mPreviouslyMadeObject = null;
            dh.clearCurrentSelectedDrawable();
        }
    }

    public void onDoubleTapEvent(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = DOUBLE_TAP;

        Log.d(TAG, "onDoubleTapEvent detected");
    }

    public void onSingleTapConfirmed(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        Log.d(TAG, "onSingleTapConfirmed detected");
    }

}
