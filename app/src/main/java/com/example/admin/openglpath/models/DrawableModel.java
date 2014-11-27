package com.example.admin.openglpath.models;

/**
 * This is the drawable model we will use to serialize/deserialize drawables
 * I am using this instead of trying to serialize or deserialize in the drawable interface
 *  because I want to seperate the OpenGL models from the network/persistence models
 * Created by Mark Stanford on 11/26/14.
 */
public class DrawableModel {
    float x;
    float y;
    float size;
    int color;

    public DrawableModel() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
