package com.example.quantumninemensmorris;

import java.util.ArrayList;
import java.util.Arrays;

public class EvalFunction {

    private static Integer value = 10;

    public static Integer f1(ArrayList<Integer> board) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(0) && positions.contains(i)) {
                res += value;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
            }
        }
        return res;
    }

    public static Integer f2(ArrayList<Integer> board) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(0) && positions.contains(i)) {
                res += value;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
            }
        }
        return res;
    }

    public static Integer f3(ArrayList<Integer> board) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(0) && positions.contains(i)) {
                res += value;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
            }
        }
        return res;
    }

    public static Integer f4(ArrayList<Integer> board) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 3, 6, 2, 5, 8, 15, 18, 21, 17, 20, 23));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(0) && positions.contains(i)) {
                res += value;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
            }
        }
        return res;
    }

    public static Integer f5(ArrayList<Integer> board) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(1, 4, 7, 9, 10, 11, 12, 13, 14, 16, 19, 22));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(0) && positions.contains(i)) {
                res += value;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
            }
        }
        return res;
    }

}
