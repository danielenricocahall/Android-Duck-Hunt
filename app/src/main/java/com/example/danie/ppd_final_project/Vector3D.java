package com.example.danie.ppd_final_project;

/**
 * Created by danie on 11/25/2017.
 */

public class Vector3D extends Vector {

    float phi;

    public Vector3D(float x, float y, float z)
    {
        dimensions = new float[]{x,y,z};
        computeMagnitude();
        computeTheta();
        computePhi();
    }
    public Vector3D()
    {
        this(0.0f, 0.0f, 0.0f);
    }//added a default constructor for funsies
    public Vector3D(Vector2D vec) {this(vec.dimensions[0],vec.dimensions[1], vec.dimensions[2]);}//effectively a constructor for copying another vector
    public void setY(float y)
    {
        setDimension(1,y);
        computePhi();
    }
    public void setZ(float z)
    {
        setDimension(2,z);
        computePhi();
    }

    public void computeTheta()
    {
        theta = (float)Math.atan2(dimensions[2], magnitude);
    }
    public void computePhi()
    {
        phi = (float)Math.atan2(dimensions[1], dimensions[0]);
    }
    public Vector crossProduct(Vector vector) //not sure if this will be used but if so this 100% needs to be double checked
    {
        return new Vector3D(
                dimensions[1]*vector.dimensions[2]-dimensions[2]*vector.dimensions[1],
                dimensions[2]*vector.dimensions[0]-dimensions[0]*vector.dimensions[2],
                dimensions[0]*vector.dimensions[1] - dimensions[1]*dimensions[0]);
    }
}
