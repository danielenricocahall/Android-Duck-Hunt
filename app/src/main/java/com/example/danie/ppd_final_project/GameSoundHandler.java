package com.example.danie.ppd_final_project;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by daniel on 12/10/17.
 */

public class GameSoundHandler implements Runnable {

    public SoundPool soundPool;
    public static MediaPlayer mediaPlayer;
    public static Context context;
    public SparseIntArray soundMap = new SparseIntArray();

    public BlockingQueue<Integer> sounds = new LinkedBlockingQueue<>();
    public BlockingQueue<Integer> soundsStopIDs = new LinkedBlockingQueue<>();

    volatile boolean isPlaying = true;
    private static GameSoundHandler gameSoundHandler;

    private GameSoundHandler()
    {


    }


    public static GameSoundHandler getInstance()
    {
        if(gameSoundHandler == null)
        {
            gameSoundHandler = new GameSoundHandler();
            gameSoundHandler.setContext(GameEngine.context);
            gameSoundHandler.createSoundPool();
            gameSoundHandler.loadSounds();
            return gameSoundHandler;
       }
       else
        {
            return gameSoundHandler;
        }
    }
    public void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(10)
                .build();
    }

    @SuppressWarnings("deprecation")
    public void createOldSoundPool(){
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
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void loadSounds()
    {
        soundMap.put(GameConstants.GUN_SHOT_SOUND, soundPool.load(context, GameConstants.GUN_SHOT_SOUND, 1));
        soundMap.put(GameConstants.DUCK_FLAP_SOUND, soundPool.load(context, GameConstants.DUCK_FLAP_SOUND, 1));
        soundMap.put(GameConstants.DEAD_DUCK_FALL_SOUND, soundPool.load(context, GameConstants.DEAD_DUCK_FALL_SOUND, 1));
        soundMap.put(GameConstants.DEAD_DUCK_LAND_SOUND, soundPool.load(context, GameConstants.DEAD_DUCK_LAND_SOUND, 1));
        soundMap.put(GameConstants.STARTING_SEQUENCE_SOUND, soundPool.load(context, GameConstants.STARTING_SEQUENCE_SOUND, 1));
        soundMap.put(GameConstants.TITLE_SEQUENCE_SOUND, soundPool.load(context, GameConstants.TITLE_SEQUENCE_SOUND, 1));
        soundMap.put(GameConstants.PAUSE_SOUND, soundPool.load(context, GameConstants.PAUSE_SOUND, 1));
        soundMap.put(GameConstants.ROUND_CLEAR, soundPool.load(context, GameConstants.ROUND_CLEAR, 1));
        soundMap.put(GameConstants.PERFECT_SCORE, soundPool.load(context, GameConstants.PERFECT_SCORE, 1));
        soundMap.put(GameConstants.GOT_DUCK, soundPool.load(context, GameConstants.GOT_DUCK, 1));
        soundMap.put(GameConstants.DOG_LAUGH, soundPool.load(context, GameConstants.DOG_LAUGH, 1));
        soundMap.put(GameConstants.GAME_OVER, soundPool.load(context, GameConstants.GAME_OVER, 1));
        soundMap.put(GameConstants.YOU_FAIL, soundPool.load(context, GameConstants.YOU_FAIL, 1));
        soundMap.put(GameConstants.DOG_BARKING_SOUND, soundPool.load(context, GameConstants.DOG_BARKING_SOUND, 1));
    }


    public void playSound(int sound)
    {
         //sounds.push(soundPool.play(soundMap.get(sound), 1, 1, 1, 0, 1f));
        sounds.add(soundMap.get(sound));
    }

   public void stopAllSounds()
    {
        while(!soundsStopIDs.isEmpty())
        {
            try {
                soundPool.stop(soundsStopIDs.take());
            }
            catch (InterruptedException e)
            {

            }
        }
        stopLongSound();
    }



    public void pauseAllSounds()
    {
        soundPool.autoPause();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }



    public void releaseResources()
    {
        soundPool.release();
        mediaPlayer.release();
    }


    public void resumeAllSounds()
    {
        soundPool.autoResume();
        mediaPlayer.start();
    }

    @Override
    public void run() {
        while(isPlaying) {
            if (!(sounds.isEmpty())) {
                try {
                    while (!sounds.isEmpty()) {
                        soundsStopIDs.add(soundPool.play(sounds.take(), 1, 1, 1, 0, 1f));
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }








}
