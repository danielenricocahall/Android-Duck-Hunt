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
import static com.example.danie.ppd_final_project.GameConstants.DIAGONAL;
import static com.example.danie.ppd_final_project.GameConstants.GRAVITY;
import static com.example.danie.ppd_final_project.GameConstants.HORIZONTAL;
import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DUCK_ORIENTATIONS;
import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DUCK_SPRITES;

/**
 * Created by danie on 11/28/2017.
 */

public class Duck extends GameObject {

    public Vector2D position;
    public boolean isAlive;
    protected Bitmap current_sprite;
    protected Vector2D forward;
    protected Paint paint;
    protected Bitmap[][] sprites;
    int frame;
    int duckOrientation;
    float timeSinceShot;
    int timeToSwitchOrientation;
    int timeToSwitchDirection;
    private String duckColor;

    public Duck(Context context, float x, float y, String duckColor) {
        forward = new Vector2D(
                new Random().nextFloat(),
                new Random().nextFloat()*-1.0f);
        sprites = new Bitmap[NUMBER_OF_DUCK_ORIENTATIONS][NUMBER_OF_DUCK_SPRITES];
        duckOrientation = DIAGONAL;//they'll all start diagonally
        this.duckColor = duckColor;
        populateDuckSprites(context);
        int frame = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
        current_sprite = sprites[0][frame];//determines their initial flapping position
        boolean isFlipped = new Random().nextInt(1000) % 2 == 0;
        if(isFlipped)
        {
            flipSprites();
            forward.x *= -1.0f;
        }
        forward.normalize();
        position = new Vector2D(x, y);
        paint = new Paint();
        isAlive = true;
        timeSinceShot = 0.0f;
        timeToSwitchOrientation = new Random().nextInt(40) + 40;//some degree of randomness to change the sprite
        timeToSwitchDirection = new Random().nextInt(50) + 60;//some degree of randomness to change direction
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
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                current_sprite = sprites[duckOrientation][0];
            }
            else
            {
                current_sprite = sprites[duckOrientation][1];
                if (frame % 2 == 0) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(-1, 1, current_sprite.getWidth()/2, current_sprite.getHeight()/2);
                    current_sprite = Bitmap.createBitmap(current_sprite, 0, 0, current_sprite.getWidth(), current_sprite.getHeight(), matrix, true);
                }
            }
        }
        canvas.drawBitmap(current_sprite, position.x, position.y, paint);
    }

    @Override
    public void onUpdate() {
        float speed = 100.0f; //pixels per second
        if(isAlive) {
            Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
            deltaPosition.scalarMultiply(speed *
                    GameEngine.DELTA_TIME);
            this.position.add(deltaPosition);
            if(frame > 0 && frame % timeToSwitchOrientation == 0)
            {
                duckOrientation = new Random().nextInt(NUMBER_OF_DUCK_SPRITES);
            }
            if(frame > 0 && frame % timeToSwitchOrientation == 0)
            {
                forward.x *= -1.0f;
                flipSprites();
            }
        }
        else
        {
            duckOrientation = DEFEAT;
            if(timeSinceShot < DELAY_AFTER_SHOT)
            {
                timeSinceShot += GameEngine.DELTA_TIME;
                forward.x = 0.0f;
                forward.y = 0.0f;
            }
            else
            {
                forward.x = 0.0f;
                forward.y = GRAVITY;
            }
            Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
            deltaPosition.scalarMultiply(speed *
                    GameEngine.DELTA_TIME);
            this.position.add(deltaPosition);
        }
        checkBorder();
        frame++;
    }

    public void checkBorder()
    {
        if (this.position.x > (GameEngine.SCREEN_WIDTH -
                current_sprite.getWidth())) {
            this.position.x =
                    GameEngine.SCREEN_WIDTH - current_sprite.getWidth();
            this.forward = new Vector2D(
                    new Random().nextFloat() - 1.0f,
                    this.forward.y);
            this.forward.normalize();
            flipSprites();
        }
        if (this.position.x < 0) {
            this.position.x = 0;
            this.forward = new Vector2D(
                    new Random().nextFloat() + 1.0f,
                    this.forward.y);
            this.forward.normalize();
            flipSprites();
        }
        if (this.position.y < 0 || (this.position.y > GameEngine.SCREEN_HEIGHT - 600.0f)) {
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
        for(int i = 0; i<NUMBER_OF_DUCK_SPRITES; ++i)
        {
            sprites[DIAGONAL][i] = Bitmap.createBitmap(sprites[DIAGONAL][i], 0, 0, sprites[DIAGONAL][i].getWidth(), sprites[DIAGONAL][i].getHeight(), matrix, true);
            sprites[HORIZONTAL][i] = Bitmap.createBitmap(sprites[HORIZONTAL][i], 0, 0, sprites[HORIZONTAL][i].getWidth(), sprites[HORIZONTAL][i].getHeight(), matrix, true);
            sprites[BACK][i] = Bitmap.createBitmap(sprites[BACK][i], 0, 0, sprites[BACK][i].getWidth(), sprites[BACK][i].getHeight(), matrix, true);
        }
    }

    public void populateDuckSprites(Context context)
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
        }
        sprites[DEFEAT][0] = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier(duckColor+"duck_defeated1","drawable",context.getPackageName()));
        sprites[DEFEAT][1] = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier(duckColor+"duck_defeated2","drawable",context.getPackageName()));
    }
}
