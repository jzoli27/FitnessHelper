package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.fitnesshelper.StartWorkout;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkoutTemplateAdapter extends RecyclerView.Adapter<WorkoutTemplateAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<WorkoutTemplate> templateList;
    ArrayList<WorkoutDetails> arrayListMember;
    ArrayList<WorkoutDetails> hope;
    ArrayList<WorkoutDetails> hope2;
    //WorkoutTemplateAdapter.OnNoteListener mOnNoteListener;

    public WorkoutTemplateAdapter(Context ct, ArrayList<WorkoutTemplate> templatesname , RecyclerViewInterface recyclerViewInterface , ArrayList<WorkoutDetails> memberList/*,OnNoteListener onNoteListener*/){
        context = ct;
        templateList = templatesname;
        this.recyclerViewInterface = recyclerViewInterface;
        arrayListMember = memberList;
    }

    @NonNull
    @Override
    public WorkoutTemplateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_template_row_item,parent,false);

        return new MyViewHolder(view,recyclerViewInterface /*,mOnNoteListener*/);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutTemplateAdapter.MyViewHolder holder, @SuppressLint("RecyclerView")  int position) {
        hope = new ArrayList<>();
        hope2 = new ArrayList<>();

        holder.workoutTemplate_rowitem_titleTv.setText(templateList.get(position).getName());

        for (int i = 0; i < arrayListMember.size(); i++){
            if (templateList.get(position).getName().equals(arrayListMember.get(i).getName())){
                hope.add(arrayListMember.get(i));
            }
        }


        MemberAdapter memberAdapter = new MemberAdapter(hope);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        holder.memberRv.setLayoutManager(linearLayoutManager);

        holder.memberRv.setAdapter(memberAdapter);

    }


    @Override
    public int getItemCount() {
        return templateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

        TextView workoutTemplate_rowitem_titleTv;
        RecyclerView memberRv;
        ImageView workoutTemplate_rowitem_optionsIv;
        //WorkoutTemplateAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface  /*,OnNoteListener onNoteListener */) {
            super(itemView);

            workoutTemplate_rowitem_titleTv = itemView.findViewById(R.id.workoutTemplate_rowitem_titleTv);
            workoutTemplate_rowitem_optionsIv = itemView.findViewById(R.id.workoutTemplate_rowitem_optionsIv);
            workoutTemplate_rowitem_optionsIv.setOnClickListener(this);
            memberRv = itemView.findViewById(R.id.memberRv);

            workoutTemplate_rowitem_titleTv.setOnClickListener(new View.OnClickListener() {
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
            //this.onNoteListener = onNoteListener;

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.workout_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.workout_action_popup_start:
                    hope2.clear();
                    for (int i = 0; i < arrayListMember.size(); i++){
                        if (templateList.get(getAdapterPosition()).getName().equals(arrayListMember.get(i).getName())){
                            hope2.add(arrayListMember.get(i));
                        }
                    }

                    //Toast.makeText(context, "Indítás", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, StartWorkout.class);
                    //intent.putExtra("size", String.valueOf(templateList.size()));
                    //intent.putExtra("mylist", templateList);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ARRAYLIST",hope2);
                    bundle.putSerializable("templatelist",templateList);
                    bundle.putString("wtKey", templateList.get(getAdapterPosition()).getWtKey());
                    bundle.putString("wtName", templateList.get(getAdapterPosition()).getName());

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }

        /*
        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

         */


    }

    /*
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

     */



}
