package com.example.calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {
    EditText title_input, day_start_input, time_start_input, day_end_input, time_end_input;
    FloatingActionButton update_button, back_button;
    Button delete_button;

    String id, title, daystart, timestart, dayend, timeend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        title_input = findViewById(R.id.topicEvent2);
        day_start_input = findViewById(R.id.dateStart2);
        time_start_input = findViewById(R.id.timeStart2);
        day_end_input = findViewById(R.id.dateEnd2);
        time_end_input = findViewById(R.id.timeEnd2);
        update_button = findViewById(R.id.updateBtn);
        back_button = findViewById(R.id.backE2);
        delete_button = findViewById(R.id.delete_button);
        getIntentData();
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle(title);
        }
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper myDB = new DBHelper(UpdateActivity.this);
                title = title_input.getText().toString();
                daystart = day_start_input.getText().toString();
                timestart = time_start_input.getText().toString();
                dayend = day_end_input.getText().toString();
                timeend = time_end_input.getText().toString();

                //Chuyển định dạng ngày tháng
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat inputFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy", Locale.getDefault());

                try {
                    // Chuyển đổi chuỗi ngày sang kiểu Date
                    Date dateStart = inputFormat.parse(daystart);
                    Date dateEnd = inputFormat.parse(dayend);
                    // Tạo đối tượng Calendar và thiết lập ngày từ kiểu Date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateStart);

                    // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
                    daystart = outputFormat.format(calendar.getTime());


                    calendar.setTime(dateEnd);

                    // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
                    dayend= outputFormat.format(calendar.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myDB.updateEvent(id, title, daystart, timestart, dayend, timeend);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                setResult(RESULT_OK, intent);
//                startActivity(intent);
                finish();
            }
        });

        //xóa sự kiện
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        //Sự kiện chọn ngày bắt đầu
        day_start_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateStart();

            }
        });
        //Sự kiện chọn giờ bắt đầu
        time_start_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeStart();
            }
        });
        //Sự kiện chọn ngày kết thúc
        day_end_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateEnd();
            }
        });
        //Sự kiện chọn giờ kết thúc
        time_end_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeEnd();
            }
        });

        //Quay lại MainActivity
        back_button.setOnClickListener(new View.OnClickListener() {
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
                day_start_input.setText(simpleDateFormat.format(calendar.getTime()));
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
                day_end_input.setText(simpleDateFormat.format(calendar.getTime()));
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
                time_start_input.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minutes, true);
        timePickerDialog.show();
    }
    private  void setTimeEnd() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(0,0,0 , hourOfDay, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time_end_input.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minutes, true);
        timePickerDialog.show();
    }


    void getIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("datestart") && getIntent().hasExtra("timestart") && getIntent().hasExtra("dateend") && getIntent().hasExtra("timeend")  ) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            daystart = getIntent().getStringExtra("datestart");
            timestart = getIntent().getStringExtra("timestart");
            dayend = getIntent().getStringExtra("dateend");
            timeend = getIntent().getStringExtra("timeend");

            //Chuyển định dạng ngày tháng
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EE, dd 'thg' M, yyyy", Locale.getDefault());

            try {
                // Chuyển đổi chuỗi ngày sang kiểu Date
                Date dateStart = inputFormat.parse(daystart);
                Date dateEnd = inputFormat.parse(dayend);
                // Tạo đối tượng Calendar và thiết lập ngày từ kiểu Date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateStart);

                // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
                daystart = outputFormat.format(calendar.getTime());


                calendar.setTime(dateEnd);

                // Tạo chuỗi ngày mới theo định dạng yyyy-MM-dd
                dayend= outputFormat.format(calendar.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Set nội dung
            title_input.setText(title);
            day_start_input.setText(daystart);
            time_start_input.setText(timestart);
            day_end_input.setText(dayend);
            time_end_input.setText(timeend);
        } else {
        Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá " + title + " ?");
        builder.setTitle("Bạn có chắc muốn xóa " + title + " ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper myDB = new DBHelper(UpdateActivity.this);
                myDB.deleteEvent(id);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
