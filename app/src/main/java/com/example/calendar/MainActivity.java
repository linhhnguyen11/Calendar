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

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //AcvitityForResults
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    System.out.println("Result ok");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        System.out.println("BBBBBBBBBB");
                        Intent data = result.getData();
                        recreate();
                    }
                }
            });
    int daySelect;
    int monthSelect;
    int yearSelect;


    RecyclerView recyclerView;
    Button todayBtn;
    DBHelper mydb;

    String dateChoice;
    private ActivityResultLauncher<Intent> calendarLauncher;
    ArrayList<String> events_id, events_title, events_daystart, events_timestart, events_dayend, events_timeend, events_date_start, events_date_end, events_date;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Layouts
        CalendarView calendarView = findViewById(R.id.calendarView);
        FloatingActionButton addEvent = findViewById(R.id.addE);
        recyclerView = findViewById(R.id.recycleView);
        todayBtn = findViewById(R.id.todayBtn);
        //Lấy ngày, tháng, năm
        Calendar calendar = Calendar.getInstance();
        todayBtn.setText(String.valueOf(calendar.get(calendar.DAY_OF_MONTH)));
//        yearSelect = calendar.get(Calendar.YEAR);
//        monthSelect = calendar.get(Calendar.MONTH);
//        daySelect = calendar.get(Calendar.DATE);
//        calendar.set(yearSelect, monthSelect, daySelect);
        if ( savedInstanceState != null) {
            daySelect = savedInstanceState.getInt("Day");
            monthSelect = savedInstanceState.getInt("Month");
            yearSelect = savedInstanceState.getInt("Year");
            calendar.set(yearSelect, monthSelect, daySelect);
            try {
                calendarView.setDate(calendar);
                checkToday(calendar);
            } catch (OutOfDateRangeException e) {
                throw new RuntimeException(e);
            }
        } else {
            yearSelect = calendar.get(Calendar.YEAR);
            monthSelect = calendar.get(Calendar.MONTH);
            daySelect = calendar.get(Calendar.DATE);
            calendar.set(yearSelect, monthSelect, daySelect);
        }
        //sử dụng database
        mydb = new DBHelper(MainActivity.this);
        events_id = new ArrayList<>();
        events_title = new ArrayList<>();
        events_daystart = new ArrayList<>();
        events_timestart = new ArrayList<>();
        events_dayend = new ArrayList<>();
        events_timeend = new ArrayList<>();
        events_date_start = new ArrayList<>();
        events_date_end = new ArrayList<>();
        //Format định dạng thời gian
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateChoice = simpleDateFormat.format(calendar.getTime());
        //Lưu dữ liệu lấy được vào array
        storeDataInArrays(dateChoice);
        storeAllDateInArrays();
        //
        List<Date> dates = new ArrayList<>();
        for (int i=0; i<events_date_start.size(); i++) {
            String startDateString = events_date_start.get(i);
            String endDateString = events_date_end.get(i);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = format.parse(startDateString);
                endDate = format.parse(endDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(startDate);
            dates.add(startDate);

            while (newCalendar.getTime().before(endDate)) {
                newCalendar.add(Calendar.DATE, 1);
                dates.add(newCalendar.getTime());
            }

        }

        List<Calendar> calendars = new ArrayList<>();
        //Hiển thị ngày có sự kiện
        for (int i=0; i < dates.size(); i++) {
            Calendar sample = Calendar.getInstance();
            sample.setTime(dates.get(i));
            calendars.add(sample);
        }
        calendarView.setHighlightedDays(calendars);


//        //Hiển thị dữ liệu vào RecycleView
//        customAdapter = new CustomAdapter(MainActivity.this,this,events_id,events_title,events_daystart ,events_timestart,events_dayend ,events_timeend, someActivityResultLauncher);
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        renderRecycleView();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Calendar calendar = Calendar.getInstance();
                checkToday(clickedDayCalendar);
                if(customAdapter != null) {
                    customAdapter.clearData();
                }
                daySelect = clickedDayCalendar.get(clickedDayCalendar.DAY_OF_MONTH);
                monthSelect = clickedDayCalendar.get(clickedDayCalendar.MONTH) ;
                yearSelect = clickedDayCalendar.get(clickedDayCalendar.YEAR);

                calendar.set(yearSelect, monthSelect, daySelect);
                System.out.println(calendar);
                dateChoice = simpleDateFormat.format(calendar.getTime());
                //Lưu dữ liệu vào Array
                clearArrays();
                storeDataInArrays(dateChoice);
//                //Hiển thị dữ liệu vào RecycleView
//                customAdapter = new CustomAdapter(MainActivity.this,MainActivity.this,events_id,events_title,events_daystart ,events_timestart,events_dayend ,events_timeend, someActivityResultLauncher);
//                recyclerView.setAdapter(customAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                renderRecycleView();
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
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                yearSelect = calendar.get(Calendar.YEAR);
                monthSelect = calendar.get(Calendar.MONTH);
                daySelect = calendar.get(Calendar.DATE);
                dateChoice = simpleDateFormat.format(calendar.getTime());
                if(customAdapter != null) {
                    customAdapter.clearData();
                }
                //Lưu dữ liệu lấy được vào array
                clearArrays();
                storeDataInArrays(dateChoice);
                try {
                    calendarView.setDate(calendar);
                } catch (OutOfDateRangeException e) {
                    throw new RuntimeException(e);
                }
                renderRecycleView();
                todayBtn.setVisibility(View.INVISIBLE);
            }
        });

    }

    void storeDataInArrays(String dateChoice) {
        Cursor cursor = mydb.readAllData(dateChoice);
        if(cursor.getCount() == 0) {
            System.out.println("No Data");
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

    void storeAllDateInArrays() {
        Cursor cursor = mydb.readAllDate();
        while (cursor.moveToNext()) {
            events_date_start.add(cursor.getString(0));
            events_date_end.add(cursor.getString(1));
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

    void checkToday(Calendar a) {
        Calendar calendar = Calendar.getInstance();
        if (a.get(a.DAY_OF_YEAR) != calendar.get(calendar.DAY_OF_YEAR)) {
            todayBtn.setVisibility(View.VISIBLE);
        } else {
            todayBtn.setVisibility(View.INVISIBLE);
        }
    }

    void renderRecycleView() {
        //Hiển thị dữ liệu vào RecycleView
        customAdapter = new CustomAdapter(MainActivity.this,MainActivity.this,events_id,events_title,events_daystart ,events_timestart,events_dayend ,events_timeend, someActivityResultLauncher);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Day", daySelect);
        outState.putInt("Month", monthSelect);
        outState.putInt("Year", yearSelect);
    }

}