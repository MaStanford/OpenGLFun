package com.example.admin.openglpath.shapes;

import com.example.admin.openglpath.util.ShaderType;

import java.nio.FloatBuffer;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public abstract class Drawable {

    //The size of the shape
    protected float mSize = .2f;

    //The shader type for this shape
    protected ShaderType mShaderType;

    //The shader program
    protected int mProgram;

    //The vertex buffer to pass along to openGL
    protected FloatBuffer vertexBuffer;

    //The X,Y,Z of the shape
    protected float x,y,z;

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

    public Drawable(float x, float y, float z){
        setXYZ(x,y,z);
    }

    abstract public void draw();

    /**
     * Sets the X and Y of the shape
     * @param x
     * @param y
     */
    public void setXYZ(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        generateNewVertices(x,y,z);
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     * @param x
     * @param y
     */
    abstract protected void generateNewVertices(float x, float y, float z);

    public float[] getmColor() {
        return mColor;
    }

    public void setmColor(float[] mColor) {
        this.mColor = mColor;
    }

    /**
     * Check to see if object intersects the x,y.
     * Returns the Z value if it does otherwise returns 0.
     *
     * @param x
     * @param y
     * @return The Z value if it intersects otherwise returns 0 if no intersection.
     */
    abstract public float doesIntersectXY(float x, float y);

}
