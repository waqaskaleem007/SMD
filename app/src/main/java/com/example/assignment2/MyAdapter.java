package com.example.assignment2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    ArrayList<Student> mDisplayedValues;    // Values to be displayed
    private onStudentListener monStudentListener;
    DataBaseHelper myDb;


    MyAdapter(Context context, ArrayList<Student> students, onStudentListener monStudentListener) {
        this.context = context;
        this.mDisplayedValues = students;
        this.monStudentListener = monStudentListener;
        myDb = new DataBaseHelper(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.student_layout,parent,false);
        return new MyViewHolder(view,monStudentListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.textView.setText(mDisplayedValues.get(position).getName());
        holder.textRoll.setText(mDisplayedValues.get(position).getRollno());
        holder.checkBox.setChecked(mDisplayedValues.get(position).isPresent());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDisplayedValues.get(position).isPresent()) {
                    mDisplayedValues.get(position).setPresent(false);
                    holder.checkBox.setChecked(false);
                    boolean isUpdated =  myDb.updateData(mDisplayedValues.get(position).getName(),mDisplayedValues.get(position).getRollno(),false);
                    if(isUpdated){
                        Toast.makeText(context,"updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"not updated",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    mDisplayedValues.get(position).setPresent(true);
                    holder.checkBox.setChecked(true);
                    boolean isUpdated =  myDb.updateData(mDisplayedValues.get(position).getName(),mDisplayedValues.get(position).getRollno(),true);
                    if(isUpdated){
                        Toast.makeText(context,"updated",Toast.LENGTH_LONG).show();
                    }
                }
                Intent intent = new Intent("INTENT_NAME").putExtra("selectAll","start");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mDisplayedValues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements onStudentListener, View.OnClickListener {
        TextView textView;
        CheckBox checkBox;
        onStudentListener onStudentListener;
        TextView textRoll;
        public MyViewHolder(@NonNull View itemView, onStudentListener onStudentListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.studentCheck);
            textRoll = itemView.findViewById(R.id.rollno);
            this.onStudentListener = onStudentListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onStudentClick(int position) {
            onStudentListener.onStudentClick(getAdapterPosition());
            mDisplayedValues.get(getAdapterPosition()).setPresent(true);

        }



        @Override
        public void onClick(View v) {
            if(checkBox.isChecked()) {
                mDisplayedValues.get(getAdapterPosition()).setPresent(false);
                checkBox.setChecked(false);
            }
            else {
                mDisplayedValues.get(getAdapterPosition()).setPresent(true);
                checkBox.setChecked(true);
            }
            Intent intent = new Intent("INTENT_NAME").putExtra("selectAll","start");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
    }


    public void filterList(ArrayList<Student> filteredList) {
        mDisplayedValues = filteredList;
        notifyDataSetChanged();
    }

    public interface onStudentListener{
        void onStudentClick(int position);
    }

}

