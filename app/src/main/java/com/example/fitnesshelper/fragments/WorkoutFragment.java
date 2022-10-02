package com.example.fitnesshelper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.CreateWorkoutTemplate;
import com.example.fitnesshelper.DetailsActivity;
import com.example.fitnesshelper.EditWorkoutTemplateActivity;
import com.example.fitnesshelper.LoginActivity;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.RegisterActivity;
import com.example.fitnesshelper.SelectableExercisesActivity;
import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {
    private Button newTemplateBtn;
    private ListView templateLv;

    ArrayList<String> workoutTemplates;
    ArrayList<String> wtKeys;
    ArrayList<WorkoutTemplate> wtTemplates;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container,false);

        newTemplateBtn = view.findViewById(R.id.newTemplateBtn);
        templateLv = view.findViewById(R.id.templateLv);

        workoutTemplates = new ArrayList<>();
        wtKeys = new ArrayList<>();
        wtTemplates = new ArrayList<>();

        initializeListView();

        templateLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Itt kellene átdobni a sablon nevet ill a kulcsot a másik oldalnak/activitynek.

                //Toast.makeText(getActivity(), "You clicked " + workoutTemplates.get(i).toString() +" key: " + wtKeys.get(i).toString()+
                //        " note: "+ wtTemplates.get(i).getNote(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EditWorkoutTemplateActivity.class);
                //intent.putExtra("excname", wtTemplates.get(i).getName());
                intent.putExtra("wtKey", wtTemplates.get(i).getWtKey());
                intent.putExtra("name", wtTemplates.get(i).getName());

                startActivity(intent);
            }
        });
        newTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateWorkoutTemplate.class));
            }
        });

        return view;
    }

    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, workoutTemplates);
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(uid).child("Templates").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                workoutTemplates.add(snapshot.child("name").getValue(String.class));
                wtKeys.add(snapshot.child("wtKey").getValue(String.class));

                //WorkoutTemplate workoutTemplate = new WorkoutTemplate();
                //workoutTemplate = snapshot.getValue(WorkoutTemplate.class);
                wtTemplates.add(snapshot.getValue(WorkoutTemplate.class));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                workoutTemplates.remove(snapshot.child("name").getValue(String.class));
                wtKeys.remove(snapshot.child("wtKey").getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        templateLv.setAdapter(adapter);
    }

}
