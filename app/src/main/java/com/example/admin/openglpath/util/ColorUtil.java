package com.example.admin.openglpath.util;

import android.util.Log;

/**
 * Created by admin on 11/19/14.
 */
public class ColorUtil {

    /**
     * returns a r g b a float array that openGL likes
     * @param color
     * @return the r g b a float array
     */
    public static float[] getRGBAFromInt(int color){
        float b = ((color) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        return new float[]{r,g,b,a};
    }
}
