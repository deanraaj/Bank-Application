package com.dean.bank_application.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created..";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with provided Account Number is Not Exist";

    public static final String ACCOUNT_CREDIT_SUCCESS = "004";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "User Account is CREDITED..!";

    public static final String ACCOUNT_DEBIT_SUCCESS = "005";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "User Account is DEBITED..!";

    public static final String ACCOUNT_INSUFFICIENT_BALANCE_CODE = "006";
    public static final String ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance present in the Account..!";

    public static final String ACCOUNT_TRANSFER_SUCCESSFUL_CODE = "007";
    public static final String ACCOUNT_TRANSFER_SUCCESSFUL_MESSAGE = "Transfer is successful...";

    public static String getAccountNumber() {
        /*
         * 2025+ random 6 Digits
         * */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // generate a random number
        int randomNumber = (int) Math.floor((Math.random() * (max - min + 1) + min));

        String year = String.valueOf(currentYear);
        String numberRand = String.valueOf(randomNumber);
        return year+numberRand;
    }


}
