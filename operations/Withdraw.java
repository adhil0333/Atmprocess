package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.util.*;

public class Withdraw {
    private static Map<String, Account> accounts;
    private static Map<String, List<Transaction>> allTransactions;
    private static Scanner scanner;

    public static void setDependencies(Map<String, Account> acc, Map<String, List<Transaction>> txns, Scanner scan) {
        accounts = acc;
        allTransactions = txns;
        scanner = scan;
    }

    public static void execute(Account currentAccount) throws InterruptedException {
        ATMHelpers.clearScreen();
        ATMHelpers.resetDailyLimitsIfNewDay(currentAccount);

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         WITHDRAWAL TRANSACTION         ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n💳 Current Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("📊 Daily Limit Remaining: ₹" + String.format("%.2f",
                ATMHelpers.DAILY_WITHDRAWAL_LIMIT - currentAccount.dailyWithdrawal));

        System.out.print("\n💵 Enter withdrawal amount (multiple of ₹100): ₹");
        String amountStr = scanner.nextLine().trim();

        if (!amountStr.matches("\\d+")) {
            System.out.println("❌ Invalid input! Enter numeric value.");
            Thread.sleep(2000);
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Validation
        if (amount % 100 != 0) {
            System.out.println("❌ Amount must be multiple of ₹100!");
            Thread.sleep(2000);
            return;
        }

        if (amount > ATMHelpers.TRANSACTION_LIMIT) {
            System.out.println("❌ Withdrawal exceeds transaction limit (₹" + ATMHelpers.TRANSACTION_LIMIT + ")!");
            Thread.sleep(2000);
            return;
        }

        if (amount + currentAccount.dailyWithdrawal > ATMHelpers.DAILY_WITHDRAWAL_LIMIT) {
            System.out.println("❌ Exceeds daily withdrawal limit!");
            System.out.println("💰 Available: ₹" + (ATMHelpers.DAILY_WITHDRAWAL_LIMIT - currentAccount.dailyWithdrawal));
            Thread.sleep(2000);
            return;
        }

        if (amount > currentAccount.balance) {
            System.out.println("❌ Insufficient balance!");
            Thread.sleep(2000);
            return;
        }

        // Transaction PIN for amounts > 5000
        if (amount > ATMHelpers.TRANSACTION_PIN_THRESHOLD) {
            System.out.print("\n🔐 Enter 4-digit Transaction PIN: ");
            String txnPin = scanner.nextLine().trim();
            if (!txnPin.equals(currentAccount.pin)) {
                System.out.println("❌ Wrong Transaction PIN!");
                Thread.sleep(2000);
                return;
            }
        }

        // Process withdrawal
        currentAccount.balance -= amount;
        currentAccount.dailyWithdrawal += amount;

        Transaction txn = new Transaction("WITHDRAW", amount, currentAccount.balance, "", 0);
        allTransactions.get(currentAccount.cardNumber).add(txn);
        currentAccount.lastActivityTime = java.time.LocalDateTime.now();

        System.out.println("\n✅ Withdrawal successful!");
        System.out.println("💵 Amount: ₹" + String.format("%.2f", amount));
        System.out.println("💰 New Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("🎟️  Transaction ID: " + txn.id);
        Thread.sleep(2000);
    }
}
