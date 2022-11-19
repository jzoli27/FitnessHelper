package com.example.fitnesshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Repetition;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class StartWorkoutAdapter extends RecyclerView.Adapter<StartWorkoutAdapter.MyViewHolder> {
    Context context;
    //ArrayList<WorkoutTemplate> templateList;
    ArrayList<WorkoutDetails> arrayListMember;
    ArrayList<Repetition> reps;
    ArrayList<Repetition> hope;
    String wtKey;

    public StartWorkoutAdapter(Context context,  ArrayList<WorkoutDetails> arrayListMember, ArrayList<Repetition> reps, String wtKey) {
        this.context = context;
        this.arrayListMember = arrayListMember;
        this.reps = reps;
        this.wtKey = wtKey;
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
        hope = new ArrayList<>();

        //holder.start_workout_row_item_repnumberTv.setText(arrayListMember.get(position).getRepetitionnumber() + "x ");
        holder.start_workout_row_item_excnameTv.setText(arrayListMember.get(position).getExercisename());

        Repetition repetition = new Repetition("4","6","8","asd","0","-adsdsda", "-dddfggfdsa");
        for (int i = 0; i < reps.size(); i++){
            if (arrayListMember.get(position).getExercisename().equals(reps.get(i).getExerciseName())){
                hope.add(reps.get(i));
            }
        }

        StartWorkoutMemberAdapter memberAdapter = new StartWorkoutMemberAdapter(hope, wtKey, arrayListMember.get(position).getExcKey());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        holder.recyclerView.setLayoutManager(linearLayoutManager);

        holder.recyclerView.setAdapter(memberAdapter);
        holder.startworkout_repetition_item_infoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "rep: " + repetition.getExerciseName(), Toast.LENGTH_SHORT).show();
                hope.add(repetition);
                memberAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        TextView start_workout_row_item_repnumberTv,start_workout_row_item_excnameTv;
        ImageView start_workout_row_item_optionsIv, startworkout_repetition_item_infoIv;
        RecyclerView recyclerView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //start_workout_row_item_repnumberTv = itemView.findViewById(R.id.start_workout_row_item_repnumberTv);
            startworkout_repetition_item_infoIv = itemView.findViewById(R.id.startworkout_repetition_item_infoIv);
            start_workout_row_item_excnameTv = itemView.findViewById(R.id.start_workout_row_item_excnameTv);
            start_workout_row_item_optionsIv = itemView.findViewById(R.id.start_workout_row_item_optionsIv);
            start_workout_row_item_optionsIv.setOnClickListener(this);
            recyclerView = itemView.findViewById(R.id.start_workout_row_item_Rv);

        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.startworkout_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.startworkout_add:
                    Toast.makeText(context, "Hozzáadás", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.startworkout_remove:
                    Toast.makeText(context, "Törlés", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }
    }
}
