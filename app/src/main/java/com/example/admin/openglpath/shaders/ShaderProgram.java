package com.example.admin.openglpath.shaders;

import android.content.Context;

import com.example.admin.openglpath.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * This will be the base class for holding shader information.
 * This contains the handles to the uniforms, attributes and matrices in the shaders
 * Created by Mark Stanford on 11/26/14.
 */
public class ShaderProgram {

    /**
     * Shader locations.  Use a position handle to point to this location in the shader.
     */
    //Vertex
    public static final String VERTEX_POSITION              = "aPosition";
    public static final String VERTEX_MATRIX                = "u_Matrix";
    public static final String VERTEX_POINT_SIZE            = "aPointSize";
    public static final String VERTEX_COLOR                 = "aColor";
    public static final String VERTEX_VCOLOR                = "vColor";
    public static final String VERTEX_TEXTURE               = "uTexture";
    public static final String VERTEX_TEXTURE_COORD_ATTR    = "aTextureCoord";

    //Fragment
    public static final String FRAGMENT_COLOR           = "uColor";
    public static final String FRAGMENT_VCOLOR          = "vColor";

    // Shader program
    protected final int mProgram;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId, ShaderType type) {
        // Compile the shaders and link the program.
        mProgram = ShaderHelper.getInstance().buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program. glUseProgram(program);
        glUseProgram(mProgram);
    }
}
