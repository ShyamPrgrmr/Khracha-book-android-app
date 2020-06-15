package com.inventory_manager.kharcha_book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class Helper {
    private static androidx.fragment.app.FragmentManager fragmentManager;
    private static String path=" ";
    private static String name="Image";
    public static SQLiteDatabase getConnection(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db;
    }

    public static int getCount(Context context){
        int count=0;
        SQLiteDatabase db = getConnection(context);
        Cursor cur = db.rawQuery("select count from countreturn",null);
        while(cur.moveToNext()){
            count = parseInt(cur.getString(cur.getColumnIndex("count")));
        }
        return count;
    }

    public static void incrementCount(Context context){
        SQLiteDatabase db = getConnection(context);
        try{
            db.execSQL("update countreturn set count=(select count + 1 from countreturn)");
        }catch (SQLiteException se){
            Toast.makeText(context, ""+se, Toast.LENGTH_SHORT).show();
        }
    }

    public static int copyImage(Context context,File src, File dest,String id){
        try {
            FileInputStream fi = new FileInputStream(src);
            FileOutputStream fo= new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int byteRead;
            while((byteRead=fi.read(buf))>0){
                fo.write(buf,0,byteRead);
            }
            fi.close();
            fo.close();
            return 1;
        }catch (FileNotFoundException fe){
            SQLiteDatabase db = Helper.getConnection(context);
            db.execSQL("insert into image values('"+id+"','none')");
            return 0;
        }catch (IOException ie){
            Toast.makeText(context, ""+ie, Toast.LENGTH_SHORT).show();
            return 0;
        }finally{
        }
    }
    public void setFragmentManager(androidx.fragment.app.FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }
    public androidx.fragment.app.FragmentManager getFrag(){
        return this.fragmentManager;
    }
    public static String getPath() {
        return path;
    }
    public static void setPath(String path) {
        Helper.path = path;
    }
    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        Helper.name = name;
    }
}
