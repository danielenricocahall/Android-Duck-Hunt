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
    int timeToSwitchDirection;
    float timeSinceSpawned;
    public boolean timeToFlyAway = false;
    private String duckColor;
    DynamicPhysicsComponent physicsComponent;


    public Duck(float x, float y, String duckColor, final DynamicPhysicsComponent physicsComponent) {
        this.physicsComponent = physicsComponent;
        this.physicsComponent.speed = 100.0f;
        physicsComponent.forward = new Vector2D(
                new Random().nextFloat(),
                new Random().nextFloat()*-1.0f);
        sprites = new Bitmap[NUMBER_OF_DUCK_ORIENTATIONS][NUMBER_OF_DUCK_SPRITES];
        duckOrientation = DIAGONAL;//they'll all start diagonally
        this.duckColor = duckColor;
        populateSprites(GameEngine.context);
        int frame = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
        current_sprite = sprites[0][frame];//determines their initial flapping position
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
        timeToSwitchOrientation = new Random().nextInt(40) + 40;//some degree of randomness to change the sprite
        timeToSwitchDirection = new Random().nextInt(50) + 60;//some degree of randomness to change direction
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
                    canvas.drawBitmap(sprites[duckOrientation][2],position.x, deathPoint, paint);
                }
                current_sprite = sprites[duckOrientation][1];
                if (frame % 2 == 0) {
                    current_sprite = flip(current_sprite);
                }
            }
        }
        canvas.drawBitmap(current_sprite, position.x, position.y, paint);
    }

    @Override
    public void onUpdate() {
        timeSinceSpawned += GameEngine.DELTA_TIME;
        timeToFlyAway |= (timeSinceSpawned > GameConstants.TIME_ON_SCREEN);
        if(timeToFlyAway && isAlive)
        {
            flyAway();
        }
        if(isAlive) {
            GameSoundHandler.playSound(GameConstants.DUCK_FLAP_SOUND);
            performTimeChecks();
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
                    GameSoundHandler.playSound(GameConstants.DEAD_DUCK_FALL_SOUND);
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
        checkDirectionTime();
    }

    private void checkDirectionTime()
    {
        if(frame > 0 && frame % timeToSwitchDirection == 0)
        {
            this.physicsComponent.forward.x *= -1.0f;
            flipSprites();
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
        if (this.position.x > (GameEngine.SCREEN_WIDTH -
                current_sprite.getWidth())) {
            this.position.x =
                    GameEngine.SCREEN_WIDTH - current_sprite.getWidth();
            this.physicsComponent.setForward(new Vector2D(
                    this.physicsComponent.forward.x * - 1.0f,
                    this.physicsComponent.forward.y));
            this.physicsComponent.forward.normalize();
            flipSprites();
        }
        if (this.position.x < 0) {
            this.position.x = 0;
            this.physicsComponent.setForward(new Vector2D(
                    this.physicsComponent.forward.x * -1.0f,
                    this.physicsComponent.forward.y));
            this.physicsComponent.forward.normalize();
            flipSprites();
        }
        if (this.position.y > GameEngine.SCREEN_HEIGHT - GameEngine.SCREEN_HEIGHT*0.3f) {
            this.destroy = true;
            GameSoundHandler.stopAllSounds();
            GameSoundHandler.playSound(GameConstants.DEAD_DUCK_LAND_SOUND);
        }
        if(this.position.y < 0)
        {
            this.destroy = true;
        }
    }

    public String getDuckColor()
    {
        return duckColor;
    }

    public void flipSprites()
    {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, current_sprite.getWidth()/2, current_sprite.getHeight()/2);
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

    public void populateSprites(Context context)
    {
        for (int i=0; i<NUMBER_OF_DUCK_SPRITES; i++){
            int j = i + 1;
            sprites[DIAGONAL][i] =  BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor+"duck_diagonal"+j,"drawable",context.getPackageName()));
            sprites[HORIZONTAL][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor+"duck_horizontal"+j,"drawable",context.getPackageName()));
            sprites[BACK][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor+"duck_back"+j,"drawable",context.getPackageName()));
            sprites[DEFEAT][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor+"duck_defeated"+j,"drawable",context.getPackageName()));
        }
    }
}
