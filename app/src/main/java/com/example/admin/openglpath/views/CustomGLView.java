package com.example.admin.openglpath.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.admin.openglpath.R;
import com.example.admin.openglpath.shapes.Card;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomGLView extends GLSurfaceView {
    public static final String TAG = "CustomGLView";

    //Attr from xml
    int mColor;
    boolean mIsFullScreen;
    int mSize;

    //Is renderer set
    private boolean rendererSet = false;

    //Square to draw
    private static List<Drawable> squareList;

    //Renderer, we need a reference to set the data structure
    private com.example.admin.openglpath.renderers.Renderer mRenderer;

    public CustomGLView(Context context) {
        super(context);
        //Need to set colors first
        mColor = Color.DKGRAY;
        mSize = 24;

        setupOpenGL();
    }

    public CustomGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Get the attr from the xml
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomGLView, 0, 0);
        try {
            mColor = a.getColor(R.styleable.CustomGLView_color, Color.DKGRAY);
            mIsFullScreen = a.getBoolean(R.styleable.CustomGLView_full_screen, false);
            mSize = a.getInteger(R.styleable.CustomGLView_size, 24);
        }finally{
            a.recycle();
        }

        setupOpenGL();
    }

    private void setupOpenGL(){

        //Set up data structure
        squareList =  new ArrayList<Drawable>();

        //Set the context
        setEGLContextClientVersion(2);

        // Assign our renderer.
        setFocusableInTouchMode(true);

        //Initialize the renderer
        mRenderer = new com.example.admin.openglpath.renderers.Renderer(mColor , new ArrayList<Drawable>());

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //Let use interact with view
        setFocusable(true);

        //Maintain the state of the class
        rendererSet = true;

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x, y;
                float scaled[] = ViewUtils.scaleTouchEvent(view, motionEvent.getX(), motionEvent.getY(), 2);
                x = scaled[0];
                y = scaled[1];
                mRenderer.addShape(new Card(x, y));
                Log.d(TAG, "onTouch detected: " + x + ":" + y);
                return false;
            }
        });

        setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                Log.d(TAG, "onDrag detected: " + dragEvent.getX() + ":" + dragEvent.getY());
                return false;
            }
        });
    }
}
