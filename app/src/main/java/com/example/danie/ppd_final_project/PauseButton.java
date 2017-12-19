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

    // Locations for the pause button box, replay button, and quit button
    protected RectF pauseButtonBox, replayButtonBox, quitButtonBox;
    // Paint objects for the pause button box and the text in the box
    protected Paint boxPaint, textPaint;
    // If the game is paused or not
    public boolean paused = false;

    // Width of the pause button in world coordinates
    private static final float WIDTH = 0.205f;
    // Height of the pause button in world coordinates
    private static final float HEIGHT = 0.07f;


    Bitmap replay, quit;


    public PauseButton() {
        // Set the position of the button in world coordinates
        position = new Vector2D(
                0.74f,
                -0.377f
        );

        // Set the paint object for the box to black
        boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);

        // Set the paint object for the text to white, text size, right aligned
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        // Get the replay button bitmap
        replay = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("replay", "drawable", GameEngine.context.getPackageName())
        );
        // Get the quit button bit map
        quit = BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                GameEngine.context.getResources().getIdentifier("quit", "drawable", GameEngine.context.getPackageName())
        );

        // Set the location of the pause button
        pauseButtonBox = new RectF(position.x, position.y, position.x + WIDTH, position.y - HEIGHT);

        // Set the location of the replay button in world coordinates
        replayButtonBox = new RectF(0.5f - Camera.screenXDistToWorldXDist(replay.getWidth()/2),
                0.166667f + Camera.screenYDistToWorldYDist(replay.getHeight()) + 0.03f,
                0.5f + Camera.screenXDistToWorldXDist(replay.getWidth()/2),
                0.166667f + 0.03f
        );

        // Set the location of the quit button in world coordinates
        quitButtonBox = new RectF(0.5f - Camera.screenXDistToWorldXDist(quit.getWidth()/2),
                0.166667f - 0.03f,
                0.5f + Camera.screenXDistToWorldXDist(quit.getWidth()/2),
                0.166667f - Camera.screenYDistToWorldYDist(quit.getHeight()) - 0.03f
        );

        // Set the layer, or depth, of the object to the foreground
        layer = GameConstants.FOREGROUND;
    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        // Draw the box for the pause button
        canvas.drawRect(Camera.worldRectToScreenRect(pauseButtonBox), boxPaint);

        // if the game is paused
        if (paused) {
            // Draw the replay and quit buttons
            canvas.drawBitmap(replay, null, Camera.worldRectToScreenRect(replayButtonBox), null);
            canvas.drawBitmap(quit, null, Camera.worldRectToScreenRect(quitButtonBox), null);
            // Change the bause button text to Start
            canvas.drawText("Start", Camera.worldXToScreenX(position.x + WIDTH / 1.3f), Camera.worldYToScreenY(position.y - HEIGHT / 1.5f), textPaint);

        }
        else {
            // Draw Pause in the pause button
            canvas.drawText("Pause", Camera.worldXToScreenX(position.x + WIDTH / 1.3f), Camera.worldYToScreenY(position.y - HEIGHT / 1.5f), textPaint);
        }
    }

    @Override
    public void onUpdate() {

    }
}
