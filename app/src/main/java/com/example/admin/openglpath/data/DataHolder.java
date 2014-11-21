package com.example.admin.openglpath.data;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.admin.openglpath.loopers.FlingRunnable;
import com.example.admin.openglpath.shapes.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * This is just to hold data until we design a better data structure
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class DataHolder {

    private static final String TAG = "DataHolder";
    /**
     * This is currently selected drawable on the screen.
     *
     * The idea here is that this is a reference to the same object in the drawable data
     *    structure that the renderer uses to draw shapes.
     * So do not copy this before mutating it.
     */
    private Drawable mCurrentSelectedDrawable = null;

    /**
     * Holds a list of the drawables to draw to the openGL surfaceView
     */
    private List<Drawable> mDrawableList = null;

    /**
     * The surfaceView.
     *
     * Make sure we have a reference to this.
     *
     * Maintain this state on surface changes etc.
     */
    private GLSurfaceView mWorkspaceView = null;

    /**
     * The color the user currently has selected for drawing.
     */
    private int mCurrentSelectedColor = 0;

    /**
     * Map for drawables and the runnable animating their fling.
     */
    private Map<Drawable, FlingRunnable> mFlingMap;
    //Supporting structure for the flingrunnables
    private Queue<FlingRunnable> mFlingQueue;

    /**
     * Private constructor for our singleton pattern
     */
    private DataHolder(){
        mDrawableList = new ArrayList<Drawable>();
        mFlingMap =  new HashMap<Drawable, FlingRunnable>();
        mFlingQueue =  new LinkedList<FlingRunnable>();

        mCurrentSelectedColor = 255; //Blue
    }

    /**
     * Instance holder for our safe lazy instantiation pattern
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class instanceHolder{
        private static final DataHolder INSTANCE = new DataHolder();
    }

    /**
     * Returns the singleton
     * @return
     */
    public static DataHolder getInstance(){
        return instanceHolder.INSTANCE;
    }

    public Drawable getCurrentSelectedDrawable() {
        return mCurrentSelectedDrawable;
    }

    /**
     * This is the currently selected drawable.
     * This will be mutated when the user selects, drags etc
     *
     * @param mCurrentSelectedDrawable
     */
    public void setCurrentSelectedDrawable(Drawable mCurrentSelectedDrawable) {
        this.mCurrentSelectedDrawable = mCurrentSelectedDrawable;
    }

    /**
     * Clears the currently selected drawable
     */
    public void clearCurrentSelectedDrawable(){
        this.mCurrentSelectedDrawable = null;
    }

    /**
     * Checks to see if there is a currently selected drawable
     *
     * @return true is there is a drawable that is selected.
     */
    public boolean isCurrentlySelectedDrawable(){
        return this.mCurrentSelectedDrawable != null;
    }

    public List<Drawable> getDrawableList() {
        return mDrawableList;
    }

    public void setDrawableList(List<Drawable> mDrawableList) {
        this.mDrawableList = mDrawableList;
    }

    public void addDrawable(Drawable drawable){
        this.mDrawableList.add(drawable);
    }

    public void removeDrawable(Drawable drawable){
        this.mDrawableList.remove(drawable);
    }

    public GLSurfaceView getWorkspaceView() {
        return mWorkspaceView;
    }

    public void setWorkspaceView(GLSurfaceView mWorkspaceView) {
        this.mWorkspaceView = mWorkspaceView;
    }

    public int getCurrentSelectedColor() {
        return mCurrentSelectedColor;
    }

    public void setCurrentSelectedColor(int mCurrentSelectedColor) {
        this.mCurrentSelectedColor = mCurrentSelectedColor;
    }

    /**
     * Returns the drawable that intersects that x,y.
     * If multiple intersect then it selects the one with the highest Z value;
     *
     * @param x
     * @param y
     * @return Top visible drawable or null if none
     */
    public Drawable getIntersectingDrawable(float x, float y) {
        Drawable currentTopDrawable = null;
        float currentHighestZValue = 0;

        //Iterate through the list
        for(Drawable drawable : this.mDrawableList){
            //Check to see if the drawale intersects and if the Z value is good.
            float zValue = drawable.doesIntersectXY(x,y);
            if(zValue > currentHighestZValue){
                currentHighestZValue = zValue;
                currentTopDrawable = drawable;
            }
        }

        Log.d(TAG, "getIntersectingDrawable == " + (currentTopDrawable != null));

        return currentTopDrawable;
    }

    public Map<Drawable, FlingRunnable> getFlingMap() {
        return mFlingMap;
    }

    public void setFlingMap(Map<Drawable, FlingRunnable> mFlingMap) {
        this.mFlingMap = mFlingMap;
    }

    public Queue<FlingRunnable> getFlingQueue() {
        return mFlingQueue;
    }

    public void setFlingQueue(Queue<FlingRunnable> mFlingQueue) {
        this.mFlingQueue = mFlingQueue;
    }
}
