package com.example.danie.ppd_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Bundle b = getIntent().getExtras();
        final int numberOfDucks = b.getInt(GameConstants.NUMBER_OF_DUCKS);
        final int level = b.getInt(GameConstants.ROUND);
        final int score = b.getInt(GameConstants.SCORE);

        gameEngine = new GameEngine(this, numberOfDucks, point, level,score);
        setContentView(gameEngine);
        HomeWatcher homeWatcher = new HomeWatcher(this);
        homeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                gameEngine.pauseButtonPressed = true;
                gameEngine.pause();
            }
        });
        homeWatcher.startWatch();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScore();
        gameEngine.pause();
    }

    @Override
    protected void onStop() {
        saveScore();
        super.onStop();
    }

    private void saveScore() {
        // Get the SharedPreferences that the top score is stored in
        SharedPreferences prefs = this.getSharedPreferences(StartupActivity.KEY_TOP_SCORE_PREFS, Context.MODE_PRIVATE);
        // If the current score is greater than the stored score
        if (prefs.getInt(StartupActivity.KEY_TOP_SCORE, 0) < gameEngine.indicatorScore.getScore()) {
            // Replace the stored score with the current score
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(StartupActivity.KEY_TOP_SCORE, gameEngine.indicatorScore.getScore());
            // Save the preferences
            editor.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
