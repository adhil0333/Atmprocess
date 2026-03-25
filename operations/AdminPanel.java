package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.util.*;

public class AdminPanel {
    private static Map<String, Account> accounts;
    private static Map<String, List<Transaction>> allTransactions;
    private static Scanner scanner;

    public static void setDependencies(Map<String, Account> acc, Map<String, List<Transaction>> txns, Scanner scan) {
        accounts = acc;
        allTransactions = txns;
        scanner = scan;
    }

    public static void execute() throws InterruptedException {
        boolean inAdmin = true;

        while (inAdmin) {
            ATMHelpers.clearScreen();
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          ADMIN PANEL                  ║");
            System.out.println("╚════════════════════════════════════════╝");

            System.out.println("\n1️⃣  View All Accounts");
            System.out.println("2️⃣  View Transaction Logs");
            System.out.println("3️⃣  Unlock Account");
            System.out.println("4️⃣  Reset PIN");
            System.out.println("0️⃣  Back to Main Menu");
            System.out.print("\n👉 Select Option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllAccounts();
                    break;
                case "2":
                    viewTransactionLogs();
                    break;
                case "3":
                    unlockAccount();
                    break;
                case "4":
                    resetCustomerPIN();
                    break;
                case "0":
                    inAdmin = false;
                    break;
                default:
                    System.out.println("❌ Invalid option!");
                    Thread.sleep(2000);
            }
        }
    }

    private static void viewAllAccounts() throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL ACCOUNTS IN SYSTEM                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        System.out.println("Card Number      | Account Holder      | Balance    | Status");
        System.out.println("─────────────────────────────────────────────────────────────────");

        for (Account acc : accounts.values()) {
            System.out.println(acc.toString());
        }

        System.out.print("\n👉 Press ENTER to continue: ");
        scanner.nextLine();
    }

    private static void viewTransactionLogs() throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              SYSTEM TRANSACTION LOGS                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        System.out.print("📇 Enter 16-digit card number (or 'all'): ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("all")) {
            int totalTxns = 0;
            for (String card : allTransactions.keySet()) {
                List<Transaction> txns = allTransactions.get(card);
                if (!txns.isEmpty()) {
                    Account acc = accounts.get(card);
                    System.out.println("\n📝 Account: " + acc.holderName + " (" + ATMHelpers.maskCardNumber(card) + ")");
                    System.out.println("────────────────────────────────────────────────────────────────");
                    for (Transaction txn : txns) {
                        System.out.println(txn.toString());
                    }
                    totalTxns += txns.size();
                }
            }
            System.out.println("\n📊 Total Transactions: " + totalTxns);
        } else {
            if (!input.matches("\\d{16}")) {
                System.out.println("❌ Invalid card number!");
                Thread.sleep(2000);
                return;
            }

            if (!accounts.containsKey(input)) {
                System.out.println("❌ Account not found!");
                Thread.sleep(2000);
                return;
            }

            List<Transaction> txns = allTransactions.get(input);
            Account acc = accounts.get(input);

            System.out.println("\n📝 Transactions for: " + acc.holderName + " (" + ATMHelpers.maskCardNumber(input) + ")");
            System.out.println("────────────────────────────────────────────────────────────────");

            if (txns.isEmpty()) {
                System.out.println("No transactions found!");
            } else {
                for (Transaction txn : txns) {
                    System.out.println(txn.toString());
                }
            }
        }

        System.out.print("\n👉 Press ENTER to continue: ");
        scanner.nextLine();
    }

    private static void unlockAccount() throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         UNLOCK ACCOUNT                 ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("\n📇 Enter 16-digit card number: ");
        String cardNumber = scanner.nextLine().trim();

        if (!cardNumber.matches("\\d{16}")) {
            System.out.println("❌ Invalid card number!");
            Thread.sleep(2000);
            return;
        }

        if (!accounts.containsKey(cardNumber)) {
            System.out.println("❌ Account not found!");
            Thread.sleep(2000);
            return;
        }

        Account account = accounts.get(cardNumber);

        if (!account.isLocked) {
            System.out.println("ℹ️  Account is already active!");
            Thread.sleep(2000);
            return;
        }

        account.isLocked = false;
        System.out.println("\n✅ Account unlocked successfully!");
        System.out.println("👤 Account: " + account.holderName);
        Thread.sleep(2000);
    }

    private static void resetCustomerPIN() throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          RESET CUSTOMER PIN            ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("\n📇 Enter 16-digit card number: ");
        String cardNumber = scanner.nextLine().trim();

        if (!cardNumber.matches("\\d{16}")) {
            System.out.println("❌ Invalid card number!");
            Thread.sleep(2000);
            return;
        }

        if (!accounts.containsKey(cardNumber)) {
            System.out.println("❌ Account not found!");
            Thread.sleep(2000);
            return;
        }

        Account account = accounts.get(cardNumber);

        System.out.print("🔐 Enter new 4-digit PIN: ");
        String newPin = scanner.nextLine().trim();

        if (!newPin.matches("\\d{4}")) {
            System.out.println("❌ PIN must be exactly 4 digits!");
            Thread.sleep(2000);
            return;
        }

        account.pin = newPin;
        System.out.println("\n✅ PIN reset successfully!");
        System.out.println("👤 Account: " + account.holderName);
        System.out.println("🔐 New PIN: " + newPin);
        Thread.sleep(2000);
    }
}
