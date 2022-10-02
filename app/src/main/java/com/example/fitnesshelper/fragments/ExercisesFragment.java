package com.example.fitnesshelper.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.models.Altgyakorlatmodell;
import com.example.fitnesshelper.DBHelper;
import com.example.fitnesshelper.ExercisesActivity;
import com.example.fitnesshelper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExercisesFragment extends Fragment implements Serializable {
    ListView exercises_lv;
    ArrayAdapter exercisesArrayAdapter;
    List<Altgyakorlatmodell> altalanoslista;
    FloatingActionButton addBtn;
    Button saveBtn,cancelBtn;
    AlertDialog dialog;

    DBHelper dbHelper;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == getActivity().RESULT_OK) {
                altalanoslista.clear();
                altalanoslista = dbHelper.getEverything();
                exercisesArrayAdapter = new ArrayAdapter<Altgyakorlatmodell>(getActivity(), android.R.layout.simple_list_item_1,dbHelper.getEverything());
                exercises_lv.setAdapter(exercisesArrayAdapter);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container,false);


        String [] szoveg = {"alma","banan","cseresznye"};
        addBtn = view.findViewById(R.id.fragment_exercises_addbtn);

        exercises_lv = view.findViewById(R.id.exercises_listview);
        dbHelper = new DBHelper(getActivity());
        altalanoslista = new ArrayList<>();
        altalanoslista = dbHelper.getEverything();

        exercisesArrayAdapter = new ArrayAdapter<Altgyakorlatmodell>(getActivity(),
                android.R.layout.simple_list_item_1,dbHelper.getEverything());
        exercises_lv.setAdapter(exercisesArrayAdapter);

        exercises_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ExercisesActivity.class);
                intent.putExtra("gyakorlat", altalanoslista.get(i).getGyakorlat_neve());
                //int Code = 1;
                startActivity(intent);
                //startActivityForResult(intent,Code);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Gyakorlat hozzáadása");



        View v = getLayoutInflater().inflate(R.layout.layout_add_exercise_dialog,null);
        EditText eName;
        eName = v.findViewById(R.id.exercise_dialog_Et);
        saveBtn = v.findViewById(R.id.saveBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);

        builder.setView(v);
        dialog = builder.create();



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exec_name = eName.getText().toString();
                Altgyakorlatmodell altgyakorlatmodell = new Altgyakorlatmodell(2,exec_name,
                        10, 1);

                boolean success = dbHelper.addToExercises(altgyakorlatmodell);
                if (success == true){
                    Toast.makeText(getActivity(), "Gyakorlat hozzáadva!", Toast.LENGTH_SHORT).show();
                    eName.setText("");
                    altalanoslista.clear();
                    altalanoslista = dbHelper.getEverything();
                    exercisesArrayAdapter = new ArrayAdapter<Altgyakorlatmodell>(getActivity(),
                            android.R.layout.simple_list_item_1,dbHelper.getEverything());
                    exercises_lv.setAdapter(exercisesArrayAdapter);
                }else{
                    Toast.makeText(getActivity(), "Nem sikerült hozzáadni!", Toast.LENGTH_SHORT).show();
                }
                eName.setText("");
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                //openDialog();
            }
        });



        return view;
    }



    private void openDialog() {
        ExerciseDialog exerciseDialog = new ExerciseDialog();
        exerciseDialog.show(getFragmentManager(),"example dialog");
    }

}
