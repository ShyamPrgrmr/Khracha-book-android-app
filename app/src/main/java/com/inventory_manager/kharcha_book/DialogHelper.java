package com.inventory_manager.kharcha_book;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
public class DialogHelper extends DialogFragment {
    private EditText textStartDate,textEndDate;
    private DialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialog_layout,null);
        builder.setView(view).setMessage("Custom Search")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String stDate = textStartDate.getText().toString();
                        String edDate = textEndDate.getText().toString();
                        listener.getDates(stDate,edDate);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        textStartDate = view.findViewById(R.id.dialog_input_start_date);
        textEndDate = view.findViewById(R.id.dialog_input_end_date);
        return builder.create();
    }
    public interface  DialogListener{
        void getDates(String st,String ed);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"Must include Dialog Listener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Button positive = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        positive.setTextColor(Color.BLACK);
        negative.setTextColor(Color.BLACK);
    }
}
