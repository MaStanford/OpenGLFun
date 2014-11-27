package com.example.admin.openglpath.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.loopers.AnimationLoop;
import com.example.admin.openglpath.shaders.PointShaderProgram;
import com.example.admin.openglpath.shaders.ShaderHelper;
import com.example.admin.openglpath.shaders.ShaderProgram;
import com.example.admin.openglpath.shaders.ShaderType;
import com.example.admin.openglpath.shaders.SimpleShaderProgram;
import com.example.admin.openglpath.util.ColorUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by Mark Stanford on 11/18/14.
 */
public class Renderer implements GLSurfaceView.Renderer {

    public static String TAG = "Renderer";

    int BGColor;
    Context mContext;
    int screenWidth;
    int screenHeight;

    DataHolder dh = DataHolder.getInstance();

    //The projection matrix
    private final float[] projectionMatrix = new float[16];

    public Renderer(Context context, int color) {
        this.mContext = context;
        BGColor = color;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        clearColor();

        //This is where we will compile the shader for now TODO: Move this to loading screen

        //Logic for simple shader
        ShaderProgram simpleShader = new SimpleShaderProgram(mContext);
        ShaderHelper.getInstance().putCompiledShader(ShaderType.Simple, simpleShader);

        //Logic for point shader
        ShaderProgram pointShader = new PointShaderProgram(mContext);
        ShaderHelper.getInstance().putCompiledShader(ShaderType.Point, pointShader);


        //Start the animation loop
        AnimationLoop.doRun();
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

        this.screenWidth=width;
        this.screenHeight=height;

        final float aspectRatio = (float) screenWidth / (float) screenHeight;
        dh.setAspectRatio(aspectRatio);

        glViewport(0, 0, screenWidth, screenHeight);

        updateViewport(dh.getZoom(), dh.getOffset()[0], dh.getOffset()[1]);

        GLES20.glClearDepthf(1f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //Iterate through the drawableList, cannot use enhanced with an iterator if we remove or add to list while iterating
        for (int i = 0; i < DataHolder.getInstance().getDrawableList().size(); i++) {
            dh.getDrawableList().get(i).draw(projectionMatrix);
        }
    }

    public void updateViewport(float zoom, float offsetX, float offsetY) {
        //This is for setting the projection and aspect ratio
        orthoM(projectionMatrix, 0, -(dh.getAspectRatio() * zoom) + offsetX, (dh.getAspectRatio() * zoom) + offsetX, -zoom - offsetY, zoom - offsetY, -1f, 1f);
    }
}
