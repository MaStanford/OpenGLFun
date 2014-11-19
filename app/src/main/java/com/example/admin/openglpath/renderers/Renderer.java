package com.example.admin.openglpath.renderers;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.admin.openglpath.shapes.Card;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.ColorUtil;

import java.util.List;

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

    int mColor;

    private List<Drawable> drawableList;

    public Renderer(int color, List<Drawable> drawableList) {
        mColor = color;
        this.drawableList = drawableList;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        clearColor();
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public int getColor() {
        return this.mColor;
    }

    /**
     * Clears the screen with the mColor specified in the xml
     */
    public void clearColor() {
        float[] color = ColorUtil.getRGBAFromInt(mColor);

        glClearColor(color[0], color[1], color[2], color[3]);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        new Card(0,0,35).draw();

        //Iterate through the drawableList
        for (Drawable drawable : drawableList) {
            drawable.draw();
        }
    }

    public List<Drawable> getDrawableList() {
        return drawableList;
    }

    public void setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
        Log.d(TAG, "ShapeList: " + this.drawableList.size());
    }

    public void addShape(Drawable drawable) {
        this.drawableList.add(drawable);
        Log.d(TAG, "ShapeList: " + this.drawableList.size());
    }
}
