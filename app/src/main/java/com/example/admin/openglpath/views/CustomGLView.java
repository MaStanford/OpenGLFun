package com.example.admin.openglpath.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.admin.openglpath.R;
import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.data.DrawableStateManager;
import com.example.admin.openglpath.gestures.GestureListener;
import com.example.admin.openglpath.gestures.ScaleListener;

public class CustomGLView extends GLSurfaceView implements View.OnTouchListener{
    public static final String TAG = "CustomGLView";

    //Attr from xml
    int mBackGroundColor;
    int mShapeColor;
    boolean mIsFullScreen;
    int mSize;

    //Is renderer set
    private boolean rendererSet = false;

    //Renderer, we need a reference to set the data structure
    private com.example.admin.openglpath.renderers.Renderer mRenderer;

    //Gesture detector for detecting stuffs
    final GestureDetector mGestureDetector;

    //Gesture detector for scaling motions
    private ScaleGestureDetector mScaleDetector;

    private int fingerDown = 0;

    public CustomGLView(Context context) {
        super(context);
        //Need to set colors first
        mBackGroundColor = Color.DKGRAY;
        mShapeColor = Color.CYAN;
        mSize = 24;

        //Init gdt
        mGestureDetector = new GestureDetector(getContext(), new GestureListener(context));

        //Add ourselves to the dataholder
        DataHolder.getInstance().setWorkspaceView(this);

        setupOpenGL();
    }

    public CustomGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Get the attr from the xml
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomGLView, 0, 0);
        try {
            mBackGroundColor = a.getColor(R.styleable.CustomGLView_color_bg, Color.DKGRAY);
            mIsFullScreen = a.getBoolean(R.styleable.CustomGLView_full_screen, false);
            mSize = a.getInteger(R.styleable.CustomGLView_size, 24);
            mShapeColor = a.getColor(R.styleable.CustomGLView_color_shape, Color.BLUE);
        }finally{
            a.recycle();
        }

        //Init gdt
        mGestureDetector = new GestureDetector(getContext(), new GestureListener(context));

        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener(context));

        //Add ourselves to the dataholder
        DataHolder.getInstance().setWorkspaceView(this);

        DrawableStateManager.getInstance().setCurrentSelectedColor(mShapeColor);

        setupOpenGL();
    }

    private void setupOpenGL(){

        //Set the context
        setEGLContextClientVersion(2);

        // Assign our renderer.
        setFocusableInTouchMode(true);

        //Initialize the renderer
        mRenderer = new com.example.admin.openglpath.renderers.Renderer(this.getContext(), mBackGroundColor);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //Let use interact with view
        setFocusable(true);

        //Maintain the state of the class
        rendererSet = true;

        //Set the onTouchListener
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            Log.d(TAG, "MotionEvent.ACTION_POINTER_DOWN");
            fingerDown++;
        }

        if(motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            Log.d(TAG, "MotionEvent.ACTION_POINTER_UP");
            fingerDown--;
        }

        if(motionEvent.getPointerCount() >= 2) {
            //Check to see if we are scaling
            mScaleDetector.onTouchEvent(motionEvent);
        }


        if(motionEvent.getPointerCount() < 2) {
            //Send the event out to the gesture detector
            mGestureDetector.onTouchEvent(motionEvent);
        }

        return true;
    }
}
