package com.example.danie.ppd_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Aaron on 12/13/2017.
 */

public class PauseButton extends GameObject {

    protected RectF pauseButtonBox, replayButtonBox, quitButtonBox;

    protected Paint boxPaint, textPaint, paint;

    public boolean paused = false;

    private static final float WIDTH = 0.205f;
    private static final float HEIGHT = 0.07f;
    Bitmap pause, replay, quit;


    public PauseButton() {
        position = new Vector2D(
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

        pause = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("pause", "drawable", GameEngine.context.getPackageName())
        );

        replay = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("replay", "drawable", GameEngine.context.getPackageName())
        );
        quit = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("quit", "drawable", GameEngine.context.getPackageName())
        );

        pauseButtonBox = new RectF(position.x, position.y, position.x + WIDTH, position.y - HEIGHT);

        replayButtonBox = new RectF(0.5f - Camera.screenXDistToWorldXDist(replay.getWidth()/2),
                0.166667f + Camera.screenYDistToWorldYDist(replay.getHeight()) + 0.03f,
                0.5f + Camera.screenXDistToWorldXDist(replay.getWidth()/2),
                0.166667f + 0.03f
        );

        quitButtonBox = new RectF(0.5f - Camera.screenXDistToWorldXDist(quit.getWidth()/2),
                0.166667f - 0.03f,
                0.5f + Camera.screenXDistToWorldXDist(quit.getWidth()/2),
                0.166667f - Camera.screenYDistToWorldYDist(quit.getHeight()) - 0.03f
        );

        layer = GameConstants.FOREGROUND;
    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(Camera.worldRectToScreenRect(pauseButtonBox), boxPaint);
        if (paused) {

//            canvas.drawBitmap(pause, GameEngine.SCREEN_WIDTH/2 - pause.getWidth()/2, GameEngine.SCREEN_HEIGHT/2 - pause.getHeight() - Camera.worldYToScreenY(1 - 0.06f), new Paint());
            canvas.drawBitmap(replay, null, Camera.worldRectToScreenRect(replayButtonBox), null);
            canvas.drawBitmap(quit, null, Camera.worldRectToScreenRect(quitButtonBox), null);
            canvas.drawText("Start", Camera.worldXToScreenX(position.x + WIDTH / 1.3f), Camera.worldYToScreenY(position.y - HEIGHT / 1.5f), textPaint);

        }
        else {

            canvas.drawText("Pause", Camera.worldXToScreenX(position.x + WIDTH / 1.3f), Camera.worldYToScreenY(position.y - HEIGHT / 1.5f), textPaint);
        }
    }

    @Override
    public void onUpdate() {

    }
}
