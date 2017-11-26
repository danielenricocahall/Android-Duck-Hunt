package com.example.danie.ppd_final_project;

/**
 * Created by danie on 11/25/2017.
 */

public class Vector1D extends Vector {

    public Vector1D(float x)
    {
       dimensions = new float[]{x};
        dimensions[0] = x;
        magnitude = x;
        //theta = 0;
    }

    public Vector1D()
    {
        this(0.0f);
    }

    public Vector1D(Vector1D vec)
    {
        dimensions = vec.dimensions;
    }

    public void computeTheta()
    {
        theta = 0.0f;
    }

    public Vector crossProduct(Vector vector)
    {
        return new Vector2D(0, 0);
    }
}
