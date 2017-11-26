package com.example.danie.ppd_final_project;

/**
 * Created by danie on 11/25/2017.
 */

public class Vector1D extends Vector {

    public Vector1D(float x)
    {
       dimensions = new float[]{x};
        magnitude = x;
        theta = 0.0f;
    }

    public Vector1D()
    {
        this(0.0f);
    }//added a default constructor for funsies

    public Vector1D(Vector1D vec) {this(vec.dimensions[0]);}//effectively a constructor for copying another vector

    public void computeTheta()
    {
        theta = 0.0f;
    }

    public Vector crossProduct(Vector vector)
    {
        return new Vector2D(0, 0);
    }
}
