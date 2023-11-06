package com.rubankgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.format.*;
import java.util.Calendar;

public class TransactionManagerController {
    private static final AccountDatabase accountDatabase = new AccountDatabase();
    private static final int CURRENTYEAR = Calendar.getInstance().get(Calendar.YEAR); //year of date program is ran
    private static final int CURRENTMONTH = Calendar.getInstance().get(Calendar.MONTH) + 1; //month of date program is ran
    private static final int CURRENTDAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //day of date program is ran
    private static final int MINAGE = 16;
    private static final int COLLEGEMAXAGE = 24;
    @FXML
    private TextField firstNameOC, lastNameOC, balanceText, firstNameDW, lastNameDW, amountText;
    @FXML
    private DatePicker birthPickerOC, birthPickerDW;
    @FXML
    private ToggleGroup accountsOC, campusMenu, accountsDW;
    @FXML
    private RadioButton camdenButton, newarkButton, nbButton;
    @FXML
    private CheckBox isLoyal;
    @FXML
    private TextArea manageDatabaseOutput, openCloseOutput, depositWithdrawOutput;
    private boolean validDatePicker(DatePicker datePicker) {
        try {
            datePicker.getConverter().fromString(
                    datePicker.getEditor().getText());
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    private String verifyDate(Date date) {
        if(!date.isValid()) return "DOB invalid: " + date.toString() + " not a valid calendar day.";
        if(date.compareTo(new Date(CURRENTYEAR, CURRENTMONTH, CURRENTDAY)) >= 0) return "DOB invalid: " + date.toString() + " cannot be today or a future day.";
        return null;
    }
    private int getAge(Date dob) {
        int age = CURRENTYEAR - dob.getYear();
        if(CURRENTMONTH < dob.getMonth()) return age--;
        if((CURRENTMONTH == dob.getMonth()) && (CURRENTDAY < dob.getDay())) return age--;
        return age;
    }
    private void clearInputs() {
        firstNameOC.clear();
        lastNameOC.clear();
        birthPickerOC.getEditor().setText(null);
        balanceText.clear();
        accountsOC.selectToggle(null);
        hideCampusAndLoyal();
        firstNameDW.clear();
        lastNameDW.clear();
        birthPickerDW.getEditor().setText(null);
        amountText.clear();
        accountsDW.selectToggle(null);
    }
    @FXML
    protected void onOpenButtonClick() {
        if(!firstNameOC.getText().isEmpty() && !lastNameOC.getText().isEmpty() && !birthPickerOC.getEditor().getText().isEmpty() && !balanceText.getText().isEmpty() && accountsOC.getSelectedToggle() != null) {
            if(validDatePicker(birthPickerOC)) {
                Profile profile = new Profile(firstNameOC.getText(), lastNameOC.getText(), Date.toDate(birthPickerOC.getValue()));
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    int age = getAge(profile.getDOB());
                    if(age >= MINAGE) {
                        String accountType = ((RadioButton) accountsOC.getSelectedToggle()).getText();
                        double balance = Double.parseDouble(balanceText.getText());
                        if (balance > 0) {
                            Account account = null;
                            boolean fail = false;
                            switch (accountType) {
                                case "Checking":
                                    account = new Checking(profile, balance);
                                    break;
                                case "College Checking":
                                    if(age < COLLEGEMAXAGE) {
                                        Campus campus = null;
                                        switch (((RadioButton) campusMenu.getSelectedToggle()).getText()) {
                                            case "Camden":
                                                campus = Campus.CAMDEN;
                                                break;
                                            case "Newark":
                                                campus = Campus.NEWARK;
                                                break;
                                            case "New Brunswick":
                                                campus = Campus.NEW_BRUNSWICK;
                                                break;
                                            default:
                                                fail = true;
                                                break;
                                        }
                                        if (!fail) account = new CollegeChecking(profile, balance, campus);
                                        else openCloseOutput.appendText("Invalid Campus Selection!\n");
                                    }
                                    else {
                                        openCloseOutput.appendText("DOB invalid: " + profile.getDOB().toString() + " over 24.\n");
                                        fail = true;
                                    }
                                    break;
                                case "Savings":
                                    account = new Savings(profile, balance, isLoyal.isSelected());
                                    break;
                                case "Money Market":
                                    account = new MoneyMarket(profile, balance);
                                    break;
                            }
                            if (!fail) {
                                String output = profile.getFirst() + " " + profile.getLast() + " " + profile.getDOB().toString() + "(" + accountType + ")";
                                if (!accountDatabase.open(account))
                                    openCloseOutput.appendText(output + " is already in the database.\n");
                                else {
                                    openCloseOutput.appendText(output += " opened.\n");
                                    clearInputs();
                                }
                            }
                            else openCloseOutput.appendText("Missing data for opening an account.\n");
                        }
                        else openCloseOutput.appendText("Initial deposit cannot be 0 or negative.\n");
                    }
                    else openCloseOutput.appendText("DOB invalid: " + profile.getDOB().toString() + " under 16.\n");
                }
                else openCloseOutput.appendText(verifyDateOutput);
            }
            else openCloseOutput.appendText("DOB invalid: " + birthPickerOC.getEditor().getText() + "\n");
        }
        else openCloseOutput.appendText("Missing data for opening an account.\n");
    }
    @FXML
    protected void onCloseButtonClick() {
        double balance = 0;
        if(!firstNameOC.getText().isEmpty() && !lastNameOC.getText().isEmpty() && !birthPickerOC.getEditor().getText().isEmpty() && accountsOC.getSelectedToggle() != null) {
            if(validDatePicker(birthPickerOC)) {
                Profile profile = new Profile(firstNameOC.getText(), lastNameOC.getText(), Date.toDate(birthPickerOC.getValue()));
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    String accountType = ((RadioButton) accountsOC.getSelectedToggle()).getText();
                    Account account = null;
                    switch(accountType) {
                        case "Checking":
                            account = new Checking(profile, balance);
                            break;
                        case "College Checking":
                            account = new CollegeChecking(profile, balance, Campus.values()[0]);
                            break;
                        case "Savings":
                            account = new Savings(profile, balance, false);
                            break;
                        case "Money Market":
                            account = new MoneyMarket(profile, balance);
                            break;
                    }
                    String output = profile.getFirst() + " " + profile.getLast() + " " + profile.getDOB().toString() + "(" + accountType + ")";
                    if (!accountDatabase.close(account)) openCloseOutput.appendText(output + " is not in the database.\n");
                    else {
                        openCloseOutput.appendText(output += " has been closed.\n");
                        clearInputs();
                    }
                }
                else openCloseOutput.appendText(verifyDateOutput);
            }
            else openCloseOutput.appendText("DOB invalid: " + birthPickerOC.getEditor().getText() + "\n");
        }
        else openCloseOutput.appendText("Missing data for closing an account.\n");
    }
    @FXML
    protected void hideCampusAndLoyal() {
        campusMenu.selectToggle(null);
        camdenButton.setVisible(false);
        camdenButton.setMouseTransparent(true);
        newarkButton.setVisible(false);
        newarkButton.setMouseTransparent(true);
        nbButton.setVisible(false);
        nbButton.setMouseTransparent(true);
        isLoyal.setVisible(false);
        isLoyal.setMouseTransparent(true);
        isLoyal.setSelected(false);
    }
    @FXML
    protected void showCampus() {
        hideCampusAndLoyal();
        camdenButton.setVisible(true);
        camdenButton.setMouseTransparent(false);
        newarkButton.setVisible(true);
        newarkButton.setMouseTransparent(false);
        nbButton.setVisible(true);
        nbButton.setMouseTransparent(false);
    }
    @FXML
    protected void showLoyal() {
        hideCampusAndLoyal();
        isLoyal.setVisible(true);
        isLoyal.setMouseTransparent(false);
    }
    @FXML
    protected void onDepositButtonClick() {
        if(!firstNameDW.getText().isEmpty() && !lastNameDW.getText().isEmpty() && !birthPickerDW.getEditor().getText().isEmpty() && !amountText.getText().isEmpty() && accountsDW.getSelectedToggle() != null) {
            if(validDatePicker(birthPickerDW)) {
                Profile profile = new Profile(firstNameDW.getText(), lastNameDW.getText(), Date.toDate(birthPickerDW.getValue()));
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    int age = getAge(profile.getDOB());
                    if(age >= MINAGE) {
                        String accountType = ((RadioButton) accountsDW.getSelectedToggle()).getText();
                        double amount = Double.parseDouble(amountText.getText());
                        if (amount > 0) {
                            Account account = null;
                            switch(accountType) {
                                case "Checking":
                                    account = new Checking(profile, amount);
                                    break;
                                case "College Checking":
                                    account = new CollegeChecking(profile, amount, Campus.values()[0]);
                                    break;
                                case "Savings":
                                    account = new Savings(profile, amount, false);
                                    break;
                                case "Money Market":
                                    account = new MoneyMarket(profile, amount);
                                    break;
                            }
                            String output = profile.getFirst() + " " + profile.getLast() + " " + profile.getDOB().toString() + "(" + accountType + ")";
                            if (!accountDatabase.deposit(account)) depositWithdrawOutput.appendText(output + " is not in the database.\n");
                            else {
                                depositWithdrawOutput.appendText(output += " Deposit - balance updated.\n");
                                clearInputs();
                            }
                        }
                        else depositWithdrawOutput.appendText("Deposit cannot be 0 or negative.\n");
                    }
                    else depositWithdrawOutput.appendText("DOB invalid: " + profile.getDOB().toString() + " under 16.\n");
                }
                else depositWithdrawOutput.appendText(verifyDateOutput);
            }
            else depositWithdrawOutput.appendText("DOB invalid: " + birthPickerDW.getEditor().getText() + "\n");
        }
        else depositWithdrawOutput.appendText("Missing data for depositing into an account.\n");
    }
    @FXML
    protected void onWithdrawButtonClick() {
        if(!firstNameDW.getText().isEmpty() && !lastNameDW.getText().isEmpty() && !birthPickerDW.getEditor().getText().isEmpty() && !amountText.getText().isEmpty() && accountsDW.getSelectedToggle() != null) {
            if(validDatePicker(birthPickerDW)) {
                Profile profile = new Profile(firstNameDW.getText(), lastNameDW.getText(), Date.toDate(birthPickerDW.getValue()));
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    int age = getAge(profile.getDOB());
                    if(age >= MINAGE) {
                        String accountType = ((RadioButton) accountsDW.getSelectedToggle()).getText();
                        double amount = Double.parseDouble(amountText.getText());
                        if (amount > 0) {
                            Account account = null;
                            switch(accountType) {
                                case "Checking":
                                    account = new Checking(profile, amount);
                                    break;
                                case "College Checking":
                                    account = new CollegeChecking(profile, amount, Campus.values()[0]);
                                    break;
                                case "Savings":
                                    account = new Savings(profile, amount, false);
                                    break;
                                case "Money Market":
                                    account = new MoneyMarket(profile, amount);
                                    break;
                            }
                            String output = profile.getFirst() + " " + profile.getLast() + " " + profile.getDOB().toString() + "(" + accountType + ")";
                            if (!accountDatabase.contains(account)) depositWithdrawOutput.appendText(output + " is not in the database.\n");
                            else if (!accountDatabase.withdraw(account)) depositWithdrawOutput.appendText(output + " Withdraw - insufficient fund.\n");
                            else {
                                depositWithdrawOutput.appendText(output += " Withdraw - balance updated.\n");
                                clearInputs();
                            }
                        }
                        else depositWithdrawOutput.appendText("Withdraw cannot be 0 or negative.\n");
                    }
                    else depositWithdrawOutput.appendText("DOB invalid: " + profile.getDOB().toString() + " under 16.\n");
                }
                else depositWithdrawOutput.appendText(verifyDateOutput);
            }
            else depositWithdrawOutput.appendText("DOB invalid: " + birthPickerDW.getEditor().getText() + "\n");
        }
        else depositWithdrawOutput.appendText("Missing data for withdrawing from an account.\n");
    }
    @FXML
    protected void onPrintAllButtonClick() {
        if(accountDatabase.size() > 0) {
            manageDatabaseOutput.appendText("\n*Accounts sorted by account type and profile.\n");
            manageDatabaseOutput.appendText(accountDatabase.printSorted());
            manageDatabaseOutput.appendText("*end of list.\n\n");
        }
        else manageDatabaseOutput.appendText("Account Database is empty!\n");
    }
    @FXML
    protected void onPrintInterestFeesButtonClick() {
        if(accountDatabase.size() > 0) {
            manageDatabaseOutput.appendText("\n*list of accounts with fee and monthly interest\n");
            manageDatabaseOutput.appendText(accountDatabase.printFeesAndInterests());
            manageDatabaseOutput.appendText("*end of list.\n\n");
        }
        else manageDatabaseOutput.appendText("Account Database is empty!\n");
    }
    @FXML
    protected void onUpdateButtonClick() {
        if(accountDatabase.size() > 0) {
            manageDatabaseOutput.appendText("\n*list of accounts with fees and interests applied.\n");
            manageDatabaseOutput.appendText(accountDatabase.printUpdatedBalances());
            manageDatabaseOutput.appendText("*end of list.\n\n");
        }
        else manageDatabaseOutput.appendText("Account Database is empty!\n");
    }
    @FXML
    protected void onSaveButtonClick() {
        try {
            FileWriter output = new FileWriter("src/main/resources/com/rubankgui/bankAccounts.txt");
            output.write(accountDatabase.toString());
            output.close();
        }
        catch(IOException exception) {
            manageDatabaseOutput.appendText(exception + "\n");
        }
        manageDatabaseOutput.appendText("Successfully saved to bankAccounts.txt\n");
    }
    @FXML
    protected void onLoadButtonClick() {
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader("src/main/resources/com/rubankgui/bankAccounts.txt"));
            String input;
            while(((input=bufferedReader.readLine())!=null)) {
                if(!input.isEmpty()) {
                    String[] command = input.split(",");
                    String firstName = command[1];
                    String lastName = command[2];
                    String[] dobString = command[3].split("/");
                    Date dob = new Date(Integer.parseInt(dobString[2]), Integer.parseInt(dobString[0]), Integer.parseInt(dobString[1]));
                    Profile profile = new Profile(firstName, lastName, dob);
                    double balance = Double.parseDouble(command[4]);
                    switch (command[0]) {
                        case "C":
                            accountDatabase.open(new Checking(profile, balance));
                            break;
                        case "CC":
                            Campus campus = Campus.values()[Integer.parseInt(command[5])];
                            accountDatabase.open(new CollegeChecking(profile, balance, campus));
                            break;
                        case "S":
                            boolean isLoyal = Integer.parseInt(command[5]) == 1;
                            accountDatabase.open(new Savings(profile, balance, isLoyal));
                            break;
                        case "MM":
                            accountDatabase.open(new MoneyMarket(profile, balance));
                            break;
                    }
                }
            }
            bufferedReader.close();
        }
        catch (IOException exception) {
            manageDatabaseOutput.appendText(exception + "\n");
        }
        manageDatabaseOutput.appendText("Successfully loaded: bankAccounts.txt\n");
    }
}