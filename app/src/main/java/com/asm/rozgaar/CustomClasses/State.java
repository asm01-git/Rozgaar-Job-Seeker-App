package com.asm.rozgaar.CustomClasses;

import java.util.HashMap;

public class State {
    String name;
    HashMap<String,String[]> cities;

    public State(String name,HashMap<String,String[]> cities) {
        this.name = name;
        this.cities=cities;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String[]> getCities() {
        return cities;
    }
}
