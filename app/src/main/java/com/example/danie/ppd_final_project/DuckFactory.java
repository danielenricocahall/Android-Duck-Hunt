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
        int duck_type = new Random().nextInt(GameConstants.NUMBER_OF_DUCK_TYPES);
        Duck duck = new Duck(context, new Random().nextInt(GameEngine.SCREEN_WIDTH), GameEngine.SCREEN_HEIGHT * 0.6f, getDuckColor(duck_type));
        return duck;

    }

    private static String getDuckColor(int n)
    {
        String duck_type;
        switch(n)
        {
            case GameConstants.GREENDUCK:
                duck_type = "green";
                break;
            case GameConstants.REDDUCK:
                duck_type = "red";
                break;
            case GameConstants.PINKDUCK:
                duck_type = "pink";
                break;
            default:
                duck_type = "green";
                break;
        }
        return duck_type;
    }
}

