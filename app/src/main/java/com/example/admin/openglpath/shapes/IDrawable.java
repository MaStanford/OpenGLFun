package com.example.admin.openglpath.shapes;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public interface IDrawable {

    //How many bytes in a float
    static int BYTES_FLOAT = 4;

    abstract void draw();

    abstract void setXYZ(float x, float y, float z);

    abstract float[] getXYZ();

    abstract void setColor(float[] color);

    abstract float doesIntersectXY(float x, float y);

    abstract void setCoordsPerVertex(int coords);
}
