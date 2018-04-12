package com.example.anmol.fyp;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.KeyEvent;

/*Calls the GameView class*/
public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display object to get screen details
        Display display = getWindowManager().getDefaultDisplay();
        //Load screen resolution into the object
        Point size = new Point();
        display.getSize(size);

        //Create gameView object with the screen resolution
        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // If the player hits the back button, quit the app
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }


}