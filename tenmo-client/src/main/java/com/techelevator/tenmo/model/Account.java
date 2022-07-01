package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private long userId;
    private long accountId;
    private BigDecimal balance;


    public Account(long accountId, long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.userId = userId;
    }

    public Account() {
    }

    public long getAccountId() {
        return accountId;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Account { " + "balance:" + balance + ", user ID:" + userId + ", account ID:" + accountId + " }";
    }
}