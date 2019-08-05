package com.kyra.expensemanager;

public class BankData {

    public String id,bankName,accountNumber,ifscCode,remarks;

    public BankData(String id, String bankName, String accountNumber, String ifscCode, String remarks) {
        this.id = id;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.remarks = remarks;
    }

}
