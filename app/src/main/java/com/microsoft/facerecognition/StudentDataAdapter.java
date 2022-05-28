package com.microsoft.facerecognition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class StudentDataAdapter extends RecyclerView.Adapter<StudentDataAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList = new ArrayList<>();



    // Create Constructor
    public StudentDataAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studentdata,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  String st = stringList.get(position);

        holder.tvStudName.setText(st);

    }


    // this method will return the count of our list.
    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudName = itemView.findViewById(R.id.tv_name);

        }
    }
}