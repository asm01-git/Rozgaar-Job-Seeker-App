package com.asm.rozgaar.Fragments.RegisterFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.asm.rozgaar.CustomClasses.JobSeeker;
import com.asm.rozgaar.CustomClasses.LocationDatabase;
import com.asm.rozgaar.CustomClasses.State;
import com.asm.rozgaar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SeekerDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    TextInputEditText name;
    Spinner domain_spinner,state_spinner,city_spinner,area_spinner;
    Button register;
    RelativeLayout container;
    ProgressBar pb;
    Context context;
    String selectedDomain,selectedState,selectedCity,selectedArea,phone;
    static String gender;
    String TAG="tag";
    SuccessListener listener;
    HashMap<String, HashMap<String, String[]>> states;
    String[] state_names;
    HashMap<String,String[]> cities;
    String LOCATION_PREF="location_preference";
    //Constructor to initialise phone number
    public SeekerDetailsFragment(String phone){
        this.phone=phone;
    }

    public interface SuccessListener{
        void isSuccessful(boolean flag);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_seeker_details,container,false);
        name=v.findViewById(R.id.name_field);
        domain_spinner=v.findViewById(R.id.domain_spinner);
        state_spinner=v.findViewById(R.id.state_spinner);
        city_spinner=v.findViewById(R.id.city_spinner);
        area_spinner=v.findViewById(R.id.area_spinner);
        register=v.findViewById(R.id.register_button);
        this.container=v.findViewById(R.id.seeker_details_root);
        pb=v.findViewById(R.id.register_progressbar);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        if(context instanceof SuccessListener)
            listener=(SuccessListener)context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Enter details and Register");
        states=LocationDatabase.getStates_list(getActivity());
        state_names= LocationDatabase.getState_names(getActivity());
        ArrayAdapter domain_adapter=ArrayAdapter.createFromResource(context,R.array.domains,R.layout.support_simple_spinner_dropdown_item);
        domain_spinner.setAdapter(domain_adapter);
        domain_spinner.setSelection(0,true);
        domain_spinner.setOnItemSelectedListener(this);
        ArrayAdapter state_adapter=new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,state_names);
        state_spinner.setAdapter(state_adapter);
        state_spinner.setOnItemSelectedListener(this);
        city_spinner.setEnabled(false);
        area_spinner.setEnabled(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The screen is blanked and progress bar is displayed while loading
                container.setAlpha(0.1f);
                pb.setVisibility(View.VISIBLE);

                String entered_name=name.getText().toString();
                try{
                if(name.getText()==null||selectedDomain==null||gender==null)
                    Toast.makeText(context,"All fields are compulsory!",Toast.LENGTH_SHORT).show();
                else{
                    JobSeeker js=new JobSeeker(entered_name,selectedDomain,phone,gender,null,null,new ArrayList<String>(),new ArrayList<String>(),false,false,false);
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        String uid=user.getUid();
                        FirebaseFirestore db=FirebaseFirestore.getInstance();
                        db.collection("Job Seekers")
                                .document(uid)
                                .set(js)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"User added successfully!");
                                        Toast.makeText(context,"Registration successful!",Toast.LENGTH_SHORT).show();
                                        //Store the selected state,area and city in shared preferences
                                        setLOCATION_PREF();
                                        //Tell register activity that action was successful
                                        listener.isSuccessful(true);
                                        //End this fragment
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .remove(SeekerDetailsFragment.this)
                                                .commit();
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG,"Registration failed!",e);
                                        Toast.makeText(context,"Registration failed!",Toast.LENGTH_SHORT).show();
                                        listener.isSuccessful(false);
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .remove(SeekerDetailsFragment.this)
                                                .commit();
                                    }
                                });
                    }
                }
            }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection=parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.domain_spinner:
            if (selection.equals("--Select Domain--")) {
                selectedDomain = null;
            }
            else {
                selectedDomain = selection;
            }
                break;
            case R.id.state_spinner:
                if(selection.equals("--Select State of Residence--"))
                    selectedState=null;
                else {
                    selectedState = selection;
                    setUpCitySpinner();
                }
                area_spinner.setEnabled(false);
                break;
            case R.id.city_spinner:
                if(selection.equals("--Select City--")) {
                    selectedCity = null;
                    break;
                }
                selectedCity=parent.getItemAtPosition(position).toString();
                setUpAreaSpinner();
                break;
            case R.id.area_spinner:
                if(selection.equals("--Select Area--")){
                    selectedArea=null;
                    break;
                }
                selectedArea=parent.getItemAtPosition(position).toString();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedDomain=null;
        selectedArea=null;
        selectedCity=null;
        selectedState=null;
        parent.setSelection(0);
    }
    private void setUpCitySpinner(){
        cities=states.get(selectedState);
        if(cities!=null) {
            city_spinner.setEnabled(true);
            Object[] keys = cities.keySet().toArray();
            city_spinner.setAdapter(new ArrayAdapter<String>(context,
                    R.layout.support_simple_spinner_dropdown_item,
                    Arrays.copyOf(keys, keys.length, String[].class)));
            city_spinner.setSelection(0);
            city_spinner.setOnItemSelectedListener(this);
        }
    }
    private void setUpAreaSpinner(){
        String[] areas=cities.get(selectedCity);
        if(areas!=null) {
            area_spinner.setEnabled(true);
            area_spinner.setAdapter(new ArrayAdapter<String>(context,
                    R.layout.support_simple_spinner_dropdown_item,
                    areas));
            area_spinner.setSelection(0);
            area_spinner.setOnItemSelectedListener(this);
        }
    }
    public static void onClick(View view){
        switch (view.getId()){
            case R.id.male_radio:
                gender="Male";
                break;
            case R.id.female_radio:
                gender="Female";
                break;
        }
    }
    private void setLOCATION_PREF(){
        SharedPreferences sp=context.getSharedPreferences(LOCATION_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("state",selectedState)
                .putString("city",selectedCity)
                .putString("area",selectedArea)
                .putString("job_type",selectedDomain)
                .apply();
    }
}
