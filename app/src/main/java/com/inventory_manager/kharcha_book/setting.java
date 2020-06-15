package com.inventory_manager.kharcha_book;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class setting extends AppCompatActivity implements  DialogHelper.DialogListener{

    EditText inputWeek,inputMonth,inputDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        inputMonth = findViewById(R.id.settingMonthInput);
        inputDay = findViewById(R.id.settingDayInput);
        inputWeek =findViewById(R.id.settingWeekInput);
        getData();
    }

    public void getData(){
        String query = Queries.loadSettingData();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        try {
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name.equals("month")){
                    inputMonth.setText(cursor.getString(cursor.getColumnIndex("lim")));
                }
                else if(name.equals("day")){
                    inputDay.setText(cursor.getString(cursor.getColumnIndex("lim")));
                }
                else{
                    inputWeek.setText(cursor.getString(cursor.getColumnIndex("lim")));
                }
            }
        }
        catch(Exception e){
            Toast.makeText(this, "Error : "+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void save_btn_click(View view){
        String inpMonth = inputMonth.getText().toString();
        String inpDay = inputDay.getText().toString();
        String inpWeek = inputWeek.getText().toString();

        if(inpDay.equals("") || inpMonth.equals("") || inpWeek.equals("")){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else{
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.execSQL("update expenseslimit set 'lim'='" + inputMonth.getText().toString() + "' where name='month'");
                db.execSQL("update expenseslimit set 'lim'='" + inputWeek.getText().toString() + "' where name='week'");
                db.execSQL("update expenseslimit set 'lim'='" + inputDay.getText().toString() + "' where name='day'");
                Snackbar.make(view, "Saved Succeessfully", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                getData();
            }
            catch(SQLException se){
                Toast.makeText(this, "Error " +se, Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>{

        private int mode;
        private String stDate,edDate;
        private final ProgressDialog dialog = new ProgressDialog(setting.this);
        boolean memoryErr = false;

        ExportDatabaseCSVTask(int m){
            if(m==0)mode=0;
          }

        ExportDatabaseCSVTask(String st,String ed){
            mode=1;
            stDate=st;
            edDate=ed;
          }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {

            File exportDir = new File(Environment.getExternalStorageDirectory(), "/Kharcha_book/");
            if (!exportDir.exists()) { exportDir.mkdirs(); }
            DBHelper dbHelper = new DBHelper(setting.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Date d= Calendar.getInstance().getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
            String curdate1 = df1.format(d);


            SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
            String curdate = df.format(d);



            try{
                //name,date,amount,time,note

                Cursor cursor = db.rawQuery(Queries.reportQuery(),null);
                Calendar cal = Calendar.getInstance();
                Date date = null;
                try {
                    date = df.parse(curdate);
                } catch (ParseException e) {
                    Toast.makeText(setting.this,"Error : "+e, Toast.LENGTH_SHORT).show();
                }
                cal.setTime(date);
                int weekorg = cal.get(Calendar.MONTH);
                int yearorg = cal.get(Calendar.YEAR);


                if(mode==0){
                    //if month
                    File file = new File(exportDir, "khrcha"+curdate1+".csv");
                    try{
                        file.createNewFile();
                    }
                    catch (IOException io){
                        return false;
                    }

                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                    String columnName[] = new String[] {"NAME","DATE","AMOUNT","TIME","NOTE"};
                    csvWrite.writeNext(columnName);

                    while (cursor.moveToNext()){
                        String arrStr[]=null;
                        String[] mySecondStringArray = new String[cursor.getColumnNames().length];

                        String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                        try {
                            date = df.parse(chkcurdate);
                        } catch (ParseException e) {
                            Toast.makeText(setting.this,"Error : "+e, Toast.LENGTH_SHORT).show();
                        }
                        cal.setTime(date);
                        int weekchk = cal.get(Calendar.MONTH);
                        int yearchk = cal.get(Calendar.YEAR);
                        if(weekchk == weekorg && yearorg == yearchk) {
                            mySecondStringArray[0] = cursor.getString(cursor.getColumnIndex("name"));
                            mySecondStringArray[1] = cursor.getString(cursor.getColumnIndex("date"));
                            mySecondStringArray[2] = cursor.getString(cursor.getColumnIndex("amount"));
                            mySecondStringArray[3] = cursor.getString(cursor.getColumnIndex("time"));
                            mySecondStringArray[4] = cursor.getString(cursor.getColumnIndex("note"));
                            csvWrite.writeNext(mySecondStringArray);
                        }
                    }

                    csvWrite.close();
                    cursor.close();
                    return true;
                }
                else if(mode==1) {
                    //if custom

                    String filedate1,filedate2;
                    filedate1=stDate;
                    filedate2=edDate;

                    filedate1=filedate1.replaceAll("/","-");
                    filedate2=filedate2.replaceAll("/","-");
                    File file = new File(exportDir, "khrcha"+filedate1+"to"+filedate2+".csv");

                    try{
                        file.createNewFile();
                    }
                    catch (IOException io){
                        return false;
                    }

                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


                    String message[]=new String[] {"Report From ",stDate," to ",edDate,""};
                    csvWrite.writeNext(message);

                    String columnName[] = new String[] {"NAME","DATE","AMOUNT","TIME","NOTE"};
                    csvWrite.writeNext(columnName);
                    Date d1=null,d2=null;
                    Date chdatech=null;
                    try{
                        d1 = df.parse(stDate);
                        d2 = df.parse(edDate);
                    }
                    catch (ParseException pe){
                    }

                    while (cursor.moveToNext()){
                        String arrStr[]=null;
                        String[] mySecondStringArray = new String[cursor.getColumnNames().length];
                        String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                        try {
                            chdatech = df.parse(chkcurdate);
                        } catch (ParseException e) {

                        }

                        if(chdatech.after(d1) && chdatech.before(d2) || chdatech.equals(d1) || chdatech.equals(d2)) {
                            mySecondStringArray[0] = cursor.getString(cursor.getColumnIndex("name"));
                            mySecondStringArray[1] = cursor.getString(cursor.getColumnIndex("date"));
                            mySecondStringArray[2] = cursor.getString(cursor.getColumnIndex("amount"));
                            mySecondStringArray[3] = cursor.getString(cursor.getColumnIndex("time"));
                            mySecondStringArray[4] = cursor.getString(cursor.getColumnIndex("note"));
                            csvWrite.writeNext(mySecondStringArray);
                        }
                    }

                    csvWrite.close();
                    cursor.close();
                    return true;
                }


            }
            catch(SQLiteException se){

            }
            catch (IOException io){
             return false;
            }
            return true;
        }
        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(setting.this, "File exported successfully in kharchbook folder in sdcard!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(setting.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void downloadReportMonth(View view){
        try {
            ExportDatabaseCSVTask task = new ExportDatabaseCSVTask(0);
            task.execute();
        }
        catch(Exception e){
            Toast.makeText(this, "Error : "+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadReportCustom(View view){
        try {
            DialogHelper dh = new DialogHelper();
            dh.show(getSupportFragmentManager(),"Custom Search");
        }
        catch(Exception e){
            Toast.makeText(this, "Error : "+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDates(String st, String ed) {
        try {
            ExportDatabaseCSVTask task = new ExportDatabaseCSVTask(st, ed);
            task.execute();
        }
        catch (Exception e){
            Toast.makeText(this, "Error : "+e, Toast.LENGTH_SHORT).show();
        }
    }
}
