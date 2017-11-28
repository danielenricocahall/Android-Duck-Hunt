package com.example.danie.ppd_final_project;

import java.util.ArrayList;

/**
 * Created by danie on 11/25/2017.
 */



public class Vector2D {
    public float x;
    public float y;
    //public float theta;



    public Vector2D(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D()
    {
        this(0.0f, 0.0f);
    }
    public void add(Vector2D vector)
    {
        this.x += vector.x;
        this.y += vector.y;
    }
    public void subtract(Vector2D vector)
    {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    public float computeMagnitude()
    {
        return this.x*this.x + this.y*this.y;
    }

    public void scalarMultiply(float a)
    {
        this.x *= a;
        this.y *= a;
    }

    public float dotProduct(Vector2D vector)
    {
        return this.x*vector.x + this.y*vector.y;
    }

    public void normalize()
    {
        float magnitude = computeMagnitude();
        this.x /= magnitude;
        this.y /= magnitude;
    }

    public float getTheta()
    {
        return (float) Math.atan2(this.y, this.x);
    }



}
