package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

import static com.example.danie.ppd_final_project.GameConstants.BACK;
import static com.example.danie.ppd_final_project.GameConstants.DEFEAT;
import static com.example.danie.ppd_final_project.GameConstants.DELAY_AFTER_SHOT;
import static com.example.danie.ppd_final_project.GameConstants.DELAY_TO_DISPLAY_SCORE;
import static com.example.danie.ppd_final_project.GameConstants.DIAGONAL;
import static com.example.danie.ppd_final_project.GameConstants.GRAVITY;
import static com.example.danie.ppd_final_project.GameConstants.HORIZONTAL;
import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DUCK_ORIENTATIONS;
import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DUCK_SPRITES;

/**
 * Created by danie on 11/28/2017.
 */

public class Duck extends GameObject {

    public boolean isAlive;
    protected Bitmap current_sprite;
    protected float deathPoint;
    protected Paint paint;
    protected Bitmap[][] sprites;

    int frame;
    int duckOrientation;
    float timeSinceShot;
    int timeToSwitchOrientation;
    int timeToSwitchHorizontalDirection;
    int timeToSwitchVerticalDirection;
    float timeSinceSpawned;
    public boolean timeToFlyAway = false;
    private String duckColor;
    DynamicPhysicsComponent physicsComponent;

    public Duck(float x, float y, String duckColor, final DynamicPhysicsComponent physicsComponent) {
        this.physicsComponent = physicsComponent;
        physicsComponent.speed = GameConstants.DUCK_SPEED;
        physicsComponent.forward = new Vector2D(
                new Random().nextFloat()*0.5f+0.25f,
                -1.0f);
        sprites = new Bitmap[NUMBER_OF_DUCK_ORIENTATIONS][NUMBER_OF_DUCK_SPRITES];
        duckOrientation = DIAGONAL;//they'll all start diagonally
        this.duckColor = duckColor;
        populateSprites(GameEngine.context);
        scaleSprites();
        int frame = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
        current_sprite = sprites[GameConstants.DIAGONAL][frame];//determines their initial flapping position
        boolean isFlipped = new Random().nextInt(1000) % 2 == 0;
        if(isFlipped)
        {
            flipSprites();
            this.physicsComponent.forward.x *= -1.0f;
        }
        this.physicsComponent.forward.normalize();
        position = new Vector2D(x, y);
        paint = new Paint();
        isAlive = true;
        timeSinceSpawned = 0.0f;
        timeSinceShot = 0.0f;
        timeToSwitchOrientation = new Random().nextInt(20) + 20;//some degree of randomness to change the sprite
        timeToSwitchHorizontalDirection = new Random().nextInt(60) + 30;//some degree of randomness to change direction
        timeToSwitchVerticalDirection = new Random().nextInt(90) + 60;//some degree of randomness to change direction
        layer = GameConstants.BACKGROUND;
    }


    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        if(isAlive) {
            current_sprite = sprites[duckOrientation][frame % NUMBER_OF_DUCK_SPRITES];
        }
        else
        {
            duckOrientation = DEFEAT;
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                deathPoint = position.y;
                current_sprite = sprites[duckOrientation][0];
            }
            else
            {
                if(timeSinceShot < DELAY_TO_DISPLAY_SCORE)
                {
                    canvas.drawBitmap(sprites[duckOrientation][2], Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(deathPoint), paint);
                }
                current_sprite = sprites[duckOrientation][1];
                if (frame % 2 == 0) {
                    current_sprite = flip(current_sprite);
                }
            }
        }
        canvas.drawBitmap(current_sprite, Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
    }

    @Override
    public void onUpdate() {
        timeToFlyAway |= (timeSinceSpawned > GameConstants.TIME_ON_SCREEN);
        if(isAlive)
        {
            GameSoundHandler.getInstance().playSound(GameConstants.DUCK_FLAP_SOUND);
            if(timeToFlyAway) {
                flyAway();
            }
            else
            {
                timeSinceSpawned += GameEngine.DELTA_TIME;
                performTimeChecks();
            }
        }
        else
        {
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                timeSinceShot += GameEngine.DELTA_TIME;
                this.physicsComponent.forward.x = 0.0f;
                this.physicsComponent.forward.y = 0.0f;
            }
            else
            {
                //the duck hasn't started falling yet, so play the falling sound
                if(this.physicsComponent.forward.y <= 0.0f) {
                    GameSoundHandler.getInstance().playSound(GameConstants.DEAD_DUCK_FALL_SOUND);
                }
                    this.physicsComponent.forward.x = 0.0f;
                    this.physicsComponent.forward.y = GRAVITY;
            }
        }
        physicsComponent.update(this);
        checkBorder();
        frame++;
    }


    private void performTimeChecks()
    {
        checkOrientationTime();
        checkHorizontalDirectionTime();
        checkVerticalDirectionTime();
    }

    private void checkHorizontalDirectionTime()
    {
        if(frame > 0 && frame % timeToSwitchHorizontalDirection == 0)
        {
            this.physicsComponent.forward.x *= -1.0f;
            flipSprites();
        }
    }

    private void checkVerticalDirectionTime()
    {
        if(frame > 0 && frame % timeToSwitchVerticalDirection == 0)
        {
            this.physicsComponent.forward.y *= -1.0f;
        }
    }

    private void checkOrientationTime()
    {
        if(frame > 0 && frame % timeToSwitchOrientation == 0) {
            duckOrientation = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
        }
    }

    private void flyAway()
    {
        duckOrientation = GameConstants.BACK;
        this.physicsComponent.forward.y = GameConstants.ESCAPE_VELOCITY;
        this.physicsComponent.forward.x = 0.0f;
    }

    public void checkBorder()
    {
        if (this.position.x > (1 - Camera.screenXToWorldX(current_sprite.getWidth()))) {
            this.position.x = 1 - Camera.screenXToWorldX(current_sprite.getWidth());
            this.physicsComponent.forward.x *= -1.0f;
            flipSprites();
        }
        if (this.position.x < 0) {
            this.position.x = 0;
            this.physicsComponent.forward.x *= -1.0f;
            flipSprites();
        }
        if (this.position.y < GameConstants.GROUND) {
            if(!isAlive) {
                this.destroy = true;
                GameSoundHandler.getInstance().stopAllSounds();
                //GameSoundHandler.getInstance().pauseAllSounds();
                GameSoundHandler.getInstance().playSound(GameConstants.DEAD_DUCK_LAND_SOUND);
            }
            else
            {
                this.physicsComponent.forward.y *= -1.0f;
            }
        }
        if(this.position.y > 1)
        {
            if(timeToFlyAway) {
                this.destroy = true;
            }
            else
            {
                this.physicsComponent.forward.y *= -1.0f;
            }
        }
    }

    public String getDuckColor()
    {
        return duckColor;
    }


    public void flipSprites()
    {
        for(int i = 0;i < NUMBER_OF_DUCK_ORIENTATIONS-1; ++i) {
            for (int j = 0; j < NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = flip(sprites[i][j]);
            }
        }
    }

    public Bitmap flip(Bitmap bitmap)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void populateSprites(Context context) {
        for (int i = 0; i < NUMBER_OF_DUCK_SPRITES; i++) {
            int j = i + 1;
            sprites[DIAGONAL][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor + "duck_diagonal" + j, "drawable", context.getPackageName()));
            sprites[HORIZONTAL][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor + "duck_horizontal" + j, "drawable", context.getPackageName()));
            sprites[BACK][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor + "duck_back" + j, "drawable", context.getPackageName()));
            sprites[DEFEAT][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor + "duck_defeated" + j, "drawable", context.getPackageName()));
        }

    }

    public void scaleSprites()
    {
        for(int i = 0;i < NUMBER_OF_DUCK_ORIENTATIONS-1; ++i) {
            for (int j = 0; j < NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = Bitmap.createScaledBitmap(
                        sprites[i][j], GameEngine.SCREEN_WIDTH/11, GameEngine.SCREEN_WIDTH/11, false);
            }
        }
        sprites[DEFEAT][0] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][0], GameEngine.SCREEN_WIDTH/11, GameEngine.SCREEN_WIDTH/11, false);
        sprites[DEFEAT][1] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][1], GameEngine.SCREEN_WIDTH/11, GameEngine.SCREEN_WIDTH/11, false);
        sprites[DEFEAT][2] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][2], GameEngine.SCREEN_WIDTH/20, GameEngine.SCREEN_WIDTH/28, false);
    }
}
