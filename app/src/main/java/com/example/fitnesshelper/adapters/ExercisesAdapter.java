package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.MyViewHolder> {

    ArrayList<Exercise> exerciseList;
    //ArrayList<Exercise> exerciseListFull;

    Context context;

    public ExercisesAdapter(Context ct, ArrayList<Exercise> exercises){
        context = ct;
        exerciseList = exercises;
        //exerciseListFull = new ArrayList<>(exercises);
    }
    /*
    public void setFilteredList(ArrayList<Exercise> filteredList){
        this.exerciseList = filteredList;
        notifyDataSetChanged();
    }

     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercises_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Exercise exercise = exerciseList.get(position);

        holder.excName.setText(exerciseList.get(position).getExerciseName());
        holder.excMouscle.setText(exerciseList.get(position).getMuscleGroup());


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView excName, excMouscle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            excName = itemView.findViewById(R.id.excercises_row_item_name_tv);
            excMouscle = itemView.findViewById(R.id.excercises_row_item_mgroup_tv);
        }
    }

    //Ezek kellenek a keresés funkcióhoz, fentről kivágtam még a: "implements Filterable" sort...
    /*
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Exercise> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(exerciseListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Exercise item : exerciseListFull){
                    if (item.getExerciseName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            exerciseList.clear();
            exerciseList.addAll((List) filterResults.values);

            notifyDataSetChanged();
        }
    };

     */
}
