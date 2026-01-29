package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ATMState {
    private double cashAvailable;
    private String firmwareVersion;
    private PaperTank paperTank;
    private boolean isOperational;
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime lastCashRefillDate;
    private int totalTransactionsProcessed;
    private double totalAmountDispensed;
    private List<ATMEvent> eventLog;
    private static final double MAXIMUM_CASH_CAPACITY = 50000.0;
    private static final double MINIMUM_CASH_THRESHOLD = 500.0;

    public ATMState(double cashAvailable, String firmwareVersion, PaperTank paperTank) {
        this.cashAvailable = cashAvailable;
        this.firmwareVersion = firmwareVersion;
        this.paperTank = paperTank;
        this.isOperational = true;
        this.lastMaintenanceDate = LocalDateTime.now();
        this.lastCashRefillDate = LocalDateTime.now();
        this.totalTransactionsProcessed = 0;
        this.totalAmountDispensed = 0;
        this.eventLog = new ArrayList<>();
        logEvent("ATM_INITIALIZED", "Cash: $" + cashAvailable + " | Firmware: " + firmwareVersion);
    }

    public double getCashAvailable() { 
        return cashAvailable; 
    }

    public boolean addCash(double amount) {
        if (amount < 0 || (cashAvailable + amount) > MAXIMUM_CASH_CAPACITY) {
            logEvent("CASH_ADDITION_FAILED", "Amount: $" + amount);
            return false;
        }
        cashAvailable += amount;
        lastCashRefillDate = LocalDateTime.now();
        logEvent("CASH_ADDED", "$" + amount);
        return true;
    }

    public boolean addCashDeposit(double amount) {
        return addCash(amount);
    }

    public boolean dispenseCash(double amount) {
        if (amount > cashAvailable) {
            logEvent("CASH_DISPENSING_FAILED", "Requested: $" + amount + " | Available: $" + cashAvailable);
            return false;
        }
        if ((cashAvailable - amount) < MINIMUM_CASH_THRESHOLD) {
            logEvent("LOW_CASH_WARNING", "Remaining: $" + (cashAvailable - amount));
        }
        cashAvailable -= amount;
        totalAmountDispensed += amount;
        totalTransactionsProcessed++;
        logEvent("CASH_DISPENSED", "$" + amount);
        return true;
    }

    public void performMaintenance() {
        lastMaintenanceDate = LocalDateTime.now();
        logEvent("MAINTENANCE_PERFORMED", "System checked and verified");
    }

    public boolean isLowOnCash() {
        return cashAvailable < MINIMUM_CASH_THRESHOLD;
    }

    public boolean isNearCapacity() {
        return cashAvailable > (MAXIMUM_CASH_CAPACITY * 0.9);
    }

    public PaperTank getPaperTank() { 
        return paperTank; 
    }

    public String getFirmwareVersion() { 
        return firmwareVersion; 
    }

    public void updateFirmware(String version) { 
        firmwareVersion = version;
        logEvent("FIRMWARE_UPDATED", "New version: " + version);
    }

    public boolean isOperational() {
        return isOperational && !paperTank.isEmpty();
    }

    public void setOperational(boolean operational) {
        this.isOperational = operational;
        logEvent(operational ? "ATM_ENABLED" : "ATM_DISABLED", "");
    }

    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public LocalDateTime getLastCashRefillDate() {
        return lastCashRefillDate;
    }

    public int getTotalTransactionsProcessed() {
        return totalTransactionsProcessed;
    }

    public double getTotalAmountDispensed() {
        return totalAmountDispensed;
    }

    public void displayDiagnostics() {
        System.out.println("\n========== ATM DIAGNOSTICS ==========");
        System.out.println("Status: " + (isOperational ? "OPERATIONAL" : "OUT OF SERVICE"));
        System.out.println("Cash Available: $" + String.format("%.2f", cashAvailable));
        System.out.println("Cash Level: " + getCashPercentage() + "%");
        System.out.println("Paper Tank: " + paperTank.getPaperCount() + " sheets");
        System.out.println("Firmware Version: " + firmwareVersion);
        System.out.println("Total Transactions: " + totalTransactionsProcessed);
        System.out.println("Total Dispensed: $" + String.format("%.2f", totalAmountDispensed));
        System.out.println("Last Maintenance: " + lastMaintenanceDate);
        System.out.println("Last Cash Refill: " + lastCashRefillDate);
        if (isLowOnCash()) System.out.println("WARNING: Low cash level!");
        if (paperTank.isEmpty()) System.out.println("WARNING: Out of paper!");
        System.out.println("=====================================\n");
    }

    private int getCashPercentage() {
        return (int) ((cashAvailable / MAXIMUM_CASH_CAPACITY) * 100);
    }

    private void logEvent(String eventType, String details) {
        eventLog.add(new ATMEvent(eventType, details));
    }

    public void displayEventLog() {
        System.out.println("\n========== EVENT LOG ==========");
        for (ATMEvent event : eventLog) {
            System.out.println(event);
        }
        System.out.println("===============================\n");
    }

    public static class ATMEvent {
        private LocalDateTime timestamp;
        private String eventType;
        private String details;

        public ATMEvent(String eventType, String details) {
            this.timestamp = LocalDateTime.now();
            this.eventType = eventType;
            this.details = details;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s - %s", timestamp, eventType, details);
        }
    }
}
