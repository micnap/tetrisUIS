package com.mickeywilliamson.mickey.tetrisuis;


import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;

/**
 * Created by mickey on 7/12/17.
 */

public abstract class Piece {

    int colorIndex;

    public static int[] colors = {
            0,
            Color.RED,
            Color.rgb(255,69,0), // Orange
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.rgb(128, 0, 128), // Purple
            Color.WHITE,
    };

    int[][] block;
    private int rotation = 0;
    int color;
    Point coords = new Point(4,0);
    String fileName;

    public void rotate(String direction) {
        if (direction.equals("left")) {
            rotation = (rotation == 0) ? 3 : --rotation;
        } else {
            rotation = (rotation == 3) ? 0 : ++rotation;
        }
        setRotation(rotation);
    }

    abstract void setRotation(int rotation);

    public int getHeight() {
        return block.length;
    }

    public int getWidth() {
        return block[0].length;
    }

    public int getRotation() {
        return rotation;
    }

    public int getColor() {
        return colors[colorIndex];
    }

    public int[][] getBlock() {
        return block;
    }

    public String getFileName() { return fileName; }
}

