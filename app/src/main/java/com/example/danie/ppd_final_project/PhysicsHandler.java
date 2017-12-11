package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/10/17.
 */

public class PhysicsHandler {

    private Vector2D position;
    private Vector2D forward;
    float speed;



    public PhysicsHandler(Vector2D position, Vector2D forward)
    {
        this.position = position;
        this.forward = forward;
    }
    public void addToPosition()
    {
        Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
        deltaPosition.scalarMultiply(speed *
                GameEngine.DELTA_TIME);
        this.position.add(deltaPosition);
    }

    public void setForward(Vector2D forward)
    {
        this.forward = forward;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
