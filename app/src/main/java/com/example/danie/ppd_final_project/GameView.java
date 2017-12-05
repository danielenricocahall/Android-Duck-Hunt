package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Iterator;
import java.util.Random;

/**
 * Created by danie on 11/24/2017.
 */

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {


    volatile boolean isPlaying = false;
    protected Thread gameThread;
    protected SurfaceHolder surfaceHolder;
    private static final int DESIRED_FPS = 35;
    private static final int TIME_BETWEEN_FRAMES = 1000/DESIRED_FPS;
    private long previousTimeMillis;
    private long currentTimeMillis;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static float DELTA_TIME;
    Paint paint;
    Bitmap background;
    Dog dog;
    boolean completedStartingSequence;

    public GameView(Context context, Point point) {
        super(context);
        surfaceHolder = getHolder();
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        paint = new Paint();
        dog = new Dog(getContext());
        gameObjects.add(dog);
        this.setOnTouchListener(this);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),
                R.drawable.background), SCREEN_WIDTH, SCREEN_HEIGHT, true);
    }

    @Override
    public void run() {
        previousTimeMillis = System.currentTimeMillis();
        while(isPlaying)
        {
            if(dog.destroy && !completedStartingSequence)
            {
                for(int ii = 0; ii < 5; ++ii) {
                    gameObjects.add(new Duck(getContext(), new Random().nextInt(SCREEN_WIDTH), 1100.0f));
                }
                completedStartingSequence = true;
            }
            update();
            draw();
            currentTimeMillis = System.currentTimeMillis();
            DELTA_TIME = (currentTimeMillis - previousTimeMillis)/1000.0f;
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
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext();) {
            GameObject gameObject = iterator.next();
            if(gameObject.destroy)
            {
                iterator.remove();
            }
            else
            {
                gameObject.onUpdate();
            }
        }
    }

    public void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(background, 0.0f, 0.0f, paint);
            for(GameObject gameObject: gameObjects)
            {
                gameObject.onDraw(canvas);
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
                for(GameObject o: gameObjects)
                {
                    if(o instanceof Duck)
                    {
                        float delta_x = event.getRawX() - ((Duck) o).position.x;
                        float delta_y = event.getRawY() - ((Duck) o).position.y;
                        float distance = (float) Math.sqrt(delta_x*delta_x + delta_y*delta_y);
                        if(distance < 100.0f)
                        {
                            ((Duck) o).isAlive = false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;

        }
        return false;
    }
}
