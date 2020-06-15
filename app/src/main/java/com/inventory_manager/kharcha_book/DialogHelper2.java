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

public class DialogHelper2 extends DialogFragment {

    private EditText textName,textAmount,textNote;
    private DialogHelper2.DialogListener2 listener;
    private String n,a,no,idid;

    DialogHelper2(String id,String name,String amount,String note){
        idid =id;
        n=name;
        a=amount;
        no=note;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialog_layout_2,null);

        builder.setView(view).setMessage("Edit Expense Information")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name   = textName.getText().toString();
                        String amount = textAmount.getText().toString();
                        String note   = textNote.getText().toString();
                        listener.getData(idid,name,amount,note);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        textName   = view.findViewById(R.id.dialog2Name);
        textAmount = view.findViewById(R.id.dialog2Amount);
        textNote   = view.findViewById(R.id.dialog2Note);
        textName.setText(n);
        textAmount.setText(a);
        textNote.setText(no);

        return builder.create();
    }

    public interface  DialogListener2{
        void getData(String id,String name,String amount,String note);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogHelper2.DialogListener2) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"Must include Dialog Listner");
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
