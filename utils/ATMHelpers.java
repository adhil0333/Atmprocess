package utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ATMHelpers {
    public static final int MAX_WRONG_ATTEMPTS = 3;
    public static final long SESSION_TIMEOUT_MINUTES = 5;
    public static final double DAILY_WITHDRAWAL_LIMIT = 20000;
    public static final double DAILY_DEPOSIT_LIMIT = 100000;
    public static final double TRANSACTION_LIMIT = 50000;
    public static final double TRANSFER_FEE = 10;
    public static final double TRANSACTION_PIN_THRESHOLD = 5000;
    public static final double INITIAL_BALANCE = 25000;

    public static boolean isSessionTimeout(LocalDateTime sessionStartTime) {
        if (sessionStartTime == null)
            return false;
        long minutesElapsed = ChronoUnit.MINUTES.between(sessionStartTime, LocalDateTime.now());
        return minutesElapsed >= SESSION_TIMEOUT_MINUTES;
    }

    public static String formatSessionTime(LocalDateTime sessionStartTime) {
        if (sessionStartTime == null)
            return "N/A";
        long minutes = ChronoUnit.MINUTES.between(sessionStartTime, LocalDateTime.now());
        long seconds = ChronoUnit.SECONDS.between(sessionStartTime, LocalDateTime.now()) % 60;
        return minutes + "m " + seconds + "s";
    }

    public static String maskCardNumber(String cardNumber) {
        return "****" + cardNumber.substring(12);
    }

    public static String formatName(String name, int maxLength) {
        if (name.length() > maxLength - 2) {
            return name.substring(0, maxLength - 3) + "...";
        }
        return String.format("%-" + maxLength + "s", name);
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void resetDailyLimitsIfNewDay(models.Account currentAccount) {
        if (currentAccount.lastActivityTime != null) {
            LocalDateTime lastActivity = currentAccount.lastActivityTime;
            LocalDateTime now = LocalDateTime.now();

            if (lastActivity.toLocalDate().isBefore(now.toLocalDate())) {
                currentAccount.dailyWithdrawal = 0;
            }
        }
    }
}
