package com.mickeywilliamson.mickey.tetrisuis;

import android.graphics.Color;

/**
 * Created by mickey on 7/12/17.
 */

public class S extends Piece {

    public S() {
        setRotation(0);
        block = getBlock();
        colorIndex = 5; // Blue
        fileName = "s";
    }

    public void setRotation(int rotate) {
        switch (rotate) {
            case 0:
            case 2:
                block = new int[][]{
                        {1, 0},
                        {1, 1},
                        {0, 1}
                };

                break;
            case 1:
            case 3:
                block = new int[][]{
                        {0, 1, 1},
                        {1, 1, 0}
                };
                break;
        }
    }

    public String toString() {
        return "S";
    }
}
