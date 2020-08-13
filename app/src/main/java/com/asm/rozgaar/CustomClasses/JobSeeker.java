package com.asm.rozgaar.CustomClasses;

import java.util.ArrayList;

public class JobSeeker {
    String name,domain,phone,gender,address,dob;
    boolean isAadharUploaded,isLicenseUploaded,isPanUploaded;
    ArrayList<String> known_langs,interests;



    public JobSeeker(String name, String domain, String phone, String gender,
                     String address, String dob, ArrayList<String> known_langs, ArrayList<String> interests,
                     boolean isAadharUploaded, boolean isLicenseUploaded, boolean isPanUploaded) {
        this.name = name;
        this.domain = domain;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.known_langs = known_langs;
        this.interests=interests;
        this.isAadharUploaded=isAadharUploaded;
        this.isLicenseUploaded=isLicenseUploaded;
        this.isPanUploaded=isPanUploaded;
    }

    public JobSeeker() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setKnown_langs(ArrayList<String> known_langs) {
        this.known_langs = known_langs;
    }

    public boolean isAadharUploaded() {
        return isAadharUploaded;
    }

    public void setAadharUploaded(boolean aadharUploaded) {
        isAadharUploaded = aadharUploaded;
    }

    public boolean isLicenseUploaded() {
        return isLicenseUploaded;
    }

    public void setLicenseUploaded(boolean licenseUploaded) {
        isLicenseUploaded = licenseUploaded;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }
    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }


    public ArrayList<String> getKnown_langs() {
        return known_langs;
    }

    public String getPhone() {
        return phone;
    }
    public boolean isPanUploaded() {
        return isPanUploaded;
    }

    public void setPanUploaded(boolean panUploaded) {
        isPanUploaded = panUploaded;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }
}
