package operations;
import models.Account;
import utils.ATMHelpers;

public class CheckBalance {
    public static void execute(Account currentAccount) throws InterruptedException {
        ATMHelpers.clearScreen();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         BALANCE INFORMATION            ║");
        System.out.println("╚════════════════════════════════════════╝");

        ATMHelpers.resetDailyLimitsIfNewDay(currentAccount);

        System.out.println("\n👤 Account Holder: " + currentAccount.holderName);
        System.out.println("💳 Card Number: " + ATMHelpers.maskCardNumber(currentAccount.cardNumber));
        System.out.println("💰 Current Balance: ₹" + String.format("%.2f", currentAccount.balance));

        System.out.print("\n👉 Press ENTER to continue: ");
        new java.util.Scanner(System.in).nextLine();
    }
}
