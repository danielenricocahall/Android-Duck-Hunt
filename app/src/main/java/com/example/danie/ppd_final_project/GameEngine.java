package com.example.danie.ppd_final_project;


import android.content.Context;
import android.content.Intent;
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
    StationaryObject background_top, background_bottom;
    Dog dog;
    Thread soundThread;
    IndicatorShots indicatorShots;
    IndicatorDucks indicatorDucks;
    IndicatorScore indicatorScore;
    UserIndicator userIndicator;
    PauseButton pauseButton;
    boolean completedStartingSequence;
    public static int numberOfDucksPerStage;
    Stack<Duck> duckies = new Stack<>();
    DuckFactory duckFactory;
    boolean outOFBullets = false;
    static Context context;
    static int round;
    static boolean roundComplete;
    boolean pauseButtonPressed = false;
    Stack<Float> deadDuckLandingSpots = new Stack<>();
    int numDucksHitThisStage;
    int numDucksHitThisRound = 0;


    public GameEngine(Context context, int numberOfDucksPerStage, Point point, int round, int score) {
        super(context);
        this.context = context;
        this.round = round;
        roundComplete = false;
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

        userIndicator = new UserIndicator(round);
        gameObjects.add(userIndicator);

        indicatorShots = new IndicatorShots();
        gameObjects.add(indicatorShots);

        indicatorDucks = new IndicatorDucks();
        gameObjects.add(indicatorDucks);

        indicatorScore = new IndicatorScore();
        indicatorScore.setScore(score);
        gameObjects.add(indicatorScore);

        pauseButton = new PauseButton();
        gameObjects.add(pauseButton);

        this.setOnTouchListener(this);
        this.numberOfDucksPerStage = numberOfDucksPerStage;
        for(int ii = 0; ii < GameConstants.NUMBER_OF_DUCKS_DEPLOYED; ++ii)
        {
            duckies.push(duckFactory.makeRandomDuck());
        }
        completedStartingSequence = false;
        GameSoundHandler.getInstance().playLongSound(GameConstants.STARTING_SEQUENCE_SOUND);
    }



    @Override
    public void run() {
        // runs the game thread
        // it's a bit different than the one we implemented in class
        // because handleGameLogic can take a while due to the delay
        // required to play the tune after each round, which then throws off delta_t
        // and gives the ducks a jerky movement when first spawned
        while (isPlaying) {
            handleGameLogic();
            previousTimeMillis = System.currentTimeMillis();
            if (!roundComplete) {
                update();
                draw();
                currentTimeMillis = System.currentTimeMillis();
                DELTA_TIME = (currentTimeMillis - previousTimeMillis) / 1000.0f;
                try {
                    gameThread.sleep(TIME_BETWEEN_FRAMES);
                } catch (InterruptedException e) {

                }
            } else {
                goToNextRound();
                isPlaying = false;
            }
        }
    }

    public void handleGameLogic() {
        if (dog.position.y <= GameConstants.GROUND && dog.layer == GameConstants.BACKGROUND && !completedStartingSequence) {
            completedStartingSequence = true;
        }
        if (completedStartingSequence) {
            boolean readyToDeployMoreDucks = true;
            for (GameObject o : gameObjects) {
                if (o instanceof Duck) {
                    readyToDeployMoreDucks = false;
                }
            }
            if (readyToDeployMoreDucks) {
                handleEndOfStage();
                if (duckies.empty()) {
                    roundComplete = true;
                } else {
                    addMoreDucks();
                }
            }
        }
    }


    // updates each game object in the list
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

    // draws each game object in order of layers
    // so the objects in the background are drawn first, midground second, and foreground last
    // this enables some of the nifty animations we have
    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawARGB(255, 63, 191, 255);
            for(int ii = GameConstants.BACKGROUND; ii <= GameConstants.FOREGROUND; ++ii) {
                for (GameObject gameObject : gameObjects) {
                    if (gameObject.layer == ii) {
                        gameObject.onDraw(canvas);
                    }
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    // pauses the game, plays the pause sound, and displays the restart/quit options
    // the pauseButtonPressed flag is used because onPause() is called after finish()
    // and we don't want to play the pause sound every time our activity ends
    public void pause() {
        if (pauseButtonPressed) {
            pauseButton.paused = true;
            isPlaying = !isPlaying;
            GameSoundHandler.getInstance().isPlaying = !isPlaying;
            GameSoundHandler.getInstance().pauseAllSounds();
            GameSoundHandler.getInstance().playSound(GameConstants.PAUSE_SOUND);
            draw();
        }
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.d("GameThread", "Error pausing!");
        }

    }


    //resumes the game after being paused
    public void resume() {
        pauseButtonPressed = false;
        pauseButton.paused = false;
        isPlaying = true;
        GameSoundHandler.getInstance().isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        soundThread = new Thread(GameSoundHandler.getInstance());
        soundThread.start();
        GameSoundHandler.getInstance().resumeAllSounds();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //handles if the user pressed the pause/start button
                if (Camera.worldRectToScreenRect(pauseButton.pauseButtonBox).contains(event.getRawX(), event.getRawY())) {
                    if (isPlaying) {
                        pauseButtonPressed = true;
                        pause();
                    } else {
                        resume();
                    }
                    break;
                }
                if (!pauseButton.paused) {
                    // ensures the shot sound doesn't play if the game is paused
                    // or if the user is out of bullets
                    if(!outOFBullets) {
                        GameSoundHandler.getInstance().playSound(GameConstants.GUN_SHOT_SOUND);
                    }
                    outOFBullets = indicatorShots.shoot();
                    // checks if the user actually hit a duck
                    for (GameObject o : gameObjects) {
                        if (o instanceof Duck) {
                            if(!((Duck) o).timeToFlyAway) {
                                Vector2D screenPos = Camera.worldToScreen(((Duck) o).position);
                                float delta_x = event.getRawX() - screenPos.x; // difference in x position between shot and duck
                                float delta_y = event.getRawY() - screenPos.y; // difference in y position between the shot and duck
                                float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
                                if (distance < GameConstants.SHOOTING_RADIUS && ((Duck) o).isAlive) {
                                    shootDuck(((Duck) o));
                                }
                                // if the user ran out of bullets and the duck is alive, the duck should fly away
                                else if(outOFBullets && ((Duck) o).isAlive){
                                    ((Duck) o).timeToFlyAway = true;
                                }
                            }
                        }
                    }
                }
                else {
                    // handles if the user pressed restart or quit after pressing pause
                    // restart - starts the game from Round 1
                    // quit - brings the user back to main menu
                    if (Camera.worldRectToScreenRect(pauseButton.replayButtonBox).contains(event.getRawX(), event.getRawY())) {
                        GameSoundHandler.getInstance().stopAllSounds();
                        Intent i_start = new Intent(context, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage); //Your id
                        b.putInt(GameConstants.ROUND, 1); //Your id
                        b.putInt(GameConstants.SCORE, 0);
                        i_start.putExtras(b);
                        context.startActivity(i_start);
                    }
                    else if (Camera.worldRectToScreenRect(pauseButton.quitButtonBox).contains(event.getRawX(), event.getRawY())) {
                        GameSoundHandler.getInstance().stopAllSounds();
                        Intent i_start = new Intent(context, StartupActivity.class);
                        context.startActivity(i_start);
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

    // sets the status of the duck from alive to dead
    // records the position where it was shot for the dog to collect and appear
    // adds to the current stage and round scores
    // increments the number of ducks per stage and round accumulators
    public void shootDuck(Duck duck)
    {
        duck.isAlive = false;
        deadDuckLandingSpots.push(duck.position.x);
        indicatorDucks.hitDuck(true);
        indicatorScore.addToScore(GameConstants.COLOR_TO_SCORE.get(duck.getDuckColor()));
        numDucksHitThisStage++;
        numDucksHitThisRound++;
    }

    // pops a 1 or two ducks off of the stack (depending on the Game Mode A or B)
    // and adds them to the game object list, which places them in the game to be updated and drawn
    // also resets the out of bullets flag, sets number of bullets available to 3
    public void addMoreDucks()
    {
        numDucksHitThisStage = 0;
        //the check below covers the case if you are doing 2 duck mode
        //and hit two ducks. The stack would still contain the position of the
        //first duck you hit.
        if (!deadDuckLandingSpots.empty()) {
            deadDuckLandingSpots.pop();
        }
        for (int ii = 0; ii < numberOfDucksPerStage; ++ii) {
            Duck duck = duckies.pop();
            duck.physicsComponent.setSpeed(0.5f * GameConstants.DUCK_SPEED + 0.1f * round); // the speed of each duck is linear with respect to round
            // so later rounds have faster ducks, presumably making it more difficult
            gameObjects.add(duck);
            indicatorShots.setNumShots(3);
            outOFBullets = false;
        }
    }

    // handles the end of a stage (which is after 1 duck or 2 ducks, depending on the mode)
    // the dog pops up holding up one duck, two ducks, or laughing based on how many ducks the user killed,
    // and the location is based on where the duck was shot. If no ducks were killed, he appears at the center of the screen
    // also updates the duck indicators at the bottom - if a duck was hit, it should have been set red earlier
    // otherwise, it's set to white
    // also, either a victorious tune plays, or the dog laughs at you (depending on the number of ducks hit
    public void handleEndOfStage() {
        GameSoundHandler.getInstance().purgeSounds();
        if (duckies.size() < 10) {
            float popUpSpot;
            if(numDucksHitThisStage > 0){
                popUpSpot = deadDuckLandingSpots.pop();
                GameSoundHandler.getInstance().playSound(GameConstants.GOT_DUCK);
            } else {
                popUpSpot = 0.5f - Camera.screenXToWorldX(dog.current_sprite.getWidth());
                GameSoundHandler.getInstance().playSound(GameConstants.DOG_LAUGH);
            }
            dog.comeUpToFinishStage(numDucksHitThisStage, popUpSpot);
            draw();
            // delay to play the tune/laugh
            try {
                soundThread.sleep(1500);
            } catch (InterruptedException e) {
            }
            for(int i = 0; i < (numberOfDucksPerStage - numDucksHitThisStage); i++){
                indicatorDucks.hitDuck(false);
            }
            dog.returnToGrass();
            draw();
        }

    }

    // returns how many ducks need to be killed in a round to progress to the next one
    private int ducksRequiredToProgress()
    {
        if(round < 10)
        {
            return 6;
        }
        else if(round < 13)
        {
            return 7;
        }
        else if(round < 16)
        {
            return 8;
        }
        else if(round < 20)
        {
            return 9;
        }
        else
        {
            return 10;
        }

    }

    // either sends the user to the next round if they successfully killed enough ducks
    // or sends them to the main menu if they failed. Also plays a tune based on
    // the results
    public void goToNextRound() {
        GameSoundHandler.getInstance().stopAllSounds();
        if (ducksRequiredToProgress() <= numDucksHitThisRound) {
            userIndicator.nextRound();
            draw();
            GameSoundHandler.getInstance().playLongSound(GameConstants.ROUND_CLEAR);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            if (numDucksHitThisRound == GameConstants.NUMBER_OF_DUCKS_DEPLOYED) {
                GameSoundHandler.getInstance().playSound(GameConstants.PERFECT_SCORE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            Intent i_start = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            round++;
            b.putInt(GameConstants.ROUND, round);
            b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage);
            b.putInt(GameConstants.SCORE, indicatorScore.getScore());
            i_start.putExtras(b);
            context.startActivity(i_start);
        } else {
            userIndicator.gameOver();
            draw();
            GameSoundHandler.getInstance().playSound(GameConstants.GAME_OVER);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            Intent i_start = new Intent(context, StartupActivity.class);
            context.startActivity(i_start);
        }
    }
}
