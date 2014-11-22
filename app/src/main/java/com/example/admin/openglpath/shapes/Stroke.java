package com.example.admin.openglpath.shapes;

import android.opengl.GLES20;

import com.example.admin.openglpath.util.ColorUtil;
import com.example.admin.openglpath.util.ShaderHelper;
import com.example.admin.openglpath.util.ShaderType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class Stroke extends Drawable{

    private static final String TAG = "Stroke";

    private List<Float> strokePoints;

    public Stroke(float x, float y, float z, int color) {
        super(x, y, z);

        strokePoints =  new ArrayList<Float>();
        strokePoints.add(x);
        strokePoints.add(y);
        strokePoints.add(z);


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
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, VERTEX_POSITION);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Enable the attribute in the shader
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // get handle to fragment shader's vColor member from the shader
        mColorHandle = GLES20.glGetUniformLocation(mProgram, FRAGMENT_COLOR);

        // Set mColor for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        //Get the handle to projection matrix
        muMVPMatrixHandle = glGetUniformLocation(mProgram, VERTEX_MATRIX);

        // Draw the stroke
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, (shapeCoords.length / COORDS_PER_VERTEX));

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     *
     * @param x
     * @param y
     */
    @Override
    protected void generateNewVertices(float x, float y, float z) {
        shapeCoords =  convertFloats(strokePoints);

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

    public void addPoint(float x, float y, float z){
        strokePoints.add(x);
        strokePoints.add(y);
        strokePoints.add(z);
        generateNewVertices(0,0,0);
    }

    @Override
    public float doesIntersectXY(float x, float y) {
        //TODO: Figure this out
        return -1;
    }

    @Override
    public void setXYZ(float x, float y, float z) {
        //TODO: figure this out
    }

    private static float[] convertFloats(List<Float> floats){
        float[] ret = new float[floats.size()];
        for (int i=0; i < ret.length; i++){
            ret[i] = floats.get(i).floatValue();
        }
        return ret;
    }

}
