package com.example.fitnesshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class StartWorkoutAdapter extends RecyclerView.Adapter<StartWorkoutAdapter.MyViewHolder> {
    Context context;
    //ArrayList<WorkoutTemplate> templateList;
    ArrayList<WorkoutDetails> arrayListMember;

    public StartWorkoutAdapter(Context context,  ArrayList<WorkoutDetails> arrayListMember) {
        this.context = context;
        this.arrayListMember = arrayListMember;
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
        holder.start_workout_row_item_repnumberTv.setText(arrayListMember.get(position).getRepetitionnumber() + "x ");
        holder.start_workout_row_item_excnameTv.setText(arrayListMember.get(position).getExercisename());
    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView start_workout_row_item_repnumberTv,start_workout_row_item_excnameTv;
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            start_workout_row_item_repnumberTv = itemView.findViewById(R.id.start_workout_row_item_repnumberTv);
            start_workout_row_item_excnameTv = itemView.findViewById(R.id.start_workout_row_item_excnameTv);
            recyclerView = itemView.findViewById(R.id.start_workout_row_item_Rv);
        }
    }
}
