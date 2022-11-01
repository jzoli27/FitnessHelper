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
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.FitnessMachine;

import java.util.ArrayList;

public class FitnessMachineAdapter extends RecyclerView.Adapter<FitnessMachineAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<FitnessMachine> Fmachines;
    Context context;


    public FitnessMachineAdapter(Context ct, ArrayList<FitnessMachine> fmachinesList, RecyclerViewInterface recyclerViewInterface){
        context = ct;
        Fmachines = fmachinesList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public FitnessMachineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fitnessmachine_item,parent,false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.machineNameTv.setText(Fmachines.get(position).getMachineName());
    }


    @Override
    public int getItemCount() {
        return Fmachines.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView machineNameTv;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            machineNameTv = itemView.findViewById(R.id.fitnessmachine_settings_NameTv);

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

        }

    }

}
