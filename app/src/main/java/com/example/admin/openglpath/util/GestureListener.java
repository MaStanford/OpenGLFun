package com.example.admin.openglpath.util;

import android.content.Context;
import android.view.MotionEvent;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Card;
import com.example.admin.openglpath.shapes.Drawable;

/**
 * Use this to create or modify object, select objects or w/e
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    public static final String TAG = "GestureListener";

    Context mContext;

    public GestureListener(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
//        Log.d(TAG, "onSingleTapUp detected: " + x + ":" + y);
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
//        Log.d(TAG, "onLongPress detected: " + x + ":" + y);
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //TODO:Check to see if we have a currently selected object.  If so, then change it's x,y according to the scroll.

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
//            Log.d(TAG, "onScroll detected: " + x + ":" + y);
            DataHolder.getInstance().getCurrentSelectedDrawable().setXYZ(x,y,1);
        }

        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //TODO:Check to see if we have a currently selected object.  If so, then change it's x,y according to the fling.

        DataHolder dh = DataHolder.getInstance();
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
//            Log.d(TAG, "onFling detected: " + x + ":" + y);
            DataHolder.getInstance().getCurrentSelectedDrawable().setXYZ(x,y,1);
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
//        Log.d(TAG, "onTouch detected: " + x + ":" + y);

        //Check to see if we intersect an object here.  If not then create one.
        Drawable drawable = dh.getIntersectingDrawable(x,y);
        if(drawable != null) {
            drawable.setXYZ(x,y,1);
            dh.setCurrentSelectedDrawable(drawable);
        }else{ //TODO: we need to do a check to see what we are supposed to draw here
            Card newCard = new Card(x, y, 1, dh.getCurrentSelectedColor());
            dh.setCurrentSelectedDrawable(newCard);
            dh.addDrawable(newCard);
        }


        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
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
