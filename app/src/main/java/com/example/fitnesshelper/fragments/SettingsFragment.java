package com.example.fitnesshelper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.DetailsActivity;
import com.example.fitnesshelper.FitnessMachineDetails;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.adapters.FitnessMachineAdapter;
import com.example.fitnesshelper.interfaces.FitnessRecyclerViewInterface;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.ExpandedExercise;
import com.example.fitnesshelper.models.FitnessMachine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment implements FitnessRecyclerViewInterface {

    private static final int ACTION_REQUEST_GALLERY = 111;
    private static final int ACTION_REQUEST_CAMERA = 112;
    private ImageView imageView;
    private Button photoButton;
    private RecyclerView recyclerView;
    private ArrayList<FitnessMachine> Fmachines;
    private FitnessMachineAdapter fitnessMachineAdapter;
    DatabaseReference reference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference SettingsReference = FirebaseDatabase.getInstance().getReference("FitnessMachine");

    DatabaseReference fromRef;
    DatabaseReference toRef;
    DatabaseReference baseExerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
    DatabaseReference usersExerciseReference = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference FitnessReference = FirebaseDatabase.getInstance().getReference("FitnessMachine");

    private ArrayList<String> Settingkey;
    private ArrayList<String> Settingvalue;
    private HashMap<String,String> hashmap;

    private String checkFragment;
    private String newExerciseName;
    private int machinePosition;
    private String FmKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        recyclerView = view.findViewById(R.id.setting_machinesRv);
        Fmachines = new ArrayList<>();

        Settingkey = new ArrayList<>();
        Settingvalue = new ArrayList<>();
        hashmap = new HashMap<>();

        //Settingkey.add("asd");
        //Settingkey.add("asd");
        //Settingvalue.add("qwe");
        //Settingvalue.add("qwe");
        checkFragment = "ures";

        if (getArguments() != null){
            checkFragment = getArguments().getString("fragment");
            newExerciseName = getArguments().getString("exercise");
            machinePosition = getArguments().getInt("machinepos");
            FmKey = getArguments().getString("fmkey");

        }

        photoButton = (Button) view.findViewById(R.id.picturesBtn);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ez kell, csak kivettem a gyakorlatok másolása miatt
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);

                //addNewExercises();
            }
        });

        initializeRecyclerView();

        return view;
    }


    private void initializeRecyclerView() {
        fitnessMachineAdapter = new FitnessMachineAdapter(getActivity(), Fmachines, this);

        reference = FirebaseDatabase.getInstance().getReference("FitnessMachine").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    FitnessMachine fmachine = dataSnapshot.getValue(FitnessMachine.class);

                    Fmachines.add(fmachine);
                    //fitnessMachineAdapter.notifyDataSetChanged();
                }
                if (checkFragment.equals("newexercises")){
                    FitnessMachine modifiedfmachine = Fmachines.get(machinePosition);
                    modifiedfmachine.setAssignedExercise(newExerciseName);
                    Fmachines.set(machinePosition, modifiedfmachine);
                    FitnessReference.child(uid).child(FmKey).child("assignedExercise").setValue(newExerciseName);
                }
                fitnessMachineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(fitnessMachineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void initializeData(int position){
        SettingsReference.child(uid).child(Fmachines.get(position).getFmKey()).child("Settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashmap = (HashMap<String, String>) snapshot.getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position, String msg) {
        //Toast.makeText(getActivity(), "szöveg: " + msg, Toast.LENGTH_SHORT).show();
        if (msg.equals("machine")){
            Intent intent = new Intent(getActivity(), FitnessMachineDetails.class);
            intent.putExtra("name",Fmachines.get(position).getMachineName());
            intent.putExtra("fmKey",Fmachines.get(position).getFmKey());
            intent.putExtra("imgLink",Fmachines.get(position).getImgLink());
            startActivity(intent);
        }else if(msg.equals("exercise")){
            NewExercisesFragment fragment = new NewExercisesFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle args = new Bundle();
            args.putString("fragment", "settings");
            args.putString("fmKey", Fmachines.get(position).getFmKey());
            args.putInt("machinepos", position);
            fragment.setArguments(args);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }

    }


    public void addNewExercises() {
        baseExerciseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot current_data: snapshot.getChildren()){
                    Exercise exercise = current_data.getValue(Exercise.class);
                    //Log.d("HOPE", "value: " + exercise.getExerciseName());

                    ExpandedExercise expandedExercise = new ExpandedExercise(exercise.getExerciseName(), exercise.getExerciseType(), exercise.getDescription(), exercise.getMuscleGroup(),
                            exercise.getIcon(), exercise.getExerciseKey(), exercise.getSelected(), "nincs","nincs", "nincs");

                    usersExerciseReference.child(uid).child("Exercises").child(exercise.getExerciseKey()).setValue(expandedExercise);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}


