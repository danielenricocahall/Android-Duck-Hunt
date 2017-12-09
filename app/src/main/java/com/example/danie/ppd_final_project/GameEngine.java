package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by danie on 11/24/2017.
 */

public class GameEngine extends SurfaceView implements Runnable, View.OnTouchListener {


    volatile boolean isPlaying = false;
    protected Thread gameThread;
    protected SurfaceHolder surfaceHolder;
    private static final int DESIRED_FPS = 30;
    private static final int TIME_BETWEEN_FRAMES = 1000/DESIRED_FPS;
    private long previousTimeMillis;
    private long currentTimeMillis;
    private ArrayList<GameObject> gameObjects;
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static float DELTA_TIME;
    Paint paint;
    Bitmap background;
    Dog dog;
    IndicatorShots indicatorShots;
    IndicatorDucks indicatorDucks;
    IndicatorScore indicatorScore;
    boolean completedStartingSequence;
    public static final int totalNumberOfDucks = 10;
    Stack<Duck> duckies = new Stack<>();
    DuckFactory duckFactory;


    public GameEngine(Context context, Point point) {
        super(context);
        surfaceHolder = getHolder();
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        paint = new Paint();

        gameObjects = new ArrayList<>();

        dog = new Dog(getContext());
        gameObjects.add(dog);
        duckFactory = new DuckFactory(context);

        indicatorShots = new IndicatorShots(getContext());
        gameObjects.add(indicatorShots);

        indicatorDucks = new IndicatorDucks(getContext());
        gameObjects.add(indicatorDucks);

        indicatorScore = new IndicatorScore(getContext());
        gameObjects.add(indicatorScore);

        this.setOnTouchListener(this);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),
                R.drawable.background), SCREEN_WIDTH, SCREEN_HEIGHT, true);
        for(int ii = 0; ii < totalNumberOfDucks; ++ii)
        {
            duckies.push(duckFactory.makeRandomDuck());
        }
    }

    @Override
    public void run() {
        previousTimeMillis = System.currentTimeMillis();
        while(isPlaying)
        {
            if(dog.destroy && !completedStartingSequence)
            {
                completedStartingSequence = true;
            }
            if(completedStartingSequence && !duckies.empty())
            {
                //there's probably a better way to do this.....
                boolean hackyAsFuck = false;
                for(GameObject o: gameObjects) {
                    if (o instanceof Duck)
                    {
                        hackyAsFuck = true;
                    }
                }

                if(!hackyAsFuck)
                {
                    gameObjects.add(duckies.pop());
                }
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

                indicatorScore.addToScore(50);

                boolean outOFBullets = indicatorShots.shoot();
                boolean duckWasHit = false;

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
                            duckWasHit = true;
                        }
                    }
                }

                indicatorDucks.hitDuck(duckWasHit);

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;

        }
        return false;
    }
}
