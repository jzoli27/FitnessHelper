package com.example.fitnesshelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Repetition;
import com.example.fitnesshelper.models.WorkoutDetails;

import java.util.ArrayList;

public class StartWorkoutMemberAdapter extends RecyclerView.Adapter<StartWorkoutMemberAdapter.MyViewHolder> {

    ArrayList<Repetition> reps;

    public StartWorkoutMemberAdapter(ArrayList<Repetition> reps) {
        this.reps = reps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.startworkout_repetition_item,parent,false);

        return new StartWorkoutMemberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.repetitionSerieEt.setText(reps.get(position).getSeries()+".");
        holder.repetitionWeightEt.setText(reps.get(position).getWeight()+" kg");
        holder.repetitionEt.setText(reps.get(position).getNumberOfRepetitions()+" db");
    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        EditText repetitionSerieEt, repetitionWeightEt, repetitionEt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            repetitionSerieEt = itemView.findViewById(R.id.repetitionSerieEt);
            repetitionWeightEt = itemView.findViewById(R.id.repetitionWeightEt);
            repetitionEt = itemView.findViewById(R.id.repetitionEt);

        }
    }
}
