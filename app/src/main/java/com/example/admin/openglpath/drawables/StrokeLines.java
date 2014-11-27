package com.example.admin.openglpath.drawables;

import com.example.admin.openglpath.shaders.ShaderHelper;
import com.example.admin.openglpath.shaders.ShaderType;
import com.example.admin.openglpath.shaders.SimpleShaderProgram;
import com.example.admin.openglpath.util.ColorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Stroke using POINTS to draw
 *
 * Created by Mark Stanford on 11/21/14.
 */
public class StrokeLines extends Drawable{

    private static final String TAG = "StrokePoints";

    private List<Float> strokePoints;

    float mLineWidth = 1f;

    SimpleShaderProgram mShader;

    public StrokeLines(float x, float y, float z, float size, int color) {

        strokePoints =  new ArrayList<Float>();
        strokePoints.add(x);
        strokePoints.add(y);
        strokePoints.add(z);

        this.mLineWidth = size;

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape and the program id where the shader is loaded
        mShaderType = ShaderType.Simple;
        mShader = (SimpleShaderProgram) ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

        //Generating the vertices using the x,y
        generateNewVertices(this.x, this.y, this.z);

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
    public void draw(float[] matrix) {

        // Add program to OpenGL ES environment
        mShader.useProgram();

        // Prepare the triangle coordinate data
        glVertexAttribPointer(mShader.getPositionHandle(), COORDS_PER_VERTEX, GL_FLOAT, false, vertexStride, vertexBuffer);

        // Enable the attribute in the shader
        glEnableVertexAttribArray(mShader.getPositionHandle());

        // Set mColor for drawing the triangle
        glUniform4fv(mShader.getColorHandle(), 1, mColor, 0);

        //Change line size
        glLineWidth(mLineWidth);

        //Send the matrix to the shader
        glUniformMatrix4fv(mShader.getMatrixHandle(), 1, false, matrix, 0);

        // Draw the triangle
        glDrawArrays(GL_LINE_STRIP, 0, (shapeCoords.length / COORDS_PER_VERTEX));

        // Disable vertex array
        glDisableVertexAttribArray(mShader.getPositionHandle());

        //Revert back to default line width
        glLineWidth(1f);
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

        //Make sure we have an even number of points so we don't get wierd artifacts
        if(strokePoints.size() % 2 == 0){
            generateNewVertices(0,0,0);
        }
    }

    private static float[] convertFloats(List<Float> floats){
        float[] ret = new float[floats.size()];
        for (int i=0; i < ret.length; i++){
            ret[i] = floats.get(i).floatValue();
        }
        return ret;
    }

}
