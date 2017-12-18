package com.example.danie.ppd_final_project;

import android.graphics.Canvas;

/**
 * Created by danie on 11/24/2017.
 */

public abstract class GameObject {

    public int layer; // determines when the object is rendered
    public Vector2D position; // determines the position of the object
    public boolean destroy;// determines whether or not the object should continue being updated and drawn in the game engine

    public abstract void init();

    public abstract void onDraw(Canvas canvas);

    public abstract void onUpdate();
}
