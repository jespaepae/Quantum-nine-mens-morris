package com.example.quantumninemensmorris;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Game extends Activity {

    private ArrayList<TextView> textViews;
    private String turn="X";
    private String phase = "Placing";
    private Integer xPiecesPlaced = 9, oPiecesPlaced = 9, xPieces = 0, oPieces = 0, millsBefore,
        millsAfter, moving = -1;
    private Button btnReset;
    private ArrayList<Integer> board;
    private Boolean newMill= false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        board = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            board.add(2);
        }
        btnReset = findViewById(R.id.btnReset);
        showTurn();
        showPhase();
        textViews = new ArrayList<>();
        getTextViews();
        setListeners();
    }

    private void setListeners() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turn = "X";
                phase = "Placing";
                xPiecesPlaced = 9;
                oPiecesPlaced = 9;
                xPieces = 0;
                oPieces = 0;
                newMill = false;
                for (TextView tv : textViews) {
                    tv.setText("");
                }
                for (int i = 0; i < board.size(); i++) {
                    board.set(i, 2);
                }
                showTurn();
                showPhase();
            }
        });
        for (TextView tv : textViews) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (phase) {
                        case "Placing":
                            if (tv.getText().equals("") && !newMill) {
                                if(turn.equals("X")) {
                                    millsBefore = numberOfMills(1);
                                    xPiecesPlaced--;
                                    xPieces++;
                                    board.set(textViews.indexOf(tv), 1);
                                    millsAfter = numberOfMills(1);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(Game.this, "Player X formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    millsBefore = numberOfMills(0);
                                    oPiecesPlaced--;
                                    oPieces++;
                                    board.set(textViews.indexOf(tv), 0);
                                    millsAfter = numberOfMills(0);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(Game.this, "Player O formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                tv.setText(turn);
                                if(!newMill)choosePlayer();
                                choosePhase();
                                showTurn();
                                showPhase();
                            } else if(newMill) {
                                if(turn.equals("X") && tv.getText().equals("O")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                } else if (turn.equals("O") && tv.getText().equals("X")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                }
                            }
                            break;
                        case "Moving":
                            if(!tv.getText().equals("") && tv.getText().equals(turn) && moving < 0) {
                                moving = textViews.indexOf(tv);
                                tv.setTypeface(null, Typeface.BOLD);
                                Toast.makeText(Game.this, "Moving piece", Toast.LENGTH_SHORT).show();
                            } else if (moving >= 0 && !newMill) {
                                if(turn.equals("X")) {
                                    board.set(moving, 2);
                                    textViews.get(moving).setText("");
                                    millsBefore = numberOfMills(1);
                                    xPiecesPlaced--;
                                    xPieces++;
                                    board.set(textViews.indexOf(tv), 1);
                                    millsAfter = numberOfMills(1);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(Game.this, "Player X formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    board.set(moving, 2);
                                    textViews.get(moving).setText("");
                                    millsBefore = numberOfMills(0);
                                    oPiecesPlaced--;
                                    oPieces++;
                                    board.set(textViews.indexOf(tv), 0);
                                    millsAfter = numberOfMills(0);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(Game.this, "Player O formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                tv.setText(turn);
                                if(!newMill)choosePlayer();
                                choosePhase();
                                showTurn();
                                showPhase();
                                moving = -1;
                            } else if(newMill) {
                                if(turn.equals("X") && tv.getText().equals("O")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                } else if (turn.equals("O") && tv.getText().equals("X")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                }
                            }
                            break;
                        case "Flying":
                            break;
                        default:
                            finish();
                    }

                }
            });
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switch (phase) {
                        case "Placing":
                            break;
                        case "Moving":

                    }
                    return false;
                }
            });
        }

    }

    private void getTextViews() {
        for(int i=0; i < 24; i++) {
            int textViewId = getResources().getIdentifier("tv" + (i+1), "id", getPackageName());
            TextView tv = findViewById(textViewId);
            textViews.add(tv);
        }
    }

    private void choosePlayer() {
        if (turn.equals("X")) {
            turn = "O";
        } else {
            turn = "X";
        }
    }

    private void choosePhase() {
        if(phase.equals("Placing")){
            if(xPiecesPlaced.equals(0) && oPiecesPlaced.equals(0)) {
                phase = "Moving";
            }
        }
    }

    private void showTurn() {
        TextView tvTurn = findViewById(R.id.tvTurn);
        if(turn.equals("X")) {
            tvTurn.setText("Player X");
        } else {
            tvTurn.setText("Player 0");
        }
    }

    private void showPhase() {
        TextView tvPhase = findViewById(R.id.tvPhase);
        switch (phase) {
            case "Placing":
                tvPhase.setText("Phase 1: Placing Pieces");
                break;
            case "Moving":
                tvPhase.setText("Phase 2: Moving Pieces");
                break;
            case "Flying":
                tvPhase.setText("Phase 3: 'Flying'");
        }
    }

    private Integer numberOfMills(Integer num) {
        Integer res = 0;
        if(board.get(0).equals(num) && board.get(1).equals(num) && board.get(2).equals(num)) {
            res++;
        }
        if (board.get(3).equals(num) && board.get(4).equals(num) && board.get(5).equals(num)) {
            res++;
        }
        if (board.get(6).equals(num) && board.get(7).equals(num) && board.get(8).equals(num)) {
            res++;
        }
        if (board.get(9).equals(num) && board.get(10).equals(num) && board.get(11).equals(num)) {
            res++;
        }
        if (board.get(12).equals(num) && board.get(13).equals(num) && board.get(14).equals(num)) {
            res++;
        }
        if (board.get(15).equals(num) && board.get(16).equals(num) && board.get(17).equals(num)) {
            res++;
        }
        if (board.get(18).equals(num) && board.get(19).equals(num) && board.get(20).equals(num)) {
            res++;
        }
        if (board.get(21).equals(num) && board.get(22).equals(num) && board.get(23).equals(num)) {
            res++;
        }
        if (board.get(0).equals(num) && board.get(9).equals(num) && board.get(21).equals(num)) {
            res++;
        }
        if (board.get(3).equals(num) && board.get(10).equals(num) && board.get(18).equals(num)) {
            res++;
        }
        if (board.get(6).equals(num) && board.get(11).equals(num) && board.get(15).equals(num)) {
            res++;
        }
        if (board.get(1).equals(num) && board.get(4).equals(num) && board.get(7).equals(num)) {
            res++;
        }
        if (board.get(16).equals(num) && board.get(19).equals(num) && board.get(22).equals(num)) {
            res++;
        }
        if (board.get(8).equals(num) && board.get(12).equals(num) && board.get(17).equals(num)) {
            res++;
        }
        if (board.get(5).equals(num) && board.get(13).equals(num) && board.get(20).equals(num)) {
            res++;
        }
        if (board.get(2).equals(num) && board.get(14).equals(num) && board.get(23).equals(num)) {
            res++;
        }
        return res;
    }

    private ArrayList<Integer> availableMovesOf(Integer piece) {
        ArrayList<Integer> res = new ArrayList<>();
        switch (piece) {
            case 0:
                res.add(0);
                if(boxIsEmpty(1)) {
                    res.add(1);
                }
                if (boxIsEmpty(9)){
                    res.add(9);
                }
                break;
            case 1:
                res.add(1);
                if(boxIsEmpty(0)) {
                    res.add(0);
                }
                if (boxIsEmpty(2)){
                    res.add(2);
                }
                if (boxIsEmpty(4)){
                    res.add(4);
                }
                break;
            case 2:
                res.add(2);
                if(boxIsEmpty(1)) {
                    res.add(1);
                }
                if (boxIsEmpty(14)){
                    res.add(14);
                }
                break;
            case 3:
                res.add(3);
                if(boxIsEmpty(4)) {
                    res.add(4);
                }
                if (boxIsEmpty(10)){
                    res.add(10);
                }
                break;
            case 4:
                res.add(4);
                if(boxIsEmpty(1)) {
                    res.add(1);
                }
                if (boxIsEmpty(3)){
                    res.add(3);
                }
                if (boxIsEmpty(5)){
                    res.add(5);
                }
                if (boxIsEmpty(7)) {
                    res.add(7);
                }
                break;
            case 5:
                res.add(5);
                if(boxIsEmpty(4)) {
                    res.add(4);
                }
                if (boxIsEmpty(13)){
                    res.add(13);
                }
                break;
            case 6:
                res.add(6);
                if(boxIsEmpty(7)) {
                    res.add(7);
                }
                if (boxIsEmpty(11)){
                    res.add(11);
                }
                break;
            case 7:
                res.add(7);
                if(boxIsEmpty(4)) {
                    res.add(4);
                }
                if (boxIsEmpty(6)){
                    res.add(6);
                }
                if (boxIsEmpty(8)){
                    res.add(8);
                }
                break;
        }
        return res;
    }

    private Boolean boxIsEmpty(Integer box) {
        Boolean res = false;
        if (board.get(box).equals(2)) {
            res = true;
        }
        return res;
    }

}
