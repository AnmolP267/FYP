package com.example.anmol.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/*Enemy Ship spawns on the right hand side of the screen and moves to the left
* once it has reached the left side, it'll respawn into the right. contains a hit box for collision detection*/
public class EnemyShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    //for enemies leaving the screen
    private int maxX;
    private int minX;

    //For spawning
    private int maxY;

    private Rect hitBox;


    public EnemyShip(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, true);

        this.maxX = screenX;
        this.maxY = screenY;
        minX = 0;

        Random randomGenerator;
        randomGenerator = new Random();
        speed = randomGenerator.nextInt(6)+10;

        this.x = screenX;
        this.y = randomGenerator.nextInt(maxY) - bitmap.getHeight();

        // Initialize the hit box
        this.hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed){
        // Move to the left
        this.x -= playerSpeed;
        this.x -= speed;
        //respawn when off screen
        if(x < minX-bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+10;
            this.x = maxX;
            this.y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitBox() {
        return hitBox;
    }
}


