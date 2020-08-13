package com.asm.rozgaar.CustomClasses;

import java.util.HashMap;

public class JobProvider {
    HashMap<String,String> personalDetails;
    HashMap<String,String> companyDetails;
    HashMap<String,String> jobDetails;

    //Initialising all details seperately
    public JobProvider(){
        personalDetails=new HashMap<>();
        companyDetails=new HashMap<>();
        jobDetails=new HashMap<>();
    }
    public HashMap<String, String> getpersonalDetails() {
        return personalDetails;
    }

    public HashMap<String, String> getcompanyDetails() {
        return companyDetails;
    }

    public HashMap<String, String> getjobDetails() {
        return jobDetails;
    }

    public void setpersonalDetails(String name, String phone, String address, String emailID, String aadharUri) {
        personalDetails.put("name",name);
        personalDetails.put("phone",phone);
        personalDetails.put("address",address);
        personalDetails.put("emailID",emailID);
        personalDetails.put("aadharUri",aadharUri);
    }
    public void setcompanyDetails(String companyName,String cin,String companyCategory,String officeAddress){
        companyDetails.put("companyName",companyName);
        companyDetails.put("cinNumber",cin);
        companyDetails.put("companyCategory",companyCategory);
        companyDetails.put("officeAddress",officeAddress);
    }
    public void setjobDetails(String jobCategory,String jobDescription,String salary,String deadline,
                                    String jobVacancies,String jobRequirements,HashMap<String,String> location){
        jobDetails.put("jobCategory",jobCategory);
        jobDetails.put("jobDescription",jobDescription);
        jobDetails.put("jobSalary",salary);
        jobDetails.put("applicationDeadline",deadline);
        jobDetails.put("jobVacancies",jobVacancies);
        jobDetails.put("jobRequirements",jobRequirements);
        jobDetails.put("state",location.get("state"));
        jobDetails.put("city",location.get("city"));
        jobDetails.put("area",location.get("area"));
    }
}
