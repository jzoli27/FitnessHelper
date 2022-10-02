package com.example.fitnesshelper.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.AlertReceiver;
import com.example.fitnesshelper.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VitaminFragment extends Fragment {
    Button vitaminTimePickerBtn, cancelBtn;
    int hour, minute;
    TextView vitaminTitleTv, alarmTv;
    AlarmManager alarmManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vitamin, container,false);

        vitaminTimePickerBtn = view.findViewById(R.id.vitaminTimePickerBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);

        vitaminTitleTv = view.findViewById(R.id.vitaminTitleTv);
        alarmTv = view.findViewById(R.id.alarmTv);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });

        vitaminTimePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c.set(Calendar.MINUTE, selectedMinute);
                        c.set(Calendar.SECOND, 0);

                        updateTimeText(c);
                        startalarm(c);

                        hour = selectedHour;
                        minute = selectedMinute;
                        //vitaminTimePickerBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        //itt kéne textviewhoz is adni ha kellene
                        //vitaminTitleTv.setText("Hour: " + hour + " Minute: " + minute);
                    }
                };
                int style = AlertDialog.THEME_HOLO_DARK; // spinneres órához
                //a spinneres órához kell a style is---- TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select time");
                //az ok gomb szöveg módosítása ---- timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE,"pozitiv gomb",timePickerDialog);
                timePickerDialog.show();

            }
        });





        return view;
    }

    private void startalarm(Calendar c) {
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE) ;
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),1,intent,0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void updateTimeText(Calendar c) {
        String alarmDatetext = "Időzítő beállítva:";
        alarmDatetext += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        alarmTv.setText(alarmDatetext);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),1,intent,0);

        alarmManager.cancel(pendingIntent);
        alarmTv.setText("Időzítő kikapcsolva");
    }
}
