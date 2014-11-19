package com.example.admin.openglpath.util;

import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark Stanford on 11/19/14.
 */
public class ShaderHelper {

    public static final String TAG = "ShaderHelper";

    //Map of shaderTypes in the shape to the program id int
    public static Map<ShaderType, Integer> compiledShaders;

    /**
     * Just debugging for adding shaders and loading them and what not.  This will be different
     */
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);


    /**
     * This will eventually compile and load up all the shaders for the whole program
     * It will iterate through the list of shaders and shader types and fill up the map
     */
    public void compileAndLoadShader(){

        this.compiledShaders =  new HashMap<ShaderType, Integer>();

        int mProgram = GLES20.glCreateProgram();         // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

        this.compiledShaders.put(ShaderType.Triangle, mProgram);
    }

    /**
     * loads a shader into the openGL context
     *
     * @param type GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

//    public static int compileVertexShader(String shaderCode) {
//        return compileShader(GL_VERTEX_SHADER, shaderCode);
//    }
//    public static int compileFragmentShader(String shaderCode) {
//        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
//    }
//    private static int compileShader(int type, String shaderCode) {
//
//    }


}
