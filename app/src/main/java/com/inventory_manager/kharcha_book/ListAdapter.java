package com.inventory_manager.kharcha_book;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private ListViewHelper[] listViewHelpers;
    String path;
    ShowImage showImage;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_custom_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String p="none";
        try {
            holder.id.setText(listViewHelpers[position].getId());
            holder.name.setText((position+1)+". "+listViewHelpers[position].getName());
            holder.amount.setText(listViewHelpers[position].getAmount());
            holder.date.setText(listViewHelpers[position].getDate());
            holder.note.setText(listViewHelpers[position].getNote());

            if(listViewHelpers[position].getImgPath()=="none"){
                //There is nothing to do here. cause default image is already loaded...
                p="none";

            }
            else{
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path = root+"/Kharcha_book/images/"+listViewHelpers[position].getImgPath();
                File image = new File(path);
                if(image.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                    int height = 90;
                    int width  = 90;
                    bitmap=Bitmap.createScaledBitmap(bitmap, width,height, true);
                    holder.img.setImageBitmap(bitmap);
                    p=image.getAbsolutePath();

                }
                else{}

                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = ListActivity.context;
                        try {
                            View v1 = (View) v.getParent();
                            TextView txt = v1.findViewById(R.id.row_id);
                            TextView txt1 = v1.findViewById(R.id.row_name);
                            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String path = root+"/Kharcha_book/images/"+ txt.getText().toString()+".png";
                            Helper.setPath(path);
                            Helper.setName(txt1.getText().toString().substring(txt1.getText().toString().indexOf(" ")+1));
                            Intent intent = new Intent(context, PopupImage.class);
                            context.startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(context, "Error : "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (NullPointerException ne){
            System.out.println("Error" + ne);
            Log.e("ListAdapter",ne+"");
        }
        catch (Exception e){
            Log.e("ListAdapter",e+"");
        }
    }
    public ListAdapter(ListViewHelper[] listViewHelper){
        System.out.println(listViewHelper.length);
        this.listViewHelpers = listViewHelper;
    }
    @Override
    public int getItemCount() {
        return this.listViewHelpers.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,amount,date,note,id;
        private ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_name);
            amount = (TextView) itemView.findViewById(R.id.row_amount);
            date = (TextView) itemView.findViewById(R.id.row_date);
            note = (TextView) itemView.findViewById(R.id.row_note);
            id=(TextView)itemView.findViewById(R.id.row_id);
            img=(ImageView)itemView.findViewById(R.id.imageButton);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    interface ShowImage{
        public void imageData(String path);
    }
}