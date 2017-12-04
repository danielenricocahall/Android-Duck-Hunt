package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by danie on 11/28/2017.
 */

public class Duck extends GameObject {

    public Vector2D position;
    public boolean isAlive;
    protected String bitmapName;
    protected Bitmap bitmap;
    protected Vector2D forward;
    protected Paint paint;
    protected Bitmap[][] sprites;
    int frame = 0;
    int duckOrientation;
    float timeSinceShot;

    public Duck(Context context,
                //String bitmapName,
                float x, float y) {
        forward = new Vector2D(
                0.0f,
                0.0f);
        forward.y = new Random().nextFloat() * -1.0f;
        forward.x = new Random().nextFloat();
        sprites = new Bitmap[4][3];
        duckOrientation = GameConstants.DIAGONAL;//they'll all start diagonally
        int duck_type = new Random().nextInt(2);
        populateDuckSprites(duck_type, context);
        bitmap = sprites[0][new Random().nextInt(2)];//determines their initial flapping position
        boolean isFlipped = new Random().nextInt(1000) % 2 == 0;
        if(isFlipped)
        {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
            flipSprites(matrix);
            forward.x *= -1.0f;
        }
        forward.normalize();
        position = new Vector2D(x, y);
        paint = new Paint();
        isAlive = true;
        timeSinceShot = 0.0f;
    }


    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        if(isAlive) {
            if (frame % 3 == 0) {
                bitmap = sprites[duckOrientation][0];
            } else if (frame % 3 == 2) {
                bitmap = sprites[duckOrientation][1];
            } else {
                bitmap = sprites[duckOrientation][2];
            }
        }
        else
        {
            if(timeSinceShot < 1.0f)
            {
                bitmap = sprites[duckOrientation][0];
            }
            else
            {
                bitmap = sprites[duckOrientation][1];
                if (frame % 2 == 0) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        }
        canvas.drawBitmap(bitmap, position.x, position.y, paint);
    }

    @Override
    public void onUpdate() {
        float speed = 100.0f; //pixels per second
        if(isAlive) {
            Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
            deltaPosition.scalarMultiply(speed *
                    GameView.DELTA_TIME);
            this.position.add(deltaPosition);
            if (this.position.y < GameConstants.HORIZON) {
                duckOrientation = GameConstants.BACK;
            } else {
                duckOrientation = GameConstants.DIAGONAL;
            }
            checkBorder();
        }
        else
        {
            duckOrientation = GameConstants.DEFEAT;
            if(timeSinceShot < 1.0f)
            {
                timeSinceShot += GameView.DELTA_TIME;
                forward.x = 0.0f;
                forward.y = 0.0f;
            }
            else
            {
                forward.x = 0.0f;
                forward.y = 10.0f;
            }
            Vector2D deltaPosition = new Vector2D(forward.x, forward.y);
            deltaPosition.scalarMultiply(speed *
                    GameView.DELTA_TIME);
            this.position.add(deltaPosition);
        }
        frame++;
    }

    public void checkBorder()
    {
        Matrix matrix = new Matrix();
        if (this.position.x > (GameView.getScreenWidth() -
                bitmap.getWidth())) {
            this.position.x =
                    GameView.SCREEN_WIDTH - bitmap.getWidth();
            this.forward = new Vector2D(
                    new Random().nextFloat() - 1.0f,
                    this.forward.y);
            this.forward.normalize();
            matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
            flipSprites(matrix);
        }
        if (this.position.x < 0) {
            this.position.x = 0;
            this.forward = new Vector2D(
                    new Random().nextFloat() + 1.0f,
                    this.forward.y);
            this.forward.normalize();
            matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
            flipSprites(matrix);
        }
    }

    public void flipSprites(Matrix matrix)
    {
        for(int i = 0; i<3; ++i)
        {
            sprites[duckOrientation][i] = Bitmap.createBitmap(sprites[duckOrientation][i], 0, 0, sprites[duckOrientation][i].getWidth(), sprites[duckOrientation][i].getHeight(), matrix, true);
        }
    }

    public void populateDuckSprites(int n, Context context)
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
        for (int i=0; i<3; i++){
            int j = i + 1;
                Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duck_type+"duck"+j,"drawable",context.getPackageName()));
                sprites[GameConstants.DIAGONAL][i] =bitmap;
        }
        for (int i=3; i<6; i++){
            int j = i + 1;
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duck_type+"duck"+j,"drawable",context.getPackageName()));
            sprites[GameConstants.HORIZONTAL][i-3] = bitmap;
        }
        for (int i=6; i<9; i++){
            int j = i + 1;
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duck_type+"duck"+j,"drawable",context.getPackageName()));
            sprites[GameConstants.BACK][i-6] = bitmap;
        }
        for (int i=9; i<11; i++){
            int j = i + 1;
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duck_type+"duck"+j,"drawable",context.getPackageName()));
            sprites[GameConstants.DEFEAT][i-9] = bitmap;
        }
    }
}
