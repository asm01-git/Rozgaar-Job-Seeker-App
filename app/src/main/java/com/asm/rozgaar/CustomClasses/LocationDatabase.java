package com.asm.rozgaar.CustomClasses;

import android.app.Activity;


import com.asm.rozgaar.R;

import java.util.HashMap;

public class LocationDatabase {
    static String[] state_names;
    static HashMap<String,HashMap<String,String[]>> states_list;
    Activity activity;

    public LocationDatabase(Activity activity) {
        this.activity=activity;
    }
    public static String[] getState_names(Activity activity) {
        return activity.getResources().getStringArray(R.array.india_states);
    }
    public static HashMap<String,HashMap<String,String[]>> getStates_list(Activity activity){
        state_names=new String[36];
        states_list=new HashMap<>();
        state_names=getState_names(activity);
        HashMap<String,String[]> cities;
        //Telengana
        cities=new HashMap<>();
        cities.put("--Select city--",null);
        cities.put("Hyderabad",new String[]{"--Select area--","Secunderabad","Narsingi","Kukatpet","Sangareddy","Gachibowli"});
        cities.put("Amaravati",new String[]{"--Select area--","Area1","Area2"});
        cities.put("Warangal",new String[]{"--Select area--","Area1","Area2"});
        states_list.put("Telangana",cities);
        //Kerala
        cities=new HashMap<>();
        cities.put("--Select city--",null);
        cities.put("Trivandrum",new String[]{"--Select area--","Kowdiar","Kovalam","Attingal","Nedumangad","Pattom"});
        cities.put("Ernakulam",new String[]{"--Select area--","Vytilla","Aluva","Kadavanthra","Edapally"});
        cities.put("Thrissur",new String[]{"--Select area--","Ponnani","Shoranur"});
        states_list.put("Kerala",cities);
        return states_list;
    }
}
