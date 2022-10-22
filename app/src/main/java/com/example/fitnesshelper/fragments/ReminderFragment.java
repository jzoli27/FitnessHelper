package com.example.fitnesshelper.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ReminderFragment extends Fragment {

    private Button mPickDateButton;
    private ArrayList<String> datesByDatePicker;
    private ListView listOfDates;

    private TextView mShowSelectedDateText;
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference fromRef;
    DatabaseReference toRef;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    String date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        datesByDatePicker = new ArrayList<>();
        listOfDates = view.findViewById(R.id.listOfDates);

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, datesByDatePicker);


        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = view.findViewById(R.id.pick_date_button);
        mShowSelectedDateText = view.findViewById(R.id.show_selected_date);

        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        listOfDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                copyRecord();
                //Toast.makeText(getActivity(), "" + datesByDatePicker.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        //Toast.makeText(getActivity(), "date: " +materialDatePicker.getHeaderText() , Toast.LENGTH_SHORT).show();
                        date = materialDatePicker.getHeaderText();
                        datesByDatePicker.add(materialDatePicker.getHeaderText().toString());
                        arrayAdapter.notifyDataSetChanged();

                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });

        listOfDates.setAdapter(arrayAdapter);

        return view;
    }
    public void copyRecord() {
        fromRef = FirebaseDatabase.getInstance().getReference().child("Users");
        toRef = FirebaseDatabase.getInstance().getReference().child("PlannedWorkout");

        //toRef.child(date).setValue("hello");

        fromRef.child(uid).child("Templates").child("-NBnHwbvMeXRiEVlw2tm").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                if (snapshot.getValue() != null){
                    Toast.makeText(getActivity(),"NEM üres a snapshot" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Üres a snapshot" , Toast.LENGTH_SHORT).show();
                }
                 */
                toRef.child(date).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(getActivity(), "siker", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Rossz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                /*
                for (DataSnapshot child: snapshot.getChildren()) {
                    String wtKey = child.getKey();
                    if (wtKey.equals("-NBnHwbvMeXRiEVlw2tm")) {



                    }
                }

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
