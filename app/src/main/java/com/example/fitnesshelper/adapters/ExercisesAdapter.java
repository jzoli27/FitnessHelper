package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.ExpandedExercise;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.MyViewHolder> {

    //régi
    //ArrayList<Exercise> exerciseList;
    ArrayList<ExpandedExercise> exerciseList;

    //ArrayList<Exercise> exerciseListFull;

    Context context;
    RecyclerViewInterface recyclerViewInterface;

    public ExercisesAdapter(Context ct, ArrayList<ExpandedExercise> exercises, RecyclerViewInterface recyclerViewInterface){
        context = ct;
        exerciseList = exercises;
        this.recyclerViewInterface = recyclerViewInterface;
        //exerciseListFull = new ArrayList<>(exercises);
    }

    // ez jó keresés volt, majd tedd vissza
    public void setFilteredList(ArrayList<ExpandedExercise> filteredList){
        this.exerciseList = filteredList;
        notifyDataSetChanged();
    }



    // method for filtering our recyclerview items.
    public void filterList(ArrayList<ExpandedExercise> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        exerciseList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercises_item,parent,false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExpandedExercise exercise = exerciseList.get(position);

        holder.excName.setText(exerciseList.get(position).getExerciseName());
        holder.excMouscle.setText(exerciseList.get(position).getMuscleGroup());

        //holder.excercises_row_iconIV.setImageBitmap();

        //Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.excercises_row_iconIV);

        if (!exercise.getIcon().isEmpty()){
            //holder.excercises_row_iconIV.setImageResource(R.drawable.ic_launcher_foreground);
            //Toast.makeText(context, "value: " + exercise.getExerciseName(), Toast.LENGTH_SHORT).show();
            Log.d("HIBA","value: " + exercise.getIcon());
            Picasso.get().load(exercise.getIcon()).into(holder.excercises_row_iconIV);
            //Picasso.get().load(exercise.getIcon()).into(holder.excercises_row_iconIV);
        }
        Log.d("HIBA2","foron kivul " + exercise.getIcon() + " neve:" + exercise.getExerciseName());







        holder.excercises_row_item_description.setText(exerciseList.get(position).getDescription());
        boolean isExpanded = exerciseList.get(position).getSelected();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView excName, excMouscle,excercises_row_item_description;
        ImageView excercises_row_iconIV;
        RelativeLayout main_relativeLayout;
        ConstraintLayout expandableLayout;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            excName = itemView.findViewById(R.id.excercises_row_item_name_tv);
            excMouscle = itemView.findViewById(R.id.excercises_row_item_mgroup_tv);
            excercises_row_item_description = itemView.findViewById(R.id.excercises_row_item_description);
            excercises_row_iconIV = itemView.findViewById(R.id.excercises_row_iconIV);

            main_relativeLayout = itemView.findViewById(R.id.excercises_row_item_mainlayout);
            expandableLayout = itemView.findViewById(R.id.expandablelayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            /*
            main_relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExpandedExercise exercise = exerciseList.get(getAdapterPosition());
                    exercise.setSelected(!exercise.getSelected());
                    notifyItemChanged(getAdapterPosition());
                }
            });

             */

            excName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Exercise exercise = exerciseList.get(getAdapterPosition());
                    //exercise.setSelected(!exercise.getSelected());
                    //notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
