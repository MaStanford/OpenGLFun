package com.example.admin.openglpath.drawables;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.data.DrawableStateManager;
import com.example.admin.openglpath.models.HistoryEntry;

import java.util.Random;

/**
 * This will create and add drawables to the drawable data structure.
 *
 * Created by Mark Stanford on 11/25/14.
 */
public class DrawableFactory {

    DataHolder dh;
    DrawableStateManager dsm;

    /**
     * Private constructor for our singleton pattern
     */
    private DrawableFactory(){
        dh = DataHolder.getInstance();
        dsm = DrawableStateManager.getInstance();
    }

    /**
     * Instance holder for our safe lazy instantiation pattern
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class instanceHolder{
        private static final DrawableFactory INSTANCE = new DrawableFactory();
    }

    /**
     * Returns the singleton
     * @return The GestureHandler singleton reference
     */
    public static DrawableFactory getInstance(){
        return instanceHolder.INSTANCE;
    }


    public void createDrawable(DrawableType type, float x, float y, float z, int color){
        Drawable newObj;
        switch(type){
            case Card:
                newObj = new Card(x, y, 1, new Random().nextInt(Integer.MAX_VALUE));
                dsm.setCurrentSelectedDrawable(newObj);
                dh.addDrawable(newObj);
                //Add this action to history stack
                dsm.addToHistoryStack(new HistoryEntry(HistoryEntry.HistoryAction.Create, newObj, null));
                break;
            case Stroke:
                newObj  = new StrokeLines(x, y, 1, dsm.getCurrentLineWidth(), color);
                dsm.setCurrentSelectedDrawable(newObj);
                dh.addDrawable(newObj);
                //Add this action to history stack
                dsm.addToHistoryStack(new HistoryEntry(HistoryEntry.HistoryAction.Create, newObj, null));
                break;
            case Point:
        }
    }
}
