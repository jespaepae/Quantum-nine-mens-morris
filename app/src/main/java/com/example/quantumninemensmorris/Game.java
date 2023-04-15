package com.example.quantumninemensmorris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Game extends Activity {

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16,
            tv17, tv18, tv19, tv20, tv21, tv22, tv23, tv24;
    private String turn="X";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        getTextViews();
        setListeners();
    }

    private void getTextViews() {
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv10 = findViewById(R.id.tv10);
        tv11 = findViewById(R.id.tv11);
        tv12 = findViewById(R.id.tv12);
        tv13 = findViewById(R.id.tv13);
        tv14 = findViewById(R.id.tv14);
        tv15 = findViewById(R.id.tv15);
        tv16 = findViewById(R.id.tv16);
        tv17 = findViewById(R.id.tv17);
        tv18 = findViewById(R.id.tv18);
        tv19 = findViewById(R.id.tv19);
        tv20 = findViewById(R.id.tv20);
        tv21 = findViewById(R.id.tv21);
        tv22 = findViewById(R.id.tv22);
        tv23 = findViewById(R.id.tv23);
        tv24 = findViewById(R.id.tv24);
    }

    private void setListeners() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv1.getText().equals("")) {
                    tv1.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv2.getText().equals("")) {
                    tv2.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv3.getText().equals("")) {
                    tv3.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv4.getText().equals("")) {
                    tv4.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv5.getText().equals("")) {
                    tv5.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv6.getText().equals("")) {
                    tv6.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv7.getText().equals("")) {
                    tv7.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv8.getText().equals("")) {
                    tv8.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv9.getText().equals("")) {
                    tv9.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv10.getText().equals("")) {
                    tv10.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv11.getText().equals("")) {
                    tv11.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv12.getText().equals("")) {
                    tv12.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv13.getText().equals("")) {
                    tv13.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv14.getText().equals("")) {
                    tv14.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv15.getText().equals("")) {
                    tv15.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv16.getText().equals("")) {
                    tv16.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv17.getText().equals("")) {
                    tv17.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv18.getText().equals("")) {
                    tv18.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv19.getText().equals("")) {
                    tv19.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv20.getText().equals("")) {
                    tv20.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv21.getText().equals("")) {
                    tv21.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv22.getText().equals("")) {
                    tv22.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv23.getText().equals("")) {
                    tv23.setText(turn);
                    choosePlayer();
                }

            }
        });
        tv24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv24.getText().equals("")) {
                    tv24.setText(turn);
                    choosePlayer();
                }

            }
        });
    }

    private void choosePlayer() {
        if (turn.equals("X")) {
            turn = "O";
        } else {
            turn = "X";
        }
    }
}
