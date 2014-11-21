package com.example.admin.openglpath.gestures;

import android.content.Context;
import android.view.ScaleGestureDetector;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private static final String TAG = "ScaleListener";
    Context mContext;

    public ScaleListener(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        GestureHandler.getInstance().onScale(detector);
        return super.onScale(detector);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        GestureHandler.getInstance().onScaleBegin(detector);
        return super.onScaleBegin(detector);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        GestureHandler.getInstance().onScaleEnd(detector);
        super.onScaleEnd(detector);
    }
}
