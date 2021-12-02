package kth.jjve.memeolise.game;
/*
function:
activity: results_activity
Jitse van Esch & Elisa Perini
2.12.21
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import kth.jjve.memeolise.R;

public class ResultsDialog extends AppCompatDialogFragment {
    private EditText inputName;
    private ResultsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_resultdialog, null);

        builder.setView(view)
                .setTitle("Name for results")
                .setNegativeButton("cancel", (dialog, which) -> {
                    listener.savingCancelled();
                })
                .setPositiveButton("save result", (dialog, which) -> {
                    String resultName = inputName.getText().toString();
                    listener.applyName(resultName);
                });
        inputName = view.findViewById(R.id.edit_inputname);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =(ResultsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ResultDialogListener");
        }
    }

    public interface ResultsDialogListener{
        void applyName(String name);

        void savingCancelled();
    }
}
