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
    protected Vector2D[] bulletPositions;

    public IndicatorShots() {
        numShots = 3;
        populateSprites(GameEngine.context);
        paint = new Paint();

        position = new Vector2D(
                0.11f,
                -0.6f
        );
        bulletPositions = new Vector2D[3];
        bulletPositions[0] = new Vector2D(
                0.10f,
                -0.53333f
        );
        bulletPositions[1] = new Vector2D(
                0.13f,
                -0.53333f
        );
        bulletPositions[2] = new Vector2D(
                0.16f,
                -0.53333f
        );
        layer = GameConstants.FOREGROUND;


        layer = GameConstants.FOREGROUND;

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(bmpShot, Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);

        if (numShots <= 0) return;

        // Draw first bullet
        canvas.drawBitmap(bmpBullet, Camera.worldXToScreenX(bulletPositions[0].x), Camera.worldYToScreenY(bulletPositions[0].y), paint);

        if (numShots <= 1) return;

        // Draw second bullet
        canvas.drawBitmap(bmpBullet, Camera.worldXToScreenX(bulletPositions[1].x), Camera.worldYToScreenY(bulletPositions[1].y), paint);

        if (numShots <= 2) return;

        // Draw third bullet
        canvas.drawBitmap(bmpBullet, Camera.worldXToScreenX(bulletPositions[2].x), Camera.worldYToScreenY(bulletPositions[2].y), paint);

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

    // Call this method to inform the indicator that
    // the user has shot a bullet.
    // The indicator will update its visual feedback
    // and return a boolean indicating if the player
    // is out of bullets
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
