package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.Repetition;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RepetitionAdapter extends RecyclerView.Adapter<RepetitionAdapter.MyViewHolder> {

    ArrayList<Repetition> reps;
    Context context;

    public RepetitionAdapter(Context ct, ArrayList<Repetition> reps){
        context = ct;
        this.reps = reps;
    }

    @NonNull
    @Override
    public RepetitionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repetition_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepetitionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final Repetition rep = reps.get(position);

        holder.repetitionSerieEt.setText(reps.get(position).getSeries());
        holder.repetitionWeightEt.setText(reps.get(position).getWeight());
        holder.repetitionEt.setText(reps.get(position).getNumberOfRepetitions());

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
