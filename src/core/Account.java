package core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String cardNumber;
    private String pin;
    private double balance;
    private LocalDate cardExpirationDate;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawalUsed;
    private LocalDate lastWithdrawalDate;
    private List<Transaction> transactionHistory;

    private static final double WITHDRAWAL_FEE = 2.50;
    private static final double TRANSFER_FEE = 1.00;
    private static final double DEFAULT_DAILY_LIMIT = 500.0;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.cardExpirationDate = LocalDate.now().plusYears(5);
        this.dailyWithdrawalLimit = DEFAULT_DAILY_LIMIT;
        this.dailyWithdrawalUsed = 0;
        this.lastWithdrawalDate = LocalDate.now();
        this.transactionHistory = new ArrayList<>();
    }

    // FOR UNIT TEST
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public LocalDate getCardExpirationDate() { return cardExpirationDate; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }
    public double getDailyWithdrawalUsed() { return dailyWithdrawalUsed; }
    public double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }

    public boolean isCardExpired() {
        return LocalDate.now().isAfter(cardExpirationDate);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        addTransaction("DEPOSIT", amount, "Deposit successful");
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (!lastWithdrawalDate.equals(LocalDate.now())) {
            dailyWithdrawalUsed = 0;
            lastWithdrawalDate = LocalDate.now();
        }

        if (dailyWithdrawalUsed + amount > dailyWithdrawalLimit) {
            return false;
        }

        double totalAmount = amount + WITHDRAWAL_FEE;
        if (totalAmount > balance) {
            return false;
        }

        balance -= totalAmount;
        dailyWithdrawalUsed += amount;
        addTransaction("WITHDRAWAL", amount, "Withdrawal fee: $" + WITHDRAWAL_FEE);
        return true;
    }

    public boolean transfer(double amount, String recipientCard) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        double totalAmount = amount + TRANSFER_FEE;
        if (totalAmount > balance) {
            return false;
        }

        balance -= totalAmount;
        addTransaction("TRANSFER_OUT", amount,
                "Transfer to: " + recipientCard + " | Fee: $" + TRANSFER_FEE);
        return true;
    }

    public void receiveTransfer(double amount, String senderCard) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        balance += amount;
        addTransaction("TRANSFER_IN", amount, "Transfer from: " + senderCard);
    }

    private void addTransaction(String type, double amount, String details) {
        transactionHistory.add(new Transaction(type, amount, balance, details));
    }

    public void printTransactionHistory(int limit) {
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        int count = 0;
        for (int i = transactionHistory.size() - 1; i >= 0 && count < limit; i--) {
            System.out.println(transactionHistory.get(i));
            count++;
        }
        System.out.println("=========================================\n");
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    public static class Transaction {
        private String type;
        private double amount;
        private double balanceAfter;
        private LocalDateTime timestamp;
        private String details;

        public Transaction(String type, double amount, double balanceAfter, String details) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.timestamp = LocalDateTime.now();
            this.details = details;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s | Amount: $%.2f | Balance: $%.2f | %s",
                    timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    type, amount, balanceAfter, details);
        }
    }
}
