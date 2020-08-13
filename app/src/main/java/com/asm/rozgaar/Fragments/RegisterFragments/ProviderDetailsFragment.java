package com.asm.rozgaar.Fragments.RegisterFragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asm.rozgaar.Activities.LanguageSelectionActivity;
import com.asm.rozgaar.Activities.RegisterActivity;
import com.asm.rozgaar.CustomClasses.JobProvider;
import com.asm.rozgaar.CustomClasses.LocationDatabase;
import com.asm.rozgaar.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class ProviderDetailsFragment extends Fragment {
    Context context;
    Toolbar toolbar;
    RelativeLayout personalDetails,companyDetails,jobDetails;
    TextInputEditText nameInput,phoneInput,addressInput,emailInput,companyNameInput,cinInput,officeAddressInput;
    TextInputEditText jobDescriptionInput,jobReqInput,jobVacanciesInput,salaryInput,deadlineInput;
    Spinner companyCategorySpinner,jobCategorySpinner,stateSpinner,citySpinner,areaSpinner;
    TextView aadharUriView;
    ImageButton aadharUpload;
    Button nextButton1,nextButton2,backButton1,backButton2,buttonRegister;
    int IMAGE_PICK_RC=101;
    String companyCategory,jobCategory,selectedState,selectedCity,selectedArea;
    JobProvider jp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_provider_details, container, false);
        toolbar=v.findViewById(R.id.toolbar_job_provider);
        personalDetails=v.findViewById(R.id.personal_details_page);
        companyDetails=v.findViewById(R.id.company_details_page);
        jobDetails=v.findViewById(R.id.job_details_page);
        //TextInput Edit texts
        nameInput=v.findViewById(R.id.name_field);
        phoneInput=v.findViewById(R.id.phone_field);
        addressInput=v.findViewById(R.id.address_field);
        emailInput=v.findViewById(R.id.email_field);
        companyNameInput=v.findViewById(R.id.company_name_field);
        cinInput=v.findViewById(R.id.cin_number_field);
        officeAddressInput=v.findViewById(R.id.office_address_field);
        aadharUriView=v.findViewById(R.id.aadhar_uri);
        jobDescriptionInput=v.findViewById(R.id.job_description_field);
        jobReqInput=v.findViewById(R.id.job_req_field);
        jobVacanciesInput=v.findViewById(R.id.job_vacancies_field);
        deadlineInput=v.findViewById(R.id.deadline_field);
        salaryInput=v.findViewById(R.id.salary_field);
        //Buttons
        aadharUpload=v.findViewById(R.id.aadhar_upload_button);
        nextButton1=v.findViewById(R.id.next_button1);
        nextButton2=v.findViewById(R.id.button_next2);
        backButton1=v.findViewById(R.id.button_back1);
        backButton2=v.findViewById(R.id.button_back2);
        buttonRegister=v.findViewById(R.id.button_register);
        //Spinners
        companyCategorySpinner=v.findViewById(R.id.spinner_company_category);
        jobCategorySpinner=v.findViewById(R.id.spinner_job_category);
        stateSpinner=v.findViewById(R.id.state_spinner);
        citySpinner=v.findViewById(R.id.city_spinner);
        areaSpinner=v.findViewById(R.id.area_spinner);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Set toolbar as action bar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPrevPage();
            }
        });
        jp=new JobProvider();
        //Setting up aadhar upload button
        aadharUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                startActivityForResult(galleryIntent,IMAGE_PICK_RC);
            }
        });
        //Setting up the register button
        setUpRegisterButton();
        setUpNextButtons();
        setUpBackButtons();
        setUpSpinner(companyCategorySpinner,R.array.CompanyCategories);
        setUpSpinner(jobCategorySpinner,R.array.JobCategories);
        //setting up state spinner will set up city and area spinner recursively
        setUpSpinner(stateSpinner,R.array.india_states);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_PICK_RC){
            if(resultCode==RESULT_OK&&data!=null){
                aadharUriView.setText(data.getDataString());
            }
        }
        else
            makeToast("Something went wrong!Please try again");
    }
        private void makeToast(String s){
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        }
        private void backToPrevPage(){
            new AlertDialog.Builder(context)
                    .setTitle("Go Back?")
                    .setMessage("All entered data will be lost")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserTypeFragment utf=new UserTypeFragment();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.root_register_activity,utf)
                                    .commit();
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
        private void setUpRegisterButton(){
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence jobDescription=jobDescriptionInput.getText();
                    CharSequence jobReq=jobReqInput.getText();
                    CharSequence jobVacancies=jobVacanciesInput.getText();
                    CharSequence salary=salaryInput.getText();
                    CharSequence deadline=deadlineInput.getText();
                    //Setting a hashmap of location
                    HashMap<String,String> location=new HashMap<>();
                    location.put("state",selectedState);
                    location.put("city",selectedCity);
                    location.put("area",selectedArea);
                    if(jobCategory==null||jobDescription==null||salary==null||deadline==null||jobVacancies==null||jobReq==null)
                        makeToast("Enter all mandatory fields!");
                    else {
                        jp.setjobDetails(jobCategory, jobDescription.toString(), salary.toString(),
                                deadline.toString(), jobVacancies.toString(), jobReq.toString(), location);
                        storeInDB();
                    }
                }
            });
        }
        private void storeInDB(){
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            db.collection("JobProviders")
                    .document(user.getUid())
                    .set(jp)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                makeToast("Data was stored successfully");
                                buttonRegister.setEnabled(false);
                                buttonRegister.setAlpha(0.5f);
                            }
                        }
                    });
        }
        private void setUpNextButtons(){
            //Setting up first next button
            nextButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence name=nameInput.getText();
                    CharSequence phone=phoneInput.getText();
                    CharSequence address=addressInput.getText();
                    CharSequence emailID=emailInput.getText();
                    CharSequence aadharUri=aadharUriView.getText().equals("No image uploaded")?
                            null:aadharUriView.getText();
                    if(name==null||phone==null||address==null||emailID==null||aadharUri==null)
                        makeToast("Enter all fields!");
                    else {
                        jp.setpersonalDetails(name.toString(),phone.toString(),address.toString(),emailID.toString(),aadharUri.toString());
                        personalDetails.setVisibility(View.GONE);
                        companyDetails.setVisibility(View.VISIBLE);
                    }
                }
            });
            nextButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence companyName=companyNameInput.getText();
                    CharSequence cin=cinInput.getText();
                    CharSequence officeAddress=officeAddressInput.getText();
                    if(companyName==null||cin==null||officeAddress==null||companyCategory==null)
                        makeToast("Please enter all fields!");
                    else{
                        jp.setcompanyDetails(companyName.toString(),cin.toString(),companyCategory,officeAddress.toString());
                        companyDetails.setVisibility(View.GONE);
                        jobDetails.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        private void setUpBackButtons(){
            backButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyDetails.setVisibility(View.GONE);
                    personalDetails.setVisibility(View.VISIBLE);
                }
            });
            backButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobDetails.setVisibility(View.GONE);
                    companyDetails.setVisibility(View.VISIBLE);
                }
            });
        }
        private void setUpSpinner(final Spinner spinner, int datasetResourceId){
        ArrayAdapter adapter=null;
        if(datasetResourceId!=-1)
            adapter=ArrayAdapter.createFromResource(context,datasetResourceId,R.layout.support_simple_spinner_dropdown_item);
        else {
            HashMap<String, HashMap<String, String[]>> states_list = LocationDatabase.getStates_list((Activity) context);
            HashMap<String, String[]> citiesList=states_list.get(selectedState);
            if(citiesList==null)
                makeToast("Sorry, no cities of the selected state have been registered");
            else {
                if (spinner.getId() == R.id.city_spinner) {
                    Object[] keys = citiesList.keySet().toArray();
                    adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
                                Arrays.copyOf(keys, keys.length, String[].class));
                }
                else if (spinner.getId() == R.id.area_spinner) {
                    if(citiesList.get(selectedCity)==null)
                        makeToast("Sorry, there are no registered areas for the selected city");
                    else
                        adapter=new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,citiesList.get(selectedCity));
                }
            }
        }
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()){
                    case R.id.spinner_company_category:
                        if(position==0)
                            companyCategory=null;
                        else
                            companyCategory=parent.getItemAtPosition(position).toString();
                        break;
                        case R.id.spinner_job_category:
                            if(position==0)
                                jobCategory=null;
                            else
                                jobCategory=parent.getItemAtPosition(position).toString();
                            break;
                    case R.id.state_spinner:
                        if(position==0)
                           selectedState=null;
                        else {
                            selectedState = parent.getItemAtPosition(position).toString();
                            setUpSpinner(citySpinner,-1);
                        }
                        break;
                    case R.id.city_spinner:
                        if(position==0)
                            selectedCity=null;
                        else{
                            selectedCity=parent.getItemAtPosition(position).toString();
                            setUpSpinner(areaSpinner,-1);
                        }
                        break;
                    case R.id.area_spinner:
                        if(position==0)
                            selectedArea=null;
                        else
                            selectedArea=parent.getItemAtPosition(position).toString();
                        break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
