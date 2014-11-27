package com.example.admin.openglpath.gestures;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.data.DrawableStateManager;
import com.example.admin.openglpath.drawables.Drawable;
import com.example.admin.openglpath.drawables.DrawableFactory;
import com.example.admin.openglpath.drawables.DrawableType;
import com.example.admin.openglpath.loopers.FlingAnimation;
import com.example.admin.openglpath.models.HistoryEntry;
import com.example.admin.openglpath.views.ViewUtils;

import java.util.Random;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class GestureHandler {

    public static final String TAG = "GestureHandler";

    //Dataholder
    DataHolder dh = DataHolder.getInstance();

    //Drawable state manager
    DrawableStateManager dsm = DrawableStateManager.getInstance();

    //Drawable factory
    DrawableFactory df = DrawableFactory.getInstance();

    //The previously made object in case we have a double tap or long
    Drawable mPreviouslyMadeObject;

    //Maintian state of strokes
    private int mCurrentObject          = -1;

    //Maintain state of gestures
    private int mLastCommand            = NONE;

    /**
     * Drawable currently selected or last made
     */
    public static final int NO_SHAPE           = 3;
    public static final int CARD               = 4;
    public static final int STROKE             = 5;

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
        dsm.clearCurrentSelectedDrawable();

        //Do some math to figure out how we are scaling TODO: make a helper to scale this to the screen and zoom.
        float previous = detector.getPreviousSpan();
        float current = detector.getCurrentSpan();
        float difference = (previous/current);
        float zoom = dh.getZoom();

        Log.d(TAG, "ScaleDifference: " + difference);

        dh.addToZoom(difference);

        dh.getRenderer().changeProjection(dh.getZoom());

        Log.d(TAG, "onScale");
    }

    public void onScaleBegin(ScaleGestureDetector detector) {

        //Delete the created object from the onDown
        if(mLastCommand == DOWN) {
            dh.removeDrawable(mPreviouslyMadeObject);
            dsm.clearCurrentSelectedDrawable();
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

        dsm.clearCurrentSelectedDrawable();
        Log.d(TAG, "onSingleTapUp detected");
    }

    public void onLongPress(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY());
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onLongPress detected: " + x + ":" + y);

        //For debugging purposes
        dh.getDrawableList().clear();
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY());
        x = scaled[0];
        y = scaled[1];

        //Check to see if we have an up gesture
        if(e2.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onScroll action up == " + (e2.getAction() == MotionEvent.ACTION_UP));
            mLastCommand = NONE;
        }

        if(dsm.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            Log.d(TAG, "onScroll detected: " + x + ":" + y);
            dsm.getCurrentSelectedDrawable().setXYZ(x, y, 1);
            //We need to add this action to the history stack
            if(mLastCommand != SCROLL && mCurrentObject != STROKE){
                //Create new entry
                dsm.addToHistoryStack(new HistoryEntry(HistoryEntry.HistoryAction.Move,dsm.getCurrentSelectedDrawable(), null));
                dsm.getCurrentSelectedDrawable().setXYZ(x, y, 1);
            }else if(mLastCommand == SCROLL && mCurrentObject != STROKE){
                //Update current entry
                dsm.getCurrentSelectedDrawable().setXYZ(x, y, 1);
                dsm.updateLatestHistoryEntry((new HistoryEntry(HistoryEntry.HistoryAction.Move, dsm.getCurrentSelectedDrawable(), null)));
            }
            //Update the state of commands
            mLastCommand = SCROLL;
        }else{
            //Change the location of the viewport
            Log.d(TAG, "Viewport scroll detected: " + x + ":" + y);
            dh.getRenderer().changeViewport((int)x,(int)y);
        }
    }

    public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = FLING;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY());
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onFling detected: " + x + ":" + y);

        if(dsm.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            dsm.getCurrentSelectedDrawable().setXYZ(x, y , 1);
        }

        //Check to see if we have an up gesture and then calculate the fling
        if(e2.getAction() == MotionEvent.ACTION_UP){
            //Make sure we have a drawable under our touch
            if(dsm.isCurrentlySelectedDrawable()){
                //Fling the object!
                FlingAnimation fling = new FlingAnimation(dsm.getCurrentSelectedDrawable(), x, y, velocityX, velocityY);
                dh.getAnimationMap().put(dsm.getCurrentSelectedDrawable(), fling);
                dsm.clearCurrentSelectedDrawable();
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

        if(mLastCommand == DOUBLE_TAP) {
            mLastCommand = DOWN;
            return;
        }

        //Maintain handler state
        mLastCommand = DOWN;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY());
        x = scaled[0];
        y = scaled[1];
        Log.d(TAG, "onDown detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dsm.getIntersectingDrawable(x, y);
        if (drawable != null) {
            dsm.setCurrentSelectedDrawable(drawable);

            //Check to see if we are flinging.  If we are then we need to kill it.
            if(dh.getAnimationMap().containsKey(drawable)){
                dh.getAnimationMap().get(drawable).stop();
            }
        } else if(dsm.getCurrentShapeToDraw() != NO_SHAPE) {
            //Draw the shape
            Drawable newObj;
            switch (dsm.getCurrentShapeToDraw()) {
                case CARD:
                    df.createDrawable(DrawableType.Card, x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                    mPreviouslyMadeObject = dsm.getCurrentSelectedDrawable();
                    mCurrentObject = CARD;
                    break;
                case STROKE:
                    df.createDrawable(DrawableType.Stroke, x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                    mPreviouslyMadeObject = dsm.getCurrentSelectedDrawable();
                    mCurrentObject = STROKE;
                    break;
                default:
                    mCurrentObject = NO_SHAPE;
                    break;
            }
        }else{
            dsm.setCurrentSelectedDrawable(null);
            mCurrentObject = NO_SHAPE;
        }
    }

    public void onDoubleTap(MotionEvent e) {
        //Disregard if scale is happening
        if(mLastCommand == SCALE_START)
            return;

        mLastCommand = DOUBLE_TAP;

        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY());
        x = scaled[0];
        y = scaled[1];

        Log.d(TAG, "onDoubleTap detected: " + x + ":" + y);

        //Check to see if we intersect an object here and then delete it
        Drawable drawable = dsm.getIntersectingDrawable(x,y);
        if(drawable != null) {
            if(mPreviouslyMadeObject != null) {
                dh.removeDrawable(mPreviouslyMadeObject);
                mPreviouslyMadeObject = null;
            }
            dh.removeDrawable(drawable);
            dsm.clearCurrentSelectedDrawable();
            //Add this action to the history stack
            dsm.addToHistoryStack(new HistoryEntry(HistoryEntry.HistoryAction.Delete, null, drawable));
        }else{ // Else make sure we delete the object the first down made TODO: this is for debugging and we will need new logic when we allow down presses to scroll the workspace and not only make objects
            if(mPreviouslyMadeObject != null) {
                dh.removeDrawable(mPreviouslyMadeObject);
                mPreviouslyMadeObject = null;
            }
            dsm.clearCurrentSelectedDrawable();
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
