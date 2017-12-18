package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/12/17.
 */

//to be used by any game object which will be moving
    //however, the class which inherits from it will define the specific behavior
    //the beauty of using this sort of structure is the fact that we can now create more
    //(potentially advanced) physics components which extend from this class, and pass them
    //into our game objects seamlessly
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
