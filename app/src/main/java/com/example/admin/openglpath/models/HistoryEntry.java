package com.example.admin.openglpath.models;

import com.example.admin.openglpath.shapes.Drawable;

/**
 * Created by Mark Stanford on 11/24/14.
 */
public class HistoryEntry {
    public static enum HistoryAction {
        Create,
        Move,
        Delete
    }

    HistoryAction mAction;
    Drawable mCurrentState;
    Drawable mPreviousState;
    float[] mPreviousXYZ;

    public HistoryEntry(HistoryAction mAction, Drawable mCurrentState, Drawable mPreviousState) {
        this.mAction = mAction;
        this.mCurrentState = mCurrentState;
        this.mPreviousState = mPreviousState;
    }

    public HistoryAction getAction() {
        return mAction;
    }

    public void setAction(HistoryAction mAction) {
        this.mAction = mAction;
    }

    public Drawable getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(Drawable mCurrentState) {
        this.mCurrentState = mCurrentState;
    }

    public Drawable getPreviousState() {
        return mPreviousState;
    }

    public void setPreviousState(Drawable mPreviousState) {
        this.mPreviousState = mPreviousState;
    }

    public float[] getPreviousXYZ() {
        return mPreviousXYZ;
    }

    public void setPreviousXYZ(float[] mPreviousXYZ) {
        this.mPreviousXYZ = mPreviousXYZ;
    }
}
