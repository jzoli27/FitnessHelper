package com.example.fitnesshelper.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.EditWorkoutActivity;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.adapters.ExercisesAdapter;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.ExpandedExercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewExercisesFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView exercisesRv;
    private FloatingActionButton addBtn;
    private TextView fragment_new_exercises_titleTv;
    private ArrayList<Exercise> exercisesList;
    private ArrayList<ExpandedExercise> ExpandedExercisesList;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    private int LAUNCH_SECOND_ACTIVITY = 1;

    String uid = FirebaseAuth.getInstance().getUid().toString();

    ExercisesAdapter exercisesAdapter;

    SearchView searchView;

    String checkFragment;
    int machinePosition;
    String FmKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_exercises, container,false);

        exercisesList = new ArrayList<>();
        ExpandedExercisesList = new ArrayList<>();

        exercisesRv = view.findViewById(R.id.fragment_new_exercises_Rv);
        addBtn = view.findViewById(R.id.fragment_new_exercises_addbtn);
        fragment_new_exercises_titleTv = view.findViewById(R.id.fragment_new_exercises_titleTv);
        searchView = view.findViewById(R.id.fragment_new_exercises_searchview);
        searchView.clearFocus();

        checkFragment = "ures";

        if (getArguments() != null){
            checkFragment = getArguments().getString("fragment");
            machinePosition = getArguments().getInt("machinepos");
            FmKey = getArguments().getString("fmKey");
        }


        if (!checkFragment.equals("ures")){
            fragment_new_exercises_titleTv.setText("Válassz gyakorlatot " );
        }

        initializeRecyclerView();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditWorkoutActivity.class);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                //Intent intent = new Intent(getActivity(), EditWorkoutActivity.class);
                //startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                //kikapcsoltam a gyakorlatok áthelyezése müvelet végett..majd aktiváld vissza
                filterList(text);
                return true;
            }
        });


        return view;
    }


    /*
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Exercise> filteredlist = new ArrayList<Exercise>();

        // running a for loop to compare elements.
        for (Exercise item : exercisesList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getExerciseName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            exercisesAdapter.filterList(filteredlist);
        }
    }

     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
                initializeRecyclerView();
            }
        }
    }

    // ez jó keresés volt, majd tedd vissza
    private void filterList(String text) {
        ArrayList<ExpandedExercise> filteredList = new ArrayList<>();
        for(ExpandedExercise exercise: ExpandedExercisesList){
            if (exercise.getExerciseName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(exercise);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        }else {
            exercisesAdapter.setFilteredList(filteredList);
        }
    }




    private void initializeRecyclerView() {
        exercisesAdapter = new ExercisesAdapter(getActivity(), ExpandedExercisesList, this);
        reference = FirebaseDatabase.getInstance().getReference("Users");

        ExpandedExercisesList.clear();
        reference.child(uid).child("Exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ExpandedExercise expandedExercise = dataSnapshot.getValue(ExpandedExercise.class);
                    ExpandedExercisesList.add(expandedExercise);
                    exercisesAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        exercisesRv.setAdapter(exercisesAdapter);
        exercisesRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();

        if (checkFragment.equals("settings")){
            //Toast.makeText(getActivity(), "fragment: " + checkFragment, Toast.LENGTH_SHORT).show();
            SettingsFragment fragment = new SettingsFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle args = new Bundle();
            args.putString("fragment", "newexercises");
            args.putString("exercise", ExpandedExercisesList.get(position).getExerciseName());
            args.putInt("machinepos", machinePosition);
            args.putString("fmkey", FmKey);
            fragment.setArguments(args);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
    }
}
