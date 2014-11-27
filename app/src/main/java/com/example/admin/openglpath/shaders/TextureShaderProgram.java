package com.example.admin.openglpath.shaders;

import android.content.Context;

import com.example.admin.openglpath.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Contains the shader program and handles for textures.
 *
 * Created by Mark Stanford on 11/26/14.
 */
public class TextureShaderProgram extends ShaderProgram{

    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;


    protected TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader, ShaderType.Texture);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(mProgram, VERTEX_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(mProgram, VERTEX_TEXTURE);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(mProgram, VERTEX_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, VERTEX_TEXTURE_COORD_ATTR);
    }

    public int getuMatrixLocation() {
        return uMatrixLocation;
    }

    public int getuTextureUnitLocation() {
        return uTextureUnitLocation;
    }

    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}
