package com.example.fitnesshelper.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.adapters.ExercisesAdapter;
import com.example.fitnesshelper.adapters.ReminderAdapter;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.TemplateDate;
import com.example.fitnesshelper.models.WorkoutTemplate;
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
import java.util.HashMap;

public class ReminderFragment extends Fragment implements RecyclerViewInterface {

    private Button mPickDateButton;
    private ArrayList<String> datesByDatePicker;
    private ArrayList<String> wtTemplates;
    private ArrayList<WorkoutTemplate> reminderWtTemplates;
    private HashMap<String,String> listedDates;
    //private ListView listOfDates;

    private TextView mShowSelectedDateText;
    //private ArrayAdapter<String> arrayAdapter;
    DatabaseReference fromRef;
    DatabaseReference toRef;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    String date;
    private int datepPosition;

    private DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private String checkFragment;
    private int checkFragmentPos;
    private DatabaseReference plannedworkoutref = FirebaseDatabase.getInstance().getReference("Plannedworkout");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        datesByDatePicker = new ArrayList<>();
        wtTemplates = new ArrayList<>();
        reminderWtTemplates = new ArrayList<>();
        listedDates = new HashMap<>();
        //listOfDates = view.findViewById(R.id.listOfDates);

        checkFragment = "ures";
        if (getArguments() != null){
            checkFragment = getArguments().getString("Key");
            checkFragmentPos = getArguments().getInt("Pos");
            datepPosition = getArguments().getInt("datePos");
        }

        recyclerView = view.findViewById(R.id.listOfDatesRv);
        initializeRecyclerView();
        initializeData();


        //arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, datesByDatePicker);


        mPickDateButton = view.findViewById(R.id.pick_date_button);
        mShowSelectedDateText = view.findViewById(R.id.show_selected_date);

        mShowSelectedDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkFragment.isEmpty()){
                    //Toast.makeText(getActivity(), "fragment: " + checkFragment + " position: " + checkFragmentPos, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "data: " + reminderWtTemplates.get(checkFragmentPos).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ezt próbáld meg beletenni az initializedata()-ba, mert valószinü, hogy nem fut le -> nincs tömb amire tudna itt hivatkozni
        /*
        if (!checkFragment.equals("ures")){
            Toast.makeText(getActivity(), "position:" + checkFragmentPos, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "sablon name:" + reminderWtTemplates.get(checkFragmentPos).getName(), Toast.LENGTH_SHORT).show();
            //wtTemplates.add(reminderWtTemplates.get(checkFragmentPos));
            //reminderAdapter.notifyDataSetChanged();
        }

         */


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");

                    }
                });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        //mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        date = materialDatePicker.getHeaderText();
                        datesByDatePicker.add(materialDatePicker.getHeaderText().toString());

                        listedDates.put("date", date);
                        TemplateDate templateDate = new TemplateDate(date,"Kérlek válassz sablont");
                        plannedworkoutref.child(uid).push().setValue(templateDate);
                        //Toast.makeText(getActivity(), "Date: " + date, Toast.LENGTH_SHORT).show();
                        reminderAdapter.notifyItemInserted(datesByDatePicker.size());
                    }
                });

        return view;
    }


    //lehet be kéne tenni initialize databa, hogy ne legyen olyan ütközés, hogy melyiket hozza létre előbb
    private void initializeRecyclerView() {

        //fel kéne tölteni a wtTemplates ugyanannyi sorral mint amennyi a datesbydatepicker lesz,
        reminderAdapter = new ReminderAdapter(getActivity(), datesByDatePicker, wtTemplates,this);
        datesByDatePicker.clear();

        plannedworkoutref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot current_data: snapshot.getChildren()){
                    //String actualdate = snapshot.getKey().toString();
                    TemplateDate templateDate = current_data.getValue(TemplateDate.class);
                    Log.d("FOR1", "VALUE: "+current_data.getKey().toString());
                    Log.d("FOR1", "date: "+templateDate.getDate() + " name:" + templateDate.getTemplateName());

                    datesByDatePicker.add(templateDate.getDate());
                    wtTemplates.add(templateDate.getTemplateName());
                    reminderAdapter.notifyDataSetChanged();
                    for(DataSnapshot current_user_data: current_data.getChildren()){
                        Log.d("FOR2", "VALUE: "+current_user_data.getValue().toString());
                        //String actualdate = current_user_data.getValue().toString();
                        //String actualTemplateName = current_user_data.getValue().toString();
                        //TemplateDate templateDate = current_user_data.getValue(TemplateDate.class);
                        //datesByDatePicker.add(templateDate.getDate());
                        //wtTemplates.add(templateDate.getTemplateName());
                        //wtTemplates.add("Kérlek válassz sablont");
                        //Log.d("FOR2", "key "+current_user_data.getKey());
                        //Log.d("FOR2", "date: "+actualdate + " name: " + actualTemplateName);
                        //ide johetne az ifes rész, ha van adat az argumentsből
                        //itt kéne ua mennyiségű szöveggel feltölteni a wttemplates is amit felül lehetne majd később írni/akár string tömb is lehetne egyébként

                    }
                }
                /*
                if (!checkFragment.equals("ures") && !datesByDatePicker.isEmpty()){
                    wtTemplates.set(datepPosition,reminderWtTemplates.get(checkFragmentPos).getName());
                    checkFragment = "ures";
                }
                 */
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setAdapter(reminderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void copyRecord() {
        fromRef = FirebaseDatabase.getInstance().getReference().child("Users");
        toRef = FirebaseDatabase.getInstance().getReference().child("PlannedWorkout");

        //toRef.child(date).setValue("hello");

        fromRef.child(uid).child("Templates").child("-NBnHwbvMeXRiEVlw2tm").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void initializeData(){
        userDbReference.child("Templates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot current_data: snapshot.getChildren()){
                    WorkoutTemplate workoutTemplate = current_data.getValue(WorkoutTemplate.class);
                    reminderWtTemplates.add(workoutTemplate);
                }

                //lehet mehetne ez a init recyclerviewba
                /*
                if (!checkFragment.equals("ures")){
                    Toast.makeText(getActivity(), "position:" + checkFragmentPos, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "sablon name:" + reminderWtTemplates.get(checkFragmentPos).getName(), Toast.LENGTH_SHORT).show();
                    //wtTemplates.add(reminderWtTemplates.get(checkFragmentPos).getName());
                    //reminderAdapter.notifyDataSetChanged();
                }
                 */
                /*
                if (!reminderWtTemplates.isEmpty() && getArguments() != null){
                    checkFragment = getArguments().getString("Key");
                    checkFragmentPos = getArguments().getInt("Pos");
                    wtTemplates.add(reminderWtTemplates.get(checkFragmentPos));
                }

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        WorkoutFragment fragment = new WorkoutFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("Key", "reminder");
        args.putInt("datepos", position);
        fragment.setArguments(args);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

    }
}
