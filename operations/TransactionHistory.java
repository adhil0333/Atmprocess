package operations;

import models.Account;
import models.Transaction;
import utils.ATMHelpers;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionHistory {
    private static Map<String, List<Transaction>> allTransactions;
    private static Scanner scanner;

    public static void setDependencies(Map<String, List<Transaction>> txns, Scanner scan) {
        allTransactions = txns;
        scanner = scan;
    }

    public static void execute(Account currentAccount) throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               TRANSACTION HISTORY (Last 10 Transactions)            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝\n");

        List<Transaction> transactions = allTransactions.get(currentAccount.cardNumber);
        List<Transaction> lastTen = transactions.stream()
                .skip(Math.max(0, transactions.size() - 10))
                .collect(Collectors.toList());

        if (lastTen.isEmpty()) {
            System.out.println("📋 No transactions found!");
        } else {
            System.out.println("ID      | Date & Time          | Type          | Amount   | Balance     | Details");
            System.out.println("────────────────────────────────────────────────────────────────────────────────────");

            int count = lastTen.size();
            for (int i = 0; i < count; i++) {
                Transaction txn = lastTen.get(i);
                String txnId = String.format("%-7s", txn.id);
                String txnType = String.format("%-13s", txn.type);
                String amount = String.format("₹%-7.2f", txn.amount);
                String balance = String.format("₹%-10.2f", txn.balanceAfter);

                String details = (txn.toCard != null && !txn.toCard.isEmpty())
                        ? "To: " + ATMHelpers.maskCardNumber(txn.toCard)
                        : "";

                System.out.println(txnId + "| " + txn.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        + " | " + txnType + " | " + amount + " | " + balance + " | " + details);
            }
        }

        System.out.print("\n👉 Press ENTER to continue: ");
        scanner.nextLine();
    }
}
