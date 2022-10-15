package com.example.fitnesshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.adapters.EditWorkoutTemplateAdapter;
import com.example.fitnesshelper.adapters.ExercisesAdapter;
import com.example.fitnesshelper.models.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewExercisesFragment extends Fragment {

    private RecyclerView exercisesRv;
    private FloatingActionButton addBtn;
    private ArrayList<Exercise> exercisesList;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    ExercisesAdapter exercisesAdapter;

    //SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_exercises, container,false);

        exercisesList = new ArrayList<>();

        exercisesRv = view.findViewById(R.id.fragment_new_exercises_Rv);
        addBtn = view.findViewById(R.id.fragment_new_exercises_addbtn);
        //searchView = view.findViewById(R.id.fragment_new_exercises_searchview);

        initializeRecyclerView();
        /*
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filterList(text);
                return true;
            }
        });

         */



        return view;
    }

    /*
    private void filterList(String text) {
        ArrayList<Exercise> filteredList = new ArrayList<>();
        for(Exercise exercise: exercisesList){
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

     */


    private void initializeRecyclerView() {
        exercisesAdapter = new ExercisesAdapter(getActivity(), exercisesList);

        reference = FirebaseDatabase.getInstance().getReference("Exercises");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);

                    exercisesList.add(exercise);
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
    /* ez lenne a keresés, ha jó lenne az action/toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //MenuInflater inflater1 = getActivity().getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                exercisesAdapter.getFilter().filter(s);

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
     */
}
