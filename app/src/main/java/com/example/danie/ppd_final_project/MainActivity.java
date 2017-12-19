package com.example.danie.ppd_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

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
        gameEngine.pause();
    }

    @Override
    protected void onStop() {

        SharedPreferences prefs = this.getSharedPreferences(StartupActivity.KEY_TOP_SCORE_PREFS, Context.MODE_PRIVATE);
        if (prefs.getInt(StartupActivity.KEY_TOP_SCORE, 0) < gameEngine.roundScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(StartupActivity.KEY_TOP_SCORE, gameEngine.roundScore);
            editor.commit();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
