package com.example.danie.ppd_final_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by danie on 11/24/2017.
 */

public class Polygon extends GameObject {

    protected Vector2D position;

    protected int numberOfSides;
    protected  Vector2D points[];
    protected Paint paint;
    protected float radius;
    protected float accumulatedAngle = 0.0f;

    public Polygon(Vector2D position, int numberOfSides, float radius) {
        this.position = position;
        if(numberOfSides <= 2) {
            numberOfSides = 3;
        }
        if(radius <= 0.0f)
        {
            radius = 0.0f;
        }
        this.numberOfSides = numberOfSides;

        this.radius = radius;

        paint = new Paint();
        paint.setColor(Color.argb(255, 0, 255, 0));
        paint.setStrokeWidth(4.0f);
        points = new Vector2D[numberOfSides];
        calculatePoints();

    }

    protected void calculatePoints()
    {
        float deltaAngle = (float)(2.0 * Math.PI/numberOfSides);
        double cumulativeRadians = deltaAngle;
        for(int ii = 0; ii < numberOfSides; ++ii)
        {
            points[ii] = new Vector2D(radius * (float)Math.cos(cumulativeRadians + accumulatedAngle),
                    radius * (float)Math.sin(cumulativeRadians + accumulatedAngle));
            cumulativeRadians += deltaAngle;
        }
       // points[points.length-1] =
    }


    @Override
    public void init() {

    }

    @Override
    public void onDraw(Canvas canvas) {

        Vector2D temp;
        Vector2D temp2;
        int length = points.length;
        for(int ii = 0; ii < length-1; ++ii )
        {

            temp = new Vector2D(position);
            temp2 = new Vector2D(position);

            temp.add(points[ii]);
            temp2.add(points[ii+1]);
            //temp2 = Vector2D.addVectors(position, points[ii+1]);
            canvas.drawLine(temp.dimensions[0], temp.dimensions[1], temp2.dimensions[0], temp2.dimensions[1], paint);
        }
        temp = new Vector2D(position);
        temp2 = new Vector2D(position);
        temp.add(points[0]);
        temp2.add(points[length-1]);
        canvas.drawLine(temp.dimensions[0], temp.dimensions[1], temp2.dimensions[0], temp2.dimensions[1], paint);
    }

    @Override
    public void onUpdate() {

        accumulatedAngle += (float)(Math.PI/20.0f) * GameView.DELTA_TIME;
        calculatePoints();

    }



}
