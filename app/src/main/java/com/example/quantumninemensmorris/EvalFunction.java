package com.example.quantumninemensmorris;

import java.util.ArrayList;
import java.util.Arrays;

public class EvalFunction {

    private static Integer value = 10;

    public static Integer f1(ArrayList<String> board, String player) {
        System.out.println(board);
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).contains(player) && positions.contains(i)) {
                res += value;
            }
        }
        return res;
    }

    public static Integer f2(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).contains(player) && positions.contains(i)) {
                res += value;
            }
        }
        return res;
    }

    public static Integer f3(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).contains(player) && positions.contains(i)) {
                res += value;
            }
        }
        return res;
    }

    public static Integer f4(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f5(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f6(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f7(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f8(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f9(ArrayList<String> board, String player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).contains(player) || board.get(pos2).contains(player)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).contains(player) && board.get(pos2).contains(player)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f10(ArrayList<String> board, String player) {
        Integer res = 0;

        if (board.get(6).equals(player) && board.get(7).equals(player) && board.get(8).equals(player)) {
            res += value;
        }
        if (board.get(15).equals(player) && board.get(16).equals(player) && board.get(17).equals(player)) {
            res += value;
        }
        if (board.get(6).equals(player) && board.get(11).equals(player) && board.get(15).equals(player)) {
            res += value;
        }
        if (board.get(8).equals(player) && board.get(12).equals(player) && board.get(17).equals(player)) {
            res += value;
        }
        return res;

    }

    public static Integer f11(ArrayList<String> board, String player) {
        Integer res = 0;

        if (board.get(3).equals(player) && board.get(4).equals(player) && board.get(5).equals(player)) {
            res += value;
        }
        if (board.get(18).equals(player) && board.get(19).equals(player) && board.get(20).equals(player)) {
            res += value;
        }
        if (board.get(3).equals(player) && board.get(10).equals(player) && board.get(18).equals(player)) {
            res += value;
        }
        if (board.get(5).equals(player) && board.get(13).equals(player) && board.get(20).equals(player)) {
            res += value;
        }
        return res;

    }

    public static Integer f12(ArrayList<String> board, String player) {
        Integer res = 0;

        if (board.get(0).equals(player) && board.get(1).equals(player) && board.get(2).equals(player)) {
            res += value;
        }
        if (board.get(21).equals(player) && board.get(22).equals(player) && board.get(23).equals(player)) {
            res += value;
        }
        if (board.get(0).equals(player) && board.get(9).equals(player) && board.get(21).equals(player)) {
            res += value;
        }
        if (board.get(2).equals(player) && board.get(14).equals(player) && board.get(23).equals(player)) {
            res += value;
        }
        return res;

    }

    public static Integer f13(ArrayList<String> board, String player) {
        Integer res = 0;

        if (board.get(1).equals(player) && board.get(4).equals(player) && board.get(7).equals(player)) {
            res += value;
        }
        if (board.get(9).equals(player) && board.get(10).equals(player) && board.get(11).equals(player)) {
            res += value;
        }
        if (board.get(12).equals(player) && board.get(13).equals(player) && board.get(14).equals(player)) {
            res += value;
        }
        if (board.get(16).equals(player) && board.get(19).equals(player) && board.get(22).equals(player)) {
            res += value;
        }
        return res;

    }

}
