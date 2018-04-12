package com.example.anmol.fyp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button playButton;
    private Button howTOButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        howTOButton = (Button) findViewById(R.id.howToButton);
        howTOButton.setOnClickListener(this);


    }



    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.playButton:
                //Create intent to launch new activity when play button is pressed
                i = new Intent(this,GameActivity.class);
                startActivity(i);
                //closes the current activity down
                finish();
                break;

            case R.id.howToButton:

                i = new Intent(this, HowToPlayActivity.class);
                startActivity(i);
                finish();
                break;

            default:
                break;

        }
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
