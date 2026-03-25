package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.util.*;

public class Transfer {
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
        System.out.println("║        BALANCE TRANSFER TRANSACTION    ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n💳 Your Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("📊 Transfer Fee: ₹" + ATMHelpers.TRANSFER_FEE);

        System.out.print("\n📇 Enter recipient's 16-digit card number: ");
        String recipientCard = scanner.nextLine().trim();

        if (!recipientCard.matches("\\d{16}")) {
            System.out.println("❌ Invalid card number! Must be 16 digits.");
            Thread.sleep(2000);
            return;
        }

        if (!accounts.containsKey(recipientCard)) {
            System.out.println("❌ Recipient account not found!");
            Thread.sleep(2000);
            return;
        }

        if (recipientCard.equals(currentAccount.cardNumber)) {
            System.out.println("❌ Cannot transfer to same account!");
            Thread.sleep(2000);
            return;
        }

        System.out.print("💵 Enter transfer amount (multiple of ₹100): ₹");
        String amountStr = scanner.nextLine().trim();

        if (!amountStr.matches("\\d+")) {
            System.out.println("❌ Invalid input! Enter numeric value.");
            Thread.sleep(2000);
            return;
        }

        double amount = Double.parseDouble(amountStr);
        double totalDebit = amount + ATMHelpers.TRANSFER_FEE;

        // Validation
        if (amount % 100 != 0) {
            System.out.println("❌ Amount must be multiple of ₹100!");
            Thread.sleep(2000);
            return;
        }

        if (amount > ATMHelpers.TRANSACTION_LIMIT) {
            System.out.println("❌ Transfer exceeds transaction limit (₹" + ATMHelpers.TRANSACTION_LIMIT + ")!");
            Thread.sleep(2000);
            return;
        }

        if (totalDebit > currentAccount.balance) {
            System.out.println("❌ Insufficient balance (including ₹" + ATMHelpers.TRANSFER_FEE + " fee)!");
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

        // Confirm transfer
        Account recipient = accounts.get(recipientCard);
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  Recipient: " + recipient.holderName);
        System.out.println("  Card: " + ATMHelpers.maskCardNumber(recipientCard));
        System.out.println("  Amount: ₹" + String.format("%.2f", amount));
        System.out.println("  Fee: ₹" + ATMHelpers.TRANSFER_FEE);
        System.out.println("  Total: ₹" + String.format("%.2f", totalDebit));
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        System.out.print("Confirm transfer? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("❌ Transfer cancelled!");
            Thread.sleep(2000);
            return;
        }

        // Process transfer
        currentAccount.balance -= totalDebit;
        recipient.balance += amount;
        currentAccount.dailyWithdrawal += amount;

        Transaction senderTxn = new Transaction("TRANSFER", amount, currentAccount.balance, recipientCard,
                ATMHelpers.TRANSFER_FEE);
        Transaction recipientTxn = new Transaction("TRANSFER_IN", amount, recipient.balance, currentAccount.cardNumber,
                0);

        allTransactions.get(currentAccount.cardNumber).add(senderTxn);
        allTransactions.get(recipientCard).add(recipientTxn);
        currentAccount.lastActivityTime = java.time.LocalDateTime.now();

        System.out.println("\n✅ Transfer successful!");
        System.out.println("💰 Your New Balance: ₹" + String.format("%.2f", currentAccount.balance));
        System.out.println("🎟️  Transaction ID: " + senderTxn.id);
        Thread.sleep(2000);
    }
}
