package com.asm.rozgaar.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asm.rozgaar.Fragments.RegisterFragments.SeekerDetailsFragment;
import com.asm.rozgaar.Fragments.RegisterFragments.UserTypeFragment;
import com.asm.rozgaar.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements SeekerDetailsFragment.SuccessListener {

    UserTypeFragment utf;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phone=getIntent().getStringExtra("phone");

        Bundle args=new Bundle();
        args.putString("phone",phone);
        utf=new UserTypeFragment();
        utf.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_register_activity,utf)
                .commit();

    }

    @Override
    public void isSuccessful(boolean flag) {
        if(flag){
            //Set progress bar while loading
            findViewById(R.id.register_progressbar).setVisibility(View.VISIBLE);
            findViewById(R.id.root_register_activity).setBackgroundColor(getResources().getColor(R.color.colorBlack));
            removeUserTypeFragment();
            Intent goToMain=new Intent(this, MainActivity.class);
            startActivity(goToMain);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("All entered data will be lost")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent backToLanguage=new Intent(RegisterActivity.this, LanguageSelectionActivity.class);
                        backToLanguage.putExtra("fromRegisterActivity","true");
                        startActivity(backToLanguage);
                        RegisterActivity.this.finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
    private void removeUserTypeFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .remove(utf)
                .commit();
    }
    public static void radioButtonOnClicked(View v){
        SeekerDetailsFragment.onClick(v);
    }
}
