package com.example.admin.openglpath.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.admin.openglpath.R;
import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.loopers.AnimationLoop;
import com.example.admin.openglpath.util.ColorUtil;
import com.example.admin.openglpath.util.ShaderHelper;
import com.example.admin.openglpath.util.ShaderType;
import com.example.admin.openglpath.util.TextResourceReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public class Renderer implements GLSurfaceView.Renderer {

    public static String TAG = "Renderer";

    int BGColor;
    Context mContext;

    //Projection
    int muMVPMatrixHandle;

    public Renderer(Context context, int color) {
        this.mContext = context;
        BGColor = color;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        clearColor();

        //This is where we will compile the shader for now TODO: Move this to loading screen
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader .readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        Log.d(TAG, vertexShaderSource + fragmentShaderSource);

        int vertexShader = ShaderHelper.getInstance().compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.getInstance().compileFragmentShader(fragmentShaderSource);

        int program = ShaderHelper.getInstance().attachShader(vertexShader,fragmentShader);

        ShaderHelper.getInstance().putCompiledShader(ShaderType.Triangle, program);

        //Start the animation loop
        AnimationLoop.doRun();
    }

    public void setBGColor(int color) {
        this.BGColor = color;
    }

    public int getBGColor() {
        return this.BGColor;
    }

    /**
     * Clears the screen with the mColor specified in the xml
     */
    public void clearColor() {
        float[] color = ColorUtil.getRGBAFromInt(BGColor);

        glClearColor(color[0], color[1], color[2], color[3]);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

        GLES20.glClearDepthf(1f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //Iterate through the drawableList, cannot use enhanced with an iterator if we remove or add to list while iterating
        for (int i = 0; i< DataHolder.getInstance().getDrawableList().size() ; i++) {
            DataHolder.getInstance().getDrawableList().get(i).draw();
        }
    }
}
