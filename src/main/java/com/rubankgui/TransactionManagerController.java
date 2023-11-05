package com.rubankgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.*;
import java.time.format.*;
import java.util.Calendar;

public class TransactionManagerController {
    private static AccountDatabase accountDatabase = new AccountDatabase();
    private static final int CURRENTYEAR = Calendar.getInstance().get(Calendar.YEAR); //year of date program is ran
    private static final int CURRENTMONTH = Calendar.getInstance().get(Calendar.MONTH) + 1; //month of date program is ran
    private static final int CURRENTDAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //day of date program is ran
    private static final int MINAGE = 16;
    private static final int COLLEGEMAXAGE = 24;
    @FXML
    private TextField firstName, lastName, balanceText;
    @FXML
    private DatePicker birthPicker;
    @FXML
    private ToggleGroup accounts;
    @FXML
    private MenuButton campusMenu;
    @FXML
    private CheckBox isLoyal;
    @FXML
    private TextArea openCloseOutput;
    private boolean validDatePicker(ActionEvent event) {
        try {
            birthPicker.getConverter().fromString(
                    birthPicker.getEditor().getText());
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
    private Profile newProfile(ActionEvent event) {
        String first = firstName.getText();
        String last = lastName.getText();
        Date birthDate = Date.toDate(birthPicker.getValue());
        return new Profile(first, last, birthDate);
    }
    private void clearInputs(ActionEvent event) {
        firstName.clear();
        lastName.clear();
        birthPicker.getEditor().setText(null);
        balanceText.clear();
        accounts.selectToggle(null);
        isLoyal.setSelected(false);
    }
    @FXML
    protected void onOpenButtonClick(ActionEvent event) {
        if(!firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !birthPicker.getEditor().getText().isEmpty() && !balanceText.getText().isEmpty() && accounts.getSelectedToggle() != null) {
            if(validDatePicker(event)) {
                Profile profile = newProfile(event);
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    String accountType = ((RadioButton) accounts.getSelectedToggle()).getText();
                    double balance = Double.parseDouble(balanceText.getText());
                    if(balance > 0) {
                        Account account = null;
                        boolean fail = false;
                        switch(accountType) {
                            case "Checking":
                                account = new Checking(profile, balance);
                                break;
                            case "College Checking":
                                Campus campus = null;
                                switch (campusMenu.getText()) {
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
                                if(!fail) account = new CollegeChecking(profile, balance, campus);
                                break;
                            case "Savings":
                                account = new Savings(profile, balance, isLoyal.isSelected());
                                break;
                            case "Money Market":
                                account = new MoneyMarket(profile, balance);
                                break;
                        }
                        if(!fail) {
                            String output = profile.getFirst() + " " + profile.getLast() + " " + profile.getDOB().toString() + "(" + accountType + ")";
                            if (!accountDatabase.open(account)) openCloseOutput.appendText(output + " is already in the database.\n");
                            else {
                                openCloseOutput.appendText(output += " opened.\n");
                                clearInputs(event);
                            }
                        }
                        else openCloseOutput.appendText("Missing data for opening an account.\n");
                    }
                    else openCloseOutput.appendText("Initial deposit cannot be 0 or negative.\n");
                }
                else openCloseOutput.appendText(verifyDateOutput);
            }
            else openCloseOutput.appendText("DOB invalid: " + birthPicker.getEditor().getText() + "\n");
        }
        else openCloseOutput.appendText("Missing data for opening an account.\n");
    }
    @FXML
    protected void onCloseButtonClick(ActionEvent event) {
        double balance = 0;
        if(!firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !birthPicker.getEditor().getText().isEmpty() && accounts.getSelectedToggle() != null) {
            if(validDatePicker(event)) {
                Profile profile = newProfile(event);
                String verifyDateOutput = verifyDate(profile.getDOB());
                if(verifyDateOutput == null) {
                    String accountType = ((RadioButton) accounts.getSelectedToggle()).getText();
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
                        clearInputs(event);
                    }
                }
                else openCloseOutput.appendText(verifyDateOutput);
            }
            else openCloseOutput.appendText("DOB invalid: " + birthPicker.getEditor().getText() + "\n");
        }
        else openCloseOutput.appendText("Missing data for closing an account.\n");
    }
}