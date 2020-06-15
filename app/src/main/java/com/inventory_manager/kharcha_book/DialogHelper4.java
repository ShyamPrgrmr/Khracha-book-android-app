package com.inventory_manager.kharcha_book;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class DialogHelper4 extends DialogFragment {
    private String path;
    private ImageView imageView;
    public DialogHelper4(String bitmap){
        this.path=bitmap;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.image_dialog,null);
        builder.setView(view).setMessage("Custom Search")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        ImageView imageView = (ImageView)view.findViewById(R.id.DialogImage);
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = root+"/Kharcha_book/images/"+this.path;
        File image = new File(path);
        if(image.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
        else{}
        return builder.create();
   }
}
