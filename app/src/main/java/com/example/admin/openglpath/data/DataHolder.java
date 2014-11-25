package com.example.admin.openglpath.data;

import android.opengl.GLSurfaceView;

import com.example.admin.openglpath.loopers.Animation;
import com.example.admin.openglpath.drawables.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is just to hold data until we design a better data structure
 *
 * Created by Mark Stanford on 11/20/14.
 */
public class DataHolder {

    private static final String TAG = "DataHolder";

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
     * Map for drawables and the runnable animating their fling.
     */
    private Map<Drawable, Animation> mAnimationMap;


    /**
     * Private constructor for our singleton pattern
     */
    private DataHolder(){
        mDrawableList = new ArrayList<Drawable>();
        mAnimationMap =  new HashMap<Drawable, Animation>();
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

    public List<Drawable> getDrawableList() {
        return mDrawableList;
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

    public Map<Drawable, Animation> getAnimationMap() {
        return mAnimationMap;
    }
}
