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

//class which represents the duck in the game
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

    //huge constructor
    //this should be cleaned up sometime
    //but currently initializes all necessary fields
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
        //if the duck is alive, just sift through the sprites in the current row to animate
        if(isAlive) {
            current_sprite = sprites[duckOrientation][frame % NUMBER_OF_DUCK_SPRITES];
        }
        else
        {
            //the duck was shot, now to handle the duck freezing and falling
            duckOrientation = DEFEAT;
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                deathPoint = position.y;
                current_sprite = sprites[duckOrientation][0];
            }
            else
            {
                //display the score as the duck falls
                if(timeSinceShot < DELAY_TO_DISPLAY_SCORE)
                {
                    canvas.drawBitmap(sprites[duckOrientation][2], Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(deathPoint), paint);
                }
                //ensure the duck appears to spin as it falls
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
            GameSoundHandler.getInstance().playSound(GameConstants.DUCK_FLAP_SOUND);//play the duck flap sound
            try {
                Thread.sleep(15);
            }
            catch(InterruptedException e)
            {
            }
            if(timeToFlyAway) {
                flyAway();//if the user ran out of bullets or ran out of time, the duck flutters off
            }
            else
            {
                timeSinceSpawned += GameEngine.DELTA_TIME;//accumulate time since the duck was spawned
                performTimeChecks();//check if it's time to switch direction or sprite
            }
        }
        else
        {
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                timeSinceShot += GameEngine.DELTA_TIME;//accumulate time after being shot
                this.physicsComponent.forward.x = 0.0f;
                this.physicsComponent.forward.y = 0.0f;
            }
            else
            {
                //the duck hasn't started falling yet, so play the falling sound
                if(this.physicsComponent.forward.y <= 0.0f) {
                    GameSoundHandler.getInstance().playSound(GameConstants.DEAD_DUCK_FALL_SOUND);//start playing the falling sound immediately before the duck falls
                }
                    this.physicsComponent.forward.x = 0.0f;
                    this.physicsComponent.forward.y = 5.0f;
                    this.physicsComponent.speed = GRAVITY;
            }
        }
        physicsComponent.update(this);//moves the duck using whatever physics handler was passed in
        checkBorder();//check to see if the duck hit the sides of the screen or hit the top without trying to fly away
        frame++;
    }


    private void performTimeChecks()
    {
        checkOrientationTime();
        checkHorizontalDirectionTime();
        checkVerticalDirectionTime();
    }

    //checks in case the duck has to switch horizontal direction (flying left and right)
    private void checkHorizontalDirectionTime()
    {
        if(frame > 0 && frame % timeToSwitchHorizontalDirection == 0)
        {
            this.physicsComponent.forward.x *= -1.0f;
            flipSprites();
        }
    }

    //checks in case the duck has to switch vertical direction (flying up and down)
    private void checkVerticalDirectionTime()
    {
        if(frame > 0 && frame % timeToSwitchVerticalDirection == 0)
        {
            this.physicsComponent.forward.y *= -1.0f;
        }
    }

    //checks in case the duck has to switch orientation (diagonal, horizontal, or back)
    private void checkOrientationTime()
    {
        if(frame > 0 && frame % timeToSwitchOrientation == 0) {
            duckOrientation = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
        }
    }

    //sets the ducks orientation and velocity to fly straight up and away
    //this happens if the user runs out of bullets or time
    private void flyAway()
    {
        duckOrientation = GameConstants.BACK;
        this.physicsComponent.forward.y = GameConstants.ESCAPE_VELOCITY;
        this.physicsComponent.forward.x = 0.0f;
    }

    //checks to ensure the duck will switch direction if it hits the border of the screen
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

    //returns duck color
    //this is primarily used for determining score in the game engine
    public String getDuckColor()
    {
        return duckColor;
    }


    //flips all sprites to ensure they are in the proper orientation
    public void flipSprites()
    {
        for(int i = 0;i < NUMBER_OF_DUCK_ORIENTATIONS-1; ++i) {
            for (int j = 0; j < NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = flip(sprites[i][j]);
            }
        }
    }

    //flips the sprite
    public Bitmap flip(Bitmap bitmap)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //populates the 2D bitmap array for all the duck sprites
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

    //scales the sprites w.r.t to the screen size so the game is (somewhat) scalable
    public void scaleSprites()
    {
        for(int i = 0;i < NUMBER_OF_DUCK_ORIENTATIONS-1; ++i) {
            for (int j = 0; j < NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = Bitmap.createScaledBitmap(
                        sprites[i][j], Math.round(Camera.worldXDistToScreenXDist(1.0f/11.0f)),
                        Math.round(Camera.worldYDistToScreenYDist(1.0f/11.0f)), false);
            }
        }
        sprites[DEFEAT][0] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][0], Math.round(Camera.worldXDistToScreenXDist(1.0f/11.0f)),
                Math.round(Camera.worldYDistToScreenYDist(1.0f/11.0f)), false);
        sprites[DEFEAT][1] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][1], Math.round(Camera.worldXDistToScreenXDist(1.0f/11.0f)),
                Math.round(Camera.worldYDistToScreenYDist(1.0f/11.0f)), false);
        sprites[DEFEAT][2] = Bitmap.createScaledBitmap(
                sprites[DEFEAT][2], Math.round(Camera.worldXDistToScreenXDist(0.05f)),
                Math.round(Camera.worldYDistToScreenYDist(0.03571f)), false);
    }
}
