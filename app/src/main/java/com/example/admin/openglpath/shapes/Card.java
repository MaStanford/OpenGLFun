package com.example.admin.openglpath.shapes;

import android.opengl.GLES20;

import com.example.admin.openglpath.util.ColorUtil;
import com.example.admin.openglpath.util.ShaderHelper;
import com.example.admin.openglpath.util.ShaderType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public class Card extends Drawable {

    private static final String TAG = "Triangle";

    public Card(float x, float y, float z, int color) {
        super(x,y,z);
        setXYZ(x,y,z);

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape and the program id where the shader is loaded
        mShaderType = ShaderType.Triangle;
        mProgram = ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

        //Generating the vertices using the x,y
        generateNewVertices(this.x, this.y, this.z);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(shapeCoords.length * 4); // (number of coordinate values * 4 bytes per float)

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(shapeCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    @Override
    public void draw() {

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member from the shader
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member from the shader
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set mColor for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, (shapeCoords.length / COORDS_PER_VERTEX));

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     * @param x
     * @param y
     */
    @Override
    protected void generateNewVertices(float x, float y, float z){
        this.shapeCoords = new float[]
                {
                //       X           Y              Z          Triangle 1
                        (x),        (y),           (0.0f),  // top
                        (x),        (y-mSize),     (0.0f),  // bottom left
                        (x+mSize),  (y-mSize),     (0.0f),  // bottom right
                                                            // Triangle 2
                        (x),        (y),           (0.0f),  // top
                        (x+mSize),  (y-mSize),     (0.0f),  // bottom left
                        (x+mSize),  (y),           (0.0f)   // bottom right
                };

        //Buffer stuff
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(shapeCoords.length * 4); // (number of coordinate values * 4 bytes per float)

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(shapeCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    @Override
    public float doesIntersectXY(float x, float y) {
        if((x > this.x) && (x < (this.x + mSize)) && (y < this.y) && (y > (this.y - mSize))){
            return this.z;
        }
        return -1;
    }

    @Override
    public void setXYZ(float x, float y, float z) {
        x = x - mSize/2;
        y = y + mSize/2;

        this.x = x;
        this.y = y;
        this.z = z;

        generateNewVertices(x,y,z);
    }
}
