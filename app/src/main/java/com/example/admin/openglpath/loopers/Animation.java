package com.example.admin.openglpath.loopers;

import com.example.admin.openglpath.shapes.Drawable;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public interface Animation {

    public boolean doNext();

    public Drawable getDrawable();

    public void stop();
}
