package com.project.rubankgui;

/**
 Creates a profile for an account holder
 @author Tyler Amalfa, Zafar Khan
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    /**
     constructor that creates a Profile with a first name, last name, and date of birth
     @param fname first name stored as type String
     @param lname last name stored as type String
     @param dob date of birth stored as type Date
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     gets the account holder's first name
     @return returns the first name
     */
    public String getFirst() {
        return fname;
    }

    /**
     gets the account holder's last name
     @return returns the last name
     */
    public String getLast() {
        return lname;
    }

    /**
     gets the account holder's date of birth
     @return returns the dob
     */
    public Date getDOB() {
        return dob;
    }

    /**
     compares last name, first name, and dob and sorts accordingly
     @param profile profile being compared to
     @return returns -1 if profile is less than, 1 if profile is greater than, 0 if profile is equal to
     */
    public int compareTo(Profile profile) {
        int compare = lname.toLowerCase().compareTo(profile.getLast().toLowerCase());
        if(compare == 0) {
            compare = fname.toLowerCase().compareTo(profile.getFirst().toLowerCase());
            if(compare == 0) compare = dob.compareTo(profile.getDOB());
        }
        return compare;
    }

    /**
     checks if two Profiles are the same
     @param o object being checked
     @return returns true if profiles are the same returns false if they are not
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof  Profile)) return false;
        return compareTo((Profile) o) == 0;
    }
}
