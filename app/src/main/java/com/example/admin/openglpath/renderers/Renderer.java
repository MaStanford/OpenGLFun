package com.example.admin.openglpath.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
    /**
     * Shader locations.  Use a position handle to point to this location in the shader.
     */
    public static final String VERTEX_POSITION = "aPosition";
    public static final String VERTEX_MATRIX = "u_Matrix";
    public static final String VERTEX_POINT_SIZE = "aPointSize";
    public static final String VERTEX_COLOR = "aColor";
    public static final String VERTEX_VCOLOR = "vColor";
    public static final String FRAGMENT_COLOR = "uColor";
    public static final String FRAGMENT_VCOLOR = "vColor";


    int BGColor;
    Context mContext;
    int screenWidth;
    int screenHeight;


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

        changeViewport(0,0);

        changeProjection(1.0f);

        GLES20.glClearDepthf(1f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //Iterate through the drawableList, cannot use enhanced with an iterator if we remove or add to list while iterating
        for (int i = 0; i < DataHolder.getInstance().getDrawableList().size(); i++) {
            DataHolder.getInstance().getDrawableList().get(i).draw(projectionMatrix);
        }
    }

    public void changeProjection(float zoom) {

        //This is for setting the projection and aspect ratio
        final float aspectRatio = (float) screenWidth / (float) screenHeight;
        Log.d(TAG, "aspectRatio: " + aspectRatio);

        orthoM(projectionMatrix, 0, -aspectRatio * zoom, aspectRatio * zoom, -zoom, zoom, -1f, 1f);

        DataHolder.getInstance().setAspectRatio(aspectRatio);
    }

    public void changeViewport(int x, int y){
        glViewport(x, y, screenWidth, screenHeight);
    }
}
