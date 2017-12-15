package com.example.danie.ppd_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Aaron on 12/13/2017.
 */

public class PauseButton extends GameObject {

    protected Vector2D location;
    protected RectF box;

    protected Paint boxPaint, textPaint,paint;

    public boolean paused = false;

    private static final float WIDTH = 0.205f;
    private static final float HEIGHT = 0.07f;
    Bitmap pause;


    public PauseButton() {
        location = new Vector2D(
                0.74f,
                -0.377f
        );

        boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);
        paint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        box = new RectF(location.x, location.y, location.x + WIDTH, location.y - HEIGHT);
        pause = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("pause", "drawable", GameEngine.context.getPackageName())
        );
        layer = GameConstants.FOREGROUND;
    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(Camera.worldRectToScreenRect(box), boxPaint);
        if (paused) {
            canvas.drawBitmap(pause, Camera.worldXToScreenX(0.5f - Camera.screenXToWorldX(pause.getWidth())), GameEngine.SCREEN_HEIGHT/2, new Paint());
            canvas.drawText("Start", Camera.worldXToScreenX(location.x + WIDTH / 1.3f), Camera.worldYToScreenY(location.y - HEIGHT / 1.5f), textPaint);

        }
        else {

            canvas.drawText("Pause", Camera.worldXToScreenX(location.x + WIDTH / 1.3f), Camera.worldYToScreenY(location.y - HEIGHT / 1.5f), textPaint);
        }
    }

    @Override
    public void onUpdate() {

    }
}
