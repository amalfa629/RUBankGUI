package com.project.rubankgui;

/**
 Creates an account database to keep track of what accounts have been created
 @author Tyler Amalfa, Zafar Khan
 */
public class AccountDatabase {
    private Account[] accounts; //list of various types of accounts
    private int numAcct; //number of accounts in the array

    /**
     constructor that creates an AccountDatabase object with 0 accounts that can hold up to 4 accounts
     */
    public AccountDatabase() {
        accounts = new Account[4];
        numAcct = 0;
    }

    /**
     searches for an account in the AccountDatabase
     @param account account being searched for
     @return returns the index of the account if found, returns -1 if not found
     */
    private int find(Account account) {
        for(int n = 0; n < numAcct; n++) {
            if(accounts[n].equals(account)) {
                return n;
            }
        }
        return -1;
    } //search for an account in the array

    /**
     increases the capacity of the AccountDatabase by 4
     */
    private void grow() {
        Account[] newAccounts = new Account[numAcct + 4];
        for(int n = 0; n < numAcct; n++) {
            newAccounts[n] = accounts[n];
        }
        accounts = newAccounts;
    }//increase the capacity by 4

    /**
     checks if an account is contained in the array
     @param account account being searched for
     @return returns true if account is contained in the array, false if account is not in the array
     */
    public boolean contains(Account account){
        return find(account) != -1;
    } //overload if necessary

    /**
     adds a new account to the AccountDatabase
     @param account account being added
     @return returns true if account is added successfully, returns false otherwise
     */
    public boolean open(Account account){
        if(!contains(account)) {
            if (numAcct == accounts.length - 1) grow();
            accounts[numAcct] = account;
            numAcct++;
            return true;
        }
        return false;
    } //add a new account

    /**
     removes an account from the AccountDatabase
     @param account account being removed
     @return returns true if account is removed, returns false if account is not in the database
     */
    public boolean close(Account account){
        int index = find(account);
        if(index == -1) return false;
        if(accounts[index].compareTo(account) != 0) return false;
        for(int n = index; n < numAcct - 1; n++) {
            accounts[n]=accounts[n + 1];
        }
        numAcct--;
        accounts[numAcct] = null;
        return true;
    } //remove the given account

    /**
     withdraws money from an account in the AccountDatabase
     @param account account that money is being withdrawn from
     @return returns false if account is not found or fund is insufficient, returns true if money is withdrawn
     */
    public boolean withdraw(Account account){
        int index = find(account);
        if(index == -1) return false;
        if(accounts[index].compareTo(account) != 0) return false;
        return accounts[index].withdraw(account);
    } //false if insufficient fund

    /**
     deposits money to an account in the AccountDatabase
     @param account account that money is being deposited into
     @return returns false if account is not found or fund is insufficient, returns true if money is deposited
     */
    public boolean deposit(Account account){
        int index = find(account);
        if(index == -1) return false;
        if(accounts[index].compareTo(account) != 0) return false;
        accounts[index].deposit(account);
        return true;
    }

    /**
     gets the size of the AccountDatabase
     @return returns the number of accounts as type int
     */
    public int size() {
        return numAcct;
    }

    /**
     sorts the array of accounts
     @param left the left bound of the sort stored as an int
     @param right the right bound of the sort stored as an int
     */
    public void quicksort(int left, int right) {
        if(left < right) {
            int middle = partition(left, right);
            quicksort(left, middle - 1);
            quicksort(middle + 1, right);
        }
    }

    /**
     calculates partition for sorting
     @param left the left bound of the sort stored as an int
     @param right the right bound of the sort stored as an int
     @return returns the middle element of the partition for further sorting
     */
    public int partition(int left, int right) {
        Account save;
        int pivot = left;
        int middle = pivot;
        left++;
        while (left <= right) {
            if(accounts[left].compareTo(accounts[pivot]) < 0) {
                middle++;
                save = accounts[left];
                accounts[left] = accounts[middle];
                accounts[middle] = save;
            }
            left++;
        }
        save = accounts[pivot];
        accounts[pivot] = accounts[middle];
        accounts[middle] = save;
        return middle;
    }

    /**
     sorts and prints the accounts in order
     */
    public String printSorted(){
        StringBuilder out = new StringBuilder();
        quicksort(0, numAcct - 1);
        for(int n = 0; n < numAcct; n++) {
            out.append(accounts[n].toString()).append("\n");
        }
        return out.toString();
    } //sort by account type and profile

    /**
     calculates and prints the all the fees and interest rates of the accounts
     */
    public String printFeesAndInterests(){
        quicksort(0, numAcct - 1);
        StringBuilder out = new StringBuilder();
        for(int n = 0; n < numAcct; n++) {
            out.append(accounts[n].toString()).append("::fee $").append(String.format("%,.2f", accounts[n].monthlyFee())).append("::monthly interest $").append(String.format("%,.2f", accounts[n].monthlyInterest())).append("\n");
        }
        return out.toString();
    } //calculate interests/fees

    /**
     applies the fees and prints the accounts in the updated order
     */
    public String printUpdatedBalances(){
        for(int n = 0; n < numAcct; n++) {
            accounts[n].applyFeesInterest();
        }
        return printSorted();
    } //apply the interests/fees
}
