package com.example.fitnesshelper.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fitnesshelper.DetailsActivity;
import com.example.fitnesshelper.R;

import javax.xml.transform.Result;

public class SettingsFragment extends Fragment {

    private static final int ACTION_REQUEST_GALLERY = 111;
    private static final int ACTION_REQUEST_CAMERA = 112;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button photoButton = (Button) view.findViewById(R.id.picturesBtn);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });
        /*
        imageView = (ImageView) view.findViewById(R.id.imageView1);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                        Manifest.permission.CAMERA
            },
                    100);
        }

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

         */


        return view;
    }
    /*

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
        }else{
            Toast.makeText(getActivity(), "Nem készült kép", Toast.LENGTH_SHORT).show();
        }
    }
     */
    /*
    public void showDiloag(){
        Dialog dialog = new Dialog(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[] { "Gallery", "Camera" },
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");

                                Intent chooser = Intent
                                        .createChooser(
                                                intent,
                                                "Choose a Picture");
                                startActivityForResult(
                                        chooser,
                                        ACTION_REQUEST_GALLERY);

                                break;

                            case 1:
                                Intent cameraIntent = new Intent(
                                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(
                                        cameraIntent,
                                        ACTION_REQUEST_CAMERA);

                                break;

                            default:
                                break;
                        }
                    }
                });

        builder.show();
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("OnActivityResult");
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Utils.ACTION_REQUEST_GALLERY) {
                // System.out.println("select file from gallery ");
                Uri selectedImageUri = data.getData();
                String tempPath = JuiceAppUtility.getPath(
                        selectedImageUri, getActivity());

                Bitmap bm = JuiceAppUtility
                        .decodeFileFromPath(tempPath);
                imageView.setImageBitmap(bm);
            } else if (requestCode == Utils.ACTION_REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras()
                        .get("data");
                imageView.setImageBitmap(photo);
            }
        }
    }
     */
}


