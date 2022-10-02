package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;

import java.util.ArrayList;

public class EditWorkoutTemplateAdapter extends RecyclerView.Adapter<EditWorkoutTemplateAdapter.MyViewHolder>{

    ArrayList<Exercise> exerciseList;
    Context context;

    public EditWorkoutTemplateAdapter(Context ct, ArrayList<Exercise> exercises){
        context = ct;
        exerciseList = exercises;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_workout_template_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView")  int position) {


        holder.excNameTv.setText(exerciseList.get(position).getExerciseName());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView excNameTv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            excNameTv = itemView.findViewById(R.id.exc_editWorkoutTemplateTv);
        }

    }
}
