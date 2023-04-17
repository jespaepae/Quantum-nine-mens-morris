package com.example.quantumninemensmorris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.BtnPlay);
        btnExit = findViewById(R.id.BtnExit);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }

    private void initGame() {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}