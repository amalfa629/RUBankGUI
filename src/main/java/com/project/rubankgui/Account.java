package com.project.rubankgui;

/**
 Creates an abstract account type with properties that all subclasses inherit
 @author Tyler Amalfa, Zafar Khan
 */
public abstract class Account implements Comparable<Account> {
    protected final double NUMMONTHS = 12;
    protected final double NOFEE = 0;
    protected final int PERCENT = 100;
    protected Profile holder;
    protected double balance;
    public abstract double monthlyInterest();
    public abstract double monthlyFee();
    public abstract String toString();

    /**
     removes interest fees from the bank balance
     */
    public void applyFeesInterest() {
        balance = balance - monthlyFee() + monthlyInterest();
    }

    /**
     withdraws money from the account
     @param account the account that the money is being withdrawn from
     @return returns true if the money was withdrawn and returns false if the money was not withdrawn
     */
    public boolean withdraw(Account account) {
        if(balance > account.getBalance()) {
            balance -= account.getBalance();
            return true;
        }
        return false;
    }

    /**
     gets the account holder
     @return returns the account holder as type Profile
     */
    public Profile getHolder() {
        return holder;
    }

    /**
     gets the balance
     @return returns the balance as type double
     */
    public double getBalance() {
        return balance;
    }

    /**
     deposits money into the bank account
     @param account the account that money is being deposited into
     */
    public void deposit(Account account) {
        balance += account.getBalance();
    }

    /**
     prints the account information
     @return returns the bank balance and account holder as a type String
     */
    protected String profileBalanceToString() {
        return holder.getFirst() + " " + holder.getLast() + " " + holder.getDOB().toString() + "::Balance $" + String.format("%,.2f", balance);
    }

    /**
     checks if two account objects have the same holder
     @param o the account object being checked against
     @return returns true if the account holders are the same false if they are different
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Account)) return false;
        return holder.equals(((Account) o).getHolder());
    }
}
