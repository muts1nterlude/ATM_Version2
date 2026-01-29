package services;

import core.*;
import interfaces.Persistence;

public class ATMService {
    private Persistence persistence;
    private Account account;
    private ATMState atmState;
    private static final int MAX_PIN_ATTEMPTS = 3;
    private int pinAttempts = 0;

    public ATMService(Persistence persistence) {
        this.persistence = persistence;
        this.atmState = persistence.loadATMState();
    }

    public boolean authenticate(String card, String pin) {
        // Check if card is expired
        account = persistence.loadAccount(card);
        if (account.isCardExpired()) {
            System.out.println("ERROR: Your card has expired. Please contact your bank.");
            return false;
        }

        if (!account.getPin().equals(pin)) {
            pinAttempts++;
            if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                System.out.println("ERROR: Maximum PIN attempts exceeded. Card blocked.");
                return false;
            }
            System.out.println("Invalid PIN. Attempts remaining: " + (MAX_PIN_ATTEMPTS - pinAttempts));
            return false;
        }

        pinAttempts = 0;
        System.out.println("Authentication successful! Welcome, " + card);
        return true;
    }

    public void checkBalance() {
        System.out.println("\n========== ACCOUNT INFORMATION ==========");
        System.out.println("Card Number: " + maskCardNumber(account.getCardNumber()));
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println("Daily Withdrawal Limit: $" + String.format("%.2f", account.getDailyWithdrawalLimit()));
        System.out.println("Daily Used: $" + String.format("%.2f", account.getDailyWithdrawalUsed()));
        System.out.println("Available for Withdrawal: $" + String.format("%.2f", 
            account.getDailyWithdrawalLimit() - account.getDailyWithdrawalUsed()));
        System.out.println("Card Expires: " + account.getCardExpirationDate());
        System.out.println("=========================================\n");
    }

    public boolean withdraw(double amount) {
        try {
            if (amount <= 0) {
                System.out.println("ERROR: Withdrawal amount must be positive.");
                return false;
            }

            if (!atmState.dispenseCash(amount)) {
                System.out.println("ERROR: ATM has insufficient cash. Available: $" + 
                    String.format("%.2f", atmState.getCashAvailable()));
                atmState.addCash(amount); // Revert
                return false;
            }

            if (!account.withdraw(amount)) {
                atmState.addCash(amount); // Revert
                System.out.println("ERROR: Insufficient balance or daily limit exceeded.");
                return false;
            }

            printReceipt("WITHDRAWAL", amount);
            persistence.saveAccount(account);
            persistence.saveATMState(atmState);
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " withdrawn successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Withdrawal failed - " + e.getMessage());
            return false;
        }
    }

    public boolean deposit(double amount) {
        try {
            if (amount <= 0) {
                System.out.println("ERROR: Deposit amount must be positive.");
                return false;
            }

            if (!atmState.addCashDeposit(amount)) {
                System.out.println("ERROR: Cannot accept deposit at this time.");
                return false;
            }

            account.deposit(amount);
            printReceipt("DEPOSIT", amount);
            persistence.saveAccount(account);
            persistence.saveATMState(atmState);
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " deposited successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Deposit failed - " + e.getMessage());
            return false;
        }
    }

    public boolean transfer(String recipientCard, double amount) {
        try {
            if (amount <= 0) {
                System.out.println("ERROR: Transfer amount must be positive.");
                return false;
            }

            Account recipient = persistence.loadAccount(recipientCard);
            if (recipient == null) {
                System.out.println("ERROR: Recipient card not found.");
                return false;
            }

            if (!account.transfer(amount, recipientCard)) {
                System.out.println("ERROR: Insufficient balance or invalid amount.");
                return false;
            }

            recipient.receiveTransfer(amount, account.getCardNumber());
            printReceipt("TRANSFER", amount);
            persistence.saveAccount(account);
            persistence.saveAccount(recipient);
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " transferred to " + 
                maskCardNumber(recipientCard));
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Transfer failed - " + e.getMessage());
            return false;
        }
    }

    public void viewTransactionHistory() {
        account.printTransactionHistory(10);
    }

    public void changePin(String newPin) {
        if (newPin == null || newPin.length() < 4) {
            System.out.println("ERROR: PIN must be at least 4 digits.");
            return;
        }
        // In a real system, you would validate against security policies
        System.out.println("NOTE: PIN change functionality would be implemented with proper encryption.");
    }

    private void printReceipt(String transactionType, double amount) {
        if (atmState.getPaperTank().hasPaper()) {
            atmState.getPaperTank().useOne();
            System.out.println("\n========== RECEIPT ==========");
            System.out.println("Transaction: " + transactionType);
            System.out.println("Amount: $" + String.format("%.2f", amount));
            System.out.println("New Balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("Time: " + java.time.LocalDateTime.now());
            System.out.println("==============================\n");
        } else {
            System.out.println("WARNING: Out of paper. Receipt not printed.");
            atmState.getPaperTank().flagLowPaper();
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 4) return "****";
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
