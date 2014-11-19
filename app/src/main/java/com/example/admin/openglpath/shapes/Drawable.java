package com.example.admin.openglpath.shapes;

import android.util.Log;

import com.example.admin.openglpath.util.ShaderType;

import java.nio.FloatBuffer;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public abstract class Drawable {

    //The size of the shape
    protected float mSize = .1f;

    //The shader type for this shape
    protected ShaderType mShaderType;

    //The shader program
    protected int mProgram;

    //The vertex buffer to pass along to openGL
    protected FloatBuffer vertexBuffer;

    //The coordinates of the drawable
    protected float[] shapeCoords;

    // Set mColor with red, green, blue and alpha (opacity) values
    protected float mColor[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    //handle to vertex shader's vPosition member
    protected int mPositionHandle;

    //How many bytes for each vertex
    protected int vertexStride = COORDS_PER_VERTEX * 4; //4 are how many bytes in a float

    //handle to fragment shader's vColor member
    protected int mColorHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    public void draw(){
        //Do work here
        Log.d("Drawable Parent", "DRAW CALLED!!! THIS IS BAD!!!");
    };

    /**
     * Sets the X and Y of the shape
     * @param x
     * @param y
     */
    public void setXY(float x, float y){
        generateNewVertices(x,y);
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     * @param x
     * @param y
     */
    protected void generateNewVertices(float x, float y){

    }

    public float[] getmColor() {
        return mColor;
    }

    public void setmColor(float[] mColor) {
        this.mColor = mColor;
    }
}
