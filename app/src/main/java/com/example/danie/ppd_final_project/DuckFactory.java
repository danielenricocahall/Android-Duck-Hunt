package com.example.danie.ppd_final_project;

import android.content.Context;

import java.util.Random;

/**
 * Created by daniel on 12/9/17.
 */

public class DuckFactory {

    //a factory which churns out ducks
    //this was originally made with the thought that each duck would have a different movement pattern
    //for the time being, it's kind of a misuse of the factory pattern


    //creates a green duck
    public Duck makeGreenDuck()
    {
        Duck duck = new Duck(new Random().nextFloat(),0 , "green", new BasicPhysicsComponent());
        return duck;
    }

    //creates a red duck
    public Duck makeRedDuck()
    {

        Duck duck = new Duck(new Random().nextFloat(),0 , "red", new BasicPhysicsComponent());
        return duck;
    }

    //creates a pink duck
    public Duck makePinkDuck()
    {
        Duck duck = new Duck(new Random().nextFloat(),0 , "pink", new BasicPhysicsComponent());
        return duck;
    }

    //creates a duck with one of the tree colors
    public Duck makeRandomDuck()
    {
        Duck duck;
        int duck_type = new Random().nextInt(GameConstants.NUMBER_OF_DUCK_TYPES);
        switch(duck_type)
        {
            case GameConstants.GREENDUCK:
                duck = makeGreenDuck();
                break;
            case GameConstants.REDDUCK:
                duck = makeRedDuck();
                break;
            case GameConstants.PINKDUCK:
                duck = makePinkDuck();
                break;
            default:
                duck = makeGreenDuck();
                break;
        }
        return duck;

    }
}

