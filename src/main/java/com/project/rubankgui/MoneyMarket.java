package com.project.rubankgui;

/**
 Creates a money market account that inherits the properties of the Savings class
 @author Tyler Amalfa, Zafar Khan
 */
public class MoneyMarket extends Savings{
    private static final double MONEYMARKETRATE = 0.5;
    private static final double NOFEEBALANCE = 2000;
    private static final double WITHDRAWALFEE = 10;
    private static final double WITHDRAWALLIMIT = 3;
    private int withdrawal; //number of withdrawals

    /**
     constructor that creates an account of type MoneyMarket
     @param holder the account holder details stored as type Profile
     @param balance the account balance stored as type double
     */
    public MoneyMarket(Profile holder, double balance) {
        super(holder, balance, true);
        withdrawal = 0;
    }

    /**
     calculates the account monthly interest
     @return returns money market interest added to savings interest
     */
    @Override
    public double monthlyInterest() {
        return super.monthlyInterest() + ((MONEYMARKETRATE / PERCENT) * balance) / NUMMONTHS;
    }

    /**
     calculates the account monthly fee
     @return returns the total fee after adding the monthly and withdrawal fees
     */
    @Override
    public double monthlyFee() {
        double totalFee = NOFEE;
        if(balance < NOFEEBALANCE) totalFee += MONTHLYFEE;
        if(withdrawal > WITHDRAWALLIMIT) totalFee += WITHDRAWALFEE;
        return totalFee;
    }

    /**
     uses the applyFeesInterest method from the Account class to apply the interest fees to the money market account
     */
    @Override
    public void applyFeesInterest() {
        super.applyFeesInterest();
        withdrawal = 0;
    }

    /**
     withdraws money from the money market account
     @param account the account that the money is being withdrawn from
     @return returns true if the withdrawal was successful returns false otherwise
     */
    @Override
    public boolean withdraw(Account account) {
        if(balance > account.getBalance()) {
            withdrawal++;
            balance -= account.getBalance();
            if(balance < NOFEEBALANCE) isLoyal = false;
            return true;
        }
        return false;
    }

    /**
     deposits money into the money market account
     @param account the account that money is being deposited into
     */
    @Override
    public void deposit(Account account) {
        balance += account.getBalance();
        if(balance >= NOFEEBALANCE) isLoyal = true;
    }

    /**
     returns the account information as a string
     @return uses the Savings class toString method to return the account information as a string
     */
    @Override
    public String toString() {
        return "Money Market::" + super.toString() + "::withdrawal: " + withdrawal;
    }

    /**
     checks if account is a money market account and then sorts it accordingly
     @param account account being compared
     @return returns 1 if account is greater than, returns -1 if account is less than, returns 0 if account is equal to
     */
    @Override
    public int compareTo(Account account) {
        if(account instanceof Checking) return 1;
        if((account instanceof Savings) && !(account instanceof MoneyMarket)) return -1;
        return holder.compareTo(account.getHolder());
    }

    /**
     checks if an object is a money market account
     @param o the account object being checked against
     @return returns true if the object is a money market account
     */
    @Override
    public boolean equals(Object o) {
        return ((equalsHelper(o)) && (o instanceof MoneyMarket));
    }
}
