package com.example.admin.openglpath.util;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public class Constants {

    //Minimum speed in which to show fling
    public static final float MIN_SPEED = 100f;

    //Gravity is how much the velocity decrements per loop
    public static final float GRAVITY = .9f;

    /**
     *  Wait is how long the thread should sleep.  17 ms is 60fps
     *  Use this to figure out how to scale flings/animations per iteration.
     */
    public static final int WAIT = 17;

    //How many ms in a second
    public static final int SECOND = 1000;
}
