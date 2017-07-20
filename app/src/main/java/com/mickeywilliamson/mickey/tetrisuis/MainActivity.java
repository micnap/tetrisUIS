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

    private final static int GRID_HEIGHT = 15;
    private final static int GRID_WIDTH = 9;

    private Handler handler;
    private Piece piece;
    private Piece nextPiece;
    private int pieceCount = -1;
    private int row = 0;
    private int column = 4;
    private boolean endGame = false;
    private TextView score;
    private SoundPool soundPool;
    private int soundPieceMove = -1;
    private int soundPieceRotate = -1;
    private int soundPieceLand = -1;

    private int[][] gameGrid = new int[][]{
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(this, getSpeed());

            //if (!endGame) {
            if (!endGame) {
                placePiece();
                row++;
                checkForCollision();

            } else {
                handler.postDelayed(this, 10000);
                piece = null;
                nextPiece = null;
                pieceCount = -1;
                updateScore(0);
                row = 0;
                column = 4;
                endGame = false;
                gameGrid = new int[][]{
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
            }

        }
    };

    public void rotateLeft(View view) {
        if (piece == null) {
            return;
        }
        piece.rotate("left");
        soundPool.play(soundPieceRotate, 1, 1, 0, 0, 1);
    }

    public void rotateRight(View view) {
        if (piece == null) {
            return;
        }
        piece.rotate("right");
        soundPool.play(soundPieceRotate, 1, 1, 0, 0, 1);
    }

    public void moveLeft(View view) {
        if (piece == null) {
            return;
        }
        if (column > 0) {
            --column;
            soundPool.play(soundPieceMove, 1, 1, 0, 0, 1);
        }
    }

    public void moveRight(View view) {
        if (piece == null) {
            return;
        }
        if (column <= GRID_WIDTH - piece.getWidth()) {
            ++column;
            soundPool.play(soundPieceMove, 1, 1, 0, 0, 1);
        }
    }

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

    private void checkForCollision() {

        if (piece == null) {
            if (nextPiece == null) {
                nextPiece = pickPiece();
            }
            piece = nextPiece;
            nextPiece = pickPiece();

            // Display the next piece on the right side of screen.
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
            removeFullRows();
            piece = null;
            soundPool.play(soundPieceLand, 1, 1, 0, 0, 1);
            return;

        // Check if this is the last piece of the game.
        } else if (gameGrid[height][column] > 0) {
            for (int pieceRow = height - 1; pieceRow >= 0; pieceRow--) {
                int gameGridRow = height - 1;
                for (int pieceCol = 0; pieceCol < width; pieceCol++) {
                    if (block[pieceRow][pieceCol] == 1) {

                        while (gameGrid[gameGridRow][column + pieceCol] > 0 && gameGridRow > 0) {
                            gameGridRow--;
                        }

                        gameGrid[gameGridRow][column + pieceCol] = piece.colorIndex;
                    }
                }

                if (gameGridRow == 0) {
                    drawGrid();
                    piece = null;
                    soundPool.play(soundPieceLand, 1, 1, 0, 0, 1);
                    endGame = true;
                    return;
                }
            }
        }


        // All pieces except the first and last.
        // Loop through the piece.
        for (int pieceRow = height - 1; pieceRow >= 0; pieceRow--) {
            for (int pieceCol = 0; pieceCol < width; pieceCol++) {
                if (block[pieceRow][pieceCol] == 1 && gameGrid[row + pieceRow][column + pieceCol] > 0) {

                    // The piece will collide.  Register the piece on the grid.
                    registerOnGrid();
                    removeFullRows();
                    piece = null;
                    drawGrid();
                    soundPool.play(soundPieceLand, 1, 1, 0, 0, 1);
                    return;
                }
            }
        }
    }

    private void drawGrid() {
        for (int gridRow = 0; gridRow <= GRID_HEIGHT; gridRow++) {
            for (int gridCol = 0; gridCol <= GRID_WIDTH; gridCol++) {

                // If block is occupied, set appropriate color to the block.
                if (gameGrid[gridRow][gridCol] > 0) {
                    int resID = getResources().getIdentifier("block" + gridRow + "_" + gridCol, "id", getPackageName());
                    ImageView image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(Piece.colors[gameGrid[gridRow][gridCol]]);

                    // Add a border.
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                    marginParams.setMargins(5,5,5,5);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                    image.setLayoutParams(layoutParams);

                // If block is empty, set it to transparent.
                } else {
                    int resID = getResources().getIdentifier("block" + gridRow + "_" + gridCol, "id", getPackageName());
                    ImageView image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(Color.TRANSPARENT);

                    // Add a border.
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                    marginParams.setMargins(5,5,5,5);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                    image.setLayoutParams(layoutParams);
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

                    // Add a border.
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                    marginParams.setMargins(5,5,5,5);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                    image.setLayoutParams(layoutParams);
                }

                if (block[(i-row)][j-column] == 1) {
                    resID = getResources().getIdentifier("block" + i + "_" + (j), "id", getPackageName());
                    image = (ImageView) findViewById(resID);
                    image.setBackgroundColor(piece.getColor());

                    // Add a border.
                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                    marginParams.setMargins(5,5,5,5);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                    image.setLayoutParams(layoutParams);

                } else {
                    if (gameGrid[i][j] == 0) {
                        resID = getResources().getIdentifier("block" + i + "_" + (j), "id", getPackageName());
                        image = (ImageView) findViewById(resID);
                        image.setBackgroundColor(Color.TRANSPARENT);

                        // Add a border.
                        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(image.getLayoutParams());
                        marginParams.setMargins(5,5,5,5);
                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(marginParams);
                        image.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }


    private void registerOnGrid() {
        int height = piece.getBlock().length;
        int width = piece.getBlock()[0].length;
        int[][] block = piece.getBlock();

        for (int h = height - 1; h >= 0; h--) {
            for (int w = 0; w < width; w++) {

                if (block[h][w] == 1) {

                    // There's enough room.  Log it.
                    if (row > 1) {
                        gameGrid[row + h - 1][column + w] = piece.colorIndex;
                    }

                }
            }
        }

        // Update the score.
        updateScore();

    }

    private void updateScore() {
        score = (TextView) findViewById(R.id.score);
        score.setText(String.valueOf(pieceCount));
    }

    private void updateScore(int pieceCount) {
        score = (TextView) findViewById(R.id.score);
        score.setText(String.valueOf(pieceCount));
    }

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

        // Adding new rows at beginning to make up for rows that will be removed.
        int[][] tempArray = new int[16][10];

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
        }

        gameGrid = tempArray;
        drawGrid();
    }

    private Piece pickPiece() {
        row = 0;
        column = 4;
        pieceCount++;

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
        Log.d("GRID ARRAY", arrayString);
    }
}