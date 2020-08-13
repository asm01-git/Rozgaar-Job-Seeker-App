package com.asm.rozgaar.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asm.rozgaar.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    String lang;
    String LANG_PREF_KEY="preferred_lang";
    static String PHONE_PREF_KEY="signed_in_number";
    String URI_PREF_KEY="stored_image_uri";
    int rc_sign_in=111;
    static String phone;
    String presentInDB="";
    static String jobSeekerCollection="Job Seekers";
    static String jobProviderCollection="Job Providers";
    Class c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            checkDB(jobSeekerCollection, user);
            if (presentInDB.equals(jobSeekerCollection)) {
                startActivity(new Intent(LanguageSelectionActivity.this, MainActivity.class));
                LanguageSelectionActivity.this.finish();
            } else {
                displayFirstTimeScreen();
                makeSnackbar("Entry deleted!");
                getSharedPreferences(URI_PREF_KEY, MODE_PRIVATE).edit()
                        .clear().apply();
                getSharedPreferences(PHONE_PREF_KEY, MODE_PRIVATE).edit()
                        .clear().apply();
                getSharedPreferences(LANG_PREF_KEY, MODE_PRIVATE).edit()
                        .clear().apply();
            }
        }
        else {
            displayFirstTimeScreen();
        }
    }
    private void checkDB(final String collection, FirebaseUser user){
        FirebaseFirestore.getInstance().collection(collection)
                .document(user.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                    presentInDB=collection;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LanguageSelectionActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void displayFirstTimeScreen(){
        setContentView(R.layout.activity_selection);
        setTitle("Language/भाषा/భాష");
        findViewById(R.id.english_button).setOnClickListener(this);
        findViewById(R.id.hindi_button).setOnClickListener(this);
        findViewById(R.id.telugu_button).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        ProgressBar pb = findViewById(R.id.progress_selection);
        pb.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.english_button:
                lang = "en";
                setLocale();
                break;
            case R.id.hindi_button:
                lang = "hi";
                setLocale();
                break;
            case R.id.telugu_button:
                lang = "te";
                setLocale();
                break;
        }

        getSharedPreferences("SharedPref", MODE_PRIVATE).edit().putString(LANG_PREF_KEY, lang).apply();
        /*Check if this activity was started as a result of intent.
         *If so, on clicking the buttons,it should take us back to the activity it came from
        */
        if (getIntent().getStringExtra("fromRegisterActivity") != null) {
            startActivity(new Intent(this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            //Now start the OTP login intent
            List providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    rc_sign_in);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response=IdpResponse.fromResultIntent(data);
        if(requestCode==rc_sign_in){
            if(resultCode==RESULT_OK){
                findViewById(R.id.language_button_layout).setVisibility(View.GONE);
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                phone= user.getPhoneNumber();
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                DocumentReference docref=db.collection("Job Seekers").document(user.getUid());
                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc=task.getResult();
                            if(doc.exists()){
                                c=MainActivity.class;
                                getSharedPreferences("SharedPref",MODE_PRIVATE).edit()
                                        .putString(PHONE_PREF_KEY,phone)
                                        .apply();
                            }
                            else
                                c=RegisterActivity.class;
                            Intent i=new Intent(LanguageSelectionActivity.this,c);
                            if(c==RegisterActivity.class)
                                i.putExtra("phone",phone);
                            startActivity(i);
                            LanguageSelectionActivity.this.finish();
                        }
                    }
                });

            }
            else if(response==null)
                makeSnackbar("Action Cancelled");
            else
                makeSnackbar(response.getError().getMessage());
        }
        else
            makeSnackbar("Something went wrong.Please try again");
    }
    private void makeSnackbar(String s){
        Snackbar.make(findViewById(R.id.root_layout),s,Snackbar.LENGTH_SHORT).show();
    }
    private void setLocale(){
        Locale loc=new Locale(lang);
        Locale.setDefault(loc);
        Configuration configuration=new Configuration();
        configuration.locale=loc;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
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
}
