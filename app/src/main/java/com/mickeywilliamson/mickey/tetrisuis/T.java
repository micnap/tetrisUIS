package com.mickeywilliamson.mickey.tetrisuis;

import android.graphics.Color;

/**
 * Created by mickey on 7/12/17.
 */

public class T extends Piece {

    public T() {
        setRotation(0);
        block = getBlock();
        colorIndex = 6; // Purple
    }

    public void setRotation(int rotate) {
        switch (rotate) {
            case 0:
                block = new int[][]{
                        {1, 0},
                        {1, 1},
                        {1, 0}
                };
                break;
            case 1:
                block = new int[][]{
                        {1, 1, 1},
                        {0, 1, 0},

                };

                break;
            case 2:
                block = new int[][]{
                        {0, 1},
                        {1, 1},
                        {0, 1}

                };

                break;
            case 3:
                block = new int[][]{
                        {0, 1, 0},
                        {1, 1, 1},
                };
                break;
        }
    }

    public String toString() {
        return "T";
    }
}

