package com.example.admin.openglpath.shaders;

import android.content.Context;

import com.example.admin.openglpath.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Holds the point shader handles
 *
 * Created by Mark Stanford on 11/26/14.
 */
public class PointShaderProgram extends ShaderProgram {

    private final int mPositionHandle;
    private final int mColorHandle;
    private final int mMatrixHandle;
    private final int mPointHandle;

    public PointShaderProgram(Context context) {
        super(context, R.raw.point_vertex_shader, R.raw.simple_fragment_shader, ShaderType.Point);

        // get handle to vertex shader's vPosition member from the shader
        mPositionHandle = glGetAttribLocation(mProgram, VERTEX_POSITION);

        // get handle to fragment shader's vColor member from the shader
        mColorHandle = glGetUniformLocation(mProgram, FRAGMENT_COLOR);

        //Get the handle to projection matrix
        mMatrixHandle = glGetUniformLocation(mProgram, VERTEX_MATRIX);

        //get the handle for the point size
        mPointHandle = glGetUniformLocation(mProgram, VERTEX_POINT_SIZE);
    }

    public int getPositionHandle() {
        return mPositionHandle;
    }

    public int getColorHandle() {
        return mColorHandle;
    }

    public int getMatrixHandle() {
        return mMatrixHandle;
    }

    public int getPointHandle() {
        return mPointHandle;
    }
}
