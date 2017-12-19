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
    boolean finishingStage;

    //class which represents the dog in the game
    public Dog(DynamicPhysicsComponent physicsComponent) {
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
        this.physicsComponent.speed = GameConstants.DOG_SPEED;
        position = new Vector2D(0.0f, GameConstants.GROUND);
        paint = new Paint();
        layer = GameConstants.FOREGROUND;
        finishingStage = false;
    }

    public void init() {

    }

    public void onDraw(Canvas canvas) {
        //this logic handles the dog walking up to the center of the screen
        if (!readyToJump) {
            this.physicsComponent.forward.y = 0.0f;
            if (this.position.x < 0.5 - Camera.screenXToWorldX(current_sprite.getWidth())) {
                current_sprite = sprites[frame % 5];
            } else {
                current_sprite = sprites[5];
                this.physicsComponent.forward.x = 0.0f;
                readyToJump = true;
            }
        } else {
            //this logic handles the dog bark once he's gotten to the center of the screen
            //and his jump into the bushes
            if (!finishingStage) {
                bark();
                jump();
            }
        }
        canvas.drawBitmap(current_sprite, Camera.worldXToScreenX(position.x), Camera.worldYToScreenY(position.y), paint);
    }

    public void onUpdate() {
        this.physicsComponent.update(this);
        frame++;
    }

    //populates the dog sprite array
    public void populateSprites(Context context) {
        for (int i = 0; i < NUMBER_OF_DOG_SPRITES; i++) {
            int j = i + 1;
            sprites[i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier("dog" + j, "drawable", context.getPackageName()));
        }
    }

    //scaling the sprites so that the game can be played on several screen sizes
    //this scaling isn't perfect, but it's a ballpark that seemed to look okay on the one or two devices tested
    public void scaleSprites() {
        for (int i = 0; i < NUMBER_OF_DOG_SPRITES; i++) {
            sprites[i] = Bitmap.createScaledBitmap(
                    sprites[i], Math.round(Camera.worldXDistToScreenXDist(0.2f)), Math.round(Camera.worldYDistToScreenYDist(1.0f/7.0f)), false);
            ;
        }
    }


    //handles when the dog comes up after each stage (holding up one duck, two duck, or laugh)
    public void comeUpToFinishStage(int numDucksShot, float popUpSpot) {
        layer = GameConstants.MIDGROUND;
        finishingStage = true;
        position.y = 0.06f;
        switch (numDucksShot) {
            case 1:
                current_sprite = sprites[8];
                break;
            case 2:
                current_sprite = sprites[9];
                break;
            case 0:
                current_sprite = sprites[10];
                break;
        }
        position.x = popUpSpot;
        checkBorders();
    }


    public void checkBorders()
    {
        if (position.x > (1 - Camera.screenXToWorldX(current_sprite.getWidth()))) {
            // shift the dog over a bit so the sprite doesn't get cutoff at screen edges
            position.x = 1 - Camera.screenXToWorldX(current_sprite.getWidth());
        }
        if (position.x < 0) {
            // shift the dog over a bit so the sprite doesn't get cutoff at screen edges
            position.x = 0;
        }
    }

    //ensures the dog goes back into the grass after coming up for the end of the stage
    public void returnToGrass() {
        position.y = GameConstants.GROUND;
        layer = GameConstants.BACKGROUND;
    }

    //handles the dog jumping behind the bush logic
    public void jump() {
        jumpingTime += GameEngine.DELTA_TIME;
        if (jumpingTime < GameConstants.DOG_JUMPING_TIME) {
            current_sprite = sprites[6];
            this.physicsComponent.forward.y = GameConstants.DOG_JUMPING_SPEED;
        } else if (jumpingTime > GameConstants.DOG_JUMPING_TIME && this.position.y > GameConstants.GROUND) {
            current_sprite = sprites[7];
            this.physicsComponent.forward.y = -GameConstants.DOG_JUMPING_SPEED;
            layer = GameConstants.BACKGROUND;
        } else {
            //this.destroy = true;//remove this when layers are added
            this.physicsComponent.forward.y = 0.0f;
            this.physicsComponent.forward.x = 0.0f;
            this.physicsComponent.speed = 0.0f;
        }
    }

    //handles the dog barking logic
    public void bark() {
        if (!barked) {
            GameSoundHandler.getInstance().stopLongSound();
            GameSoundHandler.getInstance().playSound(GameConstants.DOG_BARKING_SOUND);
            barked = true;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {

            }
        }
    }


}
