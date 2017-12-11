package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DOG_SPRITES;


/**
 * Created by daniel on 12/4/17.
 */

public class Dog extends GameObject {
    public Vector2D position;
    public boolean readyToJump;
    protected Bitmap current_sprite;
    protected Vector2D forward;
    protected Paint paint;
    Bitmap[] sprites;
    int frame = 0;


    public Dog(Context context)
    {
        readyToJump = false;
        sprites = new Bitmap[9];
        populateDogSprites(context);
        current_sprite = sprites[0];
        forward = new Vector2D(
                0.0f,
                0.0f);
        forward.y = 0.0f;
        forward.x = new Random().nextFloat();
        forward.normalize();
        position = new Vector2D(0.0f, GameEngine.SCREEN_HEIGHT*0.73f);
        paint = new Paint();
        layer = GameConstants.FOREGROUND;
    }

    public void init()
    {

    }

    public void onDraw(Canvas canvas)
    {
        if(!readyToJump) {
            this.forward.y = 0.0f;
            if (this.position.x < GameEngine.SCREEN_WIDTH / 2 - current_sprite.getWidth()) {
                current_sprite = sprites[frame % 5];
            } else {
                current_sprite = sprites[5];
                this.forward.x = 0.0f;
                readyToJump = true;
            }
        }
        else
        {
            if(current_sprite == sprites[5]) {
                current_sprite = sprites[6];
                forward.y = -30.0f;
            }
            else
            {
                current_sprite = sprites[7];
                forward.y = 0.0f;
                this.destroy = true;
            }
        }
        canvas.drawBitmap(current_sprite, position.x, position.y, paint);
    }

    public void onUpdate()
    {
        float speed = 50.0f; //pixels per second
        Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
        deltaPosition.scalarMultiply(speed * GameEngine.DELTA_TIME);
        this.position.add(deltaPosition);
        frame++;
    }
    public void populateDogSprites(Context context)
    {
        for (int i=0; i < NUMBER_OF_DOG_SPRITES; i++){
            int j = i + 1;
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier("dog"+j,"drawable",context.getPackageName()));
            sprites[i] = bitmap;
        }
    }


}
