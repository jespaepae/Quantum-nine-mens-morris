package com.example.quantumninemensmorris;

import java.util.ArrayList;
import java.util.Arrays;

public class EvalFunction {

    private static Integer value = 10;

    public static Integer f1(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(player) && positions.contains(i)) {
                res += value;
                break;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
                break;
            }
        }
        return res;
    }

    public static Integer f2(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(player) && positions.contains(i)) {
                res += value;
                break;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
                break;
            }
        }
        return res;
    }

    public static Integer f3(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        for(int i = 0; i < board.size(); i++) {
            if (board.get(i).equals(player) && positions.contains(i)) {
                res += value;
                break;
            } else if (board.get(i).equals(3) && positions.contains(i)) {
                res += value/2;
                break;
            }
        }
        return res;
    }

    public static Integer f4(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f5(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f6(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        for (Integer pos : positions) {
            for (Integer pos2: positions) {
                if (!pos.equals(pos2)) {
                    if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                        res += value;
                    } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                            board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                        res += (int) Math.round(value*0.8);
                    } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                        res += value/2;
                    }
                }
            }
        }
        return res;
    }

    public static Integer f7(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f8(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(3, 4, 5, 10, 13, 18, 19, 20));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f9(ArrayList<Integer> board, Integer player) {
        Integer res = 0;
        ArrayList<Integer> positions1 = new ArrayList<>(Arrays.asList(0, 1, 2, 9, 14, 21, 22, 23));
        ArrayList<Integer> positions2 = new ArrayList<>(Arrays.asList(6, 7, 8, 11, 12, 15, 16, 17));

        for (Integer pos : positions1) {
            for (Integer pos2: positions2) {
                if (board.get(pos).equals(player) && board.get(pos2).equals(player)) {
                    res += value;
                } else if ((board.get(pos).equals(player) || board.get(pos2).equals(player)) &&
                        board.get(pos).equals(3) || board.get(pos2).equals(3)) {
                    res += (int) Math.round(value*0.8);
                } else if (board.get(pos).equals(3) && board.get(pos2).equals(3)) {
                    res += value/2;
                }
            }
        }
        return res;
    }

    public static Integer f10(ArrayList<Integer> board, Integer player) {
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

    public static Integer f11(ArrayList<Integer> board, Integer player) {
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

    public static Integer f12(ArrayList<Integer> board, Integer player) {
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

    public static Integer f13(ArrayList<Integer> board, Integer player) {
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
