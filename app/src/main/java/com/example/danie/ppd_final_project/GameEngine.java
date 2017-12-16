package com.example.danie.ppd_final_project;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
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
    private static final int TIME_BETWEEN_FRAMES = 1000 / DESIRED_FPS;
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
    IndicatorLevel indicatorLevel;
    PauseButton pauseButton;
    boolean completedStartingSequence;
    public static int numberOfDucksPerStage;
    Stack<Duck> duckies = new Stack<>();
    DuckFactory duckFactory;
    boolean outOFBullets = false;
    boolean[] prevFlyingAwayFlags;
    static Context context;
    static int level;
    static boolean levelComplete;
    int maxPotentialLevelScore = 0;
    int levelScore = 0;
    int maxPotentialRoundScore = 0;
    int roundScore = 0;
    boolean pauseButtonPressed = false;
    Stack<Float> deadDuckLandingSpots = new Stack<>();


    public GameEngine(Context context, int numberOfDucksPerStage, Point point, int level, int score) {
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

        dog = new Dog(new BasicPhysicsComponent());
        gameObjects.add(dog);

        duckFactory = new DuckFactory();

        indicatorLevel = new IndicatorLevel(level);
        gameObjects.add(indicatorLevel);

        indicatorShots = new IndicatorShots();
        gameObjects.add(indicatorShots);

        indicatorDucks = new IndicatorDucks();
        gameObjects.add(indicatorDucks);

        indicatorScore = new IndicatorScore();
        indicatorScore.setScore(score);
        gameObjects.add(indicatorScore);

        pauseButton = new PauseButton();
        gameObjects.add(pauseButton);



        prevFlyingAwayFlags = new boolean[numberOfDucksPerStage];

        this.setOnTouchListener(this);
        this.numberOfDucksPerStage = numberOfDucksPerStage;
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),
                R.drawable.background), SCREEN_WIDTH, SCREEN_HEIGHT, true);

        for (int ii = 0; ii < 1; ++ii) {
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
        while (isPlaying) {
            handleGameLogic();
            if (!levelComplete) {
                update();
                draw();
                currentTimeMillis = System.currentTimeMillis();
                DELTA_TIME = (currentTimeMillis - previousTimeMillis) / 1000.0f;
                try {
                    gameThread.sleep(TIME_BETWEEN_FRAMES);
                } catch (InterruptedException e) {

                }
                previousTimeMillis = currentTimeMillis;
            } else {
                goToNextLevel();
                isPlaying = false;
            }
        }
    }

    public void handleGameLogic() {
        if (dog.position.y <= GameConstants.GROUND && dog.layer == GameConstants.BACKGROUND && !completedStartingSequence) {
            completedStartingSequence = true;
        }
        if (completedStartingSequence) {
            boolean hackyAsFuck = false;
            int duckIdx = 0;

            for (GameObject o : gameObjects) {
                if (o instanceof Duck) {
                    hackyAsFuck = true;
                    if (
                            (!((Duck) o).timeToFlyAway && outOFBullets) // Just ran out of bullets
                                    || (((Duck) o).timeToFlyAway && !prevFlyingAwayFlags[duckIdx]) // The duck just started flying away
                            ) {
                        indicatorDucks.hitDuck(false);
                    }

                    ((Duck) o).timeToFlyAway |= outOFBullets;
                    outOFBullets |= ((Duck) o).timeToFlyAway;

                    prevFlyingAwayFlags[duckIdx] = ((Duck) o).timeToFlyAway;
                    duckIdx++;
                }
            }

            if (!hackyAsFuck) {
                dogPopUp();
                if (duckies.empty()) {
                    levelComplete = true;
                } else {
                    maxPotentialRoundScore = 0;
                    roundScore = 0;
                    //the check below covers the case if you are doing 2 duck mode
                    //and hit two ducks. The stack would still contain the position of the
                    //first duck you hit.
                    if (!deadDuckLandingSpots.empty()) {
                        deadDuckLandingSpots.pop();
                    }
                    for (int ii = 0; ii < numberOfDucksPerStage; ++ii) {
                        Duck duck = duckies.pop();
                        duck.physicsComponent.setSpeed((level+1) * 0.5f * GameConstants.DUCK_SPEED);
                        gameObjects.add(duck);
                        indicatorShots.setNumShots(3);
                        outOFBullets = false;
                        maxPotentialRoundScore += GameConstants.COLOR_TO_SCORE.get(duck.getDuckColor());
                        maxPotentialLevelScore += maxPotentialRoundScore;
                    }
                }
            }
        }
    }


    public void update() {
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject gameObject = iterator.next();
            if (!gameObject.destroy) {
                gameObject.onUpdate();
            } else {
                iterator.remove();
            }
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(background, 0.0f, 0.0f, paint);
            for (int ii = GameConstants.BACKGROUND; ii <= GameConstants.FOREGROUND; ++ii) {
                for (GameObject gameObject : gameObjects) {
                    if (gameObject.layer == ii) {
                        gameObject.onDraw(canvas);
                    }
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        if (pauseButtonPressed) {
            pauseButton.paused = true;
            isPlaying = !isPlaying;
            GameSoundHandler.pauseAllSounds();
            GameSoundHandler.playSound(GameConstants.PAUSE_SOUND);
            draw();
        }
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.d("GameThread", "Your threading sucks!");
        }

    }


    public void resume() {
        pauseButton.paused = false;
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        GameSoundHandler.resumeAllSounds();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Camera.worldRectToScreenRect(pauseButton.box).contains(event.getRawX(), event.getRawY())) {
                    if (isPlaying == true) {
                        pauseButtonPressed = true;
                        pause();
                    } else {
                        pauseButtonPressed = false;
                        resume();
                    }
                    break;
                }
                if (!pauseButton.paused) {
                    GameSoundHandler.playSound(GameConstants.GUN_SHOT_SOUND);
                    outOFBullets = indicatorShots.shoot();
                    for (GameObject o : gameObjects) {
                        if (o instanceof Duck) {
                            Vector2D screenPos = Camera.worldToScreen(((Duck) o).position);
                            float delta_x = event.getRawX() - screenPos.x;
                            float delta_y = event.getRawY() - screenPos.y;
                            float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
                            if (distance < 100.0f) {
                                ((Duck) o).isAlive = false;
                                deadDuckLandingSpots.push(o.position.x);
                                indicatorDucks.hitDuck(true);
                                indicatorScore.addToScore(GameConstants.COLOR_TO_SCORE.get(((Duck) o).getDuckColor()));
                                roundScore += GameConstants.COLOR_TO_SCORE.get(((Duck) o).getDuckColor());
                                levelScore += roundScore;

                            }
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

    public void dogPopUp() {
        if (maxPotentialLevelScore != 0) {
            int numDucks;
            float popUpSpot;
            if (roundScore == maxPotentialRoundScore) {
                numDucks = numberOfDucksPerStage;
                popUpSpot = deadDuckLandingSpots.pop();
            } else if (numberOfDucksPerStage == 2 && roundScore < maxPotentialRoundScore && roundScore > 0) {
                numDucks = 1;
                popUpSpot = deadDuckLandingSpots.pop();
            } else {
                numDucks = 0;
                popUpSpot = 0.5f - Camera.screenXToWorldX(dog.current_sprite.getWidth());
            }
            dog.comeUpToFinishRound(numDucks, popUpSpot);
            draw();
            if (numDucks > 0) {
                GameSoundHandler.playSound(GameConstants.GOT_DUCK);
            } else {
                GameSoundHandler.playSound(GameConstants.DOG_LAUGH);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            dog.returnToGrass();
            draw();
        }

    }

    public void goToNextLevel() {
        if (levelScore > 0) {
            GameSoundHandler.playLongSound(GameConstants.ROUND_CLEAR);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            if (levelScore == maxPotentialLevelScore) {
                GameSoundHandler.playSound(GameConstants.PERFECT_SCORE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            GameSoundHandler.releaseResources();
            Intent i_start = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            level++;
            b.putInt(GameConstants.LEVEL, level);
            b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage);
            b.putInt(GameConstants.SCORE, indicatorScore.getScore());
            i_start.putExtras(b);
            context.startActivity(i_start);
        } else {
            GameSoundHandler.playSound(GameConstants.GAME_OVER);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            GameSoundHandler.releaseResources();
            Intent i_start = new Intent(context, StartupActivity.class);
            context.startActivity(i_start);
        }
    }
}
