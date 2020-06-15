package com.inventory_manager.kharcha_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PopupImage extends AppCompatActivity {
    private ImageView image;
    private final static int requestCode=121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setTitle(Helper.getName());
        setContentView(R.layout.activity_popup_image);
        try {
            image = findViewById(R.id.popupimage);
            String path = Helper.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            int height = 230;
            int width = 230;
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            image.setImageBitmap(bitmap);
        }catch (Exception e){
            Context context = ListActivity.context;
            Toast.makeText(context, "Yo have not attached any image.", Toast.LENGTH_SHORT).show();
        }
    }
}
