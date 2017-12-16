package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Brian on 12/9/2017.
 */

public class IndicatorScore extends GameObject {

    protected int score;

    protected Paint paint;

    public IndicatorScore() {

        score = 0;
        layer = GameConstants.FOREGROUND;

        position = new Vector2D(
                0.91f,
                -0.5667f
        );

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.RIGHT);

        layer = GameConstants.FOREGROUND;

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(Integer.toString(score), Camera.worldXToScreenX(position.x),Camera.worldYToScreenY(position.y), paint);
        canvas.drawText("SCORE", Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y - 0.066667f), paint);
    }

    @Override
    public void onUpdate() {

    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int points) {
        score += points;
    }

}
