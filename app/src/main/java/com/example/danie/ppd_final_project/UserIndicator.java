package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/16/17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


/**
 * Created by Brian on 12/9/2017.
 */

public class UserIndicator extends GameObject {

    private int level;

    protected Paint paint;

    private float timeToDisplayLevel = 0.0f;
    private float timeToDisplayGameOver = 0.0f;
    private boolean isGameOver = false;

    Bitmap gameOver;

    public UserIndicator(int level) {

        setLevel(level);
        layer = GameConstants.FOREGROUND;


        position = new Vector2D(
                0.18f,
                -0.43f
        );

        gameOver = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("game_over","drawable",GameEngine.context.getPackageName()));

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.RIGHT);

    }

    @Override
    public void init() {

    }

    public void gameOver()
    {
        isGameOver = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        canvas.drawText("R="+Integer.toString(level), Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
        if(timeToDisplayLevel < 1.0f)
        {
            paint.setColor(Color.BLACK);
            timeToDisplayLevel += GameEngine.DELTA_TIME;
            canvas.drawText("Round "+Integer.toString(level), Camera.worldXToScreenX(0.6f), Camera.worldYToScreenY(0.3f), paint);
        }
        if(timeToDisplayGameOver < 1.0f && isGameOver)
        {
            timeToDisplayGameOver+=GameEngine.DELTA_TIME;
            canvas.drawBitmap(gameOver, Camera.worldXToScreenX(0.6f), Camera.worldYToScreenY(0.3f), paint);
        }
    }

    @Override
    public void onUpdate() {

    }

    private void setLevel(int level)
    {
        if(level >= 0)
        {
            this.level = level;
        }
        else
        {
            this.level = 0;
        }
    }

}
