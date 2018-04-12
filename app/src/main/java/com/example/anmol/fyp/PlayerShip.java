package com.example.anmol.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


public class PlayerShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;
    private boolean boosting;
    private final int GRAVITY = -12;

    private int maxY;
    private int minY;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    private Rect hitBox;

    private int shieldStrength;

    public PlayerShip(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_shooter);
        boosting = false;

        maxY = screenY - bitmap.getHeight();
        minY = 0;
        shieldStrength = 2;

        hitBox = new Rect(x, y, bitmap.getWidth() - 10, bitmap.getHeight() - 10);

    }

    public void update() {

        //is boost on?
        if(boosting) {
            speed += 3;
        } else {
            // Slow down
            speed -= 3;
        }

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY;

        if (y < minY) {
            y = minY;
        }

        if(y > maxY) {
            y = maxY;
        }

        int hitboxWidth = bitmap.getWidth() - 10;
        int hitboxHeight = bitmap.getHeight() - 10;

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + hitboxWidth;
        hitBox.bottom = y + hitboxHeight;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }

    public void reduceShieldStrength() {
        shieldStrength--;
    }

}
