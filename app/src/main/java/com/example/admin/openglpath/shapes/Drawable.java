package com.example.admin.openglpath.shapes;

import com.example.admin.openglpath.models.ShaderType;

import java.nio.FloatBuffer;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public abstract class Drawable implements IDrawable{


    //The size of the shape
    protected float mSize = .15f;

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

    //handle to fragment shader's vColor member
    protected int mColorHandle;

    //The handle to the projection matrix
    protected int muMVPMatrixHandle;

    // number of coordinates per vertex in this array
    int COORDS_PER_VERTEX = 3;

    //How many bytes each vertext plus color takes up.
    protected int vertexStride = COORDS_PER_VERTEX * BYTES_FLOAT;

    abstract public void draw();

    /**
     * Sets the X and Y of the shape.
     *
     * We may be able to assume we want to have this x and y be the center of the shape.
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
     * Sets the X and Y of the shape.
     *
     * We may be able to assume we want to have this x and y be the center of the shape.
     * @param coords xyz float array
     */
    public void setXYZ(float[] coords){
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
        generateNewVertices(x,y,z);
    }

    @Override
    public float[] getXYZ() {
        return new float[]{this.x,this.y,this.z};
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     * @param x
     * @param y
     */
    abstract protected void generateNewVertices(float x, float y, float z);

    public float[] getColor() {
        return mColor;
    }

    @Override
    public void setColor(float[] mColor) {
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

    @Override
    public void setCoordsPerVertex(int coords) {
        this.COORDS_PER_VERTEX = coords;
        this.vertexStride = COORDS_PER_VERTEX * BYTES_FLOAT;
    }
}
