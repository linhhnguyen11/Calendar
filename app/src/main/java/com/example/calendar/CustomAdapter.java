package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    Activity activity;
    private ArrayList events_id, events_title, events_daystart, events_timestart, events_dayend, events_timeend;

    CustomAdapter(Activity activity,Context context,ArrayList events_id, ArrayList events_title,ArrayList events_daystart, ArrayList events_timestart,ArrayList events_dayend, ArrayList events_timeend,ActivityResultLauncher<Intent> someActivityResultLauncher ) {
        this.context = context;
        this.activity = activity;
        this.events_id = events_id;
        this.events_title = events_title;
        this.events_daystart = events_daystart;
        this.events_timestart = events_timestart;
        this.events_dayend = events_dayend;
        this.events_timeend = events_timeend;
        this.someActivityResultLauncher = someActivityResultLauncher;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.events_title_txt.setText(String.valueOf(events_title.get(position)));
        holder.events_timestart_txt.setText(String.valueOf(events_timestart.get(position)));
        holder.events_timeend_txt.setText(String.valueOf(events_timeend.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(events_id.get(position)));
                intent.putExtra("title", String.valueOf(events_title.get(position)));
                intent.putExtra("datestart", String.valueOf(events_daystart.get(position)));
                intent.putExtra("timestart", String.valueOf(events_timestart.get(position)));
                intent.putExtra("dateend", String.valueOf(events_dayend.get(position)));
                intent.putExtra("timeend", String.valueOf(events_timeend.get(position)));

                someActivityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events_title.size();
    }

    public void clearData() {
        events_title.clear();;
        events_timestart.clear();
        events_timeend.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView events_title_txt, events_timestart_txt, events_timeend_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            events_title_txt = itemView.findViewById(R.id.event_titile);
            events_timestart_txt = itemView.findViewById(R.id.events_timestart);
            events_timeend_txt = itemView.findViewById(R.id.events_timeend);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
