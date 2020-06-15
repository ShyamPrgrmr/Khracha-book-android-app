package com.inventory_manager.kharcha_book;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private String TAG = DBHelper.class.getSimpleName();
    private static final String Tag = DBHelper.class.getSimpleName();
    public DBHelper(Context context){
        super(context,"database.db",null,13);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists expenses('id' TEXT PRIMARY KEY,'name' TEXT,'date' TEXT,'time' TEXT,'amount' TEXT,'note' TEXT)");
        db.execSQL("create table if not exists expenseslimit('name' TEXT PRIMARY KEY,'lim' TEXT)");
        db.execSQL("create table if not exists countreturn('count' INTEGER)");
        db.execSQL("create table if not exists image(id TEXT,path TEXT,foreign key(id) references expenses(id) on delete cascade)");
        try {
            db.execSQL("insert into expenseslimit values('week','100000')");
            db.execSQL("insert into expenseslimit values('month','100000')");
            db.execSQL("insert into expenseslimit values('day','100000')");
            db.execSQL("insert into countreturn values(0)");
        }
        catch(SQLiteConstraintException se){
            Toast.makeText(context, ""+se, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists expenses");
        db.execSQL("drop table if exists expenseslimit");
        db.execSQL("drop table if exists countreturn");
        db.execSQL("drop table if exists image");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            try{
            db.execSQL("PRAGMA foreign_keys=ON;");
            }catch (SQLiteException se){
                Toast.makeText(context, ""+se, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
