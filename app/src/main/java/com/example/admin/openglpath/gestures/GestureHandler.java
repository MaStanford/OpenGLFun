package com.example.admin.openglpath.gestures;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.loopers.FlingAnimation;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.shapes.Stroke;
import com.example.admin.openglpath.util.ViewUtils;

import java.util.Random;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class GestureHandler {

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

    public static final String TAG = "GestureHandler";

    //Dataholder
    DataHolder dh = DataHolder.getInstance();

    //The previously made object in case we have a double tap or long
    Drawable mPreviouslyMadeObject;

    //How many fingers are down right now
    private boolean isScaleGesture = false;

    /**
     * Weird issue where onDown gets called after onDoubleTap
     */
    boolean isDoubleLastCommand = false;

    //Fix the onDown creating when scaling
    private boolean isDownLastCommand = false;

    //Maintian state of strokes
    private boolean isCurrentObjectStroke = false;

    public void onScale(ScaleGestureDetector detector) {
        dh.clearCurrentSelectedDrawable();
        Log.d(TAG, "onScale");
    }

    public void onScaleBegin(ScaleGestureDetector detector) {
        isScaleGesture = true;

        //Delete the created object from the onDown
        if(isDownLastCommand) {
            dh.removeDrawable(mPreviouslyMadeObject);
            dh.clearCurrentSelectedDrawable();
            mPreviouslyMadeObject = null;
            isDownLastCommand = false;
        }

        Log.d(TAG, "onScaleBegin");
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        isScaleGesture = false;
        Log.d(TAG, "onScaleEnd");
    }

    public void onSingleTapUp(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        DataHolder dh = DataHolder.getInstance();
        dh.clearCurrentSelectedDrawable();
        Log.d(TAG, "onSingleTapUp detected");
    }

    public void onLongPress(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onLongPress detected: " + x + ":" + y);

        dh.getDrawableList().clear();
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        Log.d(TAG, "onScroll called");

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable() && e2.getAction() != MotionEvent.ACTION_UP){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            Log.d(TAG, "onScroll detected: " + x + ":" + y);
            DataHolder.getInstance().getCurrentSelectedDrawable().setXYZ(x, y, 1);
        }else{
            //We may be able to test drawing lines here.
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            if(isCurrentObjectStroke){
                ((Stroke)dh.getCurrentSelectedDrawable()).addPoint(x,y,0);
                isCurrentObjectStroke = true;
            }else{
                Stroke stroke = new Stroke(x,y,0,new Random().nextInt(Integer.MAX_VALUE));
                dh.setCurrentSelectedDrawable(stroke);
                dh.addDrawable(stroke);
                mPreviouslyMadeObject = stroke;
            }
        }

        //Check to see if we have an up gesture
        if(e2.getAction() == MotionEvent.ACTION_UP){
            Log.d(TAG, "onScroll action up == " + (e2.getAction() == MotionEvent.ACTION_UP));
            dh.clearCurrentSelectedDrawable();
            isCurrentObjectStroke = false;
        }
    }

    public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;


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
                float x, y;
                float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
                x = scaled[0];
                y = scaled[1];
                Log.d(TAG, String.format("onFlingUp X:Y %f:%f velX:velY %f:%f", x,y, velocityX, velocityY));
                //Fling the object!
                FlingAnimation fling = new FlingAnimation(dh.getCurrentSelectedDrawable(), x, y, velocityX, velocityY);
                dh.getAnimationMap().put(dh.getCurrentSelectedDrawable(), fling);
                dh.clearCurrentSelectedDrawable();
            }
        }
    }

    public void onShowPress(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        Log.d(TAG, "onShowPress detected!");
    }

    public void onDown(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        isDownLastCommand = true;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onDown detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dh.getIntersectingDrawable(x, y);
        if (drawable != null) {
            dh.setCurrentSelectedDrawable(drawable);
            mPreviouslyMadeObject = drawable;

            //Check to see if we are flinging.  If we are then we need to kill it.
            if(dh.getAnimationMap().containsKey(drawable)){
                dh.getAnimationMap().get(drawable).stop();
            }

        } else {
//            Card newCard = new Card(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
//            dh.setCurrentSelectedDrawable(newCard);
//            dh.addDrawable(newCard);
//            mPreviouslyMadeObject = newCard;
        }

        isDownLastCommand = true;
        isDoubleLastCommand = false;
    }

    public void onDoubleTap(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        isDoubleLastCommand = true;

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
        if(isScaleGesture)
            return;

        Log.d(TAG, "onDoubleTapEvent detected");
    }

    public void onSingleTapConfirmed(MotionEvent e) {
        //Disregard if scale is happening
        if(isScaleGesture)
            return;

        Log.d(TAG, "onSingleTapConfirmed detected");
    }

}
