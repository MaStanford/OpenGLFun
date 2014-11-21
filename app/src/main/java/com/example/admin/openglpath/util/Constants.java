package com.example.admin.openglpath.util;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public class Constants {

    //Gravity is how much the velocity decrements per loop
    public static final float GRAVITY = 25;

    //Factor for how hard it is to move through the screen
    public static final float SLUDGE = 1f;

    /**
     *  Wait is how long the thread should sleep.  17 ms is 60fps
     *  Use this to figure out how to scale flings/animations per iteration.
     */
    public static final int WAIT = 17;

    //How many ms in a second
    public static final int SECOND = 1000;
}
