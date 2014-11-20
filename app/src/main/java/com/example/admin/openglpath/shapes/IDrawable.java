package com.example.admin.openglpath.shapes;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public interface IDrawable {

    abstract void draw();

    abstract void setXYZ(float x, float y, float z);

    abstract void setColor(float[] color);

    abstract float doesIntersectXY(float x, float y);
}
