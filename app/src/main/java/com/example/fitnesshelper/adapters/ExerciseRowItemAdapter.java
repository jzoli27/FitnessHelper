package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExerciseRowItemAdapter extends RecyclerView.Adapter<ExerciseRowItemAdapter.MyViewHolder>{

    ArrayList<Exercise> exerciseList;
    Context context;

    public ExerciseRowItemAdapter(Context ct, ArrayList<Exercise> exercises){
        context = ct;
        exerciseList = exercises;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_row_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final Exercise exercise = exerciseList.get(position);

        holder.excName.setText(exerciseList.get(position).getExerciseName());
        holder.excMouscle.setText(exerciseList.get(position).getMuscleGroup());
        if (!exercise.getIcon().isEmpty()){
            Log.d("HIBA","value: " + exercise.getIcon());
            Picasso.get().load(exercise.getIcon()).into(holder.excercises_row_item_iconIV);
        }

        holder.excCb.setOnCheckedChangeListener(null);

        holder.excCb.setChecked(exercise.getSelected());

        holder.excCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                db.getReference("Exercises").child(exercise.getExerciseKey()).child("selected").setValue(b);
                //Toast.makeText(context, "key: " + exercise.getExerciseKey(), Toast.LENGTH_SHORT).show();
                exercise.setSelected(b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView excName, excMouscle;
        ImageView excercises_row_item_iconIV;
        CheckBox excCb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            excName = itemView.findViewById(R.id.exc_row_item_name_tv);
            excMouscle = itemView.findViewById(R.id.exc_row_item_mgroup_tv);
            excercises_row_item_iconIV = itemView.findViewById(R.id.excercises_row_item_iconIV);
            excCb = itemView.findViewById(R.id.exc_row_item_checkBox);
        }
    }
}
