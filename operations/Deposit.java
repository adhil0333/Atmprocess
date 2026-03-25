package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.util.*;

public class Deposit {
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
        System.out.println("║          DEPOSIT TRANSACTION           ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n💳 Current Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("📊 Daily Deposit Limit: ₹" + ATMHelpers.DAILY_DEPOSIT_LIMIT);

        System.out.print("\n💵 Enter deposit amount (multiple of ₹100): ₹");
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
            System.out.println("❌ Deposit exceeds transaction limit (₹" + ATMHelpers.TRANSACTION_LIMIT + ")!");
            Thread.sleep(2000);
            return;
        }

        if (amount > ATMHelpers.DAILY_DEPOSIT_LIMIT) {
            System.out.println("❌ Deposit exceeds daily limit (₹" + ATMHelpers.DAILY_DEPOSIT_LIMIT + ")!");
            Thread.sleep(2000);
            return;
        }

        // Process deposit
        currentAccount.balance += amount;

        Transaction txn = new Transaction("DEPOSIT", amount, currentAccount.balance, "", 0);
        allTransactions.get(currentAccount.cardNumber).add(txn);
        currentAccount.lastActivityTime = java.time.LocalDateTime.now();

        System.out.println("\n✅ Deposit successful!");
        System.out.println("💵 Amount: ₹" + String.format("%.2f", amount));
        System.out.println("💰 New Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("🎟️  Transaction ID: " + txn.id);
        Thread.sleep(2000);
    }
}
