package com.inventory_manager.kharcha_book;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 121;
    private static final int CAMERA_REQUEST = 100;
    private EditText name,date,time,amount,note;
    private TextView notifyText;
    private Date d;
    private TextView txtviewmonth,txtviewyear,txtviewweek;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        amount = findViewById(R.id.inputAmount);
        date   = findViewById(R.id.inputDate);
        time   = findViewById(R.id.inputTime);
        name   = findViewById(R.id.inputName);
        note   = findViewById(R.id.inputNote);
        notifyText = findViewById(R.id.Imagenotifier);

        d=Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
        date.setText(df.format(d));
        time.setText(df1.format(d));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                startActivity(intent);
            }
        });


        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
            } else {
                requestPermission();

            }
        }
        else
        {
        }
        loaddata();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (result == PackageManager.PERMISSION_GRANTED ) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write permission.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loaddata(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int total_am_week=0,total_am_year=0,total_am_month=0;
        Date d=Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String curdate = df.format(d);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = df.parse(curdate);
        } catch (ParseException e) {
            Toast.makeText(this,"Error : "+e, Toast.LENGTH_SHORT).show();
        }
        cal.setTime(date);
        int weekorg = cal.get(Calendar.WEEK_OF_YEAR);
        int yearorg = cal.get(Calendar.YEAR);
        int monorg = cal.get(Calendar.MONTH);

        String query = Queries.listData();
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){

            String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                date = df.parse(chkcurdate);
            } catch (ParseException e) {
                Toast.makeText(this,"Error : "+e, Toast.LENGTH_SHORT).show();
            }
            cal.setTime(date);
            int weekchk = cal.get(Calendar.WEEK_OF_YEAR);
            int yearchk = cal.get(Calendar.YEAR);
            int monchk = cal.get(Calendar.MONTH);

            if(weekchk == weekorg && yearorg == yearchk) {
                try {
                    total_am_week = total_am_week + parseInt(cursor.getString(cursor.getColumnIndex("amount")));
                }
                catch (Exception pe){
                    Toast.makeText(this, "Error : "+pe, Toast.LENGTH_SHORT).show();
                }
            }
            if(monchk == monorg && yearorg == yearchk) {
                try {
                    total_am_month = total_am_month + parseInt(cursor.getString(cursor.getColumnIndex("amount")));
                }
                catch (Exception pe){
                    Toast.makeText(this, "Error : "+pe, Toast.LENGTH_SHORT).show();
                }
            }
            if(yearorg == yearchk) {
                try {
                    total_am_year = total_am_year + parseInt(cursor.getString(cursor.getColumnIndex("amount")));
                }
                catch (Exception pe){
                    Toast.makeText(this, "Error : "+pe, Toast.LENGTH_SHORT).show();
                }
            }

        }

        /*future edit
        //txtviewweek.setText(getString(R.string.label_this_week)+" "+total_am_week+" rs.");
        //txtviewmonth.setText(getString(R.string.lable_this_month)+" "+total_am_month+" rs.");
        //txtviewyear.setText(getString(R.string.label_this_year)+" "+total_am_year+" rs.");
        //check_expenses(total_am_week,total_am_month);
        */

    }

    @Override
    public void onResume(){
        super.onResume();
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
        time.setText(df1.format(d));
        loaddata();
    }

    public int getCountData() {
        int co = 0;
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = Queries.countData();
        Cursor res = db.rawQuery(sql, null);
        if (res.moveToFirst()) {
            do {
                co = parseInt(res.getString(res.getColumnIndex("co")));
                co++;
            }
            while (res.moveToNext());
        }
        return co;
    }


    public void saveData(View view){
        String na_me = name.getText().toString();
        String da_te = date.getText().toString();
        String am_nt = amount.getText().toString();
        String ti_me = time.getText().toString();
        String no_te = note.getText().toString();
        String id = new Integer(Helper.getCount(this)).toString();

        if(na_me.equals("") || da_te.equals("") || am_nt.equals("") || ti_me.equals("")){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            if(no_te==""){
                no_te=" ";
            }
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm aa");
            String t = df1.format(d);
            String sql = Queries.insert_data(Helper.getCount(MainActivity.this)+"",na_me,da_te,ti_me,no_te,am_nt);
            try{
                db.execSQL(sql);
                try {
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File image = new File(root + "/Kharcha_book/Temp/TempImage.png");

                    File exportDir = new File(Environment.getExternalStorageDirectory(), "/Kharcha_book/images/");
                    if (!exportDir.exists()) { exportDir.mkdirs();}
                    File imageFile = new File(exportDir,id+".png");
                    try{ imageFile.createNewFile();}
                    catch (IOException io){}
                    if(Helper.copyImage(this,image,imageFile,id)==1){
                        db.execSQL("insert into image values('"+id+"','"+id+".png"+"')");
                        if(image.delete()){ }
                        else{}
                    }
                    else{db.execSQL("insert into image values('"+id+"','none')");}
                }
                catch (Exception fe){
                    Toast.makeText(this, ""+fe, Toast.LENGTH_SHORT).show();
                }

                Snackbar.make(view, "Saved Succeessfully", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                clearData();
                Helper.incrementCount(MainActivity.this);

            }
            catch (SQLException se) {
                Toast.makeText(this, "Error "+ se, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(this, "Error "+ e, Toast.LENGTH_SHORT).show();
            }
            clearData();
            loaddata();
        }
    }

    public void clearData(){
        name.setText("");
        amount.setText("");
        note.setText("");
        notifyText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),setting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attachImage(View view){

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            try{
                saveTempImage(imageBitmap);
            }
            catch (Exception e){

            }
        }
    }

    private void saveTempImage(Bitmap finalBitmap) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/Kharcha_book/Temp/");
        if (!exportDir.exists()) { exportDir.mkdirs();}
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/Kharcha_book/Temp");
        myDir.mkdirs();
        String fname = "TempImage.png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
            notifyText.setText(getString(R.string.NotificationText));
            notifyText.setTextColor(Color.RED);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getTempImage(){

    }

    public void check_expenses(int amountmonth,int amountweek){
        String query = Queries.loadSettingData();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("name")).equals("month")){
                int am = parseInt(cursor.getString(cursor.getColumnIndex("lim")));
                if(am<amountmonth){
                    txtviewmonth.setBackgroundResource(R.color.colorWarning);
                    Toast.makeText(this, "You are spending more then your monthly expense.\n" +
                            "Please update your Monthly expense limit", Toast.LENGTH_SHORT).show();
                }
                else{
                    txtviewmonth.setBackgroundResource(R.color.colorTransperent);
                }
            }
            else if(cursor.getString(cursor.getColumnIndex("name")).equals("week")){
                int am = parseInt(cursor.getString(cursor.getColumnIndex("lim")));
                if(am<amountweek){
                    Toast.makeText(this, "You are spending more then your monthly expense.\n" +
                            "Please update your Monthly expense limit", Toast.LENGTH_SHORT).show();
                    txtviewweek.setBackgroundResource(R.color.colorWarning);
                }
                else{
                    txtviewweek.setBackgroundResource(R.color.colorTransperent);
                }
            }
        }
    }
}
