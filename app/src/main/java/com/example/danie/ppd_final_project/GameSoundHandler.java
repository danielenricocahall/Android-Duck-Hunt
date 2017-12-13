package com.example.danie.ppd_final_project;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

/**
 * Created by daniel on 12/10/17.
 */

public abstract class GameSoundHandler {

    public static SoundPool soundPool;
    public static MediaPlayer mediaPlayer;
    public static Context context;
    public static SparseIntArray soundMap = new SparseIntArray();


    public static void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    public static void createOldSoundPool(){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    }

    public static void setContext(Context app_context)
    {
        context = app_context;
    }

    public static void playLongSound(int long_sound)
    {
        mediaPlayer = MediaPlayer.create(context,long_sound);
        mediaPlayer.start();
    }

    public static void stopLongSound()
    {
        if(mediaPlayer.isPlaying()) {
            //mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public static void loadSounds()
    {
        soundMap.put(GameConstants.GUN_SHOT_SOUND, soundPool.load(context, GameConstants.GUN_SHOT_SOUND, 1));
        soundMap.put(GameConstants.DUCK_FLAP_SOUND, soundPool.load(context, GameConstants.DUCK_FLAP_SOUND, 1));
        soundMap.put(GameConstants.DEAD_DUCK_FALL_SOUND, soundPool.load(context, GameConstants.DEAD_DUCK_FALL_SOUND, 1));
        soundMap.put(GameConstants.DEAD_DUCK_LAND_SOUND, soundPool.load(context, GameConstants.DEAD_DUCK_LAND_SOUND, 1));
        soundMap.put(GameConstants.STARTING_SEQUENCE_SOUND, soundPool.load(context, GameConstants.STARTING_SEQUENCE_SOUND, 1));
        soundMap.put(GameConstants.TITLE_SEQUENCE_SOUND, soundPool.load(context, GameConstants.TITLE_SEQUENCE_SOUND, 1));
    }


    public static void playSound(int sound)
    {
        soundPool.play(soundMap.get(sound), 1, 1, 1, 0, 1f);
        try{
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {

        }
    }

    public static void stopAllSounds()
    {
        for(int ii = 0; ii<soundMap.size();++ii)
        {
            stopSound(soundMap.valueAt(ii));
        }
    }

    public static void stopSound(int sound)
    {
        soundPool.stop(soundMap.get(sound));
    }








}
