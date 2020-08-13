package com.asm.rozgaar.Fragments.MainFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asm.rozgaar.Activities.MainActivity;
import com.asm.rozgaar.CustomClasses.AvailableJob;
import com.asm.rozgaar.CustomClasses.HomeScreenAdapter;
import com.asm.rozgaar.CustomClasses.LocationDatabase;
import com.asm.rozgaar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFragment extends Fragment implements Button.OnClickListener,ListView.OnItemClickListener{
    String LOCATION_PREF="location_preference";
    Context context;
    Button state_dropdown,city_dropdown,area_dropdown,job_dropdown;
    RecyclerView jobs_recycler_view;
    RelativeLayout home_page,list_layout;
    BottomNavigationView bnv;
    ListView dropdown_list;
    ImageButton cross_button;
    char list_type;
    String[] state_names;
    String selected_state,selected_city,selected_job,selected_area;
    HashMap<String,HashMap<String,String[]>> states;
    ArrayList<AvailableJob> jobs_list=new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_screen, container, false);
        state_dropdown=v.findViewById(R.id.state_dropdown);
        city_dropdown=v.findViewById(R.id.city_dropdown);
        area_dropdown=v.findViewById(R.id.area_dropdown);
        job_dropdown=v.findViewById(R.id.job_dropdown);
        home_page=v.findViewById(R.id.home_page);
        list_layout=v.findViewById(R.id.list_layout);
        dropdown_list=v.findViewById(R.id.dropdown_list);
        cross_button=v.findViewById(R.id.cross_button);
        bnv=v.findViewById(R.id.bottom_nav_bar);
        jobs_recycler_view=v.findViewById(R.id.jobs_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        //Getting last selected location or the one selected in registration page
        getLOCATION_PREF();
        //Set up bottom nav bar
        if(selected_state!=null)
            state_dropdown.setText(selected_state);
        if(selected_city!=null)
            city_dropdown.setText(selected_city);
        if(selected_area!=null)
            area_dropdown.setText(selected_area);
        job_dropdown.setText(selected_job);

        //Get the names of states,cities and areas
        state_names=LocationDatabase.getState_names(getActivity());
        states=LocationDatabase.getStates_list(getActivity());

        state_dropdown.setOnClickListener(this);
        city_dropdown.setOnClickListener(this);
        job_dropdown.setOnClickListener(this);
        area_dropdown.setOnClickListener(this);
        /*Query the available jobs
         *Based on above criteria
         * of state,city and job type
         * Store them in a list
         */
        jobs_list.add(new AvailableJob("Zomato delivery job","Lingampally","5000-7000",null));
        jobs_list.add(new AvailableJob("Swiggy","Gachibowli","7000-8000",null));
        //Now,set up the recycler view for available jobs
        HomeScreenAdapter hsa=new HomeScreenAdapter(jobs_list,context);
        jobs_recycler_view.setAdapter(hsa);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.state_dropdown:
                list_type='S';
                displayList(state_names);
                break;
            case R.id.city_dropdown:
                list_type='C';
                if(states.get(selected_state)!=null) {
                    Object[] keys = states.get(selected_state).keySet().toArray();
                    displayList(Arrays.copyOf(keys, keys.length, String[].class));
                }
                else
                    makeToast("Sorry the app is not available in this state!");
                break;
            case R.id.area_dropdown:
                list_type='A';
                if(states.get(selected_state)!=null)
                    displayList(states.get(selected_state).get(selected_city));
                break;
            case R.id.job_dropdown:
                list_type='J';
                displayList(getResources().getStringArray(R.array.domains));
                break;
        }
    }
    private void displayList(String[] s){
        home_page.setVisibility(View.GONE);
        list_layout.setVisibility(View.VISIBLE);
        if(s!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, s);
            dropdown_list.setAdapter(adapter);
            dropdown_list.setOnItemClickListener(this);
        }
        else
            makeToast("Sorry this app is not available in this state!");
        cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listToHomePage();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selection=parent.getItemAtPosition(position).toString();
        SharedPreferences.Editor editor=context.getSharedPreferences(LOCATION_PREF,Context.MODE_PRIVATE).edit();
        if(list_type=='S'){
            if(selection.equals("--Select State--")) {
                selected_state = null;
                state_dropdown.setText("State");
            }
            else{
                selected_state=selection;
                state_dropdown.setText(selected_state);
            }
            selected_city=null;
            selected_area=null;
            city_dropdown.setText("City");
            area_dropdown.setText("Area");
            editor.putString("state",selected_state).apply();
        }
        else if(list_type=='C'){
            if(selection.equals("--Select City--")) {
                selected_city = null;
                city_dropdown.setText("City");
            }
            else{
                selected_city=selection;
                city_dropdown.setText(selected_city);
            }
            selected_area=null;
            area_dropdown.setText("Area");
            editor.putString("city",selected_city).apply();
        }
        else if(list_type=='J'){
            if(selection.equals("--Select Domain--")) {
                selected_job = null;
                job_dropdown.setText("Domain");
            }
            else {
                selected_job = selection;
                job_dropdown.setText(selected_job);
                editor.putString("job_type", selected_job).apply();
            }
        }
        else if(list_type=='A'){
            if(selection.equals("--Select City--")) {
                selected_area = null;
                area_dropdown.setText("Area");
            }
            else{
                selected_area=selection;
                area_dropdown.setText(selected_area);
            }
            editor.putString("area",selected_area).apply();
        }
        listToHomePage();
    }
    private void getLOCATION_PREF(){
        SharedPreferences sp=context.getSharedPreferences(LOCATION_PREF, Context.MODE_PRIVATE);
        selected_state=sp.getString("state",null);
        selected_city=sp.getString("city",null);
        selected_area=sp.getString("area",null);
        selected_job=sp.getString("job_type",null);
    }
    private void listToHomePage(){
        list_layout.setVisibility(View.GONE);
        home_page.setVisibility(View.VISIBLE);
    }
    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
