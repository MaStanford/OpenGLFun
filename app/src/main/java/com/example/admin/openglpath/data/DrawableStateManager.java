package com.example.admin.openglpath.data;

import android.util.Log;

import com.example.admin.openglpath.gestures.GestureHandler;
import com.example.admin.openglpath.models.HistoryEntry;
import com.example.admin.openglpath.shapes.Drawable;

import java.util.Stack;

/**
 * Maintains the state of our drawables.
 * This should hold the colors, sizes, selected, history etc.
 *
 * Created by Mark Stanford on 11/24/14.
 */
public class DrawableStateManager {

    private static final String TAG = "DrawableStateManager";

    DataHolder dh = DataHolder.getInstance();

    /**
     * This is currently selected drawable on the screen.
     *
     * The idea here is that this is a reference to the same object in the drawable data
     *    structure that the renderer uses to draw shapes.
     * So do not copy this before mutating it.
     */
    private Drawable mCurrentSelectedDrawable = null;

    /**
     * The current line width for strokes
     */
    private float mCurrentLineWidth = 2.0f;

    /**
     * Our history object.  When you make a change to an object save the original here.
     * That way we can recall objects before changes.
     */
    private Stack<HistoryEntry> mHistory;

    /**
     * Current drawable the user has selected to draw
     */
    private int mCurrentShapeToDraw = GestureHandler.CARD;

    /**
     * The color the user currently has selected for drawing.
     */
    private int mCurrentSelectedColor = 0;

    /**
     * Private constructor for our singleton pattern
     */
    private DrawableStateManager(){
        mHistory = new Stack<HistoryEntry>();
        mCurrentSelectedColor = 255; //Blue
    }

    /**
     * Instance holder for our safe lazy instantiation pattern
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class instanceHolder{
        private static final DrawableStateManager INSTANCE = new DrawableStateManager();
    }

    /**
     * Returns the singleton
     * @return
     */
    public static DrawableStateManager getInstance(){
        return instanceHolder.INSTANCE;
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
        for(Drawable drawable : dh.getDrawableList()){
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

    public float getCurrentLineWidth() {
        return mCurrentLineWidth;
    }

    public void setCurrentLineWidth(float lineWidth){
        this.mCurrentLineWidth = lineWidth;
    }

    public int getCurrentSelectedColor() {
        return mCurrentSelectedColor;
    }

    public void setCurrentSelectedColor(int mCurrentSelectedColor) {
        this.mCurrentSelectedColor = mCurrentSelectedColor;
    }

    public int getCurrentShapeToDraw() {
        return mCurrentShapeToDraw;
    }

    public void setCurrentShapeToDraw(int mCurrentShapeToDraw) {
        this.mCurrentShapeToDraw = mCurrentShapeToDraw;
    }

    /**
     * adds the currently created object to the stack
     * @param history
     */
    public void addToHistoryStack(HistoryEntry history){
        Log.d(TAG, "addToHistoryStack action:" + history.getAction());
        switch(history.getAction()){
            case Create:
                mHistory.add(history);
                break;
            case Move:
                history.setPreviousXYZ(history.getCurrentState().getXYZ());
                mHistory.add(history);
                break;
            case Delete:
                mHistory.add(history);
                break;
        }
    }

    /**
     * Pops the current object from the history and then sets the one before.
     */
    public void popFromHistoryStack(){
        if(!mHistory.empty()) {
            //Stop animating this object
            dh.getAnimationMap().remove(mHistory.peek().getCurrentState());
            switch(mHistory.peek().getAction()){
                case Create:
                    dh.removeDrawable(mHistory.pop().getCurrentState());
                    break;
                case Move:
                    float[] prev = mHistory.peek().getPreviousXYZ();
                    float[] curr = mHistory.peek().getCurrentState().getXYZ();
                    Log.d(TAG, String.format("previous xyz %f:%f:%f current xyz : %f:%f:%f", prev[0],prev[1],prev[2],curr[0],curr[1],curr[2]));
                    mHistory.peek().getCurrentState().setXYZ(mHistory.pop().getPreviousXYZ());
                    break;
                case Delete:
                    dh.addDrawable(mHistory.pop().getPreviousState());
                    break;
            }
        }
    }

    /**
     * This is for long running actions like drawing or dragging.
     *
     * @param history
     */
    public void updateLatestHistoryEntry(HistoryEntry history){
        //we may need some logic here. We may not need logic here.
        //The reference to the object is the same in the history queue and the state manager
    }
}
