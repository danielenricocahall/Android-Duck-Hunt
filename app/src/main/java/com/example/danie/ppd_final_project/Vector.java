package com.example.danie.ppd_final_project;

import java.util.ArrayList;

/**
 * Created by danie on 11/25/2017.
 */


/* There's a fundamental flaw here.
    While I've made this pretty OOPish, the abstraction means that
    vectors of differing dimensions can be manipulated together
    which shouldn't be the case (it could break things if not careful). I could add dimensional checks for safety -
    but it's midnight on a Saturday, I'm drinking a chocolate milk stout, and
    I'm feeling lazy. Maybe later...*/
/*Also, some extra computations are being used in the 1D case which could be refactored
    if they are found to be a performance bottleneck
 */


public abstract class Vector {
    protected float[] dimensions;
    protected float magnitude;
    protected float theta;


    public void add(Vector vector)
    {
        for(int ii = 0; ii < this.dimensions.length; ++ii)
        {
            dimensions[ii] += vector.dimensions[ii];
        }
    }
    public void subtract(Vector vector)
    {
        for(int ii = 0; ii < dimensions.length; ++ii)
        {
            dimensions[ii] -= vector.dimensions[ii];
        }
    }

    public void computeMagnitude()
    {
        for(int ii = 0; ii<dimensions.length; ++ii)
        {
            magnitude += dimensions[ii]*dimensions[ii];
        }
        magnitude = (float)Math.sqrt(magnitude);
    }

    public void scalarMultiply(float a)
    {
        for(int ii = 0; ii<dimensions.length; ++ii)
        {
            dimensions[ii] *= a;
        }
    }

    public float dotProduct(Vector vector)
    {
        float result = 0.0f;
        for(int ii = 0; ii < this.dimensions.length; ++ii)
        {
            result += dimensions[ii] * vector.dimensions[ii];
        }
        return result;
    }

    public void normalize()
    {
        for(int ii = 0; ii < dimensions.length; ++ii)
        {
            dimensions[ii] /= magnitude;
        }
    }

    public void copy(Vector vec1, Vector vec2)
    {
        vec1.dimensions = vec2.dimensions;
    }

    public void setX(float x)
    {
        setDimension(0, x);
    }

    public void setDimension(int i, float v)
    {
        dimensions[i] = v;
        computeMagnitude();
        computeTheta();
    }

    public abstract void computeTheta();



    public abstract Vector crossProduct(Vector vector);




}
