package com.mickeywilliamson.mickey.tetrisuis;

/**
 * Created by mickey on 7/12/17.
 */

public class I extends Piece {

    public I() {
        setRotation(0);
        block = getBlock();
        colorIndex = 1; // Red
        fileName = "i";
    }

    public void setRotation(int rotate) {
        switch (rotate) {
            case 0:
            case 2:
                block = new int[][]{
                        {1},
                        {1},
                        {1},
                        {1}
                };
                break;
            case 1:
            case 3:
                block = new int[][]{
                        {1, 1, 1, 1}
                };

                break;
        }
    }

    public String toString() {
        return "I";
    }
}

