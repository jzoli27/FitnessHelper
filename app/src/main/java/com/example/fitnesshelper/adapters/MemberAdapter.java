package com.example.fitnesshelper.adapters;

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

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
    ArrayList<WorkoutTemplate> templateList;
    ArrayList<WorkoutDetails> arrayListMember;

    public MemberAdapter(/*ArrayList<WorkoutTemplate> templatesname,*/ArrayList<WorkoutDetails> memberList ){
        //templateList = templatesname;
        arrayListMember = memberList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_row_item,parent,false);

        return new MemberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.member_row_repnumberTv.setText(arrayListMember.get(position).getRepetitionnumber() + "x ");
            holder.member_row_excnameTv.setText(arrayListMember.get(position).getExercisename());

    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView member_row_repnumberTv,member_row_excnameTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            member_row_repnumberTv = itemView.findViewById(R.id.member_row_repnumberTv);
            member_row_excnameTv = itemView.findViewById(R.id.member_row_excnameTv);
        }
    }
}
