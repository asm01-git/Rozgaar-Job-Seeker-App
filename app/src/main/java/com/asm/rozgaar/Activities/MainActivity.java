package com.asm.rozgaar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;
import com.asm.rozgaar.Fragments.MainFragments.HomeScreenFragment;
import com.asm.rozgaar.Fragments.MainFragments.ProfilePageFragment;
import com.asm.rozgaar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    String PHONE_PREF_KEY="signed_in_number";
    BottomNavigationView bottomNavigationView;
    String current_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        current_page="Default";
        setNavBar();
        HomeScreenFragment hsf=new HomeScreenFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_container_home_page,hsf)
                .commit();
    }
    boolean pressedOnce=false;
    @Override
    public void onBackPressed() {
        if(pressedOnce)
            super.onBackPressed();
        pressedOnce=true;
        Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pressedOnce=false;
            }
        },2000);
    }
    private void setNavBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_page_button:
                        if(current_page.equals("Home Screen"))
                            makeToast("You are already on Home page!");
                        else {
                            goToFragment(new HomeScreenFragment());
                            current_page="Home Screen";
                        }
                        return true;
                    case R.id.profile_page_button:
                        if(current_page.equals("Profile Page"))
                            makeToast("You are already on Profile page!");
                        else {
                            goToFragment(new ProfilePageFragment());
                            current_page="Profile Page";
                        }
                        return true;
                    case R.id.notification_page_button:
                        return true;
                    case R.id.settings_page_icon:
                        return true;
                }
                return false;
            }
        });
    }
    private void makeToast(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }
    private void goToFragment(Fragment fragment){
        MainActivity.this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container_home_page,fragment)
                .commit();
    }
}

