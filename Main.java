import models.Account;
import models.Transaction;
import operations.*;
import utils.ATMHelpers;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    static Map<String, Account> accounts = new HashMap<>();
    static Map<String, List<Transaction>> allTransactions = new HashMap<>();
    static Account currentAccount = null;
    static LocalDateTime sessionStartTime = null;
    static int wrongAttempts = 0;
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        initializeData();
        initializeOperations();

        // Print welcome message only once
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     WELCOME TO ATM MANAGEMENT SYSTEM    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        while (true) {
            if (currentAccount == null) {
                cardAndPinAuthentication();
            } else {
                if (ATMHelpers.isSessionTimeout(sessionStartTime)) {
                    System.out.println("⚠️  SESSION TIMEOUT! (5 minutes inactive)");
                    Thread.sleep(2000);
                    currentAccount = null;
                    wrongAttempts = 0;
                    continue;
                }
                mainMenu();
            }
        }
    }

    // ============= INITIALIZATION =============

    static void initializeData() {
        accounts.put("1111111111111111", new Account("1111111111111111", "1234", "Raja ranadev", ATMHelpers.INITIAL_BALANCE));
        accounts.put("2222222222222222", new Account("2222222222222222", "5678", "Sham dev", ATMHelpers.INITIAL_BALANCE));
        accounts.put("3333333333333333", new Account("3333333333333333", "9012", "Aryan", ATMHelpers.INITIAL_BALANCE));
        accounts.put("0000000000000000", new Account("0000000000000000", "0000", "Admin", 0));

        for (String card : accounts.keySet()) {
            allTransactions.put(card, new ArrayList<>());
        }
    }

    static void initializeOperations() {
        Withdraw.setDependencies(accounts, allTransactions, scanner);
        Deposit.setDependencies(accounts, allTransactions, scanner);
        Transfer.setDependencies(accounts, allTransactions, scanner);
        TransactionHistory.setDependencies(allTransactions, scanner);
        ChangePIN.setDependencies(allTransactions, scanner);
        AdminPanel.setDependencies(accounts, allTransactions, scanner);
    }

    // ============= AUTHENTICATION =============

    static void cardAndPinAuthentication() throws InterruptedException {
        System.out.println("\n┌─── AUTHENTICATION ───┐");
        System.out.print("📇 Enter 16-digit Card Number (or 'exit'): ");
        String cardNumber = scanner.nextLine().trim();

        if (cardNumber.equalsIgnoreCase("exit")) {
            System.out.println("\n👋 Thank you for using ATM. Goodbye!");
            System.exit(0);
        }

        if (!cardNumber.matches("\\d{16}")) {
            System.out.println("❌ Invalid card number! Must be 16 digits.");
            Thread.sleep(2000);
            return;
        }

        if (!accounts.containsKey(cardNumber)) {
            System.out.println("❌ Card not found in system!");
            Thread.sleep(2000);
            return;
        }

        Account account = accounts.get(cardNumber);

        if (account.isLocked) {
            System.out.println("❌ ACCOUNT LOCKED! Contact admin.");
            Thread.sleep(2000);
            return;
        }

        System.out.print("🔐 Enter 4-digit PIN: ");
        String pin = scanner.nextLine().trim();

        if (!pin.matches("\\d{4}")) {
            System.out.println("❌ Invalid PIN! Must be 4 digits.");
            Thread.sleep(2000);
            return;
        }

        if (!account.pin.equals(pin)) {
            wrongAttempts++;
            System.out.println("❌ Wrong PIN! Attempt " + wrongAttempts + "/" + ATMHelpers.MAX_WRONG_ATTEMPTS);

            if (wrongAttempts >= ATMHelpers.MAX_WRONG_ATTEMPTS) {
                account.isLocked = true;
                System.out.println("🔒 ACCOUNT LOCKED after 3 wrong attempts!");
            }
            Thread.sleep(2000);
            return;
        }

        currentAccount = account;
        sessionStartTime = LocalDateTime.now();
        wrongAttempts = 0;
        currentAccount.lastActivityTime = LocalDateTime.now();
        System.out.println("✅ Authentication successful!");
        System.out.println("👤 Welcome, " + currentAccount.holderName + "!");
        Thread.sleep(2000);
    }

    // ============= MAIN MENU =============

    static void mainMenu() throws InterruptedException {
        ATMHelpers.clearScreen();
        boolean isAdmin = currentAccount.cardNumber.equals("0000000000000000");

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          MAIN MENU - " + ATMHelpers.formatName(currentAccount.holderName, 25) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("📅 Session Active: " + ATMHelpers.formatSessionTime(sessionStartTime) + "\n");

        System.out.println("1  Check Balance");
        System.out.println("2  Withdraw Cash");
        System.out.println("3  Deposit Money");
        System.out.println("4  Transfer to Other Account");
        System.out.println("5  Transaction History");
        System.out.println("6  Change PIN");

        if (isAdmin) {
            System.out.println("7  Admin Panel");
        }

        System.out.println("0  Logout");
        System.out.print("\n👉 Select Option: ");

        String choice = scanner.nextLine().trim();
        currentAccount.lastActivityTime = LocalDateTime.now();

        switch (choice) {
            case "1":
                CheckBalance.execute(currentAccount);
                break;
            case "2":
                Withdraw.execute(currentAccount);
                break;
            case "3":
                Deposit.execute(currentAccount);
                break;
            case "4":
                Transfer.execute(currentAccount);
                break;
            case "5":
                TransactionHistory.execute(currentAccount);
                break;
            case "6":
                ChangePIN.execute(currentAccount);
                break;
            case "7":
                if (isAdmin) {
                    AdminPanel.execute();
                } else {
                    System.out.println("❌ Admin access denied!");
                    Thread.sleep(2000);
                }
                break;
            case "0":
                logout();
                break;
            default:
                System.out.println("❌ Invalid option!");
                Thread.sleep(2000);
        }
    }

    // ============= LOGOUT =============

    static void logout() throws InterruptedException {
        System.out.println("\n👋 Thanks for using ATM, " + currentAccount.holderName + "!");
        System.out.println("⏱️  Session Duration: " + ATMHelpers.formatSessionTime(sessionStartTime));

        currentAccount = null;
        sessionStartTime = null;
        wrongAttempts = 0;

        Thread.sleep(2000);
    }
}
