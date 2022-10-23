package com.example.fitnesshelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesshelper.R;
import com.example.fitnesshelper.models.Repetition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RepetitionAdapter extends RecyclerView.Adapter<RepetitionAdapter.MyViewHolder> {

    ArrayList<Repetition> reps;
    Context context;
    String uid, wtKey, exckey;
    DatabaseReference reference;

    public RepetitionAdapter(Context ct, ArrayList<Repetition> reps, String uid, String wtKey, String exckey){
        context = ct;
        this.reps = reps;
        this.uid = uid;
        this.wtKey = wtKey;
        this.exckey = exckey;

    }

    @NonNull
    @Override
    public RepetitionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repetition_item,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepetitionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if (reps.get(position).getState().equals("0")){
            holder.repetitionSerieEt.setEnabled(true);
            holder.repetitionWeightEt.setEnabled(true);
            holder.repetitionEt.setEnabled(true);
        }else {
            holder.repetitionSerieEt.setEnabled(false);
            holder.repetitionWeightEt.setEnabled(false);
            holder.repetitionEt.setEnabled(false);
        }

        holder.repetitionSerieEt.setText(reps.get(position).getSeries());
        holder.repetitionSerieEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reps.get(position).setSeries(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.repetitionWeightEt.setText(reps.get(position).getWeight());
        holder.repetitionWeightEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reps.get(position).setWeight(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.repetitionEt.setText(reps.get(position).getNumberOfRepetitions());
        holder.repetitionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reps.get(position).setNumberOfRepetitions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return reps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        EditText repetitionSerieEt, repetitionWeightEt, repetitionEt;
        ImageView repetitionOptionsIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            repetitionSerieEt = itemView.findViewById(R.id.repetitionSerieEt);
            repetitionWeightEt = itemView.findViewById(R.id.repetitionWeightEt);
            repetitionEt = itemView.findViewById(R.id.repetitionEt);

            repetitionOptionsIv = itemView.findViewById(R.id.repetitionOptionsIv);
            repetitionOptionsIv.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context, "position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_edit:
                    Toast.makeText(context, "action_popup_edit at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    //repetitionSerieEt.setEnabled(false);
                    updateSerie(getAdapterPosition());
                    return true;
                case R.id.action_popup_delete:
                    //Toast.makeText(context, "action_popup_delete at position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    deleteSerie(getAdapterPosition());
                    removeAt(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }

    public void updateSerie(int position ){
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(uid).child("Templates").child(wtKey).child("Exercises")
                .child(exckey).child("Repetition").child(reps.get(position).getRepetitionKey()).setValue(reps.get(position))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Jó", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Nem jó", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void removeAt(int position) {
        reps.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reps.size());
    }

    public void deleteSerie(int position ){
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(uid).child("Templates").child(wtKey).child("Exercises")
                .child(exckey).child("Repetition").child(reps.get(position).getRepetitionKey()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Törölve!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Hiba", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
