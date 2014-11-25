package com.example.admin.openglpath.drawables;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

/**
 * http://androidblog.reindustries.com/a-real-opengl-es-2-0-2d-tutorial-part-8-rendering-text/
 * Created by Mark Stanford on 11/24/14.
 */
public class TextManager {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    private static final float RI_TEXT_WIDTH = 32.0f;
    private static final float RI_TEXT_SPACESIZE = 20f;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;

    private float[] vecs;
    private float[] uvs;
    private short[] indices;
    private float[] colors;

    private int index_vecs;
    private int index_indices;
    private int index_uvs;
    private int index_colors;

    private int texturenr;

    private float uniformscale;

    public static int[] l_size = {36, 29, 30, 34, 25, 25, 34, 33,
            11, 20, 31, 24, 48, 35, 39, 29,
            42, 31, 27, 31, 34, 35, 46, 35,
            31, 27, 30, 26, 28, 26, 31, 28,
            28, 28, 29, 29, 14, 24, 30, 18,
            26, 14, 14, 14, 25, 28, 31, 0,
            0, 38, 39, 12, 36, 34, 0, 0,
            0, 38, 0, 0, 0, 0, 0, 0};

    public Vector<Text> txtcollection;
}
