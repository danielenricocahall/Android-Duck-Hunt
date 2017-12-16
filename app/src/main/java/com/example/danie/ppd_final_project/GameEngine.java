package com.example.danie.ppd_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
    //Bitmap background;
    StationaryObject background_top, background_bottom;
    Dog dog;
    IndicatorShots indicatorShots;
    IndicatorDucks indicatorDucks;
    IndicatorScore indicatorScore;
    boolean completedStartingSequence;
    public static int numberOfDucksPerStage;
    Stack<Duck> duckies = new Stack<>();
    DuckFactory duckFactory;
    boolean outOFBullets = false;
    boolean[] prevFlyingAwayFlags;
    static Context context;
    static int level;
    static boolean levelComplete;



    public GameEngine(Context context, int numberOfDucksPerStage, Point point, int level) {
        super(context);
        this.context = context;
        this.level = level;
        levelComplete = false;
        surfaceHolder = getHolder();
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        paint = new Paint();

        gameObjects = new ArrayList<>();

        Camera.init(new Vector2D(point.x, point.y));

        background_top = new StationaryObject(
                R.drawable.background_top,
                SCREEN_WIDTH,
                (int)(SCREEN_HEIGHT * GameConstants.BACKGROUND_TOP_PERCENTAGE)
        );
        background_top.layer = GameConstants.MIDGROUND;
        gameObjects.add(background_top);

        background_bottom = new StationaryObject(
                R.drawable.background_bottom,
                SCREEN_WIDTH,
                (int)(SCREEN_HEIGHT * GameConstants.BACKGROUND_BOTTOM_PERCENTAGE)
        );
        background_bottom.layer = GameConstants.FOREGROUND;
        background_bottom.yPos = (int)(SCREEN_HEIGHT * (1 - GameConstants.BACKGROUND_BOTTOM_PERCENTAGE));
        gameObjects.add(background_bottom);

        dog = new Dog(new BasicPhysicsComponent());
        gameObjects.add(dog);

        duckFactory = new DuckFactory();

        indicatorShots = new IndicatorShots();
        gameObjects.add(indicatorShots);

        indicatorDucks = new IndicatorDucks();
        gameObjects.add(indicatorDucks);

        indicatorScore = new IndicatorScore();
        gameObjects.add(indicatorScore);

        prevFlyingAwayFlags = new boolean[numberOfDucksPerStage];

        this.setOnTouchListener(this);
        this.numberOfDucksPerStage = numberOfDucksPerStage;
        /*background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),
                R.drawable.background), SCREEN_WIDTH, SCREEN_HEIGHT, true);*/
        for(int ii = 0; ii < 1; ++ii)
        {
            duckies.push(duckFactory.makeRandomDuck());
        }
        GameSoundHandler.createSoundPool();
        GameSoundHandler.setContext(context);
        GameSoundHandler.loadSounds();
        completedStartingSequence = false;
        GameSoundHandler.playLongSound(GameConstants.STARTING_SEQUENCE_SOUND);
    }

    @Override
    public void run() {
        previousTimeMillis = System.currentTimeMillis();
        while(isPlaying)
        {
            handleGameLogic();
            if(levelComplete)
            {
                goToNextLevel();
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

    public void handleGameLogic()
    {
        if(dog.destroy && !completedStartingSequence)
        {
            completedStartingSequence = true;
        }
        if(completedStartingSequence)
        {
            boolean hackyAsFuck = false;
            int duckIdx = 0;

            for(GameObject o: gameObjects) {
                if (o instanceof Duck)
                {
                    hackyAsFuck = true;
                    if (
                            (!((Duck) o).timeToFlyAway && outOFBullets) // Just ran out of bullets
                            || (((Duck) o).timeToFlyAway && !prevFlyingAwayFlags[duckIdx]) // The duck just started flying away
                        )
                    {
                        indicatorDucks.hitDuck(false);
                    }

                    ((Duck) o).timeToFlyAway |= outOFBullets;
                    outOFBullets |= ((Duck) o).timeToFlyAway;

                    prevFlyingAwayFlags[duckIdx] = ((Duck) o).timeToFlyAway;
                    duckIdx++;
                }
            }

            if(!hackyAsFuck)
            {
                if(duckies.empty())
                {
                    levelComplete = true;
                }
                else {
                    for (int ii = 0; ii < numberOfDucksPerStage; ++ii) {
                        gameObjects.add(duckies.pop());
                        indicatorShots.setNumShots(3);
                        outOFBullets = false;
                    }
                }

            }
        }
    }

    public void update()
    {
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext();) {
            GameObject gameObject = iterator.next();
            if(!gameObject.destroy)
            {
                gameObject.onUpdate();
            }
            else
            {
                iterator.remove();
            }
        }
    }

    public void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            Canvas canvas = surfaceHolder.lockCanvas();
            //canvas.drawBitmap(background, 0.0f, 0.0f, paint);
            canvas.drawARGB(255, 63, 191, 255);
            for(int ii = GameConstants.BACKGROUND; ii <= GameConstants.FOREGROUND; ++ii) {
                for (GameObject gameObject : gameObjects) {
                    if(gameObject.layer == ii) {
                        gameObject.onDraw(canvas);
                    }
                }
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
                GameSoundHandler.playSound(GameConstants.GUN_SHOT_SOUND);
                outOFBullets = indicatorShots.shoot();
                for(GameObject o: gameObjects)
                {
                    if(o instanceof Duck)
                    {
                        Vector2D screenPos = Camera.worldToScreen(((Duck) o).position);
                        float delta_x = event.getRawX() - screenPos.x;
                        float delta_y = event.getRawY() - screenPos.y;
                        float distance = (float) Math.sqrt(delta_x*delta_x + delta_y*delta_y);
                        if(distance < 100.0f)
                        {
                            ((Duck) o).isAlive = false;
                            indicatorDucks.hitDuck(true);
                            indicatorScore.addToScore(GameConstants.COLOR_TO_SCORE.get(((Duck) o).getDuckColor()));
                        }
                    }
                }
                GameSoundHandler.stopSound(GameConstants.GUN_SHOT_SOUND);

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;

        }
        return false;
    }

    public void goToNextLevel()
    {
        Intent i_start = new Intent(context, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt(GameConstants.LEVEL, level++);
        b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage);
        i_start.putExtras(b);
        context.startActivity(i_start);
    }
}
