package com.mickeywilliamson.mickey.tetrisuis;

import android.graphics.Color;

/**
 * Created by mickey on 7/12/17.
 */

public class L extends Piece {

    public L() {
        setRotation(0);
        block = getBlock();
        colorIndex = 3; // Yellow
        fileName = "l";
    }

    public void setRotation(int rotate) {
        switch (rotate) {
            case 0:
                block = new int[][]{
                        {1, 0},
                        {1, 0},
                        {1, 1}
                };
                break;
            case 1:
                block = new int[][]{
                        {0, 0, 1},
                        {1, 1, 1}
                };

                break;
            case 2:
                block = new int[][]{
                        {1, 1},
                        {0, 1},
                        {0, 1}
                };

                break;
            case 3:
                block = new int[][]{
                        {1, 1, 1},
                        {1, 0, 0}
                };
                break;
        }
    }

    public String toString() {
        return "L";
    }
}

