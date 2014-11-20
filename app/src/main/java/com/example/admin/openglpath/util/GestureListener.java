package com.example.admin.openglpath.util;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Card;

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
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        DataHolder dh = DataHolder.getInstance();
        //TODO:Check to see if we have a currently selected object.  If so, then change it's x,y.
        if(dh.isCurrentlySelectedDrawable()){
            //We have a currently selected drawable. We need to change it's x,y;
            float x, y;
            float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e2.getX(), e2.getY(), 2);
            x = scaled[0];
            y = scaled[1];
            DataHolder.getInstance().getCurrentSelectedDrawable().setXY(x,y);
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        //TODO: Check to see if we intersect an object here.  If not then create one.
        DataHolder dh = DataHolder.getInstance();
        float x, y;
        float scaled[] = ViewUtils.scaleTouchEvent(dh.getWorkspaceView(), e.getX(), e.getY(), 2);
        x = scaled[0];
        y = scaled[1];
        Card newCard = new Card(x, y, dh.getCurrentSelectedColor());
        dh.addDrawable(newCard);
        Log.d(TAG, "onTouch detected: " + x + ":" + y);
        dh.setCurrentSelectedDrawable(newCard);

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
