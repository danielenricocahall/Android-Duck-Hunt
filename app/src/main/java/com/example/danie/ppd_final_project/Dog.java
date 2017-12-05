package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by daniel on 12/4/17.
 */

public class Dog extends GameObject {
    public Vector2D position;
    public boolean readyToJump;
    protected Bitmap bitmap;
    protected Vector2D forward;
    protected Paint paint;
    Bitmap[] sprites;
    int frame = 0;

    public Dog(Context context)
    {
        readyToJump = false;
        sprites = new Bitmap[9];
        populateDogSprites(context);
        bitmap = sprites[0];
        forward = new Vector2D(
                0.0f,
                0.0f);
        forward.y = 0.0f;
        forward.x = new Random().nextFloat();
        forward.normalize();
        position = new Vector2D(0.0f, 1250.0f);
        paint = new Paint();
    }

    public void init()
    {

    }

    public void onDraw(Canvas canvas)
    {
        if(!readyToJump) {
            this.forward.y = 0.0f;
            if (this.position.x < GameView.SCREEN_WIDTH / 2) {
                bitmap = sprites[frame % 5];
            } else {
                bitmap = sprites[5];
                this.forward.x = 0.0f;
                readyToJump = true;
            }
        }
        else
        {
            if(bitmap == sprites[5]) {
                bitmap = sprites[6];
                forward.y = -30.0f;
            }
            else
            {
                bitmap = sprites[7];
                forward.y = 0.0f;
                this.destroy = true;
            }
        }
        canvas.drawBitmap(bitmap, position.x, position.y, paint);
    }

    public void onUpdate()
    {
        float speed = 50.0f; //pixels per second
        Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
        deltaPosition.scalarMultiply(speed * GameView.DELTA_TIME);
        this.position.add(deltaPosition);
        frame++;
    }
    public void populateDogSprites(Context context)
    {
        for (int i=0; i<9; i++){
            int j = i + 1;
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier("dog"+j,"drawable",context.getPackageName()));
            sprites[i] = bitmap;
        }
    }


}
