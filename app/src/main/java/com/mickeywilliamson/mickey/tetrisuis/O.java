package com.mickeywilliamson.mickey.tetrisuis;

/**
 * Created by mickey on 7/12/17.
 */

public class O extends Piece {

    public O() {
        setRotation(0);
        block = getBlock();
        colorIndex = 4; // Green
        fileName = "o";
    }

    public void setRotation(int rotate) {
        block = new int[][]{
                {1, 1},
                {1, 1}
        };
    }

    public String toString() {
        return "O";
    }
}