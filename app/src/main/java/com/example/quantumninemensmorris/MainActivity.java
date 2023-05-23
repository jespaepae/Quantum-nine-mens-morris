package com.example.quantumninemensmorris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnExit, btnPlayAI, btnAIVsAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayAI = findViewById(R.id.BtnPlayAI);
        btnPlay = findViewById(R.id.BtnPlay);
        btnExit = findViewById(R.id.BtnExit);
        btnAIVsAI = findViewById(R.id.BtnAIVsAI);

        btnPlayAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAIGame();
            }
        });

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

        btnAIVsAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAIVsAIGame();
            }
        });
    }

    private void initGame() {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    private void initAIGame() {
        Intent intent = new Intent(this, GameVsAI.class);
        startActivity(intent);
    }

    private void initAIVsAIGame() {
        Intent intent = new Intent(this, AIVsAI.class);
        startActivity(intent);
    }
}