package com.example.anmol.fyp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.CopyOnWriteArrayList;

/*The main sequence of the game that updates and draws at each game loop all the objects*/
public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing = false;
    Thread gameThread = null;
    private PlayerShip player;
    public EnemyShip droid1;
    public EnemyShip droid2;
    public EnemyShip droid3;

    //Used to minimise ConcurrentModificationException
    public CopyOnWriteArrayList<SpaceDust> dusts = new CopyOnWriteArrayList<>();

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder myHolder;
    private int screenX;
    private int screenY;

    private int score;
    private long timeTaken;
    private long timeStarted;
    private int highest;

    private Context context;
    private boolean gameEnded;

    private Explosion explosion;

    
    private int maxBullets = 10;
    private Bullet[] bullets;
    private int nextBullet;

    private MediaPlayer explosionSound;
    private MediaPlayer shootSound;

    public GameView(Context context, int x, int y) {
        super(context);
        this.context = context;

        explosionSound = MediaPlayer.create(context, R.raw.explosion);
        shootSound = MediaPlayer.create(context, R.raw.shoot);

        screenX = x;
        screenY = y;

        myHolder = getHolder();
        paint = new Paint();


        startGame();

    }

    @Override
    public void run() {
        while(playing) {
            update();
            draw();
            control();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //Finger off the screen
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;

            //Finger on the screen
            case MotionEvent.ACTION_DOWN:

                if(event.getX() < screenX / 2) {
                    player.setBoosting();
                } else {
                    shootSound.start();
                    bullets[nextBullet].shoot(player.getHitBox().right,(player.getY() + (player.getBitmap().getHeight()) / 2));
                    nextBullet++;
                    if (nextBullet == maxBullets) {
                        nextBullet = 0;
                    }
                }

                if(gameEnded) {
                    startGame();
                }
                break;

        }
        return true;

    }

    private void update() {

        int offscreen = -120;
        boolean isHit = false;
        /*Collision detection between the player and the enemy ship*/
        if(Rect.intersects(player.getHitBox(), droid1.getHitBox())){
            isHit = true;
            droid1.setX(offscreen);}
        if(Rect.intersects(player.getHitBox(), droid2.getHitBox())){
            isHit = true;
            droid2.setX(offscreen);
        }
        if(Rect.intersects(player.getHitBox(), droid3.getHitBox())){
            isHit = true;
            droid3.setX(offscreen);
        }



        if(isHit) {
            explosion = new Explosion(20, player.getHitBox().right / 2, (player.getY() + (player.getBitmap().getHeight()) / 2));
            explosionSound.start();

            player.reduceShieldStrength();
            if (player.getShieldStrength() < 0) {

                if(score > highest) {
                    highest = score;
                }
                gameEnded = true;
            }

        }

        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].getStatus()) {
                bullets[i].update();
            }
        }

        //BULLET COLLISION DETECTION
        for (int i = 0; i < maxBullets; i++) {
            if (bullets[i].getStatus()) {
                if (droid1.getHitBox().contains(bullets[i].getX(),bullets[i].getY())) {
                    explosion = new Explosion(20, droid1.getHitBox().right / 2, (droid1.getY() + (droid1.getBitmap().getHeight()) / 2));
                    score++;
                    droid1.setX(offscreen);
                    bullets[i].setInactive();
                }

                if (droid2.getHitBox().contains(bullets[i].getX(),bullets[i].getY())) {
                    explosion = new Explosion(20, droid2.getHitBox().right / 2, (droid2.getY() + (droid2.getBitmap().getHeight()) / 2));
                    score++;
                    droid2.setX(offscreen);
                    bullets[i].setInactive();
                }
                if (droid3.getHitBox().contains(bullets[i].getX(),bullets[i].getY())) {
                    explosion = new Explosion(20, droid3.getHitBox().right / 2, (droid3.getY() + (droid3.getBitmap().getHeight()) / 2));
                    score++;
                    droid3.setX(offscreen);
                    bullets[i].setInactive();
                }




            }
        }

        /*Checking whether the bullet is on the screen or not*/
        for (int i = 0; i < maxBullets; i++) {
            if (bullets[i].getStatus()) {

                if(bullets[i].getX() < 0){
                    bullets[i].setInactive();
                }  else if(bullets[i].getX() > screenX) {
                    bullets[i].setInactive();

                }else if(bullets[i].getY() < 0) {
                    bullets[i].setInactive();

                } else if(bullets[i].getY() > screenY){
                    bullets[i].setInactive();

                }
            }
        }



        /*Start explosion if alive*/
        if(explosion != null && explosion.isAlive()) {
            explosion.update();
        }

        player.update();

        droid1.update(player.getSpeed()/2);
        droid2.update(player.getSpeed()/2);
        droid3.update(player.getSpeed()/2);

        for (SpaceDust sd : dusts) {
            sd.update(player.getSpeed());
        }

        if(!gameEnded) {
            timeTaken = System.currentTimeMillis() - timeStarted;
        }


    }

    private void draw() {

        if (myHolder.getSurface().isValid()) {

            canvas = myHolder.lockCanvas();

            //Clear last frame
            canvas.drawColor(Color.argb(255,0,0,0));


           /* // For debugging showing the hitboxes
            // Switch to white pixels
            paint.setColor(Color.argb(255, 255, 255, 255));
            // Draw Hit boxes
            canvas.drawRect(player.getHitBox().left,
                    player.getHitBox().top,
                    player.getHitBox().right,
                    player.getHitBox().bottom,
                    paint);
            canvas.drawRect(droid1.getHitBox().left,
                    droid1.getHitBox().top,
                    droid1.getHitBox().right,
                    droid1.getHitBox().bottom,
                    paint);
            canvas.drawRect(droid2.getHitBox().left,
                    droid2.getHitBox().top,
                    droid2.getHitBox().right,
                    droid2.getHitBox().bottom,paint);
            canvas.drawRect(droid3.getHitBox().left,
                    droid3.getHitBox().top,
                    droid3.getHitBox().right,
                    droid3.getHitBox().bottom,
                    paint);*/


            //Draw player
            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);

            //Draw enemies
            canvas.drawBitmap(droid1.getBitmap(), droid1.getX(), droid1.getY(), paint);
            canvas.drawBitmap(droid2.getBitmap(), droid2.getX(), droid2.getY(), paint);
            canvas.drawBitmap(droid3.getBitmap(), droid3.getX(), droid3.getY(), paint);

            paint.setColor(Color.argb(255,255,255,255));

            for (SpaceDust sd : dusts) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);

            }

            if(explosion != null && explosion.isAlive()) {
                explosion.draw(canvas);
            }

            String formatTime = DateUtils.formatElapsedTime(timeTaken);

            if(!gameEnded){
                //draw the info boxes
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.RED);
                paint.setTextSize(36);
                canvas.drawText("Time:" + formatTime , (screenX / 3) * 2, 30,paint);
                canvas.drawText("Score:" + score + "pts", 30, 30, paint);
                canvas.drawText("Shield:" + player.getShieldStrength(), (screenX / 3) , 30, paint);

            } else {
                // Show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX/2, 100, paint);
                paint.setTextSize(36);
                canvas.drawText("Time:" + formatTime , screenX / 2, 200, paint);
                canvas.drawText("Score:" + score + "pts" ,screenX/2, 160, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }

            paint.setColor(Color.RED);

            for (int i = 0; i < bullets.length; i++) {
                if (bullets[i].getStatus()) {
                    canvas.drawRect(bullets[i].getX(), bullets[i].getY(),
                            bullets[i].getX()+15,bullets[i].getY()+15, paint);
                }
            }

            myHolder.unlockCanvasAndPost(canvas);
        }


    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //if the player quits or pauses the game
    public void pause(){
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Make a new thread and restart it
    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void startGame(){
        //Initialise game objects
        player = new PlayerShip(context, screenX, screenY);
        droid1 = new EnemyShip(context, screenX, screenY);
        droid2 = new EnemyShip(context, screenX, screenY);
        droid3 = new EnemyShip(context, screenX, screenY);
        int numDusts = 40;

        for(int i = 0; i < numDusts; i++) {
            SpaceDust spaceDust = new SpaceDust(screenX, screenY);
            dusts.add(spaceDust);
        }

        bullets = new Bullet[maxBullets];
        for(int i = 0; i < maxBullets; i++) {
            bullets[i] = new Bullet();
        }

        score = 0;
        timeTaken = 0;
        // Get start time
        timeStarted = System.currentTimeMillis();

        gameEnded = false;
    }

}
