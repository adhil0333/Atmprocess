package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.util.*;

public class ChangePIN {
    private static Map<String, List<Transaction>> allTransactions;
    private static Scanner scanner;

    public static void setDependencies(Map<String, List<Transaction>> txns, Scanner scan) {
        allTransactions = txns;
        scanner = scan;
    }

    public static void execute(Account currentAccount) throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           CHANGE PIN                  ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.print("\n🔐 Enter current PIN: ");
        String oldPin = scanner.nextLine().trim();

        if (!oldPin.equals(currentAccount.pin)) {
            System.out.println("❌ Wrong current PIN!");
            Thread.sleep(2000);
            return;
        }

        System.out.print("🔐 Enter new 4-digit PIN: ");
        String newPin = scanner.nextLine().trim();

        if (!newPin.matches("\\d{4}")) {
            System.out.println("❌ PIN must be exactly 4 digits!");
            Thread.sleep(2000);
            return;
        }

        if (newPin.equals(oldPin)) {
            System.out.println("❌ New PIN cannot be same as old PIN!");
            Thread.sleep(2000);
            return;
        }

        System.out.print("🔐 Confirm new PIN: ");
        String confirmPin = scanner.nextLine().trim();

        if (!confirmPin.equals(newPin)) {
            System.out.println("❌ PINs don't match!");
            Thread.sleep(2000);
            return;
        }

        currentAccount.pin = newPin;
        System.out.println("\n✅ PIN changed successfully!");

        Transaction txn = new Transaction("PIN_CHANGE", 0, currentAccount.balance, "", 0);
        allTransactions.get(currentAccount.cardNumber).add(txn);
        currentAccount.lastActivityTime = java.time.LocalDateTime.now();

        Thread.sleep(2000);
    }
}
