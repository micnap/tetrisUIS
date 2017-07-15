package com.mickeywilliamson.mickey.tetrisuis;


import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Handler handler;
    Piece piece;
    int pieceCount = 0;
    int row = 0;
    int column = 4;
    boolean register = false;
    //Timer timerNewPiece = new Timer();
    final static int GRID_HEIGHT = 15;
    final static int GRID_WIDTH = 9;

    int[][] gameGrid = new int[][]{
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    public void rotateLeft(View view) {
        piece.rotate("left");
    }

    public void rotateRight(View view) {
        piece.rotate("right");
    }

    public void moveLeft(View view) {
        if (column > 0) {
            --column;
        }
    }

    public void moveRight(View view) {
        if (column <= GRID_WIDTH - piece.getWidth()) {
            ++column;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);

            placePiece();
            row++;
            checkForCollision();
        }
    };


    private boolean checkForCollision() {
        boolean collision = false;

        if (piece == null) {
            piece = pickPiece();
        }

        int height = piece.getBlock().length;
        int width = piece.getBlock()[0].length;
        int[][] block = piece.getBlock();

        // Check if piece has reached the bottom.
        // Need to subtract one from row as row is actually for the next move (so we can check for potential collisions)
        // and we're checking for the past move.
        if (row - 1 + height > GRID_HEIGHT) {
            registerOnGrid();
            printGrid();
            piece = null;
            return true;
        }

        // Loop through the piece.
        for (int pieceRow = 0; pieceRow < height; pieceRow++) {
            for (int pieceCol = 0; pieceCol < width; pieceCol++) {
                if (block[pieceRow][pieceCol] == 1 && gameGrid[row + pieceRow][column + pieceCol] > 0) {
                    // The piece will collide.  Register the piece on the grid.
                    registerOnGrid();
                    printGrid();
                    piece = null;
                    drawGrid();
                    return true;
                }
            }

        }


        return false;
    }



    public void drawGrid() {
        for (int gridRow = 0; gridRow <= GRID_HEIGHT; gridRow++) {
            for (int gridCol = 0; gridCol <= GRID_WIDTH; gridCol++) {
                if (gameGrid[gridRow][gridCol] > 0) {
                    int resID = getResources().getIdentifier("block" + gridRow + "_" + gridCol, "id", getPackageName());
                    ImageView image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(Piece.colors[gameGrid[gridRow][gridCol]]);
                } else {
                    int resID = getResources().getIdentifier("block" + gridRow + "_" + gridCol, "id", getPackageName());
                    ImageView image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    }

    private void placePiece() {
        if (piece == null) {
            piece = pickPiece();
        }

        // Refresh the grid to erase any bits that get left behind when rotating pieces.
        drawGrid();



        // Get piece data.
        int height = piece.getBlock().length;
        int width = piece.getBlock()[0].length;
        int[][] block = piece.getBlock();

        // Piece starts at position - 4,0
        // Loop through the block of pieces on the gameGrid that the piece will occupy.
        for (int i = row; i < height + row; i++) { // row
            for (int j = column; j <= width + column - 1 ; j++) { // column


                int resID;
                ImageView image;

                Log.d("COLUMN", "" + column);
                Log.d("J IS", "" + j);
                Log.d("ROW IS", "" + row);
                Log.d("PIECE", piece.toString());

                // Erase previous row.
                if (row > 0) {



                    resID = getResources().getIdentifier("block" + (row - 1) + "_" + (column), "id", getPackageName());
                    image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(Color.TRANSPARENT);
                }

                if (block[(i-row)][j-column] == 1) {
                    resID = getResources().getIdentifier("block" + i + "_" + (j), "id", getPackageName());
                    image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(piece.getColor());

                    // Add a border.
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                    marginParams.setMargins(2,2,2,2);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                    image.setLayoutParams(layoutParams);

                } else {
                    if (gameGrid[i][j] == 0) {
                        resID = getResources().getIdentifier("block" + i + "_" + (j), "id", getPackageName());
                        image = (ImageView) findViewById(resID);
                        image.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }

        // Check for potential collision on next move.


    }

    private void registerOnGrid() {
        int height = piece.getBlock().length;
        int width = piece.getBlock()[0].length;
        int[][] block = piece.getBlock();


        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (block[h][w] == 1) {
                    gameGrid[row + h - 1][column + w] = piece.colorIndex;
                }

            }
        }
    }

    private Piece pickPiece() {
        row = 0;
        column = 4;
        pieceCount++;
        return new T();
/*
        Random rand = new Random();
        int randomNum = rand.nextInt(7);
        switch (randomNum) {
            case 0:
                return new I();
            case 1:
                return new J();
            case 2:
                return new L();
            case 3:
                return new O();
            case 4:
                return new S();
            case 5:
                return new T();
            case 6:
                return new Z();
            default:
                return new I();
        }*/
    }

    private void printGrid() {
        String arrayString = "";
        for (int i = 0; i <= GRID_HEIGHT; i++) {
            for (int j = 0; j <= GRID_WIDTH; j++) {
                arrayString += gameGrid[i][j] + ",";
            }
            arrayString += "\n";
        }


    }
}
/*
if (piece != null) {
                int height = piece.getBlock().length;
                int width = piece.getBlock()[0].length;
                int[][] block = piece.getBlock();

                for (int w = 0; w < width; w++) {
                    Log.d("ROW", "" + row);
                    if (block[height-1][w] == 1 && gameGrid[row][w + 4] > 0) {
                        registerOnGrid(row-1);
                        //printGrid();
                        piece = null;
                    }

                }
            }
 */
