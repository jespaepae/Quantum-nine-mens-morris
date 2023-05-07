package com.example.quantumninemensmorris;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Square {

    private Boolean isQuantum;
    private ArrayList<String> text;

    public Square(Boolean isQuantum, ArrayList<String> text) {
        this.isQuantum = isQuantum;
        this.text = text;
    }

    public Square() {
        this.isQuantum = true;
        this.text = new ArrayList<>();
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public Boolean getQuantum() {
        return isQuantum;
    }

    public void setQuantum(Boolean quantum) {
        isQuantum = quantum;
    }

    public void addPiece(String newText) {
        this.text.add(newText);
    }

    @Override
    public String toString() {
        return this.getText().stream().collect(Collectors.joining(""));
    }
}
