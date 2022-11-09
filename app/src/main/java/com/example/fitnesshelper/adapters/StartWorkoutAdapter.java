package com.example.fitnesshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class StartWorkoutAdapter extends RecyclerView.Adapter<StartWorkoutAdapter.MyViewHolder> {
    Context context;
    ArrayList<WorkoutTemplate> templateList;

    public StartWorkoutAdapter(Context context, ArrayList<WorkoutTemplate> templateList) {
        this.context = context;
        this.templateList = templateList;
    }

    @NonNull
    @Override
    public StartWorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.start_workout_row_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StartWorkoutAdapter.MyViewHolder holder, int position) {
        holder.start_workout_row_item_TitleTv.setText(templateList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView start_workout_row_item_TitleTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            start_workout_row_item_TitleTv = itemView.findViewById(R.id.start_workout_row_item_TitleTv);
        }
    }
}
