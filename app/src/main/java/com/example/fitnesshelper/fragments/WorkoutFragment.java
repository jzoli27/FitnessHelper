package com.example.fitnesshelper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.CreateWorkoutTemplate;
import com.example.fitnesshelper.DetailsActivity;
import com.example.fitnesshelper.EditWorkoutTemplateActivity;
import com.example.fitnesshelper.LoginActivity;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.RegisterActivity;
import com.example.fitnesshelper.SelectableExercisesActivity;
import com.example.fitnesshelper.adapters.EditWorkoutTemplateAdapter;
import com.example.fitnesshelper.adapters.WorkoutTemplateAdapter;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.Exercise;
import com.example.fitnesshelper.models.WorkoutDetails;
import com.example.fitnesshelper.models.WorkoutTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment implements RecyclerViewInterface {
    private Button newTemplateBtn;
    private RecyclerView recyclerView;
    WorkoutTemplateAdapter workoutTemplateAdapter;
    TextView templateTitle;

    ArrayList<String> workoutTemplates;
    ArrayList<String> wtKeys;
    ArrayList<WorkoutTemplate> wtTemplates;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference userDbReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private ArrayList<WorkoutDetails> hope;

    private String checkFragment;
    private int datePosition;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container,false);

        hope = new ArrayList<>();

        newTemplateBtn = view.findViewById(R.id.newTemplateBtn);
        recyclerView = view.findViewById(R.id.templateRv);
        templateTitle = view.findViewById(R.id.templateTitle);

        workoutTemplates = new ArrayList<>();
        wtKeys = new ArrayList<>();
        wtTemplates = new ArrayList<>();
        checkFragment = "ures";

        if (getArguments() != null){
            checkFragment = getArguments().getString("Key");
            datePosition = getArguments().getInt("datepos");
        }

        if (checkFragment.equals("reminder")){
            templateTitle.setText("Válassz egy sablont!");
        }


        initializeRecyclerView();

        newTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "checkfragment: " + checkFragment, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), CreateWorkoutTemplate.class));
                //logcat azért, hogy lássuk megkapta-e a db lekérdezésből a lista az elemeket.
                //for (int i = 0; i < hope.size(); i++){
                //    Log.d("Hope:", " nev " + hope.get(i).getName() + " db: " + hope.get(i).getRepetitionnumber() + " gyakorlatnev: " + hope.get(i).getExercisename() + " size " + hope.size());
                //}
            }
        });


        //return inflater.inflate(R.layout.fragment_workout, container, false);
        return view;
    }

    private void initializeData() {
        userDbReference.child("Templates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hope.clear();
                for(DataSnapshot current_data: snapshot.getChildren()){
                    WorkoutTemplate workoutTemplate = current_data.getValue(WorkoutTemplate.class);
                    //sablon név kell a listába
                    String name = workoutTemplate.getName();
                    for(DataSnapshot current_user_data: current_data.getChildren()){

                        for(DataSnapshot for3: current_user_data.getChildren()){
                            Exercise exercise = for3.getValue(Exercise.class);
                            //gyak név kell a listába
                            String excname = exercise.getExerciseName();

                            for(DataSnapshot for4: for3.getChildren()){
                                //repetition count kellene a listába
                                String count = String.valueOf(for4.getChildrenCount());
                                int checksum = Integer.valueOf(count);
                                if (checksum != 0){
                                    WorkoutDetails workoutDetails = new WorkoutDetails(name,count,excname);
                                    hope.add(workoutDetails);
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void initializeRecyclerView() {
        initializeData();

        workoutTemplateAdapter = new WorkoutTemplateAdapter(getActivity(), wtTemplates, this, hope);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(uid).child("Templates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    WorkoutTemplate workoutTemplate = dataSnapshot.getValue(WorkoutTemplate.class);
                    //wtTemplates.add(dataSnapshot.getValue(WorkoutTemplate.class));

                    wtTemplates.add(workoutTemplate);
                    workoutTemplateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(workoutTemplateAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(getActivity(), "Hello position: " + position, Toast.LENGTH_SHORT).show();
        if (checkFragment.equals("reminder")){
            //Toast.makeText(getActivity(), "fragment: " + checkFragment, Toast.LENGTH_SHORT).show();
            ReminderFragment fragment = new ReminderFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle args = new Bundle();
            args.putString("Key", "workout");
            args.putInt("Pos", position);
            args.putInt("datePos", datePosition);

            fragment.setArguments(args);
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }else{
            Intent intent = new Intent(getActivity(), EditWorkoutTemplateActivity.class);
            intent.putExtra("wtKey", wtTemplates.get(position).getWtKey());
            intent.putExtra("name",wtTemplates.get(position).getName());
            startActivity(intent);
        }
    }




}
