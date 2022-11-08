package com.example.fitnesshelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
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
        holder.settingNameEt.setEnabled(false);
        holder.settingValueEt.setText(SettingValue.get(position));
        holder.settingValueEt.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return SettingKey.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

        EditText settingNameEt, settingValueEt;
        ImageView fmDetails_row_item_optionsIv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            settingNameEt = itemView.findViewById(R.id.fmDetails_row_item_settingNameEt);
            settingValueEt = itemView.findViewById(R.id.fmDetails_row_item_settingValueEt);
            fmDetails_row_item_optionsIv = itemView.findViewById(R.id.fmDetails_row_item_optionsIv);

            fmDetails_row_item_optionsIv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.fmdetails_popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_edit:
                    Toast.makeText(context, "action_popup_edit at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    settingNameEt.setEnabled(true);
                    settingValueEt.setEnabled(true);
                    return true;
                case R.id.action_popup_save:
                    Toast.makeText(context, "action_popup_save at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_popup_delete:
                    Toast.makeText(context, "action_popup_delete at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    return true;
                default:
                    return false;
            }
        }
    }
}
