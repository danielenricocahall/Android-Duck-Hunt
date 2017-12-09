package com.example.danie.ppd_final_project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 12/3/17.
 */

public class GameConstants {

    //I think this is bad practice, but it's convenient for such a small project...

    private GameConstants(){};


    public static final int NUMBER_OF_DUCK_SPRITES = 3;
    public static final int NUMBER_OF_DUCK_TYPES = 3;
    public static final int NUMBER_OF_DUCK_ORIENTATIONS = 4;
    public static final int NUMBER_OF_DOG_SPRITES = 9;


    public static final int GREENDUCK = 0;
    public static final int REDDUCK = 1;
    public static final int PINKDUCK = 2;

    public static final int DIAGONAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int BACK = 2;
    public static final int DEFEAT = 3;

    public static final float DELAY_AFTER_SHOT = 1.0f;
    public static final float DELAY_TO_DISPLAY_SCORE = 2.0f;
    public static final float GRAVITY = 10.0f;

    static final Map<String , Integer> COLOR_TO_SCORE = new HashMap<String, Integer>() {{
        put("green",    500);
        put("red", 1000);
        put("pink",   1500);
    }};







}
