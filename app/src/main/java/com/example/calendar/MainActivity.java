package com.example.calendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int daySelect;
    int monthSelect;
    int yearSelect;


    RecyclerView recyclerView;
    DBHelper mydb;

    String dateChoice;
    private ActivityResultLauncher<Intent> calendarLauncher;
    ArrayList<String> events_id, events_title, events_daystart, events_timestart, events_dayend, events_timeend;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AcvitityForResults
        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            recreate();
                        }
                    }
                });
        //Layouts
        FloatingActionButton addEvent = findViewById(R.id.addE);
        CalendarView calendarView = findViewById(R.id.calenderView);
        recyclerView = findViewById(R.id.recycleView);
        //Lấy ngày, tháng, năm
        Calendar calendar = Calendar.getInstance();
        yearSelect = calendar.get(Calendar.YEAR);
        monthSelect = calendar.get(Calendar.MONTH);
        daySelect = calendar.get(Calendar.DATE);
        calendar.set(yearSelect, monthSelect, daySelect);
        //sử dụng database
        mydb = new DBHelper(MainActivity.this);
        events_id = new ArrayList<>();
        events_title = new ArrayList<>();
        events_daystart = new ArrayList<>();
        events_timestart = new ArrayList<>();
        events_dayend = new ArrayList<>();
        events_timeend = new ArrayList<>();


        //Format định dạng thời gian
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateChoice = simpleDateFormat.format(calendar.getTime());
        //Lưu dữ liệu lấy được vào array
        storeDataInArrays(dateChoice);

        //Hiển thị dữ liệu vào RecycleView
        customAdapter = new CustomAdapter(MainActivity.this,this,events_id,events_title,events_daystart ,events_timestart,events_dayend ,events_timeend, someActivityResultLauncher);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(customAdapter != null) {
                    customAdapter.clearData();
                }
                daySelect = dayOfMonth;
                monthSelect = month;
                yearSelect = year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                dateChoice = simpleDateFormat.format(calendar.getTime());
                //Lưu dữ liệu vào Array
                clearArrays();
                storeDataInArrays(dateChoice);
                //Hiển thị dữ liệu vào RecycleView
                customAdapter = new CustomAdapter(MainActivity.this,MainActivity.this,events_id,events_title,events_daystart ,events_timestart,events_dayend ,events_timeend, someActivityResultLauncher);
//                System.out.println(events_id);
//                System.out.println(events_title);
//                System.out.println(events_daystart);
//                System.out.println(events_timestart);
//                System.out.println(events_dayend);
//                System.out.println(events_timeend);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                intent.putExtra("Dayofevent", daySelect);
                intent.putExtra("Monthofevent", monthSelect);
                intent.putExtra("Yearofevent", yearSelect);
                startActivity(intent);
            }


        });

    }

    void storeDataInArrays(String dateChoice) {
        Cursor cursor = mydb.readAllData(dateChoice);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                events_id.add(cursor.getString(0));
                events_title.add(cursor.getString(1));
                events_daystart.add(cursor.getString(2));
                events_timestart.add(cursor.getString(4));
                events_dayend.add(cursor.getString(3));
                events_timeend.add(cursor.getString(5));


            }
        }

    }
    void clearArrays() {
        events_id.clear();
        events_title.clear();
        events_daystart.clear();
        events_timestart.clear();
        events_dayend.clear();
        events_timeend.clear();
    }
}