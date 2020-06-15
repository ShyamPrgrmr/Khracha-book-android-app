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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogHelper3 extends DialogFragment {


    private  String name,amount,date,note,idid;
    private TextView textName,textAmount,textDate,textNote;
    DialogHelper3(String id,String name,String amount,String date,String note){
        this.idid = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    private DialogHelper3.DialogListener3 listener;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialog_layout_3,null);
        builder.setView(view).setMessage("Do you really wants to delete?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.successMessage(idid,"success");
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        textName   = view.findViewById(R.id.dialog3Name);
        textAmount = view.findViewById(R.id.dialog3Amount);
        textDate   = view.findViewById(R.id.dialog3Date);
        textNote   = view.findViewById(R.id.dialog3Note);
        textName.setText(getString(R.string.label_dialog3_name)+name);
        textAmount.setText(getString(R.string.label_dialog3_amount)+amount);
        textNote.setText(getString(R.string.label_dialog3_note)+note);
        textDate.setText(getString(R.string.label_dialog3_date)+date);
        return builder.create();
    }
    public interface  DialogListener3{
        void successMessage(String id,String msg);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogHelper3.DialogListener3) context;
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
