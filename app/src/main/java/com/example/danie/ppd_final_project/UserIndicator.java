package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/16/17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


// indicates the round to the user, as well as if the round is complete
public class UserIndicator extends GameObject {

    private int round;

    protected Paint paint;

    private float timeToDisplayLevel = 0.0f;
    private boolean isGameOver = false;
    private boolean nextRound = false;

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
        gameOver = Bitmap.createScaledBitmap(
                gameOver, gameOver.getWidth()/2, gameOver.getHeight()/2, false);
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

    public void nextRound()
    {
        nextRound = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        canvas.drawText("R="+Integer.toString(round), Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
        if(timeToDisplayLevel < 1.0f)
        {
            paint.setColor(Color.BLACK);
            timeToDisplayLevel += GameEngine.DELTA_TIME;
            canvas.drawText("Round "+Integer.toString(round), Camera.worldXToScreenX(0.6f), Camera.worldYToScreenY(0.3f), paint);
        }
        if(isGameOver)
        {
            canvas.drawBitmap(gameOver, Camera.worldXToScreenX(0.4f), Camera.worldYToScreenY(0.3f), paint);
        }
        if(nextRound)
        {
            paint.setColor(Color.BLACK);
            canvas.drawText("Round "+Integer.toString(round), Camera.worldXToScreenX(0.6f), Camera.worldYToScreenY(0.3f), paint);
            canvas.drawText("Complete!", Camera.worldXToScreenX(0.62f), Camera.worldYToScreenY(0.2f), paint);
        }
    }

    @Override
    public void onUpdate() {

    }

    private void setLevel(int level)
    {
        if(level >= 0)
        {
            this.round = level;
        }
        else
        {
            this.round = 0;
        }
    }

}
