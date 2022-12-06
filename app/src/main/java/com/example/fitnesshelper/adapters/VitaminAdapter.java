package com.example.fitnesshelper.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.AlertReceiver;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Vitamin;

import java.util.ArrayList;
import java.util.Calendar;

public class VitaminAdapter extends RecyclerView.Adapter<VitaminAdapter.MyViewHolder> {
    Context context;
    ArrayList<Vitamin> vitamins;

    public VitaminAdapter(Context context, ArrayList<Vitamin> vitamins) {
        this.context = context;
        this.vitamins = vitamins;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vitamin_item_row,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vitamin_item_dateTv.setText(vitamins.get(position).getDate());
        holder.vitamin_messageEt.setText(vitamins.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return vitamins.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        TextView vitamin_item_dateTv;
        EditText vitamin_messageEt;
        ImageView vitamin_item_optionsIv;
        Switch stateSwitch;
        Button vitamin_item_TimePickerBtn, vitamin_item_cancelBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vitamin_item_dateTv = itemView.findViewById(R.id.vitamin_item_dateTv);
            vitamin_messageEt = itemView.findViewById(R.id.vitamin_messageEt);
            vitamin_item_optionsIv = itemView.findViewById(R.id.vitamin_item_optionsIv);
            vitamin_item_optionsIv.setOnClickListener(this);
            stateSwitch = itemView.findViewById(R.id.switch1);

            //vitamin_item_TimePickerBtn = itemView.findViewById(R.id.vitamin_item_TimePickerBtn);
            //vitamin_item_cancelBtn = itemView.findViewById(R.id.vitamin_item_cancelBtn);

        }

        @Override
        public void onClick(View view) {

            showPopupMenu(view);

        }
        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.vitamin_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_start:
                    Editable message = vitamin_messageEt.getText();
                    //Toast.makeText(context, "Message: " + message, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Indítás", Toast.LENGTH_SHORT).show();

                    startalarm(vitamins.get(getAdapterPosition()).getCalendar(),vitamins.get(getAdapterPosition()).getId(), message);
                    stateSwitch.setChecked(true);
                    return true;
                case R.id.action_popup_turnoff:
                    //Toast.makeText(context, "kikapcsolás", Toast.LENGTH_SHORT).show();
                    cancelAlarm(vitamins.get(getAdapterPosition()).getId());
                    stateSwitch.setChecked(false);
                    return true;
                case R.id.action_popup_save:
                    Toast.makeText(context, "Mentés", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_popup_delete:
                    //Toast.makeText(context, "Törlés ", Toast.LENGTH_SHORT).show();
                    removeAt(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }
    private void startalarm(Calendar c, int id, Editable message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE) ;
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("title", "Cím");
        intent.putExtra("message", message.toString());
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),1,intent,0);


        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }


        // Define pendingintent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,intent, 0);
        // set() schedules an alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Toast.makeText(context, "Indítás", Toast.LENGTH_SHORT).show();
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id,intent,0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Időzítő kikapcsolva", Toast.LENGTH_SHORT).show();
    }

    public void removeAt(int position) {
        vitamins.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, vitamins.size());
    }
}
