package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public static int transactionCounter = 1000;
    public String id;
    public LocalDateTime dateTime;
    public String type; // WITHDRAW, DEPOSIT, TRANSFER, PIN_CHANGE
    public double amount;
    public double balanceAfter;
    public String toCard;
    public double fee;

    public Transaction(String type, double amount, double balanceAfter, String toCard, double fee) {
        this.id = "TXN" + (++transactionCounter);
        this.dateTime = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.toCard = toCard;
        this.fee = fee;
    }

    @Override
    public String toString() {
        String details = String.format("%s | %-10s | ₹%-8.2f | Bal:₹%-10.2f",
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                type, amount, balanceAfter);

        if (toCard != null && !toCard.isEmpty()) {
            details += String.format(" | To: %s", toCard);
        }
        if (fee > 0) {
            details += String.format(" | Fee:₹%.2f", fee);
        }
        return details;
    }
}
