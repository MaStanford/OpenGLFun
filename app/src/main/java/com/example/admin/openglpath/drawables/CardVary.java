package com.example.admin.openglpath.drawables;

import android.util.Log;

import com.example.admin.openglpath.models.DrawableType;
import com.example.admin.openglpath.util.ColorUtil;
import com.example.admin.openglpath.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.example.admin.openglpath.renderers.Renderer.VERTEX_COLOR;
import static com.example.admin.openglpath.renderers.Renderer.VERTEX_POSITION;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public class CardVary extends Drawable {

    private static final String TAG = "CardVary";

    /**
     * How many floats for each color
     */
    public static final int COLOR_COUNT = 3;

    public CardVary(float x, float y, float z, int color) {
        setXYZ(x,y,z);

        //Change the default coords from 3 to 6 and this also changes the vertex stride.
        setCoordsPerVertex(6);

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape and the program id where the shader is loaded
        mShaderType = DrawableType.CardVary;
        mProgram = ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

        Log.d(TAG, "Program : " + mProgram);

        //Generating the vertices using the x,y
        generateNewVertices(x, y, z);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(shapeCoords.length * BYTES_FLOAT); // (number of coordinate values * 4 bytes per float)

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
        glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member from the shader
        mPositionHandle = glGetAttribLocation(mProgram, VERTEX_POSITION);

        // Prepare the triangle coordinate data
        glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GL_FLOAT, false, vertexStride, vertexBuffer);

        // Enable the attribute in the shader
        glEnableVertexAttribArray(mPositionHandle);

        //Get handle for vary color.
        mColorVaryHandle = glGetAttribLocation(mProgram, VERTEX_COLOR);

        //Move to the color position
        vertexBuffer.position(COORDS_PER_VERTEX);

        //Tell opengl where to look for color data and what handle points to it in the shader
        glVertexAttribPointer(mColorVaryHandle, COLOR_COUNT, GL_FLOAT, false, vertexStride, vertexBuffer);

        //Enable the color array and handler and attribute and etc
        glEnableVertexAttribArray(mColorVaryHandle);

        // Draw the triangle
        glDrawArrays(GL_TRIANGLES, 0, (shapeCoords.length / vertexStride));

        // Disable vertex array
        glDisableVertexAttribArray(mPositionHandle);
        glDisableVertexAttribArray(mColorHandle);
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
                //       X           Y              Z       R          G           B            Triangle 1
                        (x),        (y),           (0.0f),  mColor[0], mColor[1] , mColor[2],   // top
                        (x),        (y-mSize),     (0.0f),  mColor[0], mColor[1] , mColor[2],   // bottom left
                        (x+mSize),  (y-mSize),     (0.0f),  mColor[0], mColor[1] , mColor[2],   // bottom right
                                                                                                // Triangle 2
                        (x),        (y),           (0.0f),  mColor[0], mColor[1] , mColor[2],   // top
                        (x+mSize),  (y-mSize),     (0.0f),  mColor[0], mColor[1] , mColor[2],   // bottom left
                        (x+mSize),  (y),           (0.0f),  mColor[0], mColor[1] , mColor[2]    // bottom right
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
        //Assume we want to drag and create from the center of the object
        x = x - mSize/2;
        y = y + mSize/2;

        this.x = x;
        this.y = y;
        this.z = z;

        generateNewVertices(x,y,z);
    }
}
