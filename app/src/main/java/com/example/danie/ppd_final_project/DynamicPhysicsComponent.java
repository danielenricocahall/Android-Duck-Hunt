package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/12/17.
 */

public abstract class DynamicPhysicsComponent implements PhysicsComponent {

    float speed;
    Vector2D forward;
    abstract void updatePosition(GameObject gameObject);
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void setForward(Vector2D forward)
    {
        this.forward = forward;
    }

    public float getSpeed()
    {
        return speed;
    }

    public Vector2D getForward()
    {
        return forward;
    }

}
