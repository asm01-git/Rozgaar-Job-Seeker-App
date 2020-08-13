package com.asm.rozgaar.Fragments.MainFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asm.rozgaar.CustomClasses.JobSeeker;
import com.asm.rozgaar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePageFragment extends Fragment {
    private static final String TAG ="Error" ;
    Context mContext;
    View v;
    BottomNavigationView nav_bar;
    ProgressBar pb;
    RelativeLayout main_templates;
    TextView nameview,genderview,phoneview,dobview,addressview;
    Button edit;
    TextView languages_displayed;
    TextView interests_heading;
    TextView interests_view;
    JobSeeker js;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile_page, container, false);
        nav_bar=v.findViewById(R.id.bottom_nav_bar);
        nameview=v.findViewById(R.id.name_displayed);
        genderview=v.findViewById(R.id.gender_displayed);
        phoneview=v.findViewById(R.id.phone_displayed);
        addressview=v.findViewById(R.id.address_displayed);
        dobview=v.findViewById(R.id.dob_displayed);
        interests_view=v.findViewById(R.id.interests_displayed );
        languages_displayed=v.findViewById(R.id.languages_list_displayed);
        interests_heading=v.findViewById(R.id.interests_text);
        pb=v.findViewById(R.id.profile_page_progress);
        main_templates=v.findViewById(R.id.template_wrapper);
        edit=v.findViewById(R.id.edit_button);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        getActivity().setTitle("Profile");
        //Show progress bar
        pb.setVisibility(View.VISIBLE);
        main_templates.setAlpha(0.1f);
        //Querying of data starts
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            queryInfo(user);
        }
        else
            Toast.makeText(mContext,"Something happened.Please log out and sign in again",Toast.LENGTH_LONG).show();
        edit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditFragment pef=new ProfileEditFragment(js);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container_home_page,pef)
                        .commit();
            }
        });
    }
    private void queryInfo(FirebaseUser user){
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Job Seekers")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        js=documentSnapshot.toObject(JobSeeker.class);
                                        nameview.setText(js.getName());
                                        genderview.setText("Gender - "+js.getGender());
                                        phoneview.setText(js.getPhone());
                                        if(js.getDob()!=null)
                                            dobview.setText(js.getDob());
                                        if(js.getAddress()!=null)
                                            addressview.setText("Address - "+js.getAddress());
                                        //Setting up languages view
                                        if(js.getKnown_langs()!=null){
                                            languages_displayed.setText("Known Languages - "+display_selected_items(js.getKnown_langs()));
                                        }
                                        if(js.getInterests()!=null)
                                            interests_view.setText(display_selected_items(js.getInterests()));

                                        setUploaded(js.isAadharUploaded(),(ImageView) v.findViewById(R.id.aadhar_tickmark));
                                        setUploaded(js.isLicenseUploaded(),(ImageView)v.findViewById(R.id.license_tickmark));
                                        setUploaded(js.isPanUploaded(),(ImageView)v.findViewById(R.id.pan_tickmark));
                                        pb.setVisibility(View.GONE);
                                        main_templates.setAlpha(1f);
                                    } else
                                        Toast.makeText(mContext, "Sorry, your data could not be retrieved.Please try later.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pb.setVisibility(View.GONE);
                                    main_templates.setAlpha(1f);
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, e.getMessage());
                                }
                            });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            private void setUploaded(boolean isImageUploaded,ImageView tick_mark){
                if(isImageUploaded) {
                    tick_mark.setImageResource(R.drawable.ic_done_all_black_24dp);
                    tick_mark.setBackground(null);
                }
            }
    private String display_selected_items(ArrayList<String> selected_items_list) {
        String s = "";
        for (String item : selected_items_list)
            s += item + ",";
        return s;
    }
    }

