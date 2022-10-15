package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;

import java.util.ArrayList;

public class EditWorkoutTemplateAdapter extends RecyclerView.Adapter<EditWorkoutTemplateAdapter.MyViewHolder>{

    ArrayList<Exercise> exerciseList;
    Context context;
    OnNoteListener mOnNoteListener;

    public EditWorkoutTemplateAdapter(Context ct, ArrayList<Exercise> exercises, OnNoteListener onNoteListener){
        context = ct;
        exerciseList = exercises;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_workout_template_row,parent,false);

        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView")  int position) {


        holder.excNameTv.setText(exerciseList.get(position).getExerciseName());

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView excNameTv;
        ImageView imageView;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            excNameTv = itemView.findViewById(R.id.exc_editWorkoutTemplateTv);
            imageView = itemView.findViewById(R.id.exc_editWorkoutTemplateIv);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
