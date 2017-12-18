package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/12/17.
 */

//the physics component which is used by the duck and the dog
    //movement is basic - just updates position based on speed, the
    //forward unit vector, and the change in time
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

    //updates the position using basic kinematics (velocity*time)
    public void updatePosition(GameObject gameObject)
    {
        Vector2D deltaPosition = new Vector2D(forward.x, -forward.y);
        deltaPosition.scalarMultiply(speed *
                GameEngine.DELTA_TIME);
        gameObject.position.add(deltaPosition);
    }



}
