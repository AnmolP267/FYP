package com.example.anmol.fyp;

/* The Bullet class is drawn as a point that uses x and y co-ordinates
* to travel through the screen*/
public class Bullet {

    private int x, y;

    int speed = 25;

    // Is the bullet currently in action?
    private boolean isActive;

    public Bullet() {

        // Inactive until fired
        isActive = false;

        x = 0;
        y = 0;
    }

    public boolean shoot(int startX, int startY) {
        if (!isActive) {
            x = startX;
            y = startY;

            isActive = true;
            return true;
        }

        // Bullet already active
        return false;
    }

    public void update(){
        // Move the bullet
        x = x + speed;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

}