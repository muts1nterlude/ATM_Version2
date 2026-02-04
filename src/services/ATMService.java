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
        account = persistence.loadAccount(card);
        if (account == null) {
            System.out.println("ERROR: Card not found.");
            return false;
        }

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
        System.out.println("Daily Limit: $" + String.format("%.2f", account.getDailyWithdrawalLimit()));
        System.out.println("Used Today: $" + String.format("%.2f", account.getDailyWithdrawalUsed()));
        System.out.println("=========================================\n");
    }

    // FIXED: Only ONE withdraw method now
    public boolean withdraw(double amount) {
        try {
            if (amount <= 0) {
                System.out.println("ERROR: Withdrawal amount must be positive.");
                return false;
            }

            // Validation for banknotes (Smallest note is $20, so must be multiple of 10 or 20)
            if (amount % 10 != 0) {
                System.out.println("ERROR: This ATM only dispenses $20, $50, and $100 bills.");
                return false;
            }

            // 1. Try to dispense physical cash first
            if (!atmState.dispenseCash(amount)) {
                System.out.println("ERROR: ATM cannot dispense this amount with available bills.");
                return false;
            }

            // 2. Try to deduct from digital account
            if (!account.withdraw(amount)) {
                // REVERT physical cash if bank balance check fails
                atmState.addCash(amount);
                System.out.println("ERROR: Insufficient balance or daily limit exceeded.");
                return false;
            }

            printReceipt("WITHDRAWAL", amount);
            persistence.saveAccount(account);
            persistence.saveATMState(atmState);
            System.out.println("SUCCESS: Please collect your cash.");
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
                System.out.println("ERROR: ATM capacity reached. Cannot accept deposit.");
                return false;
            }

            account.deposit(amount);
            printReceipt("DEPOSIT", amount);
            persistence.saveAccount(account);
            persistence.saveATMState(atmState);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: Deposit failed.");
            return false;
        }
    }

    public boolean transfer(String recipientCard, double amount) {
        try {
            Account recipient = persistence.loadAccount(recipientCard);
            if (recipient == null) {
                System.out.println("ERROR: Recipient not found.");
                return false;
            }

            if (account.transfer(amount, recipientCard)) {
                recipient.receiveTransfer(amount, account.getCardNumber());
                persistence.saveAccount(account);
                persistence.saveAccount(recipient);
                printReceipt("TRANSFER", amount);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void viewTransactionHistory() {
        account.printTransactionHistory(10);
    }

    public void changePin(String newPin) {
        if (newPin != null && newPin.length() >= 4) {
            account.setPin(newPin);
            persistence.saveAccount(account);
            System.out.println("SUCCESS: PIN updated.");
        } else {
            System.out.println("ERROR: Invalid PIN format.");
        }
    }

    private void printReceipt(String type, double amount) {
        if (atmState.getPaperTank().useOne()) {
            System.out.println("\n--- RECEIPT: " + type + " ---");
            System.out.println("Amount: $" + amount);
            System.out.println("Balance: $" + account.getBalance());
            System.out.println("---------------------------\n");
        } else {
            System.out.println("WARNING: Receipt could not be printed (No Paper).");
        }
    }

    private String maskCardNumber(String card) {
        return card.length() > 4 ? "****" + card.substring(card.length() - 4) : "****";
    }
}