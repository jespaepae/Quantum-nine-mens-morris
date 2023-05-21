package com.example.quantumninemensmorris;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class GameVsAI extends Activity {

    private ArrayList<TextView> textViews;
    private Graph<String, DefaultEdge> graph;
    private String turn="X";
    private String phase = "Placing";
    private Integer xPiecesPlaced = 18, oPiecesPlaced = 18, xPieces = 0, oPieces = 0, millsBefore,
        millsAfter, moving = -1;
    private Button btnReset;
    private ArrayList<Integer> board;
    private ArrayList<String> xPiecesRemaining, oPiecesRemaining, collapsePieces;
    private Boolean newMill= false;

    private Random random;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        random = new Random();

        board = new ArrayList<>();
        for(int i = 0; i < 24; i++) {
            board.add(2);
        }

        xPiecesRemaining = new ArrayList<>(Arrays.asList("X\u2081", "X\u2081", "X\u2082", "X\u2082",
                "X\u2083", "X\u2083", "X\u2084", "X\u2084", "X\u2085", "X\u2085", "X\u2086", "X\u2086",
                "X\u2087", "X\u2087", "X\u2088", "X\u2088", "X\u2089", "X\u2089"));

        oPiecesRemaining = new ArrayList<>(Arrays.asList("O\u2081", "O\u2081", "O\u2082", "O\u2082",
                "O\u2083", "O\u2083", "O\u2084", "O\u2084", "O\u2085", "O\u2085", "O\u2086", "O\u2086",
                "O\u2087", "O\u2087", "O\u2088", "O\u2088", "O\u2089", "O\u2089"));

        collapsePieces = new ArrayList<>();

        btnReset = findViewById(R.id.btnReset);

        graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

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
                xPiecesPlaced = 18;
                oPiecesPlaced = 18;
                xPieces = 0;
                oPieces = 0;
                moving = -1;
                newMill = false;
                for (TextView tv : textViews) {
                    tv.setText("");
                }
                for (int i = 0; i < board.size(); i++) {
                    board.set(i, 2);
                }
                xPiecesRemaining = new ArrayList<>(Arrays.asList("X\u2081", "X\u2081", "X\u2082", "X\u2082",
                        "X\u2083", "X\u2083", "X\u2084", "X\u2084", "X\u2085", "X\u2085", "X\u2086", "X\u2086",
                        "X\u2087", "X\u2087", "X\u2088", "X\u2088", "X\u2089", "X\u2089"));

                oPiecesRemaining = new ArrayList<>(Arrays.asList("O\u2081", "O\u2081", "O\u2082", "O\u2082",
                        "O\u2083", "O\u2083", "O\u2084", "O\u2084", "O\u2085", "O\u2085", "O\u2086", "O\u2086",
                        "O\u2087", "O\u2087", "O\u2088", "O\u2088", "O\u2089", "O\u2089"));
                collapsePieces = new ArrayList<>();
                graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                showTurn();
                showPhase();
            }
        });
        for (TextView tv : textViews) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setTextSize(12);
                    switch (phase) {
                        case "Placing":
                            if (!newMill && !tv.getText().equals("O") && !tv.getText().equals("X")) {
                                if(turn.equals("X") && !tv.getText().toString().trim().contains(xPiecesRemaining.get(0))) {
                                    millsBefore = numberOfMills(1);
                                    xPiecesPlaced--;
                                    xPieces++;
                                    placePiece(1, tv);
                                    millsAfter = numberOfMills(1);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(GameVsAI.this, "Player X formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(!newMill && (xPiecesPlaced % 2 == 0 && oPiecesPlaced % 2 == 0)){
                                    choosePlayer();
                                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                    ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                    for (int i=0;i<textViews.size();i++){
                                        TextView tv = textViews.get(i);
                                        TextView tvCopy = new TextView(GameVsAI.this);
                                        tvCopy.setText(tv.getText());
                                        textViewsCopy.set(i, tvCopy);
                                    }
                                    ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                    ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                    Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                    for (String vertex : graph.vertexSet()) {
                                        graphCopy.addVertex(vertex);
                                    }
                                    for (DefaultEdge edge : graph.edgeSet()) {
                                        String source = graph.getEdgeSource(edge);
                                        String target = graph.getEdgeTarget(edge);
                                        graphCopy.addEdge(source, target);
                                    }
                                    ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                    Integer xPiecesPlacedCopy = xPiecesPlaced;
                                    Integer oPiecesPlacedCopy = oPiecesPlaced;
                                    ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);

                                    Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                    placePiece(0, textViews.get(AIMoves.get(0)));
                                    placePiece(0, textViews.get(AIMoves.get(1)));
                                    Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                    if (numberOfMillAfter>numberOfMillBefore){
                                        ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                        for (TextView textv : textViews){
                                            if (textv.getText().equals("X")){
                                                availableBoxesAi.add(textViews.indexOf(textv));
                                            }
                                        }
                                        int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                        textViews.get(aitv).setText("");
                                        board.set(textViews.indexOf(aitv), 2);
                                    }
                                    choosePlayer();
                                }
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
                                }
                                //turno de IA
                                ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                for (int i=0;i<textViews.size();i++){
                                    TextView tv = textViews.get(i);
                                    TextView tvCopy = new TextView(GameVsAI.this);
                                    tvCopy.setText(tv.getText());
                                    textViewsCopy.set(i, tvCopy);
                                }
                                ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                for (String vertex : graph.vertexSet()) {
                                    graphCopy.addVertex(vertex);
                                }
                                for (DefaultEdge edge : graph.edgeSet()) {
                                    String source = graph.getEdgeSource(edge);
                                    String target = graph.getEdgeTarget(edge);
                                    graphCopy.addEdge(source, target);
                                }
                                ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                Integer xPiecesPlacedCopy = xPiecesPlaced;
                                Integer oPiecesPlacedCopy = oPiecesPlaced;
                                ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);

                                Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                placePiece(0, textViews.get(AIMoves.get(0)));
                                placePiece(0, textViews.get(AIMoves.get(1)));
                                Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                if (numberOfMillAfter>numberOfMillBefore){
                                    ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                    for (TextView textv : textViews){
                                        if (textv.getText().equals("X")){
                                            availableBoxesAi.add(textViews.indexOf(textv));
                                        }
                                    }
                                    int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                    textViews.get(aitv).setText("");
                                    board.set(textViews.indexOf(aitv), 2);
                                }
                                choosePlayer();
                                choosePhase();
                                showTurn();
                                showPhase();

                            }
                            break;
                        case "Moving":
                            if(tv.getText().equals(turn) && moving < 0 && availableMovesOf(textViews.indexOf(tv)).size() > 1) {
                                moving = textViews.indexOf(tv);
                                tv.setTypeface(null, Typeface.BOLD);
                                Toast.makeText(GameVsAI.this, "Moving piece", Toast.LENGTH_SHORT).show();
                            } else if (moving >= 0 && !newMill && availableMovesOf(moving).contains(textViews.indexOf(tv))
                                        && !moving.equals(textViews.indexOf(tv))) {
                                if(turn.equals("X")) {
                                    millsBefore = numberOfMills(1);
                                    collapseMoving(tv);
                                    if(tv.getText().toString().equals("")) {
                                        board.set(moving, 2);
                                        textViews.get(moving).setText("");
                                        board.set(textViews.indexOf(tv), 1);
                                        tv.setText("X");
                                    } else {
                                        board.set(textViews.indexOf(tv), 0);
                                        tv.setText("O");
                                    }
                                    xPiecesPlaced--;
                                    xPieces++;
                                    millsAfter = numberOfMills(1);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(GameVsAI.this, "Player X formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(!newMill){
                                    choosePlayer();
                                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                    ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                    for (int i=0;i<textViews.size();i++){
                                        TextView tv = textViews.get(i);
                                        TextView tvCopy = new TextView(GameVsAI.this);
                                        tvCopy.setText(tv.getText());
                                        textViewsCopy.set(i, tvCopy);
                                    }
                                    ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                    ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                    Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                    for (String vertex : graph.vertexSet()) {
                                        graphCopy.addVertex(vertex);
                                    }
                                    for (DefaultEdge edge : graph.edgeSet()) {
                                        String source = graph.getEdgeSource(edge);
                                        String target = graph.getEdgeTarget(edge);
                                        graphCopy.addEdge(source, target);
                                    }
                                    ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                    Integer xPiecesPlacedCopy = xPiecesPlaced;
                                    Integer oPiecesPlacedCopy = oPiecesPlaced;
                                    ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);
                                    Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                    collapseMoving(textViews.get(AIMoves.get(1)));
                                    if(textViews.get(AIMoves.get(1)).getText().toString().equals("")) {
                                        board.set(AIMoves.get(0), 2);
                                        textViews.get(AIMoves.get(0)).setText("");
                                        board.set(AIMoves.get(1), 1);
                                        textViews.get(AIMoves.get(1)).setText("X");
                                    } else {
                                        board.set(AIMoves.get(1), 0);
                                        textViews.get(AIMoves.get(1)).setText("O");
                                    }
                                    Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                    if (numberOfMillAfter>numberOfMillBefore){
                                        ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                        for (TextView textv : textViews){
                                            if (textv.getText().equals("X")){
                                                availableBoxesAi.add(textViews.indexOf(textv));
                                            }
                                        }
                                        int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                        textViews.get(aitv).setText("");
                                        board.set(textViews.indexOf(aitv), 2);
                                    }
                                }
                                choosePhase();
                                showTurn();
                                showPhase();
                                textViews.get(moving).setTypeface(null, Typeface.NORMAL);
                                moving = -1;
                            } else if(newMill) {
                                if(turn.equals("X") && tv.getText().equals("O")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    choosePhase();
                                    showPhase();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                }
                                ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                for (int i=0;i<textViews.size();i++){
                                    TextView tv = textViews.get(i);
                                    TextView tvCopy = new TextView(GameVsAI.this);
                                    tvCopy.setText(tv.getText());
                                    textViewsCopy.set(i, tvCopy);
                                }
                                ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                for (String vertex : graph.vertexSet()) {
                                    graphCopy.addVertex(vertex);
                                }
                                for (DefaultEdge edge : graph.edgeSet()) {
                                    String source = graph.getEdgeSource(edge);
                                    String target = graph.getEdgeTarget(edge);
                                    graphCopy.addEdge(source, target);
                                }
                                ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                Integer xPiecesPlacedCopy = xPiecesPlaced;
                                Integer oPiecesPlacedCopy = oPiecesPlaced;
                                ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);
                                Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                collapseMoving(textViews.get(AIMoves.get(1)));
                                if(textViews.get(AIMoves.get(1)).getText().toString().equals("")) {
                                    board.set(AIMoves.get(0), 2);
                                    textViews.get(AIMoves.get(0)).setText("");
                                    board.set(AIMoves.get(1), 1);
                                    textViews.get(AIMoves.get(1)).setText("X");
                                } else {
                                    board.set(AIMoves.get(1), 0);
                                    textViews.get(AIMoves.get(1)).setText("O");
                                }
                                Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                if (numberOfMillAfter>numberOfMillBefore){
                                    ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                    for (TextView textv : textViews){
                                        if (textv.getText().equals("X")){
                                            availableBoxesAi.add(textViews.indexOf(textv));
                                        }
                                    }
                                    int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                    textViews.get(aitv).setText("");
                                    board.set(textViews.indexOf(aitv), 2);
                                }
                                choosePlayer();
                                choosePhase();
                                showTurn();
                                showPhase();
                            }
                            break;
                        case "Flying":
                            if(tv.getText().equals(turn) && moving < 0 && availableMovesOf(textViews.indexOf(tv)).size() > 1) {
                                moving = textViews.indexOf(tv);
                                tv.setTypeface(null, Typeface.BOLD);
                                Toast.makeText(GameVsAI.this, "Moving piece", Toast.LENGTH_SHORT).show();
                            } else if (moving >= 0 && !newMill && availableMovesOf(moving).contains(textViews.indexOf(tv))
                                    && !moving.equals(textViews.indexOf(tv))) {
                                if(turn.equals("X")) {
                                    millsBefore = numberOfMills(1);
                                    collapseMoving(tv);
                                    if(tv.getText().toString().equals("")) {
                                        board.set(moving, 2);
                                        textViews.get(moving).setText("");
                                        board.set(textViews.indexOf(tv), 1);
                                        tv.setText("X");
                                    } else {
                                        board.set(textViews.indexOf(tv), 0);
                                        tv.setText("O");
                                    }
                                    xPiecesPlaced--;
                                    xPieces++;
                                    millsAfter = numberOfMills(1);
                                    if(millsAfter - millsBefore > 0) {
                                        newMill = true;
                                        Toast.makeText(GameVsAI.this, "Player X formed a mill!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(!newMill){
                                    choosePlayer();
                                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                    ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                    for (int i=0;i<textViews.size();i++){
                                        TextView tv = textViews.get(i);
                                        TextView tvCopy = new TextView(GameVsAI.this);
                                        tvCopy.setText(tv.getText());
                                        textViewsCopy.set(i, tvCopy);
                                    }
                                    ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                    ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                    Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                    for (String vertex : graph.vertexSet()) {
                                        graphCopy.addVertex(vertex);
                                    }
                                    for (DefaultEdge edge : graph.edgeSet()) {
                                        String source = graph.getEdgeSource(edge);
                                        String target = graph.getEdgeTarget(edge);
                                        graphCopy.addEdge(source, target);
                                    }
                                    ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                    Integer xPiecesPlacedCopy = xPiecesPlaced;
                                    Integer oPiecesPlacedCopy = oPiecesPlaced;
                                    ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);
                                    Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                    collapseMoving(textViews.get(AIMoves.get(1)));
                                    if(textViews.get(AIMoves.get(1)).getText().toString().equals("")) {
                                        board.set(AIMoves.get(0), 2);
                                        textViews.get(AIMoves.get(0)).setText("");
                                        board.set(AIMoves.get(1), 1);
                                        textViews.get(AIMoves.get(1)).setText("X");
                                    } else {
                                        board.set(AIMoves.get(1), 0);
                                        textViews.get(AIMoves.get(1)).setText("O");
                                    }
                                    Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                    if (numberOfMillAfter>numberOfMillBefore){
                                        ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                        for (TextView textv : textViews){
                                            if (textv.getText().equals("X")){
                                                availableBoxesAi.add(textViews.indexOf(textv));
                                            }
                                        }
                                        int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                        textViews.get(aitv).setText("");
                                        board.set(textViews.indexOf(aitv), 2);
                                    }
                                }
                                choosePhase();
                                showTurn();
                                showPhase();
                                textViews.get(moving).setTypeface(null, Typeface.NORMAL);
                                moving = -1;
                            } else if(newMill) {
                                if(turn.equals("X") && tv.getText().equals("O")) {
                                    tv.setText("");
                                    choosePlayer();
                                    showTurn();
                                    choosePhase();
                                    showPhase();
                                    board.set(textViews.indexOf(tv), 2);
                                    newMill = false;
                                }
                                ArrayList<Integer> boardCopy = new ArrayList<>(board);
                                ArrayList<TextView> textViewsCopy = new ArrayList<>(textViews);
                                for (int i=0;i<textViews.size();i++){
                                    TextView tv = textViews.get(i);
                                    TextView tvCopy = new TextView(GameVsAI.this);
                                    tvCopy.setText(tv.getText());
                                    textViewsCopy.set(i, tvCopy);
                                }
                                ArrayList<String> xPiecesRemainingCopy = new ArrayList<>(xPiecesRemaining);
                                ArrayList<String> oPiecesRemainingCopy = new ArrayList<>(oPiecesRemaining);
                                Graph<String, DefaultEdge> graphCopy = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                for (String vertex : graph.vertexSet()) {
                                    graphCopy.addVertex(vertex);
                                }
                                for (DefaultEdge edge : graph.edgeSet()) {
                                    String source = graph.getEdgeSource(edge);
                                    String target = graph.getEdgeTarget(edge);
                                    graphCopy.addEdge(source, target);
                                }
                                ArrayList<String> collapsePiecesCopy = new ArrayList<>(collapsePieces);
                                Integer xPiecesPlacedCopy = xPiecesPlaced;
                                Integer oPiecesPlacedCopy = oPiecesPlaced;
                                ArrayList<Integer> AIMoves = getBestMove(boardCopy, textViewsCopy, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true, phase,  xPiecesRemainingCopy, oPiecesRemainingCopy,  graphCopy, collapsePiecesCopy, xPiecesPlacedCopy, oPiecesPlacedCopy);
                                Integer numberOfMillAfter = numberOfMillsAI(0, board);
                                collapseMoving(textViews.get(AIMoves.get(1)));
                                if(textViews.get(AIMoves.get(1)).getText().toString().equals("")) {
                                    board.set(AIMoves.get(0), 2);
                                    textViews.get(AIMoves.get(0)).setText("");
                                    board.set(AIMoves.get(1), 1);
                                    textViews.get(AIMoves.get(1)).setText("X");
                                } else {
                                    board.set(AIMoves.get(1), 0);
                                    textViews.get(AIMoves.get(1)).setText("O");
                                }
                                Integer numberOfMillBefore = numberOfMillsAI(0, board);
                                if (numberOfMillAfter>numberOfMillBefore){
                                    ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                    for (TextView textv : textViews){
                                        if (textv.getText().equals("X")){
                                            availableBoxesAi.add(textViews.indexOf(textv));
                                        }
                                    }
                                    int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                    textViews.get(aitv).setText("");
                                    board.set(textViews.indexOf(aitv), 2);
                                }
                                choosePlayer();
                                choosePhase();
                                showTurn();
                                showPhase();
                            }
                            if(numberOfPiecesOf("X") < 3 || numberOfPiecesOf("O") < 3) {
                                endGame(v);
                            }
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
        } else if (phase.equals("Moving")){
            if(numberOfPiecesOf("X").equals(3) || numberOfPiecesOf("O").equals(3)) {
                phase = "Flying";
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
        if(numberOfPiecesOf(textViews.get(piece).getText().toString()) > 3) {
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
                case 8:
                    res.add(8);
                    if(boxIsEmpty(7)) {
                        res.add(7);
                    }
                    if (boxIsEmpty(12)){
                        res.add(12);
                    }
                    break;
                case 9:
                    res.add(9);
                    if(boxIsEmpty(0)) {
                        res.add(0);
                    }
                    if (boxIsEmpty(10)){
                        res.add(10);
                    }
                    if (boxIsEmpty(21)){
                        res.add(21);
                    }
                    break;
                case 10:
                    res.add(10);
                    if(boxIsEmpty(3)) {
                        res.add(3);
                    }
                    if (boxIsEmpty(9)){
                        res.add(9);
                    }
                    if (boxIsEmpty(11)){
                        res.add(11);
                    }
                    if (boxIsEmpty(18)) {
                        res.add(18);
                    }
                    break;
                case 11:
                    res.add(11);
                    if(boxIsEmpty(6)) {
                        res.add(6);
                    }
                    if (boxIsEmpty(10)){
                        res.add(10);
                    }
                    if (boxIsEmpty(15)){
                        res.add(15);
                    }
                    break;
                case 12:
                    res.add(12);
                    if(boxIsEmpty(8)) {
                        res.add(8);
                    }
                    if (boxIsEmpty(13)){
                        res.add(13);
                    }
                    if (boxIsEmpty(17)){
                        res.add(17);
                    }
                    break;
                case 13:
                    res.add(13);
                    if(boxIsEmpty(5)) {
                        res.add(5);
                    }
                    if (boxIsEmpty(12)){
                        res.add(12);
                    }
                    if (boxIsEmpty(14)){
                        res.add(14);
                    }
                    if (boxIsEmpty(20)) {
                        res.add(20);
                    }
                    break;
                case 14:
                    res.add(14);
                    if(boxIsEmpty(2)) {
                        res.add(2);
                    }
                    if (boxIsEmpty(13)){
                        res.add(13);
                    }
                    if (boxIsEmpty(23)){
                        res.add(23);
                    }
                    break;
                case 15:
                    res.add(15);
                    if(boxIsEmpty(11)) {
                        res.add(11);
                    }
                    if (boxIsEmpty(16)){
                        res.add(16);
                    }
                    break;
                case 16:
                    res.add(16);
                    if(boxIsEmpty(15)) {
                        res.add(15);
                    }
                    if (boxIsEmpty(17)){
                        res.add(17);
                    }
                    if (boxIsEmpty(19)){
                        res.add(19);
                    }
                    break;
                case 17:
                    res.add(17);
                    if(boxIsEmpty(12)) {
                        res.add(12);
                    }
                    if (boxIsEmpty(16)){
                        res.add(16);
                    }
                    break;
                case 18:
                    res.add(18);
                    if(boxIsEmpty(10)) {
                        res.add(10);
                    }
                    if (boxIsEmpty(19)){
                        res.add(19);
                    }
                    break;
                case 19:
                    res.add(19);
                    if(boxIsEmpty(16)) {
                        res.add(16);
                    }
                    if (boxIsEmpty(18)){
                        res.add(18);
                    }
                    if (boxIsEmpty(20)){
                        res.add(20);
                    }
                    if (boxIsEmpty(22)) res.add(22);
                    break;
                case 20:
                    res.add(20);
                    if(boxIsEmpty(13)) {
                        res.add(13);
                    }
                    if (boxIsEmpty(19)){
                        res.add(19);
                    }
                    break;
                case 21:
                    res.add(21);
                    if(boxIsEmpty(9)) {
                        res.add(9);
                    }
                    if (boxIsEmpty(22)){
                        res.add(22);
                    }
                    break;
                case 22:
                    res.add(22);
                    if(boxIsEmpty(19)) {
                        res.add(19);
                    }
                    if (boxIsEmpty(21)){
                        res.add(21);
                    }
                    if (boxIsEmpty(23)){
                        res.add(23);
                    }
                    break;
                case 23:
                    res.add(23);
                    if(boxIsEmpty(14)) {
                        res.add(14);
                    }
                    if (boxIsEmpty(22)){
                        res.add(22);
                    }
                    break;
            }
        } else {
            for(int i = 0; i < 24; i++) {
                if(boxIsEmpty(i)) {
                    res.add(i);
                }
            }
        }

        return res;
    }

    private Boolean boxIsEmpty(Integer box) {
        Boolean res = false;
        if (board.get(box).equals(2) || board.get(box).equals(3)) {
            res = true;
        }
        return res;
    }

    private void placePiece(Integer piece, TextView tv) {
        if (piece.equals(0)) {
            addVertexAndEdgesToGraph(oPiecesRemaining.get(0), tv);
            if(isEntangled(oPiecesRemaining.get(0), tv) && oPiecesRemaining.size() % 2 == 1) {
                tv.setText(tv.getText() + " " + oPiecesRemaining.get(0));
                collapse();
            } else {
                tv.setText(tv.getText() + " " + oPiecesRemaining.get(0));
                board.set(textViews.indexOf(tv), 3);
            }
            oPiecesRemaining.remove(0);
        } else {
            addVertexAndEdgesToGraph(xPiecesRemaining.get(0), tv);
            if(isEntangled(xPiecesRemaining.get(0), tv) && xPiecesRemaining.size() % 2 == 1) {
                tv.setText(tv.getText() + " " + xPiecesRemaining.get(0));
                collapse();
            } else {
                tv.setText(tv.getText() + " " + xPiecesRemaining.get(0));
                board.set(textViews.indexOf(tv), 3);
            }
            xPiecesRemaining.remove(0);
        }

    }

    private void addVertexAndEdgesToGraph(String piece, TextView tv) {
        graph.addVertex(piece);
        if(!tv.getText().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String item : pieces) {
                if(item.length() != 0) {
                    graph.addEdge(item, piece);
                }
            }
        }
    }

    private Boolean isEntangled(String piece, TextView tv) {
        Boolean res = false;

        if (!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String item : pieces) {
                if (!item.equals("")) {
                    for(TextView t: textViews) {
                        if(t.getText().toString().contains(piece) && t.getText().toString().contains(item)) {
                            res = true;
                            collapsePieces.add(piece);
                        }
                    }
                }
            }
        }

        PatonCycleBase<String, DefaultEdge> cycleDetector = new PatonCycleBase<>(graph);
        if (!cycleDetector.getCycleBasis().getCycles().isEmpty()) {
            res = true;
            collapsePieces.add(piece);
        }

        return res;
    }

    private void collapse() {
        if(collapsePieces.size()> 0) {
            String piece = collapsePieces.get(0);
            ArrayList<String> vertices = getConnectedVertexOf(piece);

            Boolean isValid = false;
            ArrayList<String> textViewsCopy = new ArrayList<>();
            ArrayList<Integer> boardCopy = new ArrayList<>(board);
            for(TextView tv : textViews) {
                textViewsCopy.add(tv.getText().toString());
            }

            while(!isValid) {

                for(TextView tv : textViews) {
                    tv.setText(textViewsCopy.get(textViews.indexOf(tv)));
                }
                board = new ArrayList<>();
                board.addAll(boardCopy);

                for(String vertex : vertices) {
                    ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                    Integer index;

                    if(indexesToCollapse.size() > 1) {
                        index = indexesToCollapse.get(random.nextInt(2));
                    } else if (indexesToCollapse.size() == 1){
                        index = indexesToCollapse.get(0);
                    } else {
                        break;
                    }

                    String collapsePiece = vertex.substring(0,1);

                    textViews.get(index).setText(collapsePiece);
                    if(collapsePiece.equals("X")) {
                        board.set(index, 1);
                    } else {
                        board.set(index, 0);
                    }

                    if (vertices.indexOf(vertex) == vertices.size() - 1) {
                        isValid = true;
                    }
                }
            }

            for(String vertex : vertices) {
                graph.removeVertex(vertex);
            }

            deleteRemainingQuantumPieces();
            collapsePieces = new ArrayList<>();
        }

    }

    private void collapseMoving(TextView tv) {
        if(!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String piece : pieces) {
                if(piece.length() != 0) {
                    ArrayList<String> vertices = getConnectedVertexOf(piece);

                    Boolean isValid = false;
                    ArrayList<String> textViewsCopy = new ArrayList<>();
                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                    for(TextView t : textViews) {
                        textViewsCopy.add(t.getText().toString());
                    }

                    while(!isValid) {

                        for(TextView t : textViews) {
                            t.setText(textViewsCopy.get(textViews.indexOf(t)));
                        }
                        board = new ArrayList<>();
                        board.addAll(boardCopy);

                        for(String vertex : vertices) {
                            ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                            Integer index;

                            if(indexesToCollapse.size() > 1) {
                                index = indexesToCollapse.get(random.nextInt(2));
                            } else if (indexesToCollapse.size() == 1){
                                index = indexesToCollapse.get(0);
                            } else {
                                break;
                            }

                            String collapsePiece = vertex.substring(0,1);

                            textViews.get(index).setText(collapsePiece);
                            if(collapsePiece.equals("X")) {
                                board.set(index, 1);
                            } else {
                                board.set(index, 0);
                            }

                            if (vertices.indexOf(vertex) == vertices.size() - 1) {
                                isValid = true;
                            }
                        }
                    }

                    for(String vertex : vertices) {
                        graph.removeVertex(vertex);
                    }

                    deleteRemainingQuantumPieces();

                    break;
                }
            }
        }
    }

    private ArrayList<Integer> getIndexesOfPiece(String piece) {
        ArrayList<Integer> res = new ArrayList<>();

        for (TextView tv : textViews) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String item : pieces) {
                if (!item.equals("") && item.equals(piece)) {
                    res.add(textViews.indexOf(tv));
                }
            }
        }

        return res;
    }

    private ArrayList<String> getConnectedVertexOf(String piece) {
        ArrayList<String> res = new ArrayList<>();

        ConnectivityInspector<String, DefaultEdge> ci = new ConnectivityInspector<>(graph);

        Set<String> set = ci.connectedSetOf(piece);

        res.addAll(set);

        return res;
    }

    private void deleteRemainingQuantumPieces() {
        HashMap<String, Integer> qp = new HashMap<>();

        for(TextView tv: textViews) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String piece : pieces) {
                if (!piece.equals("") && piece.length() > 1) {
                    if (qp.containsKey(piece)) {
                        Integer count = qp.get(piece);
                        count++;
                        qp.put(piece, count);
                    } else {
                        qp.put(piece, 1);
                    }
                }
            }
        }

        for(String piece: qp.keySet()) {
            if(qp.get(piece).equals(1)) {
                for (TextView tv : textViews) {
                    if(tv.getText().toString().contains(piece)) {
                        tv.setText("");
                        board.set(textViews.indexOf(tv), 2);
                    }
                }
            }
        }
    }

    private Integer numberOfPiecesOf(String piece) {
        Integer res = 0;
        Integer quantumPieces = 0;
        for(TextView tv : textViews) {
            if (tv.getText().toString().equals(piece)) {
                res ++;
            } else if (tv.getText().toString().contains(piece)) {
                quantumPieces++;
            }
        }
        res += quantumPieces/2;
        return res;
    }

    private void endGame(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, findViewById(R.id.popUpWindow));


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView popupWindowText = popupView.findViewById(R.id.popUpWindow);
        if(numberOfPiecesOf("X") < 3) {
            popupWindowText.setText("Player O Wins!!");
        } else {
            popupWindowText.setText("Player X Wins!!");
        }

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                return true;
            }
        });


    }

    private Integer minimax_alpha_beta(ArrayList<Integer> board, ArrayList<TextView> textViews, int depth, int alfa, int beta, boolean isMaximizing, String phase, ArrayList<String> xPiecesRemaining, ArrayList<String> oPiecesRemaining, Graph<String, DefaultEdge> graph, ArrayList<String> collapsePieces, Integer xPiecesPlaced, Integer oPiecesPlaced) {

        if (depth == 0 || isEndOfGame(textViews)) {
            return evaluate(board);
        }

        if(phase == "Placing"){
            if (isMaximizing) {
                Integer bestValue = Integer.MIN_VALUE;
                for (Integer place : getAvailablePlaces(board)) {
                    for (Integer place2 : getAvailablePlaces(board)){
                        if(place!=place2 && place2 > place) {
                            Integer numberOfMillBefore = numberOfMillsAI(0, board);
                            graph = addVertexAndEdgesToGraphAI(oPiecesRemaining.get(0), textViews.get(place), graph);
                            textViews.get(place).setText(textViews.get(place).getText() + " " + oPiecesRemaining.get(0));
                            oPiecesRemaining.remove(0);
                            board.set(place, 3);
                            graph = addVertexAndEdgesToGraphAI(oPiecesRemaining.get(0), textViews.get(place2), graph);
                            textViews.get(place2).setText(textViews.get(place2).getText() + " " + oPiecesRemaining.get(0));
                            oPiecesRemaining.remove(0);
                            board.set(place2, 3);
                            oPiecesPlaced--;
                            oPiecesPlaced--;
                            if (isEntangledAI(oPiecesRemaining.get(0), textViews.get(place2), textViews, collapsePieces, graph)){
                                ArrayList<TextView> newTextViews = new ArrayList<>();
                                ArrayList<Integer> newBoard = new ArrayList<>();
                                collapsePieces = new ArrayList<>();
                                collapsePieces = isEntangledAICollapsePieces(oPiecesRemaining.get(0), textViews.get(place2), textViews, collapsePieces, graph);
                                newTextViews = collapseAI(board, textViews, collapsePieces, graph);
                                newBoard = collapseAIBoard(board, textViews, collapsePieces, graph);
                                textViews = new ArrayList<>(newTextViews);
                                for (int i=0;i<textViews.size();i++){
                                    TextView tv = newTextViews.get(i);
                                    TextView tvCopy = new TextView(GameVsAI.this);
                                    tvCopy.setText(tv.getText());
                                    textViews.set(i, tvCopy);
                                }
                                collapsePieces = new ArrayList<>();
                                board = new ArrayList<>(newBoard);
                                graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                graph = collapseAIGraph(board, textViews, collapsePieces, graph);

                            }
                            Integer numberOfMillAfter = numberOfMillsAI(0, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("X")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, false, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.max(bestValue, valuePlacing);
                            alfa = Math.max(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            } else {
                Integer bestValue = Integer.MAX_VALUE;
                for (Integer place : getAvailablePlaces(board)) {
                    for (Integer place2 : getAvailablePlaces(board)){
                        if(place!=place2 && place2 > place) {

                            Integer numberOfMillBefore = numberOfMillsAI(1, board);
                            graph = addVertexAndEdgesToGraphAI(xPiecesRemaining.get(0), textViews.get(place), graph);
                            textViews.get(place).setText(textViews.get(place).getText() + " " + xPiecesRemaining.get(0));
                            xPiecesRemaining.remove(0);
                            board.set(place, 3);
                            graph = addVertexAndEdgesToGraphAI(xPiecesRemaining.get(0), textViews.get(place2), graph);
                            textViews.get(place2).setText(textViews.get(place2).getText() + " " + xPiecesRemaining.get(0));
                            xPiecesRemaining.remove(0);
                            board.set(place2, 3);
                            if (isEntangledAI(xPiecesRemaining.get(0), textViews.get(place2), textViews, collapsePieces, graph)){
                                collapsePieces = new ArrayList<>();
                                collapsePieces = isEntangledAICollapsePieces(xPiecesRemaining.get(0), textViews.get(place2), textViews, collapsePieces, graph);
                                ArrayList<TextView> newTextViews = new ArrayList<>();
                                ArrayList<Integer> newBoard = new ArrayList<>();
                                newTextViews = collapseAI(board, textViews, collapsePieces, graph);
                                newBoard = collapseAIBoard(board, textViews, collapsePieces, graph);
                                textViews = new ArrayList<>(newTextViews);
                                for (int i=0;i<textViews.size();i++){
                                    TextView tv = newTextViews.get(i);
                                    TextView tvCopy = new TextView(GameVsAI.this);
                                    tvCopy.setText(tv.getText());
                                    textViews.set(i, tvCopy);
                                }
                                oPiecesPlaced--;
                                oPiecesPlaced--;
                                board = new ArrayList<>(newBoard);
                                graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                                graph = collapseAIGraph(board, textViews, collapsePieces, graph);
                                collapsePieces = new ArrayList<>();
                            }
                            Integer numberOfMillAfter = numberOfMillsAI(1, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("O")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.min(bestValue, valuePlacing);
                            beta = Math.min(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            }
        } else if (phase == "Moving"){
            if (isMaximizing) {
                Integer bestValue = Integer.MIN_VALUE;
                for (Integer place : getPlacesWithPiece (0,  board)) {
                    for (Integer place2 : availableMovesOfAI(place,  board,  textViews)){
                        if(place!=place2) {
                            Integer numberOfMillBefore = numberOfMillsAI(0, board);
                            ArrayList<TextView> newTextViews = new ArrayList<>();
                            ArrayList<Integer> newBoard = new ArrayList<>();
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            newTextViews = collapseMovingAI(textViews.get(place2), board, textViews, graph);
                            newBoard = collapseMovingAIBoard(textViews.get(place2), board, textViews, graph);
                            textViews = new ArrayList<>(newTextViews);
                            for (int i=0;i<textViews.size();i++){
                                TextView tv = newTextViews.get(i);
                                TextView tvCopy = new TextView(GameVsAI.this);
                                tvCopy.setText(tv.getText());
                                textViews.set(i, tvCopy);
                            }
                            board = new ArrayList<>(newBoard);
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            graph = collapseMovingAIGraph(textViews.get(place2), board, textViews, graph);

                            if(textViews.get(place2).getText().toString().equals("")) {
                                    board.set(place, 2);
                                    textViews.get(place).setText("");
                                    board.set(place2, 0);
                                    textViews.get(place2).setText("O");
                            } else {
                                board.set(place2, 1);
                                textViews.get(place2).setText("X");
                            }

                            Integer numberOfMillAfter = numberOfMillsAI(0, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("X")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, false, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.max(bestValue, valuePlacing);
                            alfa = Math.max(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            } else {
                Integer bestValue = Integer.MAX_VALUE;
                for (Integer place : getPlacesWithPiece (1,  board)) {
                    for (Integer place2 : availableMovesOfAI(place,  board,  textViews)){
                        if(place!=place2) {

                            Integer numberOfMillBefore = numberOfMillsAI(1, board);
                            ArrayList<TextView> newTextViews = new ArrayList<>();
                            ArrayList<Integer> newBoard = new ArrayList<>();
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            newTextViews = collapseMovingAI(textViews.get(place2), board, textViews, graph);
                            newBoard = collapseMovingAIBoard(textViews.get(place2), board, textViews, graph);
                            textViews = new ArrayList<>(newTextViews);
                            for (int i=0;i<textViews.size();i++){
                                TextView tv = newTextViews.get(i);
                                TextView tvCopy = new TextView(GameVsAI.this);
                                tvCopy.setText(tv.getText());
                                textViews.set(i, tvCopy);
                            }
                            board = new ArrayList<>(newBoard);
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            graph = collapseMovingAIGraph(textViews.get(place2), board, textViews, graph);

                            if(textViews.get(place2).getText().toString().equals("")) {
                                board.set(place, 2);
                                textViews.get(place).setText("");
                                board.set(place2, 1);
                                textViews.get(place2).setText("X");
                            } else {
                                board.set(place2, 0);
                                textViews.get(place2).setText("O");
                            }
                            Integer numberOfMillAfter = numberOfMillsAI(1, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("O")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.min(bestValue, valuePlacing);
                            beta = Math.min(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            }

        } else{
            if (isMaximizing) {
                Integer bestValue = Integer.MIN_VALUE;
                for (Integer place : getPlacesWithPiece (0,  board)) {
                    for (Integer place2 : getAvailablePlaces(board)){
                        if(place!=place2) {
                            Integer numberOfMillBefore = numberOfMillsAI(0, board);
                            ArrayList<TextView> newTextViews = new ArrayList<>();
                            ArrayList<Integer> newBoard = new ArrayList<>();
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            newTextViews = collapseMovingAI(textViews.get(place2), board, textViews, graph);
                            newBoard = collapseMovingAIBoard(textViews.get(place2), board, textViews, graph);
                            textViews = new ArrayList<>(newTextViews);
                            for (int i=0;i<textViews.size();i++){
                                TextView tv = newTextViews.get(i);
                                TextView tvCopy = new TextView(GameVsAI.this);
                                tvCopy.setText(tv.getText());
                                textViews.set(i, tvCopy);
                            }
                            board = new ArrayList<>(newBoard);
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            graph = collapseMovingAIGraph(textViews.get(place2), board, textViews, graph);

                            if(textViews.get(place2).getText().toString().equals("")) {
                                board.set(place, 2);
                                textViews.get(place).setText("");
                                board.set(place2, 0);
                                textViews.get(place2).setText("O");
                            } else {
                                board.set(place2, 1);
                                textViews.get(place2).setText("X");
                            }

                            Integer numberOfMillAfter = numberOfMillsAI(0, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("X")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, false, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.max(bestValue, valuePlacing);
                            alfa = Math.max(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            } else {
                Integer bestValue = Integer.MAX_VALUE;
                for (Integer place : getPlacesWithPiece (1,  board)) {
                    for (Integer place2 : getAvailablePlaces(board)){
                        if(place!=place2) {

                            Integer numberOfMillBefore = numberOfMillsAI(1, board);
                            ArrayList<TextView> newTextViews = new ArrayList<>();
                            ArrayList<Integer> newBoard = new ArrayList<>();
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            newTextViews = collapseMovingAI(textViews.get(place2), board, textViews, graph);
                            newBoard = collapseMovingAIBoard(textViews.get(place2), board, textViews, graph);
                            textViews = new ArrayList<>(newTextViews);
                            for (int i=0;i<textViews.size();i++){
                                TextView tv = newTextViews.get(i);
                                TextView tvCopy = new TextView(GameVsAI.this);
                                tvCopy.setText(tv.getText());
                                textViews.set(i, tvCopy);
                            }
                            board = new ArrayList<>(newBoard);
                            graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                            graph = collapseMovingAIGraph(textViews.get(place2), board, textViews, graph);

                            if(textViews.get(place2).getText().toString().equals("")) {
                                board.set(place, 2);
                                textViews.get(place).setText("");
                                board.set(place2, 1);
                                textViews.get(place2).setText("X");
                            } else {
                                board.set(place2, 0);
                                textViews.get(place2).setText("O");
                            }
                            Integer numberOfMillAfter = numberOfMillsAI(1, board);
                            if (numberOfMillAfter>numberOfMillBefore){
                                ArrayList<Integer> availableBoxesAi = new ArrayList<Integer>();
                                for (TextView textv : textViews){
                                    if (textv.getText().equals("O")){
                                        availableBoxesAi.add(textViews.indexOf(textv));
                                    }
                                }
                                int aitv = availableBoxesAi.get((int) (Math.random()*(availableBoxesAi.size()+1)));
                                textViews.get(aitv).setText("");
                                board.set(textViews.indexOf(aitv), 2);
                            }
                            phase = choosePhaseAI(phase, xPiecesPlaced, oPiecesPlaced, textViews);
                            Integer valuePlacing = minimax_alpha_beta(board, textViews, depth - 1, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                            bestValue = Math.min(bestValue, valuePlacing);
                            beta = Math.min(alfa, bestValue);
                            if (beta <= alfa) {
                                break;
                            }
                        }
                    }
                }
                return bestValue;
            }
        }
    }

    private ArrayList<Integer> getBestMove(ArrayList<Integer> board, ArrayList<TextView> textViews, int depth, int alfa, int beta, boolean isMaximizing, String phase, ArrayList<String> xPiecesRemaining, ArrayList<String> oPiecesRemaining, Graph<String, DefaultEdge> graph, ArrayList<String> collapsePieces, Integer xPiecesPlaced, Integer oPiecesPlaced) {

        if(phase == "Placing"){
            Integer bestValue = Integer.MIN_VALUE;
            ArrayList<Integer> bestMoves = new ArrayList<>();
            for (Integer place : getAvailablePlaces(board)) {
                for (Integer place2 : getAvailablePlaces(board)){
                    if(place!=place2 && place2 > place) {
                        Integer valuePlacing = minimax_alpha_beta(board, textViews, depth, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                        if (valuePlacing>bestValue){
                            bestMoves = new ArrayList<>();
                            bestMoves.add(place);
                            bestMoves.add(place2);
                            bestValue = valuePlacing;
                        }
                    }
                }
            }
            return bestMoves;
        } else if (phase == "Moving"){
            Integer bestValue = Integer.MIN_VALUE;
            ArrayList<Integer> bestMoves = new ArrayList<>();
            for (Integer place : getPlacesWithPiece (0,  board)) {
                for (Integer place2 : availableMovesOfAI(place,  board,  textViews)){
                    if(place!=place2) {
                        Integer valuePlacing = minimax_alpha_beta(board, textViews, depth, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                        if (valuePlacing>bestValue){
                            bestMoves = new ArrayList<>();
                            bestMoves.add(place);
                            bestMoves.add(place2);
                            bestValue = valuePlacing;
                        }
                    }
                }
            }
            return bestMoves;
        } else {
            Integer bestValue = Integer.MIN_VALUE;
            ArrayList<Integer> bestMoves = new ArrayList<>();
            for (Integer place : getPlacesWithPiece (0,  board)) {
                for (Integer place2 : availableMovesOfAI(place,  board,  textViews)){
                    if(place!=place2) {
                        Integer valuePlacing = minimax_alpha_beta(board, textViews, depth, alfa, beta, true, phase, xPiecesRemaining, oPiecesRemaining, graph, collapsePieces, xPiecesPlaced, oPiecesPlaced);
                        if (valuePlacing>bestValue){
                            bestMoves = new ArrayList<>();
                            bestMoves.add(place);
                            bestMoves.add(place2);
                            bestValue = valuePlacing;
                        }
                    }
                }
            }
            return bestMoves;
        }
    }


        private Boolean isEndOfGame(ArrayList<TextView> textViews){

        if(numberOfPiecesOfAI("X", textViews) < 3 || numberOfPiecesOfAI("O", textViews) < 3){
            return true;
        }
        Integer movements = 0;
        for(TextView tv: textViews){
            if(tv.getText().toString().equals("X")){
                movements += availableMovesOf(textViews.indexOf(tv)).size();
            }
            if(tv.getText().toString().equals("O")){
                movements += availableMovesOf(textViews.indexOf(tv)).size();
            }
        }
        if (movements==0){
            return true;
        }
        return false;
    }

    private Integer numberOfPiecesOfAI(String piece, ArrayList<TextView> textViews) {
        Integer res = 0;
        Integer quantumPieces = 0;
        for(TextView tv : textViews) {
            if (tv.getText().toString().equals(piece)) {
                res ++;
            } else if (tv.getText().toString().contains(piece)) {
                quantumPieces++;
            }
        }
        res += quantumPieces/2;
        return res;
    }

    private Integer evaluate(ArrayList<Integer> board){
        return 1;
    }

    private ArrayList<Integer> getAvailablePlaces(ArrayList<Integer> board){
        ArrayList<Integer> availablePlaces = new ArrayList<>();

        for (int i = 0; i<board.size();i++){
            if (board.get(i) == 2 || board.get(i) == 3){
                availablePlaces.add(i);
            }
        }
        return availablePlaces;
    }

    private Graph<String, DefaultEdge> addVertexAndEdgesToGraphAI(String piece, TextView tv, Graph<String, DefaultEdge> graph) {
        graph.addVertex(piece);
        if(!tv.getText().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String item : pieces) {
                if(item.length() != 0) {
                    graph.addEdge(item, piece);
                }
            }
        }
        return graph;
    }

    private Boolean isEntangledAI(String piece, TextView tv, ArrayList<TextView> textViews, ArrayList<String> collapsePieces, Graph<String, DefaultEdge> graph) {
        Boolean res = false;

        if (!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String item : pieces) {
                if (!item.equals("")) {
                    for(TextView t: textViews) {
                        if(t.getText().toString().contains(piece) && t.getText().toString().contains(item)) {
                            res = true;
                            collapsePieces.add(piece);
                        }
                    }
                }
            }
        }

        PatonCycleBase<String, DefaultEdge> cycleDetector = new PatonCycleBase<>(graph);
        if (!cycleDetector.getCycleBasis().getCycles().isEmpty()) {
            res = true;
            collapsePieces.add(piece);
        }

        return res;
    }

    private ArrayList<String> isEntangledAICollapsePieces(String piece, TextView tv, ArrayList<TextView> textViews, ArrayList<String> collapsePieces, Graph<String, DefaultEdge> graph) {
        Boolean res = false;

        if (!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String item : pieces) {
                if (!item.equals("")) {
                    for(TextView t: textViews) {
                        if(t.getText().toString().contains(piece) && t.getText().toString().contains(item)) {
                            res = true;
                            collapsePieces.add(piece);
                        }
                    }
                }
            }
        }

        PatonCycleBase<String, DefaultEdge> cycleDetector = new PatonCycleBase<>(graph);
        if (!cycleDetector.getCycleBasis().getCycles().isEmpty()) {
            res = true;
            collapsePieces.add(piece);
        }

        return collapsePieces;
    }

    private ArrayList<TextView> collapseAI(ArrayList<Integer> board, ArrayList<TextView> textViews, ArrayList<String> collapsePieces, Graph<String, DefaultEdge> graph) {
        if(collapsePieces.size()> 0) {
            String piece = collapsePieces.get(0);
            ArrayList<String> vertices = getConnectedVertexOf(piece);

            Boolean isValid = false;
            ArrayList<String> textViewsCopy = new ArrayList<>();
            ArrayList<Integer> boardCopy = new ArrayList<>(board);
            for(TextView tv : textViews) {
                textViewsCopy.add(tv.getText().toString());
            }

            while(!isValid) {

                for(TextView tv : textViews) {
                    tv.setText(textViewsCopy.get(textViews.indexOf(tv)));
                }
                board = new ArrayList<>();
                board.addAll(boardCopy);

                for(String vertex : vertices) {
                    ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                    Integer index;

                    if(indexesToCollapse.size() > 1) {
                        index = indexesToCollapse.get(random.nextInt(2));
                    } else if (indexesToCollapse.size() == 1){
                        index = indexesToCollapse.get(0);
                    } else {
                        break;
                    }

                    String collapsePiece = vertex.substring(0,1);

                    textViews.get(index).setText(collapsePiece);
                    if(collapsePiece.equals("X")) {
                        board.set(index, 1);
                    } else {
                        board.set(index, 0);
                    }

                    if (vertices.indexOf(vertex) == vertices.size() - 1) {
                        isValid = true;
                    }
                }
            }

            for(String vertex : vertices) {
                graph.removeVertex(vertex);
            }

            textViews = deleteRemainingQuantumPiecesAI(board,textViews);
            collapsePieces = new ArrayList<>();
        }
        return textViews;
    }

    private ArrayList<Integer> collapseAIBoard(ArrayList<Integer> board, ArrayList<TextView> textViews, ArrayList<String> collapsePieces, Graph<String, DefaultEdge> graph) {
        if(collapsePieces.size()> 0) {
            String piece = collapsePieces.get(0);
            ArrayList<String> vertices = getConnectedVertexOf(piece);

            Boolean isValid = false;
            ArrayList<String> textViewsCopy = new ArrayList<>();
            ArrayList<Integer> boardCopy = new ArrayList<>(board);
            for(TextView tv : textViews) {
                textViewsCopy.add(tv.getText().toString());
            }

            while(!isValid) {

                for(TextView tv : textViews) {
                    tv.setText(textViewsCopy.get(textViews.indexOf(tv)));
                }
                board = new ArrayList<>();
                board.addAll(boardCopy);

                for(String vertex : vertices) {
                    ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                    Integer index;

                    if(indexesToCollapse.size() > 1) {
                        index = indexesToCollapse.get(random.nextInt(2));
                    } else if (indexesToCollapse.size() == 1){
                        index = indexesToCollapse.get(0);
                    } else {
                        break;
                    }

                    String collapsePiece = vertex.substring(0,1);

                    textViews.get(index).setText(collapsePiece);
                    if(collapsePiece.equals("X")) {
                        board.set(index, 1);
                    } else {
                        board.set(index, 0);
                    }

                    if (vertices.indexOf(vertex) == vertices.size() - 1) {
                        isValid = true;
                    }
                }
            }

            for(String vertex : vertices) {
                graph.removeVertex(vertex);
            }

            board = deleteRemainingQuantumPiecesAIBoard(board, textViews);
            collapsePieces = new ArrayList<>();
        }
        return board;
    }

    private Graph<String, DefaultEdge> collapseAIGraph(ArrayList<Integer> board, ArrayList<TextView> textViews, ArrayList<String> collapsePieces, Graph<String, DefaultEdge> graph) {
        if(collapsePieces.size()> 0) {
            String piece = collapsePieces.get(0);
            ArrayList<String> vertices = getConnectedVertexOf(piece);

            Boolean isValid = false;
            ArrayList<String> textViewsCopy = new ArrayList<>();
            ArrayList<Integer> boardCopy = new ArrayList<>(board);
            for(TextView tv : textViews) {
                textViewsCopy.add(tv.getText().toString());
            }

            while(!isValid) {

                for(TextView tv : textViews) {
                    tv.setText(textViewsCopy.get(textViews.indexOf(tv)));
                }
                board = new ArrayList<>();
                board.addAll(boardCopy);

                for(String vertex : vertices) {
                    ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                    Integer index;

                    if(indexesToCollapse.size() > 1) {
                        index = indexesToCollapse.get(random.nextInt(2));
                    } else if (indexesToCollapse.size() == 1){
                        index = indexesToCollapse.get(0);
                    } else {
                        break;
                    }

                    String collapsePiece = vertex.substring(0,1);

                    textViews.get(index).setText(collapsePiece);
                    if(collapsePiece.equals("X")) {
                        board.set(index, 1);
                    } else {
                        board.set(index, 0);
                    }

                    if (vertices.indexOf(vertex) == vertices.size() - 1) {
                        isValid = true;
                    }
                }
            }

            for(String vertex : vertices) {
                graph.removeVertex(vertex);
            }
        }
        return graph;
    }

    private ArrayList<TextView> deleteRemainingQuantumPiecesAI(ArrayList<Integer> board, ArrayList<TextView> textViews) {
        HashMap<String, Integer> qp = new HashMap<>();

        for(TextView tv: textViews) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String piece : pieces) {
                if (!piece.equals("") && piece.length() > 1) {
                    if (qp.containsKey(piece)) {
                        Integer count = qp.get(piece);
                        count++;
                        qp.put(piece, count);
                    } else {
                        qp.put(piece, 1);
                    }
                }
            }
        }

        for(String piece: qp.keySet()) {
            if(qp.get(piece).equals(1)) {
                for (TextView tv : textViews) {
                    if(tv.getText().toString().contains(piece)) {
                        tv.setText("");
                        board.set(textViews.indexOf(tv), 2);
                    }
                }
            }
        }
        return textViews;
    }

    private ArrayList<Integer> deleteRemainingQuantumPiecesAIBoard(ArrayList<Integer> board, ArrayList<TextView> textViews) {
        HashMap<String, Integer> qp = new HashMap<>();

        for(TextView tv: textViews) {
            String[] pieces = tv.getText().toString().split(" ");
            for (String piece : pieces) {
                if (!piece.equals("") && piece.length() > 1) {
                    if (qp.containsKey(piece)) {
                        Integer count = qp.get(piece);
                        count++;
                        qp.put(piece, count);
                    } else {
                        qp.put(piece, 1);
                    }
                }
            }
        }

        for(String piece: qp.keySet()) {
            if(qp.get(piece).equals(1)) {
                for (TextView tv : textViews) {
                    if(tv.getText().toString().contains(piece)) {
                        tv.setText("");
                        board.set(textViews.indexOf(tv), 2);
                    }
                }
            }
        }
        return board;
    }

    private Integer numberOfMillsAI(Integer num, ArrayList<Integer> board) {
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

    private ArrayList<Integer> getPlacesWithPiece (Integer piece, ArrayList<Integer> board){
        ArrayList<Integer> placesWithPieces = new ArrayList<>();
        if (piece==0){
            for (int i=0;i<board.size();i++){
                if (board.get(i)==0){
                    placesWithPieces.add(i);
                }
            }
            return placesWithPieces;
        } else if (piece==1){
            for (int i=0;i<board.size();i++){
                if (board.get(i)==1){
                    placesWithPieces.add(i);
                }
            }
            return placesWithPieces;
        }else {
            return placesWithPieces;
        }
    }

    private ArrayList<Integer> availableMovesOfAI(Integer piece, ArrayList<Integer> board, ArrayList<TextView> textViews) {
        ArrayList<Integer> res = new ArrayList<>();
        if(numberOfPiecesOf(textViews.get(piece).getText().toString()) > 3) {
            switch (piece) {
                case 0:
                    res.add(0);
                    if(boxIsEmptyAI(1,board)) {
                        res.add(1);
                    }
                    if (boxIsEmptyAI(9,board)){
                        res.add(9);
                    }
                    break;
                case 1:
                    res.add(1);
                    if(boxIsEmptyAI(0,board)) {
                        res.add(0);
                    }
                    if (boxIsEmptyAI(2,board)){
                        res.add(2);
                    }
                    if (boxIsEmptyAI(4,board)){
                        res.add(4);
                    }
                    break;
                case 2:
                    res.add(2);
                    if(boxIsEmptyAI(1,board)) {
                        res.add(1);
                    }
                    if (boxIsEmptyAI(14,board)){
                        res.add(14);
                    }
                    break;
                case 3:
                    res.add(3);
                    if(boxIsEmptyAI(4,board)) {
                        res.add(4);
                    }
                    if (boxIsEmptyAI(10,board)){
                        res.add(10);
                    }
                    break;
                case 4:
                    res.add(4);
                    if(boxIsEmptyAI(1,board)) {
                        res.add(1);
                    }
                    if (boxIsEmptyAI(3,board)){
                        res.add(3);
                    }
                    if (boxIsEmptyAI(5,board)){
                        res.add(5);
                    }
                    if (boxIsEmptyAI(7,board)) {
                        res.add(7);
                    }
                    break;
                case 5:
                    res.add(5);
                    if(boxIsEmptyAI(4,board)) {
                        res.add(4);
                    }
                    if (boxIsEmptyAI(13,board)){
                        res.add(13);
                    }
                    break;
                case 6:
                    res.add(6);
                    if(boxIsEmptyAI(7,board)) {
                        res.add(7);
                    }
                    if (boxIsEmptyAI(11,board)){
                        res.add(11);
                    }
                    break;
                case 7:
                    res.add(7);
                    if(boxIsEmptyAI(4,board)) {
                        res.add(4);
                    }
                    if (boxIsEmptyAI(6,board)){
                        res.add(6);
                    }
                    if (boxIsEmptyAI(8,board)){
                        res.add(8);
                    }
                    break;
                case 8:
                    res.add(8);
                    if(boxIsEmptyAI(7,board)) {
                        res.add(7);
                    }
                    if (boxIsEmptyAI(12,board)){
                        res.add(12);
                    }
                    break;
                case 9:
                    res.add(9);
                    if(boxIsEmptyAI(0,board)) {
                        res.add(0);
                    }
                    if (boxIsEmptyAI(10,board)){
                        res.add(10);
                    }
                    if (boxIsEmptyAI(21,board)){
                        res.add(21);
                    }
                    break;
                case 10:
                    res.add(10);
                    if(boxIsEmptyAI(3,board)) {
                        res.add(3);
                    }
                    if (boxIsEmptyAI(9,board)){
                        res.add(9);
                    }
                    if (boxIsEmptyAI(11,board)){
                        res.add(11);
                    }
                    if (boxIsEmptyAI(18,board)) {
                        res.add(18);
                    }
                    break;
                case 11:
                    res.add(11);
                    if(boxIsEmptyAI(6,board)) {
                        res.add(6);
                    }
                    if (boxIsEmptyAI(10,board)){
                        res.add(10);
                    }
                    if (boxIsEmptyAI(15,board)){
                        res.add(15);
                    }
                    break;
                case 12:
                    res.add(12);
                    if(boxIsEmptyAI(8,board)) {
                        res.add(8);
                    }
                    if (boxIsEmptyAI(13,board)){
                        res.add(13);
                    }
                    if (boxIsEmptyAI(17,board)){
                        res.add(17);
                    }
                    break;
                case 13:
                    res.add(13);
                    if(boxIsEmptyAI(5,board)) {
                        res.add(5);
                    }
                    if (boxIsEmptyAI(12,board)){
                        res.add(12);
                    }
                    if (boxIsEmptyAI(14,board)){
                        res.add(14);
                    }
                    if (boxIsEmptyAI(20,board)) {
                        res.add(20);
                    }
                    break;
                case 14:
                    res.add(14);
                    if(boxIsEmptyAI(2,board)) {
                        res.add(2);
                    }
                    if (boxIsEmptyAI(13,board)){
                        res.add(13);
                    }
                    if (boxIsEmptyAI(23,board)){
                        res.add(23);
                    }
                    break;
                case 15:
                    res.add(15);
                    if(boxIsEmptyAI(11,board)) {
                        res.add(11);
                    }
                    if (boxIsEmptyAI(16,board)){
                        res.add(16);
                    }
                    break;
                case 16:
                    res.add(16);
                    if(boxIsEmptyAI(15,board)) {
                        res.add(15);
                    }
                    if (boxIsEmptyAI(17,board)){
                        res.add(17);
                    }
                    if (boxIsEmptyAI(19,board)){
                        res.add(19);
                    }
                    break;
                case 17:
                    res.add(17);
                    if(boxIsEmptyAI(12,board)) {
                        res.add(12);
                    }
                    if (boxIsEmptyAI(16,board)){
                        res.add(16);
                    }
                    break;
                case 18:
                    res.add(18);
                    if(boxIsEmptyAI(10,board)) {
                        res.add(10);
                    }
                    if (boxIsEmptyAI(19,board)){
                        res.add(19);
                    }
                    break;
                case 19:
                    res.add(19);
                    if(boxIsEmptyAI(16,board)) {
                        res.add(16);
                    }
                    if (boxIsEmptyAI(18,board)){
                        res.add(18);
                    }
                    if (boxIsEmptyAI(20,board)){
                        res.add(20);
                    }
                    if (boxIsEmptyAI(22,board)) res.add(22);
                    break;
                case 20:
                    res.add(20);
                    if(boxIsEmptyAI(13,board)) {
                        res.add(13);
                    }
                    if (boxIsEmptyAI(19,board)){
                        res.add(19);
                    }
                    break;
                case 21:
                    res.add(21);
                    if(boxIsEmptyAI(9,board)) {
                        res.add(9);
                    }
                    if (boxIsEmptyAI(22,board)){
                        res.add(22);
                    }
                    break;
                case 22:
                    res.add(22);
                    if(boxIsEmptyAI(19,board)) {
                        res.add(19);
                    }
                    if (boxIsEmptyAI(21,board)){
                        res.add(21);
                    }
                    if (boxIsEmptyAI(23,board)){
                        res.add(23);
                    }
                    break;
                case 23:
                    res.add(23);
                    if(boxIsEmptyAI(14,board)) {
                        res.add(14);
                    }
                    if (boxIsEmptyAI(22,board)){
                        res.add(22);
                    }
                    break;
            }
        } else {
            for(int i = 0; i < 24; i++) {
                if(boxIsEmptyAI(i,board)) {
                    res.add(i);
                }
            }
        }

        return res;
    }

    private Boolean boxIsEmptyAI(Integer box, ArrayList<Integer> board) {
        Boolean res = false;
        if (board.get(box).equals(2) || board.get(box).equals(3)) {
            res = true;
        }
        return res;
    }

    private ArrayList<TextView> collapseMovingAI(TextView tv, ArrayList<Integer> board, ArrayList<TextView> textViews, Graph<String, DefaultEdge> graph) {
        if(!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String piece : pieces) {
                if(piece.length() != 0) {
                    ArrayList<String> vertices = getConnectedVertexOf(piece);

                    Boolean isValid = false;
                    ArrayList<String> textViewsCopy = new ArrayList<>();
                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                    for(TextView t : textViews) {
                        textViewsCopy.add(t.getText().toString());
                    }

                    while(!isValid) {

                        for(TextView t : textViews) {
                            t.setText(textViewsCopy.get(textViews.indexOf(t)));
                        }
                        board = new ArrayList<>();
                        board.addAll(boardCopy);

                        for(String vertex : vertices) {
                            ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                            Integer index;

                            if(indexesToCollapse.size() > 1) {
                                index = indexesToCollapse.get(random.nextInt(2));
                            } else if (indexesToCollapse.size() == 1){
                                index = indexesToCollapse.get(0);
                            } else {
                                break;
                            }

                            String collapsePiece = vertex.substring(0,1);

                            textViews.get(index).setText(collapsePiece);
                            if(collapsePiece.equals("X")) {
                                board.set(index, 1);
                            } else {
                                board.set(index, 0);
                            }

                            if (vertices.indexOf(vertex) == vertices.size() - 1) {
                                isValid = true;
                            }
                        }
                    }

                    for(String vertex : vertices) {
                        graph.removeVertex(vertex);
                    }

                    textViews = deleteRemainingQuantumPiecesAI(board,textViews);

                    break;
                }
            }
        }
        return textViews;
    }

    private ArrayList<Integer> collapseMovingAIBoard(TextView tv, ArrayList<Integer> board, ArrayList<TextView> textViews, Graph<String, DefaultEdge> graph) {
        if(!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String piece : pieces) {
                if(piece.length() != 0) {
                    ArrayList<String> vertices = getConnectedVertexOf(piece);

                    Boolean isValid = false;
                    ArrayList<String> textViewsCopy = new ArrayList<>();
                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                    for(TextView t : textViews) {
                        textViewsCopy.add(t.getText().toString());
                    }

                    while(!isValid) {

                        for(TextView t : textViews) {
                            t.setText(textViewsCopy.get(textViews.indexOf(t)));
                        }
                        board = new ArrayList<>();
                        board.addAll(boardCopy);

                        for(String vertex : vertices) {
                            ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                            Integer index;

                            if(indexesToCollapse.size() > 1) {
                                index = indexesToCollapse.get(random.nextInt(2));
                            } else if (indexesToCollapse.size() == 1){
                                index = indexesToCollapse.get(0);
                            } else {
                                break;
                            }

                            String collapsePiece = vertex.substring(0,1);

                            textViews.get(index).setText(collapsePiece);
                            if(collapsePiece.equals("X")) {
                                board.set(index, 1);
                            } else {
                                board.set(index, 0);
                            }

                            if (vertices.indexOf(vertex) == vertices.size() - 1) {
                                isValid = true;
                            }
                        }
                    }

                    for(String vertex : vertices) {
                        graph.removeVertex(vertex);
                    }

                    board = deleteRemainingQuantumPiecesAIBoard(board,textViews);

                    break;
                }
            }
        }
        return board;
    }

    private Graph<String, DefaultEdge> collapseMovingAIGraph(TextView tv, ArrayList<Integer> board, ArrayList<TextView> textViews, Graph<String, DefaultEdge> graph) {
        if(!tv.getText().toString().equals("")) {
            String[] pieces = tv.getText().toString().split(" ");
            for(String piece : pieces) {
                if(piece.length() != 0) {
                    ArrayList<String> vertices = getConnectedVertexOf(piece);

                    Boolean isValid = false;
                    ArrayList<String> textViewsCopy = new ArrayList<>();
                    ArrayList<Integer> boardCopy = new ArrayList<>(board);
                    for(TextView t : textViews) {
                        textViewsCopy.add(t.getText().toString());
                    }

                    while(!isValid) {

                        for(TextView t : textViews) {
                            t.setText(textViewsCopy.get(textViews.indexOf(t)));
                        }
                        board = new ArrayList<>();
                        board.addAll(boardCopy);

                        for(String vertex : vertices) {
                            ArrayList<Integer> indexesToCollapse = getIndexesOfPiece(vertex);

                            Integer index;

                            if(indexesToCollapse.size() > 1) {
                                index = indexesToCollapse.get(random.nextInt(2));
                            } else if (indexesToCollapse.size() == 1){
                                index = indexesToCollapse.get(0);
                            } else {
                                break;
                            }

                            String collapsePiece = vertex.substring(0,1);

                            textViews.get(index).setText(collapsePiece);
                            if(collapsePiece.equals("X")) {
                                board.set(index, 1);
                            } else {
                                board.set(index, 0);
                            }

                            if (vertices.indexOf(vertex) == vertices.size() - 1) {
                                isValid = true;
                            }
                        }
                    }

                    for(String vertex : vertices) {
                        graph.removeVertex(vertex);
                    }

                    break;
                }
            }
        }
        return graph;
    }

    private String choosePhaseAI(String phase, Integer xPiecesPlaced, Integer oPiecesPlaced, ArrayList<TextView>textViews) {
        if(phase.equals("Placing")){
            if(xPiecesPlaced.equals(0) && oPiecesPlaced.equals(0)) {
                phase = "Moving";
            }
        } else if (phase.equals("Moving")){
            if(numberOfPiecesOfAI("X",textViews).equals(3) || numberOfPiecesOfAI("O",textViews).equals(3)) {
                phase = "Flying";
            }
        }
        return phase;
    }

}
