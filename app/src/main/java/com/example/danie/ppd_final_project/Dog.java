package com.example.danie.ppd_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


import static com.example.danie.ppd_final_project.GameConstants.NUMBER_OF_DOG_SPRITES;


/**
 * Created by daniel on 12/4/17.
 */

public class Dog extends GameObject {
    public boolean readyToJump;
    protected Bitmap current_sprite;
    protected Paint paint;
    Bitmap[] sprites;
    int frame = 0;
    DynamicPhysicsComponent physicsComponent;
    float jumpingTime;
    boolean barked;
    boolean finishingRound;


    public Dog(DynamicPhysicsComponent physicsComponent)
    {
        this.physicsComponent = physicsComponent;
        readyToJump = false;
        jumpingTime = 0.0f;
        barked = false;
        sprites = new Bitmap[12];
        populateSprites(GameEngine.context);
        scaleSprites();
        current_sprite = sprites[0];
        this.physicsComponent.forward = new Vector2D(
                0.0f,
                0.0f);
        this.physicsComponent.forward.y = 0.0f;
        this.physicsComponent.forward.x = 0.75f;
        this.physicsComponent.forward.normalize();
        this.physicsComponent.speed = 0.047f;
        position = new Vector2D(0.0f, -0.216667f);
        paint = new Paint();
        layer = GameConstants.FOREGROUND;
        finishingRound = false;

    }

    public void init()
    {

    }

    public void onDraw(Canvas canvas)
    {
        if(!readyToJump) {
            this.physicsComponent.forward.y = 0.0f;
            if (this.position.x < 0.5 - Camera.screenXToWorldX(current_sprite.getWidth())) {
                current_sprite = sprites[frame % 5];
            } else {
                current_sprite = sprites[5];
                this.physicsComponent.forward.x = 0.0f;
                readyToJump = true;
            }
        }
        else
        {
            if(!finishingRound)
            {
                bark();
                jump();
            }
        }
        canvas.drawBitmap(current_sprite, Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
    }

    public void onUpdate()
    {
        this.physicsComponent.update(this);
        frame++;
    }
    public void populateSprites(Context context)
    {
        for (int i=0; i < NUMBER_OF_DOG_SPRITES; i++){
            int j = i + 1;
            sprites[i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier("dog"+j,"drawable",context.getPackageName()));
        }
    }
    public void scaleSprites()
    {
        for (int i=0; i < NUMBER_OF_DOG_SPRITES; i++){
            sprites[i] = Bitmap.createScaledBitmap(
                    sprites[i], GameEngine.SCREEN_WIDTH/6, GameEngine.SCREEN_WIDTH/7, false);;
        }
    }

    public void comeUpToFinishRound(int numDucksShot, float popUpSpot)
    {
        layer = GameConstants.MIDGROUND;
        finishingRound = true;
        position.y = 0.06f;
        position.x = popUpSpot;
        switch(numDucksShot)
        {
            case 1: current_sprite = sprites[8];
                break;
            case 2: current_sprite = sprites[9];
                break;
            case 0: current_sprite = sprites[10];
                break;
        }
    }

    public void returnToGrass()
    {
        position.y = -0.216667f;
        layer = GameConstants.BACKGROUND;
    }

    public void jump()
    {
        jumpingTime+=GameEngine.DELTA_TIME;
        if(jumpingTime < 0.8f) {
            current_sprite = sprites[6];
            this.physicsComponent.forward.y = -GameEngine.SCREEN_HEIGHT*0.006f;
        }
        else if(jumpingTime > 0.8f && this.position.y > GameConstants.GROUND) {
            current_sprite = sprites[7];
            this.physicsComponent.forward.y = GameEngine.SCREEN_HEIGHT*0.006f;
            layer = GameConstants.BACKGROUND;
        }
        else {
            //this.destroy = true;//remove this when layers are added
            this.physicsComponent.forward.y = 0.0f;
            this.physicsComponent.forward.x = 0.0f;
            this.physicsComponent.speed = 0.0f;
        }
    }

    public void bark()
    {
        if(!barked) {
            //GameSoundHandler.stopLongSound();
            GameSoundHandler.getInstance().playSound(GameConstants.DOG_BARKING_SOUND);
            barked = true;
            try
            {
                Thread.sleep(300);
            }
            catch (InterruptedException e)
            {

            }
        }
        GameSoundHandler.getInstance().stopLongSound();
        return;
    }




}
