package com.example.danie.ppd_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartupActivity extends Activity {


    protected static final String KEY_TOP_SCORE_PREFS = "KEY_DUCK_HUNT_TOP_SCORE_PREFS";
    protected static final String KEY_TOP_SCORE = "KEY_DUCK_HUNT_TOP_SCORE";
    protected Button gameA_start;
    protected Button gameB_start;
    protected TextView topScoreView;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        mediaPlayer = MediaPlayer.create(this,R.raw.title_screen);
        mediaPlayer.start();

        gameA_start = (Button) findViewById(R.id.gameA_button);
        gameB_start = (Button) findViewById(R.id.gameB_button);
        gameA_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(StartupActivity.this,R.raw.gun_shot);
                mediaPlayer.start();
                Intent i_start = new Intent(StartupActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt(GameConstants.NUMBER_OF_DUCKS, 1); //Your id
                b.putInt(GameConstants.ROUND, 1); //Your id
                b.putInt(GameConstants.SCORE, 0);
                i_start.putExtras(b);
                startActivity(i_start);
            }
        });

        gameB_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(StartupActivity.this,R.raw.gun_shot);
                mediaPlayer.start();
                Intent i_start = new Intent(StartupActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt(GameConstants.NUMBER_OF_DUCKS, 2); //Your id
                b.putInt(GameConstants.ROUND, 1); //Your id
                b.putInt(GameConstants.SCORE, 0);
                i_start.putExtras(b);
                startActivity(i_start);            }
        });

        topScoreView = (TextView)findViewById(R.id.textView_topScore);

        SharedPreferences prefs = this.getSharedPreferences(KEY_TOP_SCORE_PREFS, Context.MODE_PRIVATE);
        int topScore = prefs.getInt(KEY_TOP_SCORE, 0);

        topScoreView.setText(getString(R.string.string_topScore, topScore));
    }


}
