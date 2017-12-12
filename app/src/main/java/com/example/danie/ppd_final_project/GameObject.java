package com.example.danie.ppd_final_project;

import android.graphics.Canvas;

/**
 * Created by danie on 11/24/2017.
 */

public abstract class GameObject {

    public static int layer;
    public Vector2D position;
    public boolean destroy;

    public abstract void init();

    public abstract void onDraw(Canvas canvas);

    public abstract void onUpdate();
}
