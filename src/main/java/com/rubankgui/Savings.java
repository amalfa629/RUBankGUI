package com.rubankgui;

/**
 Creates a savings account that inherits the properties of the Account class
 @author Tyler Amalfa, Zafar Khan
 */
public class Savings extends Account{
    private static final double ANNUALRATE = 4;
    private static final double LOYALTYBONUSRATE = 0.25;
    private static final double NOFEEBALANCE = 500;
    protected double MONTHLYFEE = 25;
    protected boolean isLoyal; //loyal customer status

    /**
     constructor that creates an Account of type Savings
     @param holder the name of the account holder stored as type Profile
     @param balance the savings balance stored as type double
     @param isLoyal the loyal customer status stored as type boolean
     */
    public Savings(Profile holder, double balance, boolean isLoyal) {
        this.holder = holder;
        this.balance = balance;
        this.isLoyal = isLoyal;
    }

    /**
     calculates monthly interest based on whether the account holder is loyal
     @return returns monthly rate as a double
     */
    public double monthlyInterest() {
        if(isLoyal) return (((ANNUALRATE + LOYALTYBONUSRATE) / PERCENT) * balance) / NUMMONTHS;
        return ((ANNUALRATE / PERCENT) * balance) / NUMMONTHS;
    }

    /**
     calculates the monthly fee
     @return returns no fee if the balance is >= the no fee balance otherwise it returns the monthly fee
     */
    public double monthlyFee() {
        if(balance >= NOFEEBALANCE) return NOFEE;
        return MONTHLYFEE;
    }
    public boolean isLoyal() {
        return isLoyal;
    }
    /**
     returns the account information as a string
     @return uses the profileBalanceToString method to return the account information as a string
     */
    public String toString() {
        if(isLoyal) return "Savings::" + profileBalanceToString() + "::is loyal";
        return "Savings::" + profileBalanceToString();
    }

    /**
     checks if the account is a savings account and sorts it accordingly
     @param account account being compared
     @return returns 1 if account is greater returns 0 if account is equal to
     */
    public int compareTo(Account account) {
        if(!(account instanceof Savings) || (account instanceof MoneyMarket)) return 1;
        return holder.compareTo(account.getHolder());
    }

    /**
     helper function for the equals function
     @param o object being checked
     @return returns true or false after calling equals method from the Account class on the object
     */
    protected boolean equalsHelper(Object o) {
        return super.equals(o);
    }

    /**
     checks if an object is a savings account
     @param o the account object being checked against
     @return returns true is an object is a savings account
     */
    @Override
    public boolean equals(Object o) {
        return (equalsHelper(o)) && (o instanceof Savings) && !(o instanceof MoneyMarket);
    }
}
