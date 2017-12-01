package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by danie on 11/24/2017.
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {


    volatile boolean isPlaying = false;
    protected Thread gameThread;
    protected SurfaceHolder surfaceHolder;
    private static final int DESIRED_FPS = 30;
    private static final int TIME_BETWEEN_FRAMES = 1000/DESIRED_FPS;
    private long previousTimeMillis;
    private long currentTimeMillis;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Paint paint;
    private int numSides = 3;

    private static int SCREEN_HEIGHT;

    private static int SCREEN_WIDTH;

    public static float DELTA_TIME;

    public GameView(Context context, Point point) {

        super(context);
        surfaceHolder = getHolder();
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        //gameObjects.add(new StarField(600, 3.0f));
        //gameObjects.add(new Polygon(new Vector2D(getScreenWidth()/2.0f, getScreenHeight()/2.0f), 5, 60.0f));
        this.setOnTouchListener(this);
    }

    @Override
    public void run() {
        previousTimeMillis = System.currentTimeMillis();
        while(isPlaying)
        {
            update();
            draw();
            currentTimeMillis = System.currentTimeMillis();
            DELTA_TIME = (currentTimeMillis = previousTimeMillis)/1000.0f;
            try{
                gameThread.sleep(TIME_BETWEEN_FRAMES);
            }
            catch (InterruptedException e)
            {

            }
            previousTimeMillis = currentTimeMillis;
        }
    }

    public void update()
    {
        for(int ii = 0; ii < gameObjects.size(); ++ii)
        {
            gameObjects.get(ii).onUpdate();
        }
    }

    public void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));

            for(int ii = 0; ii < gameObjects.size(); ++ii)
            {
                gameObjects.get(ii).onDraw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause()
    {
        isPlaying = !isPlaying;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {
            Log.d("GameThread", "Your threading sucks!");
        }

    }

    public static void setScreenHeight(int screenHeight) {
        SCREEN_HEIGHT = screenHeight;
    }

    public static void setScreenWidth(int screenWidth) {
        SCREEN_WIDTH = screenWidth;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public void resume()
    {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                gameObjects.add(new Polygon(new Vector2D(event.getX(), event.getY()), numSides, 60.0f));
                numSides++;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;

        }
        return false;
    }


}
