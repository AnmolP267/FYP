package com.example.anmol.fyp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HowToPlayActivity extends Activity implements View.OnClickListener {

    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        //closes the current activity down
        finish();


    }
}
