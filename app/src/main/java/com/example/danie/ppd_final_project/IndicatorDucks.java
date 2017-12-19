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
    private int numDucksServiced = 0;

    public IndicatorDucks() {

        populateSprites(GameEngine.context);
        populatePositions();

        paint = new Paint();
        layer = GameConstants.FOREGROUND;

        hits = new boolean[NUM_DUCKS];
        Arrays.fill(hits, false);

        layer = GameConstants.FOREGROUND;

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawBitmap(bmpHit, Camera.worldXToScreenX(hitPosition.x), Camera.worldYToScreenY(hitPosition.y), paint);
        canvas.drawBitmap(bmpBlueLines, Camera.worldXToScreenX(blueLinesPosition.x), Camera.worldYToScreenY(blueLinesPosition.y), paint);

        for(int i = 0; i < NUM_DUCKS; i++){
            canvas.drawBitmap(
                    hits[i] ? bmpDuckRed : bmpDuckWhite,
                    Camera.worldXToScreenX(duckPositions[i].x), Camera.worldYToScreenY(duckPositions[i].y), paint
            );
        }

    }

    @Override
    public void onUpdate() {

    }

    // This method updates the indicator
    public void hitDuck(boolean gotDuck) {

        // This should never happen, but it's handled
        // to avoid a null reference exception
        if (numDucksServiced > NUM_DUCKS - 1) return;

        if(gotDuck){
            hits[numDucksServiced] = true; // Setting this true adds a red duck
            numDucksHit++;                 // Keep track of number of hits
        }

        numDucksServiced++;
    }


    private void populateSprites(Context context) {
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
                0.27f,
                -0.55f
        );

        blueLinesPosition = new Vector2D(
                0.36f,
                -0.6f
        );

        Vector2D duckStartPos = new Vector2D(
                0.36f,
                -0.55f
        );
        float duckSpacing = 0.03f;

        duckPositions = new Vector2D[NUM_DUCKS];

        for(int i = 0; i < duckPositions.length; i++){
            duckPositions[i] = new Vector2D(
                    duckStartPos.x + (i * duckSpacing),
                    duckStartPos.y
            );
        }
    }
}
