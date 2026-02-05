package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ATMState {
    // Banknote counts
    private int count100;
    private int count50;
    private int count20;
    private int inkLevel;

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

    public ATMState(int c100, int c50, int c20, String firmwareVersion, PaperTank paperTank) {
        this.count100 = c100;
        this.count50 = c50;
        this.count20 = c20;
        this.inkLevel = 15;
        this.firmwareVersion = firmwareVersion;
        this.paperTank = paperTank;
        this.isOperational = true;
        this.lastMaintenanceDate = LocalDateTime.now();
        this.lastCashRefillDate = LocalDateTime.now();
        this.totalTransactionsProcessed = 0;
        this.totalAmountDispensed = 0;
        this.eventLog = new ArrayList<>();
        logEvent("ATM_INITIALIZED", "System ready with mixed denominations.");
    }


    public ATMState(double cashAvailable, String firmwareVersion, PaperTank paperTank) {
        this.count100 = 0;
        this.count50 = (int) (cashAvailable / 50); // Distribute total cash into $50 notes
        this.count20 = 0;
        this.firmwareVersion = firmwareVersion;
        this.paperTank = paperTank;
        this.isOperational = true;
        this.lastMaintenanceDate = LocalDateTime.now();
        this.lastCashRefillDate = LocalDateTime.now();
        this.eventLog = new ArrayList<>();
    }

    public double getCashAvailable() {
        return (count100 * 100) + (count50 * 50) + (count20 * 20);
    }

    public boolean dispenseCash(double amount) {
        int remaining = (int) amount;

        // Validation: Must be a multiple of the smallest note
        if (remaining % 10 != 0 || remaining > getCashAvailable()) return false;

        // Greedy algorithm to calculate notes
        int t100 = Math.min(remaining / 100, count100);
        int remAfter100 = remaining - (t100 * 100);

        int t50 = Math.min(remAfter100 / 50, count50);
        int remAfter50 = remAfter100 - (t50 * 50);

        int t20 = Math.min(remAfter50 / 20, count20);
        int remAfter20 = remAfter50 - (t20 * 20);

        // Success only if we reached exactly zero
        if (remAfter20 == 0) {
            this.count100 -= t100;
            this.count50 -= t50;
            this.count20 -= t20;
            this.totalAmountDispensed += amount;
            this.totalTransactionsProcessed++;
            logEvent("CASH_DISPENSED", String.format("Dispensed: $100x%d, $50x%d, $20x%d", t100, t50, t20));
            return true;
        }
        return false;
    }
    //note-based deposits
    public boolean addCash(int c100, int c50, int c20) {
        double newTotal = getCashAvailable() + (c100 * 100) + (c50 * 50) + (c20 * 20);
        if (newTotal > MAXIMUM_CASH_CAPACITY) return false;

        this.count100 += c100;
        this.count50 += c50;
        this.count20 += c20;
        this.lastCashRefillDate = LocalDateTime.now();
        return true;
    }

    public boolean addCash(double totalAmount) {
        // Fallback: assume deposit is mostly 50s and 20s if just a double is given
        return addCash(0, (int)totalAmount/50, (int)(totalAmount%50)/20);
    }

    public boolean addCashDeposit(double amount) { return addCash(amount); }

    public int getInkLevel() { return inkLevel; }
    public void setInkLevel(int inkLevel) { this.inkLevel = inkLevel; }
    public void displayDiagnostics() {

        System.out.println("\n========== ATM DIAGNOSTICS ==========");
        System.out.println("Status: " + (isOperational ? "OPERATIONAL" : "OUT OF SERVICE"));
        System.out.println("Firmware: " + firmwareVersion);
        System.out.println("Ink Level: [" + inkLevel + "%]");
        System.out.println("Total Cash: $" + String.format("%.2f", getCashAvailable()));
        System.out.println("Inventory: [$100 x " + count100 + "] [$50 x " + count50 + "] [$20 x " + count20 + "]");
        System.out.println("Paper Tank: " + paperTank.getPaperCount() + " sheets");
        System.out.println("=====================================\n");
    }

    private void logEvent(String eventType, String details) {
        eventLog.add(new ATMEvent(eventType, details));
    }

    public void addCash(int denomination, int count) {
        if (denomination == 100) this.count100 += count;
        else if (denomination == 50) this.count50 += count;
        else if (denomination == 20) this.count20 += count;
        logEvent("CASH_ADDED", "Technician added " + count + " notes of $" + denomination);
    }


    public String getFirmwareVersion() {
        return this.firmwareVersion;
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
        public String toString() { return String.format("[%s] %s - %s", timestamp, eventType, details); }
    }

    public int getCount100() {
        return this.count100;
    }

    public int getCount50() {
        return this.count50;
    }

    public int getCount20() {
        return this.count20;
    }



    public PaperTank getPaperTank() { return paperTank; }
    public boolean isOperational() { return isOperational && !paperTank.isEmpty(); }
    public void setOperational(boolean op) { this.isOperational = op; }
    public void performMaintenance() { this.lastMaintenanceDate = LocalDateTime.now(); }
    public void updateFirmware(String v) { this.firmwareVersion = v; }
    public boolean isLowOnCash() { return getCashAvailable() < MINIMUM_CASH_THRESHOLD; }
    public boolean isNearCapacity() { return getCashAvailable() > (MAXIMUM_CASH_CAPACITY * 0.9); }
    public void displayEventLog() {
        for(ATMEvent e : eventLog) System.out.println(e);
    }
}