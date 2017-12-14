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


    public Dog(DynamicPhysicsComponent physicsComponent)
    {
        this.physicsComponent = physicsComponent;
        readyToJump = false;
        sprites = new Bitmap[9];
        populateSprites(GameEngine.context);
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

            GameSoundHandler.stopLongSound();
            GameSoundHandler.playLongSound(GameConstants.DOG_BARKING_SOUND);
            if(current_sprite == sprites[5]) {
                current_sprite = sprites[6];
                this.physicsComponent.forward.y = -30.0f;
            }
            else
            {
                current_sprite = sprites[7];
                this.physicsComponent.forward.y = 0.0f;
                this.destroy = true;

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
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier("dog"+j,"drawable",context.getPackageName()));
            sprites[i] = bitmap;
        }
    }


}
