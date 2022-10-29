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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class WorkoutTemplateAdapter extends RecyclerView.Adapter<WorkoutTemplateAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<WorkoutTemplate> templateList;
    ArrayList<WorkoutDetails> arrayListMember;
    //WorkoutTemplateAdapter.OnNoteListener mOnNoteListener;

    public WorkoutTemplateAdapter(Context ct, ArrayList<WorkoutTemplate> templatesname , RecyclerViewInterface recyclerViewInterface , ArrayList<WorkoutDetails> memberList/*,OnNoteListener onNoteListener*/){
        context = ct;
        templateList = templatesname;
        this.recyclerViewInterface = recyclerViewInterface;
        arrayListMember = memberList;
        //this.mOnNoteListener = onNoteListener;
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


        holder.workoutTemplate_rowitem_titleTv.setText(templateList.get(position).getName());

        MemberAdapter memberAdapter = new MemberAdapter(templateList, arrayListMember);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        holder.memberRv.setLayoutManager(linearLayoutManager);

        holder.memberRv.setAdapter(memberAdapter);

    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        TextView workoutTemplate_rowitem_titleTv;
        RecyclerView memberRv;
        //WorkoutTemplateAdapter.OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface  /*,OnNoteListener onNoteListener */) {
            super(itemView);

            workoutTemplate_rowitem_titleTv = itemView.findViewById(R.id.workoutTemplate_rowitem_titleTv);
            memberRv = itemView.findViewById(R.id.memberRv);

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
            //this.onNoteListener = onNoteListener;

            //itemView.setOnClickListener(this);
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
