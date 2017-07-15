package com.mickeywilliamson.mickey.tetrisuis;

import android.graphics.Color;

/**
 * Created by mickey on 7/12/17.
 */

public class Z extends Piece {

    public Z() {
        setRotation(0);
        block = getBlock();
        colorIndex = 7; // White
    }

    public void setRotation(int rotate) {
        switch (rotate) {
            case 0:
            case 2:
                block = new int[][]{
                        {0, 1},
                        {1, 1},
                        {1, 0}
                };

                break;
            case 1:
            case 3:
                block = new int[][]{
                        {1, 1, 0},
                        {0, 1, 1}
                };
                break;
        }
    }

    public String toString() {
        return "Z";
    }
}
