package com.rubankgui;

/**
 Creates a college checking account that inherits the properties of a checking account
 @author Tyler Amalfa, Zafar Khan
 */
public class CollegeChecking extends Checking{
    private static final double NOFEE = 0;
    private Campus campus; //campus code

    /**
     constructor that creates an account of type CollegeChecking
     @param holder the name of the account holder stored as type Profile
     @param balance the account balance stored as type double
     @param campus the campus stored as type Campus
     */
    public CollegeChecking(Profile holder, double balance, Campus campus) {
        super(holder, balance);
        this.campus = campus;
    }

    /**
     returns the monthly fee
     @return returns a double NoFee
     */
    @Override
    public double monthlyFee() {
        return NOFEE;
    }
    public Campus getCampus() {
        return campus;
    }

    /**
     returns the account information using the toString function from the Checking class
     @return the account details as type String
     */
    @Override
    public String toString() {
        return "College " + super.toString() + "::" + campus;
    }

    /**
     checks if the account is a college checking account and sorts it accordingly
     @param account account being compared
     @return returns 1 if account is greater, returns -1 if account is less than, returns 0 if account is equal to
     */
    @Override
    public int compareTo(Account account) {
        if((account instanceof Checking) && (!(account instanceof CollegeChecking))) return 1;
        if(!(account instanceof CollegeChecking)) return -1;
        return holder.compareTo(account.getHolder());
    }
}
