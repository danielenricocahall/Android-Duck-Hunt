package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/12/17.
 */

public class BasicPhysicsComponent extends DynamicPhysicsComponent {


    public BasicPhysicsComponent()
    {
        this(0.0f, new Vector2D(0.0f, 0.0f));
    }

    public BasicPhysicsComponent(float speed, Vector2D forward)
    {
        this.speed = speed;
        this.forward = forward;
    }

    public void update(GameObject gameObject)
    {
        updatePosition(gameObject);
    }

    public void updatePosition(GameObject gameObject)
    {
        Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
        deltaPosition.scalarMultiply(speed *
                GameEngine.DELTA_TIME);
        gameObject.position.add(deltaPosition);
    }



}
