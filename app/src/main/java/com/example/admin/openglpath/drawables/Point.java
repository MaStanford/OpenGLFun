package com.example.admin.openglpath.drawables;

import android.opengl.GLES20;

import com.example.admin.openglpath.shaders.PointShaderProgram;
import com.example.admin.openglpath.shaders.ShaderHelper;
import com.example.admin.openglpath.shaders.ShaderType;
import com.example.admin.openglpath.util.ColorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Mark Stanford on 11/21/14.
 */
public class Point extends Drawable {

    private static final String TAG = "Point";

    PointShaderProgram mShader;

    public Point(float x, float y, float z, int color) {
        setXYZ(x, y, z);

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape and the program id where the shader is loaded
        mShaderType = ShaderType.Point;
        mShader = (PointShaderProgram) ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

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
    public void draw(float[] matrix) {

        // Add program to OpenGL ES environment
        mShader.useProgram();

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mShader.getPositionHandle(), COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Enable the attribute in the shader
        GLES20.glEnableVertexAttribArray(mShader.getPositionHandle());

        // Set mColor for drawing the triangle
        GLES20.glUniform4fv(mShader.getColorHandle(), 1, mColor, 0);

        //Send the matrix to the shader
        glUniformMatrix4fv(mShader.getMatrixHandle(), 1, false, matrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, (shapeCoords.length / COORDS_PER_VERTEX));

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mShader.getPositionHandle());
    }

    /**
     * Generates the new vertices of the shape based on the X,Y and size
     *
     * @param x
     * @param y
     */
    @Override
    protected void generateNewVertices(float x, float y, float z) {
        this.shapeCoords = new float[]{(x), (y), (0.0f),};

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
        if ((x > this.x) && (x < (this.x + mSize)) && (y < this.y) && (y > (this.y - mSize))) {
            return this.z;
        }
        return -1;
    }

    @Override
    public void setXYZ(float x, float y, float z) {
        x = x - mSize / 2;
        y = y + mSize / 2;

        this.x = x;
        this.y = y;
        this.z = z;

        generateNewVertices(x, y, z);
    }
}
