package com.example.danie.ppd_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Brian on 12/13/2017.
 */

public class StationaryObject extends GameObject {

    protected Bitmap bitmap;
    protected int bitmapId;
    protected Paint paint;
    public int xPos = 0, yPos = 0;

    public StationaryObject(int bitmapId, int xScale, int yScale) {
        this.bitmapId = bitmapId;
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                GameEngine.context.getResources(),
                bitmapId),
                xScale, yScale,
                true
        );

        paint = new Paint();
    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPos, yPos, paint);
    }

    @Override
    public void onUpdate() {

    }

}
