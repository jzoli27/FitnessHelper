package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Repetition;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StartWorkoutMemberAdapter extends RecyclerView.Adapter<StartWorkoutMemberAdapter.MyViewHolder> {

    ArrayList<Repetition> reps;
    String wtKey;
    String excKey;

    public StartWorkoutMemberAdapter(ArrayList<Repetition> reps, String wtKey, String excKey) {
        this.reps = reps;
        this.wtKey = wtKey;
        this.excKey = excKey;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.startworkout_repetition_item,parent,false);

        return new StartWorkoutMemberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getUid().toString();
        String repKey = reps.get(position).getRepetitionKey();

        holder.repetitionSerieEt.setText(reps.get(position).getSeries()+".");
        holder.repetitionWeightEt.setText(reps.get(position).getWeight()+" kg");
        holder.repetitionEt.setText(reps.get(position).getNumberOfRepetitions()+" db");

        holder.startworkout_repetition_item_Cb.setOnCheckedChangeListener(null);

        //holder.startworkout_repetition_item_Cb.setChecked(exercise.getSelected());

        holder.startworkout_repetition_item_Cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    db.getReference("Users").child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").child(repKey).child("state").setValue("1");
                }else{
                    db.getReference("Users").child(uid).child("Templates").child(wtKey).child("Exercises").child(excKey).child("Repetition").child(repKey).child("state").setValue("0");
                }

                //Toast.makeText(context, "key: " + exercise.getExerciseKey(), Toast.LENGTH_SHORT).show();
                //exercise.setSelected(b);
                Log.d("CHECKBOX", "uid: "+ uid+" wtKey: "+wtKey + " excKey: " +excKey + " value: " +reps.get(position).getExerciseName() + " key: "+ reps.get(position).getRepetitionKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        EditText repetitionSerieEt, repetitionWeightEt, repetitionEt;
        CheckBox startworkout_repetition_item_Cb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            repetitionSerieEt = itemView.findViewById(R.id.repetitionSerieEt);
            repetitionWeightEt = itemView.findViewById(R.id.repetitionWeightEt);
            repetitionEt = itemView.findViewById(R.id.repetitionEt);
            startworkout_repetition_item_Cb = itemView.findViewById(R.id.startworkout_repetition_item_Cb);

        }
    }
}
