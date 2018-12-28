package com.example.liyixun.TopGroup.Calendar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.liyixun.TopGroup.R;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.Viewholder>  {

    private List<Event> mTextList;
    static class Viewholder extends RecyclerView.ViewHolder{
        TextView textContent;
        TextView textdate;
        public Viewholder(View view){
            super(view);
            textContent=(TextView)view.findViewById(R.id.textOne);
            textdate=(TextView)view.findViewById(R.id.textTime);
        }
    }
    public TextAdapter(List<Event> a){
        mTextList=a;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.textpad_item,parent,false);
        Viewholder holder=new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-M-dd hh:mm:ss a", Locale.getDefault());
        String Eventcontent=mTextList.get(position).getData().toString();
        java.util.Calendar EventTime=java.util.Calendar.getInstance(Locale.getDefault());
        EventTime.setTimeInMillis(mTextList.get(position).getTimeInMillis());
        java.util.Date date=EventTime.getTime();
        holder.textContent.setText(Eventcontent);
        holder.textdate.setText(dateFormatForDisplaying.format(date));
    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }


}
