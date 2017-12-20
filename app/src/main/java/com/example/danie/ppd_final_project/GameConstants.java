package com.example.danie.ppd_final_project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 12/3/17.
 */

public class GameConstants {

    // (12/3/2017) I think this is bad practice, but it's convenient for such a small project.../
    // (12/17/2017) Oh god this thing became gigantic
    // Well, these are all the constants used within the game, conveniently defined
    // in one central location
    // I think most of the names are pretty self explanatory

    private GameConstants(){};

    public static final String NUMBER_OF_DUCKS = "Number Of Ducks";
    public static final String ROUND = "Round";
    public static final String SCORE = "Score";

    public static final int NUMBER_OF_DUCK_SPRITES = 3;
    public static final int NUMBER_OF_DUCK_TYPES = 3;
    public static final int NUMBER_OF_DUCK_ORIENTATIONS = 4;
    public static final int NUMBER_OF_DOG_SPRITES = 12;

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
    public static final int GOT_DUCK = R.raw.got_duck;
    public static final int DOG_LAUGH = R.raw.dog_laugh;
    public static final int GAME_OVER = R.raw.game_over;
    public static final int YOU_FAIL = R.raw.fail;

    public static final int DIAGONAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int BACK = 2;
    public static final int DEFEAT = 3;

    public static final float DELAY_AFTER_SHOT = 0.5f;
    public static final float DELAY_TO_DISPLAY_SCORE = 1.0f;
    public static final float TIME_ON_SCREEN = 5.0f;
    public static final float SHOOTING_RADIUS = 75.0f;

    public static final float DOG_JUMPING_TIME = 0.42f;
    public static final float DOG_JUMPING_SPEED = -Camera.worldYDistToScreenYDist(0.01f);

    public static final float DOG_SPEED = 0.09f;

    public static final float GRAVITY = 0.6f;
    public static final float ESCAPE_VELOCITY = -1.5f;

    public static final int BACKGROUND = 1;
    public static final int MIDGROUND = 2;
    public static final int FOREGROUND = 3;

    public static final float GROUND = -0.216667f;

    public static final float DUCK_SPEED = 0.64f;

    // Constants concerning background image placement
    public static final float BACKGROUND_TOP_PERCENTAGE = 0.70f;
    public static final float BACKGROUND_BOTTOM_PERCENTAGE = 0.40f;

    static final Map<String , Integer> COLOR_TO_SCORE = new HashMap<String, Integer>() {{
        put("green",    500);
        put("red", 1000);
        put("pink",   1500);
    }};

}
