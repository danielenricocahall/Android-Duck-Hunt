package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Arrays;

/**
 * Created by Brian on 12/9/2017.
 */

public class IndicatorDucks extends GameObject {

    static private final int NUM_DUCKS = 10;

    protected Bitmap bmpHit, bmpBlueLines, bmpDuckWhite, bmpDuckRed;
    protected Bitmap bmpCur;
    protected Paint paint;
    protected Vector2D hitPosition, blueLinesPosition;
    protected Vector2D[] duckPositions;

    protected boolean[] hits;
    private int numDucksHit = 0;

    public IndicatorDucks(Context context) {

        populateBitmaps(context);
        populatePositions();

        paint = new Paint();

        hits = new boolean[NUM_DUCKS];
        Arrays.fill(hits, false);

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(bmpHit, hitPosition.x, hitPosition.y, paint);
        canvas.drawBitmap(bmpBlueLines, blueLinesPosition.x, blueLinesPosition.y, paint);

        for(int i = 0; i < NUM_DUCKS; i++){
            canvas.drawBitmap(
                    hits[i] ? bmpDuckRed : bmpDuckWhite,
                    duckPositions[i].x, duckPositions[i].y, paint
            );
        }

    }

    @Override
    public void onUpdate() {

    }

    public void hitDuck(boolean gotDuck) {
        if(gotDuck){
            hits[numDucksHit] = true;
        }
        numDucksHit++;
    }

    private void populateBitmaps(Context context) {
        bmpHit = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("hit", "drawable", context.getPackageName())
        );
        bmpBlueLines = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("blue_lines", "drawable", context.getPackageName())
        );
        bmpDuckWhite = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("indicator_duck_white", "drawable", context.getPackageName())
        );
        bmpDuckRed = BitmapFactory.decodeResource(
                context.getResources(),
                context.getResources().getIdentifier("indicator_duck_red", "drawable", context.getPackageName())
        );
    }

    private void populatePositions() {

        hitPosition = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.27f,
                GameEngine.SCREEN_HEIGHT * 0.93f
        );

        blueLinesPosition = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.36f,
                GameEngine.SCREEN_HEIGHT * 0.96f
        );

        Vector2D duckStartPos = new Vector2D(
                GameEngine.SCREEN_WIDTH * 0.36f,
                GameEngine.SCREEN_HEIGHT * 0.93f
        );
        float duckSpacing = GameEngine.SCREEN_WIDTH * 0.03f;

        duckPositions = new Vector2D[NUM_DUCKS];

        for(int i = 0; i < duckPositions.length; i++){
            duckPositions[i] = new Vector2D(
                    duckStartPos.x + (i * duckSpacing),
                    duckStartPos.y
            );
        }


    }

}
