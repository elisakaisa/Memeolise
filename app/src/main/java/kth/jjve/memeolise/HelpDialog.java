package kth.jjve.memeolise;
/*
This class creates the help dialog in the home screen
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class HelpDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.dialog_help_title)
                .setMessage(R.string.dialog_help_message)
                .setPositiveButton("ok", (dialog, i) -> {
                });
        return builder.create();
    }
}
