package self.joanciscar.myapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import self.joanciscar.myapplication.R;

public class ItemDialog extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(ItemDialog dialog);
        void onDialogNegativeClick(ItemDialog dialog);
    }
    private NoticeDialogListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_item_formulary, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    //    mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
     //   String title = getArguments().getString("title", "Enter Name");
      //  getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
    //    mEditText.requestFocus();
     //   getDialog().getWindow().setSoftInputMode(
     //           WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("fire", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if(id == Dialog.BUTTON_NEGATIVE) {
                    listener.onDialogNegativeClick(ItemDialog.this);
                } else if(id == Dialog.BUTTON_POSITIVE) {
                    listener.onDialogPositiveClick(ItemDialog.this);
                } else {

                }
            }
        });
        /*builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });*/
        // Create the AlertDialog object and return it
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.activity_item_formulary,new LinearLayout(getActivity()), false));
        return builder.create();
    }
}