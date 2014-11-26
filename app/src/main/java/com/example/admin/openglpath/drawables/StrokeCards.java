package com.example.admin.openglpath.drawables;

import java.util.ArrayList;
import java.util.List;

/**
 * Stroke using CARDS to draw
 *
 * Created by Mark Stanford on 11/21/14.
 */
public class StrokeCards extends Drawable{

    private static final String TAG = "Stroke";

    private List<Float> strokePoints;

    private List<Card> shapes;

    private int shapesColor;

    public StrokeCards(float x, float y, float z, int color) {

        this.shapesColor = color;
        strokePoints =  new ArrayList<Float>();
        strokePoints.add(x);
        strokePoints.add(y);
        strokePoints.add(z);

        shapes = new ArrayList<Card>();
        shapes.add(new Card(x,y,z,color));
    }

    @Override
    public void draw(float[] matrix) {
        //generateNewVertices(0,0,0);
        for(int i = 0; i < shapes.size(); i++){
            shapes.get(i).draw(matrix);
        }

    }

    /**
     * Generates the new vertices of the shape based on the strokePoints data structure
     *
     * @param x
     * @param y
     */
    @Override
    protected void generateNewVertices(float x, float y, float z) {
        shapes.clear();
        for(int i = 0; i < strokePoints.size() - 3; i += 3){
            shapes.add(new Card(strokePoints.get(i),strokePoints.get(i+1),strokePoints.get(i+2),this.shapesColor));
        }
    }

    @Override
    public float doesIntersectXY(float x, float y) {
        //TODO: Figure this out
        return -1;
    }

    @Override
    public void setXYZ(float x, float y, float z) {
        strokePoints.add(x);
        strokePoints.add(y);
        strokePoints.add(z);
        shapes.add(new Card(x,y,z,this.shapesColor));
    }

    private static float[] convertFloats(List<Float> floats){
        float[] ret = new float[floats.size()];
        for (int i=0; i < ret.length; i++){
            ret[i] = floats.get(i).floatValue();
        }
        return ret;
    }

}
