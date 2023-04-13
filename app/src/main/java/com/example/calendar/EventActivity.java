package com.example.calendar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    //Khởi tạo cái view
    FloatingActionButton backE, saveE;
    EditText timeStart;
    EditText dateStart;
    EditText dateEnd;
    EditText timeEnd;
    TextInputEditText titleEvent;


    //Biến ngày tháng năm
    int dayEvent, monthEvent, yearEvent;
    //Khai báo database
    private DBHelper mydb ;
    //Biến để update
    int id_To_Update = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //
        mydb = new DBHelper(this);
        //Lấy dữ liệu từ MainActivity
        Bundle extras = getIntent().getExtras();
        dayEvent = extras.getInt("Dayofevent");
        monthEvent = extras.getInt("Monthofevent");
        yearEvent = extras.getInt("Yearofevent");

        //
        saveE = findViewById(R.id.saveE);
        backE = (FloatingActionButton) findViewById(R.id.backE);
        dateStart = (EditText)findViewById(R.id.dateStart);
        timeStart = (EditText)findViewById(R.id.timeStart);
        dateEnd = (EditText)findViewById(R.id.dateEnd);
        timeEnd = (EditText)findViewById(R.id.timeEnd);
        titleEvent = (TextInputEditText) findViewById(R.id.topicEvent);

        //Set ngày tháng năm giờ cho các EditText
        setDefaultTime();
        //Sự kiện chọn ngày bắt đầu
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateStart();

            }
        });
        //Sự kiện chọn giờ bắt đầu
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeStart();
            }
        });
        //Sự kiện chọn ngày kết thúc
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateEnd();
            }
        });
        //Sự kiện chọn giờ kết thúc
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeEnd();
            }
        });
        //

        saveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent(v);
            }
        });
        //Quay lại MainActivity
        backE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //Hàm tạo text cho EditText Ngày bắt đầu
    private void setDateStart() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy");
                dateStart.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    //Hàm tạo text cho EditText Ngày kết thúc
    private void setDateEnd() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy");
                dateEnd.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    //Hàm tạo text cho EditText Giờ bắt đầu
    private  void setTimeStart() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0,0 , hourOfDay, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                timeStart.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minutes, true);
        timePickerDialog.show();
    }

    private void setDefaultTime() {
        Calendar calendar = Calendar.getInstance();
        int year = yearEvent;
        int month = monthEvent;
        int day = dayEvent;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy");
        dateStart.setText(simpleDateFormat.format(calendar.getTime()));
        dateEnd.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.set(0,0,0 , hour, minutes);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        timeStart.setText(simpleTimeFormat.format(calendar.getTime()));
        calendar.set(0,0,0 , hour, minutes + 10);
        timeEnd.setText(simpleTimeFormat.format(calendar.getTime()));

    }
    //Hàm tạo text cho EditText Giờ kết thúc
    private  void setTimeEnd() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0,0 , hourOfDay, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                timeEnd.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minutes, true);
        timePickerDialog.show();



    }

    //Hàm để lưu sự kiện vào SQLite
    public void saveEvent(View view) {
        Bundle extras = getIntent().getExtras();
        // Khai báo đối tượng SimpleDateFormat
        SimpleDateFormat inputFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String editTextDateStart = dateStart.getText().toString();
        String editTextDateEnd = dateEnd.getText().toString();
        try {
            // Chuyển đổi chuỗi ngày sang kiểu Date
            Date dateStart = inputFormat.parse(editTextDateStart);
            Date dateEnd = inputFormat.parse(editTextDateEnd);
            // Tạo đối tượng Calendar và thiết lập ngày từ kiểu Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateStart);

            // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
            editTextDateStart = outputFormat.format(calendar.getTime());


            calendar.setTime(dateEnd);

            // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
            editTextDateEnd= outputFormat.format(calendar.getTime());

            // In ra kết quả
            System.out.println(editTextDateStart); // Output: 2022-03-28
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(extras != null) {
                if(mydb.insertEvent(titleEvent.getText().toString(), editTextDateStart, timeStart.getText().toString(), editTextDateEnd, timeEnd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat inputFormat2 = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault());
                    try {
                        //Date dateStart = inputFormat2.parse(editTextDateStart);
                        Date timeStartValue = simpleDateFormat.parse(timeStart.getText().toString());



                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(dateStart);

                        calendar.set(Calendar.HOUR,timeStartValue.getHours());
                        calendar.set(Calendar.MINUTE,timeStartValue.getMinutes());
                        calendar.set(Calendar.SECOND,0);
                        System.out.println(calendar);
                        System.out.println(timeStartValue.getHours());
                        System.out.println(timeStartValue.getMinutes());
                        long alarmStartTime = calendar.getTimeInMillis();
                        System.out.println("giờ báo thức trang even: "+calendar.getTimeInMillis());
                        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);

                        Intent intent= new Intent(this, Alarm.class);
                        intent.setAction("MyAction");
                        intent.putExtra("mess", titleEvent.getText().toString());
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmStartTime,pendingIntent);
                        Toast.makeText(this,"Your Alarm is Set",Toast.LENGTH_LONG).show();


                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.out.println("CÁI NÀY Ở TRONG CATCH");

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            setResult(RESULT_OK, intent);
            finish();

        }
    }
    @Override
    public void finish() {
        super.finish();
    }


}
