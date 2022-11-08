package com.example.fitnesshelper.adapters;

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
import com.example.fitnesshelper.models.WorkoutTemplate;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    Context ct;
    ArrayList<String> dates;
    ArrayList<String> wtTemplates;
    private final RecyclerViewInterface recyclerViewInterface;

    public ReminderAdapter(Context ct, ArrayList<String> dates, ArrayList<String> wtTemplates, RecyclerViewInterface recyclerViewInterface) {
        this.ct = ct;
        this.dates = dates;
        this.wtTemplates = wtTemplates;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.reminder_item,parent,false);

        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.MyViewHolder holder, int position) {
        holder.reminder_DateTv.setText(dates.get(position));
        holder.reminder_WtNameTv.setText(wtTemplates.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView reminder_DateTv, reminder_WtNameTv;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            reminder_DateTv = itemView.findViewById(R.id.reminder_DateTv);
            reminder_WtNameTv = itemView.findViewById(R.id.reminder_WtNameTv);

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
