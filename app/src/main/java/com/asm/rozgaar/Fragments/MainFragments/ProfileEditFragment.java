package com.asm.rozgaar.Fragments.MainFragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asm.rozgaar.CustomClasses.LanguagesInterestsAdapter;
import com.asm.rozgaar.CustomClasses.JobSeeker;
import com.asm.rozgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEditFragment extends Fragment implements View.OnClickListener,
             DatePickerDialog.OnDateSetListener
{
    JobSeeker prof_details;
    ArrayList<String> known_langs;
    ArrayList<String> selected_interests;
    String[] languages_list,interests_list;
    Toolbar toolbar;
    RelativeLayout inputs;
    TextInputEditText namefield,phonefield,genderfield,addressfield,dobfield;
    Button cancel,save;
    ImageButton aadhar_upload,license_upload,pan_upload;
    ProgressBar pb;
    TextView selected_interests_view,aadharUri,licenseUri,panUri,known_langs_display;
    private Context context;
    String DATE_PREF_KEY="pref_date",URI_PREF_KEY="stored_image_uri";
    int AADHAR_RC=101,LICENSE_RC=111,PAN_RC=121;
    FirebaseUser user;
    RecyclerView interests_rv,lang_rv;
    LanguagesInterestsAdapter adapter;

    public ProfileEditFragment(JobSeeker prof_details) {
        this.prof_details = prof_details;
    }

    public ProfileEditFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View v=inflater.inflate(R.layout.fragment_profile_edit, container, false);
        toolbar=v.findViewById(R.id.profile_edit_toolbar);
        namefield=v.findViewById(R.id.name_field);
        phonefield=v.findViewById(R.id.phone_field);
        genderfield=v.findViewById(R.id.gender_field);
        addressfield=v.findViewById(R.id.address_field);
        dobfield=v.findViewById(R.id.dob_field);
        cancel=v.findViewById(R.id.cancel_button);
        save=v.findViewById(R.id.save_button);
        inputs=v.findViewById(R.id.input_layout);
        pb=v.findViewById(R.id.profile_edit_pb);
        known_langs_display=v.findViewById(R.id.select_languages_view);
        aadhar_upload=v.findViewById(R.id.aadhar_upload_button);
        aadharUri=v.findViewById(R.id.aadhar_uri);
        licenseUri=v.findViewById(R.id.license_uri);
        panUri=v.findViewById(R.id.pan_uri);
        license_upload=v.findViewById(R.id.license_upload_button);
        pan_upload=v.findViewById(R.id.pan_upload_button);
        selected_interests_view=v.findViewById(R.id.selected_interests_view);
        interests_rv=v.findViewById(R.id.recycler_view_interests);
        lang_rv=v.findViewById(R.id.recycler_view_languages);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Setting current user object
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Getting list of languages
        languages_list=getResources().getStringArray(R.array.languages);
        interests_list=getResources().getStringArray(R.array.interests);
        //Get uri from preferences
        SharedPreferences sp=context.getSharedPreferences(URI_PREF_KEY,Context.MODE_PRIVATE);
        if(sp.getString("aadhar",null)!=null)
             aadharUri.setText(sp.getString("aadhar","No file uploaded!"));

        if(sp.getString("license",null)!=null)
            licenseUri.setText(sp.getString("license","No file uploaded!"));

        if(sp.getString("pan",null)!=null)
            panUri.setText(sp.getString("pan","No file uploaded!"));

        //Now we need to check the earlier list of languages and mark the items in main list
        // that are already present in the earlier list
        //Store the array which has true on items common to both lists
        known_langs=prof_details.getKnown_langs();
        if(known_langs==null||known_langs.isEmpty())
            known_langs=new ArrayList<>();
        setUpRecyclerView(known_langs,lang_rv);

        selected_interests=prof_details.getInterests();
        if(selected_interests==null)
            selected_interests=new ArrayList<>();
        setUpRecyclerView(selected_interests,interests_rv);

        //Setting fields from known details
        namefield.setText(prof_details.getName());
        phonefield.setText("Contact - "+prof_details.getPhone());
        genderfield.setText(prof_details.getGender());
        addressfield.setText(prof_details.getAddress());
        dobfield.setText(prof_details.getDob());
        //Setting date of birth picker
        dobfield.setOnClickListener(this);
        //Setting up language and interests multichoice list
        known_langs_display.setOnClickListener(this);
        selected_interests_view.setOnClickListener(this);
        //Setting click listeners for cancel and save
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        //Setting click listener for all upload buttons
        aadhar_upload.setOnClickListener(this);
        license_upload.setOnClickListener(this);
        pan_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_button:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container_home_page,new ProfilePageFragment())
                        .commit();
                break;
            case R.id.save_button:
                startProgressBar();
                prof_details.setAddress(addressfield.getText().toString());
                prof_details.setDob(dobfield.getText().toString());
                prof_details.setKnown_langs(known_langs);
                prof_details.setInterests(selected_interests);
                updateDB();
                break;
            case R.id.select_languages_view:
                setMultiChoiceDialog(languages_list,getChecked_items(languages_list,known_langs),
                        known_langs,lang_rv);
                break;

            case R.id.selected_interests_view:
                interests_list=getResources().getStringArray(R.array.interests);
                setMultiChoiceDialog(interests_list,getChecked_items(interests_list,selected_interests),
                        selected_interests,interests_rv);
                break;

            case R.id.dob_field:
                Calendar currentdate=Calendar.getInstance();
                SharedPreferences sp=context.getSharedPreferences(DATE_PREF_KEY,Context.MODE_PRIVATE);
                if(sp.getInt("day",0)!=0)
                    currentdate.set(sp.getInt("year",2020),
                            sp.getInt("month",1),sp.getInt("day",1));

                final DatePickerDialog dobpicker=new DatePickerDialog(context,ProfileEditFragment.this,
                        currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH)-1,currentdate.get(Calendar.DAY_OF_MONTH));
                dobpicker.show();
                break;
            case R.id.aadhar_upload_button:
                Intent galleryIntent1=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent1,AADHAR_RC);
                break;
            case R.id.license_upload_button:
                Intent galleryIntent2=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent2,LICENSE_RC);
                break;
            case R.id.pan_upload_button:
                Intent galleryIntent3=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent3,PAN_RC);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startProgressBar();
        if(requestCode==AADHAR_RC){
            if(resultCode==RESULT_OK&&data!=null){
                Uri aadharUri=data.getData();
                uploadImage(aadharUri,"/aadhar.jpg");
            }
            else {
                endProgressBar();
                Toast.makeText(context, "Image could not be uploaded.Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==LICENSE_RC){
            if(resultCode==RESULT_OK&&data!=null){
                Uri licenseUri=data.getData();
                uploadImage(licenseUri,"/license.jpg");
            }
            else {
                Toast.makeText(context, "Image could not be uploaded.Please try again!", Toast.LENGTH_SHORT).show();
                endProgressBar();
            }
        }
        else if(requestCode==PAN_RC){
            if(resultCode==RESULT_OK&&data!=null){
                Uri panUri=data.getData();
                uploadImage(panUri,"/PAN.jpg");
            }
            else {
                Toast.makeText(context, "Image could not be uploaded.Please try again!", Toast.LENGTH_SHORT).show();
                endProgressBar();
            }
        }
        else{
            Toast.makeText(context,"Request could not be sent!Please try again",Toast.LENGTH_SHORT).show();
            endProgressBar();
        }
    }
    private void startProgressBar(){
        pb.setVisibility(View.VISIBLE);
        inputs.setAlpha(0.1f);
        save.setAlpha(0.6f);
    }
    private void endProgressBar(){
        pb.setVisibility(View.GONE);
        inputs.setAlpha(1f);
        save.setAlpha(1f);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dobfield.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        context.getSharedPreferences(DATE_PREF_KEY,Context.MODE_PRIVATE).edit()
                .putInt("day",dayOfMonth)
                .putInt("month",month)
                .putInt("year",year)
                .apply();
    }
    private void uploadImage(final Uri uri, final String imagename) {
        StorageReference sf = FirebaseStorage.getInstance().getReference();
        sf.child(user.getUid() + imagename)
                .putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                            DocumentReference docref=FirebaseFirestore.getInstance()
                                    .collection("Job Seekers")
                                    .document(user.getUid());
                            SharedPreferences.Editor editor=context.getSharedPreferences(URI_PREF_KEY,Context.MODE_PRIVATE).edit();
                            Task updateTask=null;
                            if(imagename.equals("/aadhar.jpg")) {
                                updateTask = docref.update("aadharUploaded", true);
                                prof_details.setAadharUploaded(true);
                                editor.putString("aadhar",uri.toString()).apply();
                                aadharUri.setText(uri.toString());
                            }
                            else if(imagename.equals("/license.jpg")) {
                                updateTask = docref.update("licenseUploaded", true);
                                prof_details.setLicenseUploaded(true);
                                editor.putString("license",uri.toString()).apply();
                                licenseUri.setText(uri.toString());
                            }
                            else if(imagename.equals("/PAN.jpg")) {
                                updateTask = docref.update("panUploaded", true);
                                prof_details.setPanUploaded(true);
                                editor.putString("pan",uri.toString()).apply();
                                panUri.setText(uri.toString());
                            }
                            updateTask.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(getContext(), "Database updated!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    endProgressBar();
                                }
                            });
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                            endProgressBar();
                        }
                    }
                });
    }
    private void updateDB(){
        FirebaseFirestore.getInstance()
                .collection("Job Seekers")
                .document(user.getUid())
                .set(prof_details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        endProgressBar();
                        if (task.isSuccessful()) {
                            Toast.makeText(context,"Profile updated succesfully!",Toast.LENGTH_SHORT).show();
                            ProfilePageFragment ppf=new ProfilePageFragment();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.root_container_home_page,ppf)
                                    .commit();
                        }
                        else
                            Toast.makeText(context,"Failed to update profile",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setMultiChoiceDialog(final String[] list, final boolean[] checked_items,
                                      final ArrayList<String> selected_items_list,
                                      final RecyclerView rv) {
        new AlertDialog.Builder(context)
                .setMultiChoiceItems(list, checked_items,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    checked_items[which]=true;
                                    selected_items_list.add(list[which]);
                                }
                                else{
                                    checked_items[which]=false;
                                    selected_items_list.remove(list[which]);
                                }
                            }
                        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        rv.getAdapter().notifyDataSetChanged();
                    }
                }).create().show();
    }
    private boolean[] getChecked_items(String[] list,ArrayList<String> selected_items_list){
        boolean[] checked_items=new boolean[list.length];
        for (int i = 0; i < list.length; i++) {
            if (selected_items_list.contains(list[i]))
                checked_items[i] = true;
        }
        return checked_items;
    }
    private String display_selected_items(ArrayList<String> selected_items_list) {
    String s = "";
    for (String item : selected_items_list)
        s += item + ",";
    return s;
}
    private void setUpRecyclerView(ArrayList<String> dataset,RecyclerView rv){
        adapter=new LanguagesInterestsAdapter(dataset,rv);
        LanguagesInterestsAdapter adapter1=adapter;
        adapter.getAdapter(adapter1);
        rv.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,false));
        rv.setAdapter(adapter);
    }
}
