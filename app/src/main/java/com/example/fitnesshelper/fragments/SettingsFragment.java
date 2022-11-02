package com.example.fitnesshelper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.DetailsActivity;
import com.example.fitnesshelper.FitnessMachineDetails;
import com.example.fitnesshelper.R;
import com.example.fitnesshelper.adapters.FitnessMachineAdapter;
import com.example.fitnesshelper.interfaces.RecyclerViewInterface;
import com.example.fitnesshelper.models.FitnessMachine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment implements RecyclerViewInterface {

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

    private ArrayList<String> Settingkey;
    private ArrayList<String> Settingvalue;
    private HashMap<String,String> hashmap;

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

        photoButton = (Button) view.findViewById(R.id.picturesBtn);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
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
                    fitnessMachineAdapter.notifyDataSetChanged();
                }
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
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), FitnessMachineDetails.class);
        intent.putExtra("name",Fmachines.get(position).getMachineName());
        intent.putExtra("fmKey",Fmachines.get(position).getFmKey());
        intent.putExtra("imgLink",Fmachines.get(position).getImgLink());
        startActivity(intent);
        //Toast.makeText(getActivity(), "Clicked at position: " + position, Toast.LENGTH_SHORT).show();
        //initializeData(position);
        /*
        if (!hashmap.isEmpty()){
            for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                //String key = entry.getKey();
                //Object value = entry.getValue();
                Settingkey.add(entry.getKey());
                Settingvalue.add(entry.getValue());
            }
            if (!Settingvalue.isEmpty() && !Settingkey.isEmpty()){
                Intent intent = new Intent(getActivity(), FitnessMachineDetails.class);
                intent.putExtra("keys", Settingkey);
                intent.putExtra("values", Settingvalue);
                intent.putExtra("name",Fmachines.get(position).getMachineName());
                intent.putExtra("fmKey",Fmachines.get(position).getFmKey());
                intent.putExtra("imgLink",Fmachines.get(position).getImgLink());
                startActivity(intent);
            }
        }

         */

    }
}


