package com.example.danie.ppd_final_project;

/**
 * Created by danie on 11/25/2017.
 */

public class Vector2D extends Vector {

    //float theta;

    public Vector2D(float x, float y)
    {
        dimensions = new float[]{x,y};
        theta = (float)Math.atan2(y,x);
        computeMagnitude();
    }
    public Vector2D()
    {
        this(0.0f, 0.0f);
    }
    public Vector2D(Vector2D vec)
    {
        dimensions = new float[]{vec.dimensions[0],vec.dimensions[1]};
    }
    public Vector crossProduct(Vector vector)
    {
        return new Vector3D(0, 0,dimensions[0]*vector.dimensions[1] - dimensions[1]*dimensions[0]);
    }

    public void setY(float y)
    {
        setDimension(1,y);
    }

    public void computeTheta()
    {
        theta = (float)Math.atan2(dimensions[1], dimensions[0]);
    }

    public static Vector2D addVectors(Vector2D v1, Vector2D v2)
    {
        Vector2D vec = new Vector2D(0,0);
        for(int ii = 0; ii<v1.dimensions.length;++ii)
        {
            vec.dimensions[ii] = v1.dimensions[ii] + v2.dimensions[ii];
        }
        return vec;
    }

}
