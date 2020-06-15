package com.inventory_manager.kharcha_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static java.lang.Integer.parseInt;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DialogHelper.DialogListener ,DialogHelper2.DialogListener2,DialogHelper3.DialogListener3 {
    private String memorable = "";
    private String customStDate="12/12/2012";
    private String customedDate="12/12/2012";
    private TextView txt;
    private int listRowPosition = 0;
    private int listRowOption = 0;
    ListViewHelper listViewHelper[];
    private GetCountOfEverything getCountOfEverything=new GetCountOfEverything();
    private RecyclerView lv;
    private RecyclerView.Adapter adapter;
    public static Context context;

    // 0 for update and 1 for delete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        txt = findViewById(R.id.row_amount);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this,
                R.array.search_spinner_data, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        lv = (RecyclerView) findViewById(R.id.listView);
        listViewHelper = new ListViewHelper[3];
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeListCallback());
        itemTouchHelper.attachToRecyclerView(lv);
        context = this;
    }



    class SwipeListCallback extends ItemTouchHelper.SimpleCallback {

        private boolean hovered=false;

        public SwipeListCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            return super.convertToAbsoluteDirection(flags, layoutDirection);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            try {
                //  Paint paint=new Paint();
                //Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    hovered=true;
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 5;

                    //Update Button
                    Paint paint1=new Paint();
                    Bitmap icon1;
                    viewHolder.itemView.setTranslationX(dX / 5);
                    paint1.setColor(getResources().getColor(R.color.colorDeleteBackground));
                    RectF background = new RectF((float) itemView.getLeft() + dX / 5,
                            (float) itemView.getTop(),
                            (float) itemView.getLeft(),
                            (float) itemView.getBottom()-20);
                    c.drawRect(background, paint1);
                    paint1.setColor(getResources().getColor(R.color.coloeTextWhite));
                    paint1.setTextSize(65);
                    c.drawText("Edit",dX/18,itemView.getTop()+100,paint1);

                    Paint paint2 = new Paint();
                    Bitmap icon2;
                    paint2.setColor(getResources().getColor(R.color.colorEditBackground));
                    RectF background1 = new RectF((float) itemView.getRight() + dX / 5,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom()-20);

                    c.drawRect(background1, paint2);
                    paint2.setColor(getResources().getColor(R.color.coloeTextWhite));
                    paint2.setTextSize(65);
                    c.drawText("Delete",(float) itemView.getRight()+dX/new Float(5.5),itemView.getTop()+100,paint2);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX/7, dY, actionState, isCurrentlyActive);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            View view  = viewHolder.itemView;
            TextView txtId = (TextView)view.findViewById(R.id.row_id);
            TextView txtName = (TextView)view.findViewById(R.id.row_name);
            TextView txtAmount = (TextView)view.findViewById(R.id.row_amount);
            TextView txtNote = (TextView)view.findViewById(R.id.row_note);
            TextView txtDate = (TextView)view.findViewById(R.id.row_date);

            String name = txtName.getText().toString().substring(txtName.getText().toString().indexOf(" ")+1,txtName.getText().toString().length());
            String id = txtId.getText().toString();
            String amount = txtAmount.getText().toString().replace(" Rs.","");
            String note = txtNote.getText().toString();
            String date = txtDate.getText().toString();

            if(direction == ItemTouchHelper.LEFT && hovered==true){
                //delete
                DialogHelper3 dialogHelper3 = new DialogHelper3(id,name,amount,date,note);
                dialogHelper3.show(getSupportFragmentManager(),null);
                hovered=false;
            }
            else if(direction == ItemTouchHelper.RIGHT && hovered==true){
                //update
                DialogHelper2 dialogHelper2 = new DialogHelper2(id,name,amount,note);
                dialogHelper2.show(getSupportFragmentManager(),null);
                hovered=false;
            }

            if(memorable=="week"){
                this_week();
            }
            else if(memorable=="month"){
                this_month();
            }
            else if(memorable=="year"){
                this_year();
            }
            else if(memorable=="custom"){
                getDates(customStDate,customedDate);
            }
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();

        if (selected.equals("This Week")) {
            memorable = "week";
            this_week();
        } else if (selected.equals("This Month")) {
            memorable = "month";
            this_month();
        } else if (selected.equals("This Year")) {
            memorable = "year";
            this_year();
        } else {
            memorable="custom";
            customSearch();
        }
    }

    public void customSearch() {
        //open dialog
        DialogHelper dh = new DialogHelper();
        dh.show(getSupportFragmentManager(), "Custom Search");
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void this_month() {
        getThisMonthList();
    }

    public void this_week() {
        getThisWeekList();
    }

    public void this_year() {
        getThisYearList();
    }

    private void getThisWeekList() {
        int totalExpense = 0;
        Toast.makeText(this, "Showing expenses of this week", Toast.LENGTH_SHORT).show();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String curdate = df.format(d);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        ListViewHelper[] list = new ListViewHelper[getCountOfEverything.getCountWeek()];

        try {
            date = df.parse(curdate);
        } catch (ParseException e) {
            Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
        }
        cal.setTime(date);
        int weekorg = cal.get(Calendar.WEEK_OF_YEAR);
        int yearorg = cal.get(Calendar.YEAR);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = Queries.listData();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext()) {

            String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                date = df.parse(chkcurdate);
            } catch (ParseException e) {
                Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
            }
            cal.setTime(date);
            int weekchk = cal.get(Calendar.WEEK_OF_YEAR);
            int yearchk = cal.get(Calendar.YEAR);
            if (weekchk == weekorg && yearorg == yearchk) {
                /*list[i] = new ListViewHelper(cursor.getString(cursor.getColumnIndex("id"))+". " cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("amount")) + " Rs.",
                        cursor.getString(cursor.getColumnIndex("date")) + "  " + cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("note"))
                );*/
                try {
                    list[i] = new ListViewHelper(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("amount")) + " Rs.",
                            cursor.getString(cursor.getColumnIndex("date")) + "  " + cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("note")), cursor.getString(cursor.getColumnIndex("path"))
                    );
                }
                catch (ArrayIndexOutOfBoundsException ae){

                }

                i++;
            }
        }
        listViewHelper =list;
        adapter = new ListAdapter(listViewHelper);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
    }

    private void getThisMonthList() {

        int totalExpense = 0;
        Toast.makeText(this, "Showing expenses of this month", Toast.LENGTH_SHORT).show();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String curdate = df.format(d);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        ListViewHelper[] list = new ListViewHelper[getCountOfEverything.getCountMonth()];

        try {
            date = df.parse(curdate);
        } catch (ParseException e) {
            Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
        }
        cal.setTime(date);
        int weekorg = cal.get(Calendar.MONTH);
        int yearorg = cal.get(Calendar.YEAR);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = Queries.listData();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext()) {

            String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                date = df.parse(chkcurdate);
            } catch (ParseException e) {
                Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
            }
            cal.setTime(date);
            int weekchk = cal.get(Calendar.MONTH);
            int yearchk = cal.get(Calendar.YEAR);
            if (weekchk == weekorg && yearorg == yearchk) {
                try {
                    list[i] = new ListViewHelper(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("amount")) + " Rs.",
                            cursor.getString(cursor.getColumnIndex("date")) + "  " + cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("note")), cursor.getString(cursor.getColumnIndex("path"))
                    );
                }catch (ArrayIndexOutOfBoundsException ae){

                }
                i++;
            }
        }
        listViewHelper =list;
        adapter = new ListAdapter(listViewHelper);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
    }

    private void getThisYearList() {
        int totalExpense = 0;
        Toast.makeText(this, "Showing expenses of this year ", Toast.LENGTH_SHORT).show();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String curdate = df.format(d);
        Calendar cal = Calendar.getInstance();
        Date date = null;
        ListViewHelper[] list = new ListViewHelper[getCountOfEverything.getCountYear()];

        try {
            date = df.parse(curdate);
        } catch (ParseException e) {
            Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
        }
        cal.setTime(date);
        int yearorg = cal.get(Calendar.YEAR);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = Queries.listData();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext()) {

            String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                date = df.parse(chkcurdate);
            } catch (ParseException e) {
                Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
            }
            cal.setTime(date);
            int yearchk = cal.get(Calendar.YEAR);
            if (yearorg == yearchk) {
                try {
                    list[i] = new ListViewHelper(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("amount")) + " Rs.",
                            cursor.getString(cursor.getColumnIndex("date")) + "  " + cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("note")), cursor.getString(cursor.getColumnIndex("path"))
                    );
                }catch (ArrayIndexOutOfBoundsException ae){

                }
                i++;
            }
        }
        listViewHelper =list;
        adapter = new ListAdapter(listViewHelper);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
    }

    @Override
    public void getDates(String st, String ed) {
        int totalExpense = 0;
        customStDate=st;
        customedDate=ed;
        Toast.makeText(this, "Showing expenses as per custom search", Toast.LENGTH_SHORT).show();
        ListViewHelper list[] = new ListViewHelper[getCountOfEverything.getCountCustom(st,ed)];

        Date d1 = null, d2 = null;
        Date chdatech = null;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            d1 = df.parse(st);
            d2 = df.parse(ed);
        } catch (ParseException pe) {
            Toast.makeText(this, "Error : " + pe, Toast.LENGTH_SHORT).show();
        }

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = Queries.listData();
        Cursor cursor = db.rawQuery(query, null);
        int i=0;
        while (cursor.moveToNext()) {

            String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                chdatech = df.parse(chkcurdate);
            } catch (ParseException e) {
                Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
            }

            if (chdatech.after(d1) && chdatech.before(d2) || chdatech.equals(d1) || chdatech.equals(d2)) {
                try {
                    list[i] = new ListViewHelper(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("amount")) + " Rs.",
                            cursor.getString(cursor.getColumnIndex("date")) + "  " + cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("note")), cursor.getString(cursor.getColumnIndex("path"))
                    );
                }catch (ArrayIndexOutOfBoundsException ae){

                }
                i++;
            }
        }

        listViewHelper =list;
        adapter = new ListAdapter(listViewHelper);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
    }

    public void load() {
        if (memorable.equals("month")) {
            getThisMonthList();
        } else if (memorable.equals("week")) {
            getThisWeekList();
        } else if (memorable.equals("year")) {
            getThisYearList();
        } else if (memorable.equals("custom")) {
            getThisWeekList();
        }
    }

    @Override
    public void getData(String id, String name, String amount, String note) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("update expenses set name='" + name + "',amount='" + amount + "',note='" + note + "' where id='" + id + "'");
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            load();
        } catch (SQLiteException se) {
            Toast.makeText(this, "Error : " + se, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void successMessage(String id, String msg) {
        if (msg.equals("success")) {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.execSQL("delete from expenses where id='" + id + "'");
                Toast.makeText(this, "Succeessfully Deleted", Toast.LENGTH_SHORT).show();
                load();
            } catch (SQLiteException se) {
                Toast.makeText(this, "Error : " + se, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetCountOfEverything {
        String query;
        Cursor cursor;
        Calendar cal;
        Date d;
        Date date;
        SimpleDateFormat df;
        int     orgValWeek,
                chkValWeek,
                orgValYear,
                chkValYear,
                orgValMonth,
                chkValMonth;

        public int getCountWeek() {
            cal = Calendar.getInstance();
            d = Calendar.getInstance().getTime();
            date = null;
            DBHelper dbHelper = new DBHelper(ListActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            df = new SimpleDateFormat("dd/MM/yyyy");
            String curdate = df.format(d);

            int count = 0;
            query = Queries.getWeekCount();
            try {
                date = df.parse(curdate);
            } catch (ParseException e) {
            }
            cal.setTime(date);
            orgValWeek = cal.get(Calendar.WEEK_OF_YEAR);
            orgValYear = cal.get(Calendar.YEAR);
            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {

                String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                try {
                    date = df.parse(chkcurdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                chkValWeek = cal.get(Calendar.WEEK_OF_YEAR);
                chkValYear = cal.get(Calendar.YEAR);
                if (orgValWeek == chkValWeek && orgValYear == chkValYear) {
                    count++;
                }
            }
            return count;
        }


        public int getCountMonth() {
            cal = Calendar.getInstance();
            d = Calendar.getInstance().getTime();
            date = null;
            DBHelper dbHelper = new DBHelper(ListActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            df = new SimpleDateFormat("dd/MM/yyyy");
            String curdate = df.format(d);

            int count=0;
            query = Queries.getWeekCount();

            try {
                date = df.parse(curdate);
            } catch (ParseException e) {
            }
            cal.setTime(date);
            orgValMonth = cal.get(Calendar.MONTH);
            orgValYear = cal.get(Calendar.YEAR);
            cursor = db.rawQuery(query, null);

            int i = 0;
            while (cursor.moveToNext()) {

                String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                try {
                    date = df.parse(chkcurdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                chkValMonth = cal.get(Calendar.MONTH);
                chkValYear = cal.get(Calendar.YEAR);

                if (orgValMonth == chkValMonth && orgValYear == chkValYear) {
                    count++;
                }

            }
            return count;
        }

        public int getCountYear() {
            cal = Calendar.getInstance();
            d = Calendar.getInstance().getTime();
            date = null;
            DBHelper dbHelper = new DBHelper(ListActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            df = new SimpleDateFormat("dd/MM/yyyy");
            String curdate = df.format(d);

            int count=0;
            query = Queries.getWeekCount();
            try {
                date = df.parse(curdate);
            } catch (ParseException e) {
            }
            cal.setTime(date);
            orgValYear = cal.get(Calendar.YEAR);
            cursor = db.rawQuery(query, null);
            int i = 0;
            while (cursor.moveToNext()) {
                String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                try {
                    date = df.parse(chkcurdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                chkValYear = cal.get(Calendar.YEAR);
                if (orgValYear == chkValYear) {
                    count++;
                }
            }
            return count;
        }

        public int getCountCustom(String start,String end){
            int count=0;
            Date d1 = null, d2 = null;
            Date chdatech = null;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                d1 = df.parse(start);
                d2 = df.parse(end);
            } catch (ParseException pe) {

            }

            DBHelper dbHelper = new DBHelper(ListActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String query = "select id,date from expenses";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {

                String chkcurdate = cursor.getString(cursor.getColumnIndex("date"));
                try {
                    chdatech = df.parse(chkcurdate);
                } catch (ParseException e) {}

                if (chdatech.after(d1) && chdatech.before(d2) || chdatech.equals(d1) || chdatech.equals(d2)) {
                    count++;
                }
            }

            return count;
        }
    }
}
