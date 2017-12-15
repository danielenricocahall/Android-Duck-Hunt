package com.example.danie.ppd_final_project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 12/3/17.
 */

public class GameConstants {

    //I think this is bad practice, but it's convenient for such a small project...

    private GameConstants(){};


    public static final String NUMBER_OF_DUCKS = "Number Of Ducks";
    public static final String LEVEL = "Level";
    public static final String SCORE = "Score";


    public static final int NUMBER_OF_DUCK_SPRITES = 3;
    public static final int NUMBER_OF_DUCK_TYPES = 3;
    public static final int NUMBER_OF_DUCK_ORIENTATIONS = 4;
    public static final int NUMBER_OF_DOG_SPRITES = 9;

    public static final int NUMBER_OF_DUCKS_DEPLOYED = 10;


    public static final int GREENDUCK = 0;
    public static final int REDDUCK = 1;
    public static final int PINKDUCK = 2;

    public static final int DUCK_FLAP_SOUND = R.raw.duck_flap;
    public static final int GUN_SHOT_SOUND = R.raw.gun_shot;
    public static final int DEAD_DUCK_LAND_SOUND = R.raw.dead_duck_land;
    public static final int DEAD_DUCK_FALL_SOUND = R.raw.dead_duck_falls;
    public static final int TITLE_SEQUENCE_SOUND = R.raw.title_screen;
    public static final int STARTING_SEQUENCE_SOUND = R.raw.intro;
    public static final int DOG_BARKING_SOUND = R.raw.dog_bark;
    public static final int PAUSE_SOUND = R.raw.pause;
    public static final int ROUND_CLEAR = R.raw.round_clear;
    public static final int PERFECT_SCORE = R.raw.perfect_score;



    public static final int DIAGONAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int BACK = 2;
    public static final int DEFEAT = 3;

    public static final float DELAY_AFTER_SHOT = 1.0f;
    public static final float DELAY_TO_DISPLAY_SCORE = 2.0f;
    public static final float TIME_ON_SCREEN = 5.0f;


    public static final float GRAVITY = 10.0f;
    public static final float ESCAPE_VELOCITY = -30.0f;


    public static final int BACKGROUND = 1;
    public static final int MIDGROUND = 2;
    public static final int FOREGROUND = 3;

    public static final float GROUND = -0.16667f;

    public static final float DUCK_SPEED = 0.12f;


    static final Map<String , Integer> COLOR_TO_SCORE = new HashMap<String, Integer>() {{
        put("green",    500);
        put("red", 1000);
        put("pink",   1500);
    }};







}
