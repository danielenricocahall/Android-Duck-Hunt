package com.example.danie.ppd_final_project;

import android.content.Context;

import java.util.Random;

/**
 * Created by daniel on 12/9/17.
 */

public class DuckFactory {


    private static Context context;

    public DuckFactory(Context context)
    {
        setContext(context);
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Duck makeGreenDuck()
    {
        Duck duck = new Duck(this.context, new Random().nextInt(GameEngine.SCREEN_WIDTH), GameEngine.SCREEN_HEIGHT * 0.6f, "green");
        return duck;
    }

    public Duck makeRedDuck()
    {

        Duck duck = new Duck(this.context, new Random().nextInt(GameEngine.SCREEN_WIDTH), GameEngine.SCREEN_HEIGHT * 0.6f, "red");
        return duck;
    }

    public Duck makePinkDuck()
    {
        Duck duck = new Duck(this.context, new Random().nextInt(GameEngine.SCREEN_WIDTH), GameEngine.SCREEN_HEIGHT * 0.6f, "pink");
        return duck;
    }

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

