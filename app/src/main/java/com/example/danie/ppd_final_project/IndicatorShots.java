package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Brian on 12/9/2017.
 */

public class IndicatorShots extends GameObject {

    private int numShots ;
    protected Bitmap bmpShot, bmpBullet;
    protected Paint paint;
    protected Vector2D shotPosition;
    protected Vector2D[] bulletPositions;

    public IndicatorShots(Context context) {
        numShots = 3;
        populateSprites(context);
        paint = new Paint();

        shotPosition = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.11f,
                GameEngine.SCREEN_HEIGHT * 0.96f
        );
        bulletPositions = new Vector2D[3];
        bulletPositions[0] = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.10f,
                GameEngine.SCREEN_HEIGHT * 0.92f
        );
        bulletPositions[1] = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.13f,
                GameEngine.SCREEN_HEIGHT * 0.92f
        );
        bulletPositions[2] = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.16f,
                GameEngine.SCREEN_HEIGHT * 0.92f
        );

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(bmpShot, shotPosition.x, shotPosition.y, paint);

        if (numShots <= 0) return;

        // Draw first bullet
        canvas.drawBitmap(bmpBullet, bulletPositions[0].x, bulletPositions[0].y, paint);

        if (numShots <= 1) return;

        // Draw second bullet
        canvas.drawBitmap(bmpBullet, bulletPositions[1].x, bulletPositions[1].y, paint);

        if (numShots <= 2) return;

        // Draw third bullet
        canvas.drawBitmap(bmpBullet, bulletPositions[2].x, bulletPositions[2].y, paint);

    }

    @Override
    public void onUpdate() {

    }

    private void populateSprites(Context context) {
        bmpShot = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("shot", "drawable", context.getPackageName())
        );
        bmpBullet = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("bullet", "drawable", context.getPackageName())
        );
    }

    public int getNumShots() {
        return numShots;
    }

    public boolean shoot() {
        numShots--;
        if(numShots <= 0) {
            return true;
        }
        return false;
    }
    public void setNumShots(int numShots)
    {
        if(numShots > 0 && numShots < 4)
        {
            this.numShots = numShots;
        }
    }


}
