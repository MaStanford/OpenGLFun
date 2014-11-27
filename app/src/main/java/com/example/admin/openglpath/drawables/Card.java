package com.example.admin.openglpath.drawables;

import com.example.admin.openglpath.shaders.ShaderHelper;
import com.example.admin.openglpath.shaders.ShaderType;
import com.example.admin.openglpath.shaders.SimpleShaderProgram;
import com.example.admin.openglpath.util.ColorUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public class Card extends Drawable {

    private static final String TAG = "Card";

    SimpleShaderProgram mShader;

    public Card(float x, float y, float z, int color) {
        setXYZ(x,y,z);

        mColor = ColorUtil.getRGBAFromInt(color);

        //Get the shader for this shape
        this.mShaderType = ShaderType.Simple;

        //Get shader program
        this.mShader = (SimpleShaderProgram)ShaderHelper.getInstance().getCompiledShaders().get(mShaderType);

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

        //Use program
        mShader.useProgram();

        // Prepare the triangle coordinate data
        glVertexAttribPointer(mShader.getPositionHandle(), COORDS_PER_VERTEX, GL_FLOAT, false, vertexStride, vertexBuffer);

        // Enable the attribute in the shader
        glEnableVertexAttribArray(mShader.getPositionHandle());


        // Set mColor for drawing the triangle
        glUniform4fv(mShader.getColorHandle(), 1, mColor, 0);


        //Send the matrix to the shader
        glUniformMatrix4fv(mShader.getMatrixHandle(), 1, false, matrix, 0);

        // Draw the triangle
        glDrawArrays(GL_TRIANGLES, 0, (shapeCoords.length / COORDS_PER_VERTEX));

        // Disable vertex array
        glDisableVertexAttribArray(mShader.getPositionHandle());
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

    @Override
    public void setXYZ(float[] coords) {
        x = coords[0] - mSize/2;
        y = coords[1] + mSize/2;
        z = coords[2];

        generateNewVertices(x,y,z);
    }
}
