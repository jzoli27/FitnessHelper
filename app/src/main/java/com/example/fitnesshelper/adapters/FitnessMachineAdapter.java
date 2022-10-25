package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.FitnessMachine;

import java.util.ArrayList;

public class FitnessMachineAdapter extends RecyclerView.Adapter<FitnessMachineAdapter.MyViewHolder> {

    ArrayList<FitnessMachine> Fmachines;

    Context context;

    public FitnessMachineAdapter(Context ct, ArrayList<FitnessMachine> fmachinesList){
        context = ct;
        Fmachines = fmachinesList;
    }

    @NonNull
    @Override
    public FitnessMachineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fitnessmachine_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.machineNameTv.setText(Fmachines.get(position).getMachineName());
    }


    @Override
    public int getItemCount() {
        return Fmachines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView machineNameTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            machineNameTv = itemView.findViewById(R.id.fitnessmachine_settings_NameTv);

        }
    }
}
