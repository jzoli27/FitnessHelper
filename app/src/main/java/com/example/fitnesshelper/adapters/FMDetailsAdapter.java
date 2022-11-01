package com.example.fitnesshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;

import java.util.ArrayList;

public class FMDetailsAdapter extends RecyclerView.Adapter<FMDetailsAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> SettingKey;
    ArrayList<String> SettingValue;

    public FMDetailsAdapter( Context context, ArrayList<String> settingKey, ArrayList<String> settinValue) {
        this.context = context;
        SettingKey = settingKey;
        SettingValue = settinValue;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fmdetails_row_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.settingNameEt.setText(SettingKey.get(position));
        holder.settingValueEt.setText(SettingValue.get(position));
    }

    @Override
    public int getItemCount() {
        return SettingKey.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        EditText settingNameEt, settingValueEt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            settingNameEt = itemView.findViewById(R.id.fmDetails_row_item_settingNameEt);
            settingValueEt = itemView.findViewById(R.id.fmDetails_row_item_settingValueEt);

        }
    }
}
