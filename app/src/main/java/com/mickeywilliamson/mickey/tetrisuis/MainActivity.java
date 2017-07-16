package com.mickeywilliamson.mickey.tetrisuis;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    final static int GRID_HEIGHT = 15;
    final static int GRID_WIDTH = 9;

    Handler handler;
    Piece piece;
    Piece nextPiece;

    int pieceCount = -1;
    int row = 0;
    int column = 4;

    TextView score;


    SoundPool soundPool;
    int soundGameOver = -1, soundPieceMove = -1, soundPieceRotate = -1, soundPieceLand = -1;

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
        handler.postDelayed(runnable, getSpeed());

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        AssetManager assetManager = getAssets();
        try {
            AssetFileDescriptor soundGameOverFD = assetManager.openFd("gameover.ogg");
            soundGameOver = soundPool.load(soundGameOverFD, 1);
            AssetFileDescriptor soundPieceMoveFD = assetManager.openFd("piecemove.ogg");
            soundPieceMove = soundPool.load(soundPieceMoveFD, 1);
            AssetFileDescriptor soundPieceRotateFD = assetManager.openFd("piecerotate.ogg");
            soundPieceRotate = soundPool.load(soundPieceRotateFD, 1);
            AssetFileDescriptor soundPieceLandFD = assetManager.openFd("pieceland.ogg");
            soundPieceLand = soundPool.load(soundPieceLandFD, 1);
        } catch(Exception e) {
            Log.d("FILE", "File does not exist");
        }


    }

    public void rotateLeft(View view) {
        piece.rotate("left");
        soundPool.play(soundPieceRotate, 1, 1, 0, 0, 1);
    }

    public void rotateRight(View view) {
        piece.rotate("right");
        soundPool.play(soundPieceRotate, 1, 1, 0, 0, 1);
    }

    public void moveLeft(View view) {
        if (column > 0) {
            --column;
            soundPool.play(soundPieceMove, 1, 1, 0, 0, 1);
        }
    }

    public void moveRight(View view) {
        if (column <= GRID_WIDTH - piece.getWidth()) {
            ++column;
            soundPool.play(soundPieceMove, 1, 1, 0, 0, 1);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, getSpeed());

            placePiece();
            row++;
            checkForCollision();
        }
    };

    private int getSpeed() {
        if (pieceCount < 2) {
            return 1000;
        } else if (pieceCount < 3) {
            return 900;
        } else if (pieceCount < 4) {
            return 800;
        } else if (pieceCount < 5) {
            return 700;
        } else if (pieceCount < 6) {
            return 600;
        } else if (pieceCount < 7) {
            return 500;
        } else if (pieceCount < 8) {
            return 400;
        } else if (pieceCount < 9) {
            return 300;
        } else if (pieceCount < 10) {
            return 200;
        } else {
            return 100;
        }
    }

    private boolean checkForCollision() {
        boolean collision = false;

        if (piece == null) {
            //piece = pickPiece();
            if (nextPiece == null) {
                nextPiece = pickPiece();
            }
            piece = nextPiece;
            nextPiece = pickPiece();
            ImageView nextPieceIV = (ImageView) findViewById(R.id.nextPieceIV);
            int drawableResId = getResources().getIdentifier(nextPiece.getFileName(), "drawable", getPackageName());
            nextPieceIV.setImageResource(drawableResId);
        }

        int height = piece.getBlock().length;
        int width = piece.getBlock()[0].length;
        int[][] block = piece.getBlock();

        // Check if piece has reached the bottom.
        // Need to subtract one from row as row is actually for the next move (so we can check for potential collisions)
        // and we're checking for the past move.
        if (row - 1 + height > GRID_HEIGHT) {
            registerOnGrid();
            printGrid(gameGrid);
            removeFullRows();
            piece = null;
            soundPool.play(soundPieceLand, 1, 1, 0, 0, 1);
            return true;
        }

        // Loop through the piece.
        for (int pieceRow = 0; pieceRow < height; pieceRow++) {
            for (int pieceCol = 0; pieceCol < width; pieceCol++) {
                if (block[pieceRow][pieceCol] == 1 && gameGrid[row + pieceRow][column + pieceCol] > 0) {
                    // The piece will collide.  Register the piece on the grid.
                    registerOnGrid();
                    printGrid(gameGrid);
                    removeFullRows();
                    piece = null;
                    drawGrid();
                    soundPool.play(soundPieceLand, 1, 1, 0, 0, 1);
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
            //piece = pickPiece();
            if (nextPiece == null) {
                nextPiece = pickPiece();
            }
            piece = nextPiece;
            nextPiece = pickPiece();
            ImageView nextPieceIV = (ImageView) findViewById(R.id.nextPieceIV);
            int drawableResId = getResources().getIdentifier(nextPiece.getFileName(), "drawable", getPackageName());
            nextPieceIV.setImageResource(drawableResId);
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

        score = (TextView) findViewById(R.id.score);
        score.setText(String.valueOf(pieceCount));

    }

    // Brute force, baby.  Brute force.
    private void removeFullRows() {
        ArrayList<Integer> fullRows = new ArrayList<>();
        for (int r = 0; r < gameGrid.length; r++) {
            int col = 0;
            while (col < gameGrid[r].length && gameGrid[r][col] > 0 ) {


                if (col == 9) {
                    // We've hit a full row.  Mark it for removal.
                    fullRows.add(r);
                }
                col++;
            }
        }

        for (int i = 0; i < fullRows.size(); i++) {
            System.out.println(fullRows.get(i));
        }
        // Adding new rows at beginning to make up for rows that will be removed.
        int[][] tempArray = new int[16][10];
        System.out.println("fullRows size = " + fullRows.size());


        Log.d("TEST", "TEMP ARRAY PRINTING");
        printGrid(tempArray);

        int counter = 0;
        for (int r = 15; r >= 0; r--) {
            if (fullRows.contains(r)) {
                counter++;
                continue;
            }
            for (int c = 0; c <= 9; c++) {

                if (!fullRows.contains(r)) {
                    tempArray[r+counter][c] = gameGrid[r][c];
                }

            }
            System.out.println(counter);

        }
        gameGrid = tempArray;
        drawGrid();
    }

    private Piece pickPiece() {
        row = 0;
        column = 4;
        pieceCount++;
        //return new T();

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
        }
    }


    private void printGrid(int[][] gameGrid) {
        String arrayString = "";
        for (int i = 0; i <= GRID_HEIGHT; i++) {
            for (int j = 0; j <= GRID_WIDTH; j++) {
                arrayString += gameGrid[i][j] + ",";
            }
            arrayString += "\n";
        }
        Log.d("TEST", arrayString);


    }



}

