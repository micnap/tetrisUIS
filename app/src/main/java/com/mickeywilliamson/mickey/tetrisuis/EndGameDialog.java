package com.mickeywilliamson.mickey.tetrisuis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by mickey on 7/16/17.
 */

public class EndGameDialog extends DialogFragment {

    int taskId;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {



        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.new_game)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Piece piece;
                        Piece nextPiece;
                        int pieceCount = -1;
                        int row = 0;
                        int column = 4;
                        boolean endGame = false;

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);


                    }
                })
                .setNegativeButton(R.string.close_app, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().moveTaskToBack(true);
                        System.exit(0);

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}



