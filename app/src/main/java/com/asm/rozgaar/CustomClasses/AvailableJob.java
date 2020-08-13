package com.asm.rozgaar.CustomClasses;

public class AvailableJob {
    String description,address,salary,logo_URI;

    public AvailableJob(String description, String address, String salary, String logo_URI) {
        this.description = description;
        this.address = address;
        this.salary = salary;
        this.logo_URI = logo_URI;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getSalary() {
        return salary;
    }

    public String getLogo_URI() {
        return logo_URI;
    }
}
