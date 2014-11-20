package com.example.admin.openglpath.util;

import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;

/**
 * Created by Mark Stanford on 11/19/14.
 */
public class ShaderHelper {

    public static final String TAG = "ShaderHelper";

    /**
     * Private constructor for our singleton pattern
     */
    private ShaderHelper(){
    }

    /**
     * Instance holder for our safe lazy instantiation pattern
     * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private static class instanceHolder{
        private static final ShaderHelper INSTANCE = new ShaderHelper();
    }

    /**
     * Returns the singleton
     * @return
     */
    public static ShaderHelper getInstance(){
        return instanceHolder.INSTANCE;
    }

    //Map of shaderTypes in the shape to the program id int
    private Map<ShaderType, Integer> compiledShaders;

    /**
     * Links shaders into a program
     */
    public int attachShader(int vertextShaderID, int fragmentShaderID){

        compiledShaders =  new HashMap<ShaderType, Integer>();

        int mProgram = GLES20.glCreateProgram();           // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertextShaderID);  // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShaderID); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                    // creates OpenGL ES program executables

        if(mProgram == 0){
            Log.w(TAG, "Program creation failed");
            return 0;
        }

        //Checking linking status
        final int[] linkStatus = new int[1];
        glGetProgramiv(mProgram, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            glDeleteProgram(mProgram);
            Log.w(TAG, "Linking of program failed.");
            return 0;
        }


        compiledShaders.put(ShaderType.Triangle, mProgram);

        return mProgram;
    }

    public int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            Log.w(TAG, "Could not create new shader.");
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);

        return shaderObjectId;
    }

    public Map<ShaderType, Integer> getCompiledShaders() {
        return compiledShaders;
    }

    public void setCompiledShaders(Map<ShaderType, Integer> compiledShaders) {
        this.compiledShaders = compiledShaders;
    }

    public void putCompiledShader(ShaderType type, int program) {
        this.compiledShaders.put(type, program);
    }
}
