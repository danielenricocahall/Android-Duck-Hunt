package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/16/17.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


/**
 * Created by Brian on 12/9/2017.
 */

public class IndicatorLevel extends GameObject {

    private int level;

    protected Paint paint;

    private float timeToDisplayLevel = 0.0f;

    public IndicatorLevel(int level) {

        setLevel(level);
        layer = GameConstants.FOREGROUND;


        position = new Vector2D(
                0.18f,
                -0.43f
        );

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.RIGHT);
        /*Typeface currentTypeFace =   paint.getTypeface();
        Typeface bold = Typeface.create(currentTypeFace, Typeface.);
        paint.setTypeface(bold);*/

    }

    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        canvas.drawText("R="+Integer.toString(level), Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
        if(timeToDisplayLevel < 1.0f)
        {
            paint.setColor(Color.BLACK);
            timeToDisplayLevel += GameEngine.DELTA_TIME;
            canvas.drawText("Level "+Integer.toString(level), Camera.worldXToScreenX(0.6f), Camera.worldYToScreenY(0.3f), paint);
        }
    }

    @Override
    public void onUpdate() {

    }

    private void setLevel(int level)
    {
        if(level >= 0)
        {
            this.level = level;
        }
        else
        {
            this.level = 0;
        }
    }

}
