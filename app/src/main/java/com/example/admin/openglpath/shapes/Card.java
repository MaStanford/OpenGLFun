package com.example.admin.openglpath.shapes;

import android.opengl.GLES20;
import android.util.Log;

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

    float shapeCoords[] = new float[]
            {
            //       X           Y              Z          Triangle 1
                    (0),        (0),           (0.0f),  // top
                    (0),        (0-mSize),     (0.0f),  // bottom left
                    (0+mSize),  (0-mSize),     (0.0f),  // bottom right
                                                        // Triangle 2
                    (0),        (0),           (0.0f),  // top
                    (0+mSize),  (0-mSize),     (0.0f),  // bottom left
                    (0),        (0+mSize),     (0.0f)   // bottom right
            };

    public Card(float x, float y, int color) {

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape and the program id where the shader is loaded
        mShaderType = ShaderType.Triangle;
        mProgram = ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

        Log.d(TAG, "Prgram: " + mProgram);

        //Generating the vertices using the x,y
        generateNewVertices(x, y);

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

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set mColor for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     * @param x
     * @param y
     */
    @Override
    protected void generateNewVertices(float x, float y){
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
    }


}
