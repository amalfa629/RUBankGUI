package com.project.rubankgui;

/**
 Creates a checking account that inherits the properties of the Account class
 @author Tyler Amalfa, Zafar Khan
 */
public class Checking extends Account{
    private static final double ANNUALRATE = 1;
    private static final double MINBALANCE = 1000;
    private static final double MONTHLYFEE = 12;

    /**
     constructor that creates an account of type Checking
     @param holder the name of the account holder stored as type Profile
     @param balance the amount of money in the bank account stored as type double
     */
    public Checking(Profile holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    /**
     calculates the monthly interest of the checking account
     @return returns the monthly interest as type double
     */
    public double monthlyInterest() {
        return ((ANNUALRATE / PERCENT) * balance) / NUMMONTHS;
    }

    /**
     returns the monthly fee
     @return returns the monthly fee as type double if balance less than min balance; returns no fee if balance >= min balance
     */
    public double monthlyFee() {
        if(balance >= MINBALANCE) return NOFEE;
        return MONTHLYFEE;
    }

    /**
     returns the account information as a string
     @return returns a String with the details of the checking account using the profileBalanceToString method
     */
    public String toString() {
        return "Checking::" + profileBalanceToString();
    }

    /**
     checks if the account is a checking account and sorts it accordingly
     @param account account being compared to
     @return returns -1 if account is less than returns 0 if account is equal to
     */
    public int compareTo(Account account) {
        if(!(account instanceof Checking) || (account instanceof CollegeChecking)) return -1;
        return holder.compareTo(account.getHolder());
    }

    /**
     checks if an object is a checking account or a college checking account
     @param o the account object being checked against
     @return returns true if the account is a checking account returns false if account is not a checking account
     */
    @Override
    public boolean equals(Object o) {
        return ((super.equals(o)) && (o instanceof Checking));
    }
}
