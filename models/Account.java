package models;

import java.time.LocalDateTime;

public class Account {
    public String cardNumber;
    public String pin;
    public String holderName;
    public double balance;
    public double dailyWithdrawal;
    public boolean isLocked;
    public LocalDateTime lastActivityTime;

    public Account(String cardNumber, String pin, String holderName, double initialBalance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.holderName = holderName;
        this.balance = initialBalance;
        this.dailyWithdrawal = 0;
        this.isLocked = false;
        this.lastActivityTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%-18s | %-20s | ₹%-10.2f | %s",
                cardNumber, holderName, balance, isLocked ? "LOCKED" : "ACTIVE");
    }
}
