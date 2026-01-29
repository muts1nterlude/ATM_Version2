package services;

import core.ATMState;
import interfaces.Persistence;
import interfaces.TechActions;

public class V2Technician implements TechActions {

    private ATMState state;              
    private Persistence persistence;

    public V2Technician(ATMState state, Persistence persistence) {
        this.state = state;              
        this.persistence = persistence;
    }

    @Override
    public void displayStatus() {
        state.displayDiagnostics();
    }

    @Override
    public void addCash(double amount) {
        if (state.addCash(amount)) {
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " added to cash supply");
            persistence.saveATMState(state);
        } else {
            System.out.println("ERROR: Cannot add cash. Cash capacity may be exceeded.");
        }
    }

    @Override
    public void collectCash() {
        double amount = state.getCashAvailable();
        if (amount > 0) {
            System.out.println("Collecting $" + String.format("%.2f", amount) + " from ATM...");
            state.addCash(-amount);
            System.out.println("SUCCESS: Cash collected");
            persistence.saveATMState(state);
        } else {
            System.out.println("No cash available to collect.");
        }
    }

    @Override
    public void refillPaper() {
        state.getPaperTank().refill(500);
        System.out.println(
            "SUCCESS: Paper refilled to " + state.getPaperTank().getPaperCount() + " sheets"
        );
        persistence.saveATMState(state);
    }

    @Override
    public void updateFirmware() {
        String newVersion = "v2.0.1";
        state.updateFirmware(newVersion);
        System.out.println("SUCCESS: Firmware updated to " + newVersion);
        persistence.saveATMState(state);
    }

    @Override
    public void performMaintenance() {
        state.performMaintenance();
        persistence.saveATMState(state);
        System.out.println("SUCCESS: Maintenance completed");
    }

    public void viewEventLog() {
        state.displayEventLog();
    }

    public void enableATM() {
        state.setOperational(true);
        persistence.saveATMState(state);
        System.out.println("SUCCESS: ATM enabled");
    }

    public void disableATM() {
        state.setOperational(false);
        persistence.saveATMState(state);
        System.out.println("SUCCESS: ATM disabled");
    }

    public void checkSystemHealth() {
        System.out.println("\n========== SYSTEM HEALTH CHECK ==========");

        System.out.print("Cash Level: ");
        if (state.isLowOnCash()) {
            System.out.println("⚠ LOW - Refill required!");
        } else if (state.isNearCapacity()) {
            System.out.println("⚠ HIGH - Collection may be needed");
        } else {
            System.out.println("✓ NORMAL");
        }

        System.out.print("Paper Level: ");
        if (state.getPaperTank().isEmpty()) {
            System.out.println("✗ CRITICAL - Out of paper!");
        } else if (state.getPaperTank().isLowOnPaper()) {
            System.out.println("⚠ LOW - Refill soon");
        } else {
            System.out.println("✓ NORMAL");
        }

        System.out.println(
            "ATM Status: " + (state.isOperational() ? "✓ OPERATIONAL" : "✗ OUT OF SERVICE")
        );
        System.out.println("=========================================\n");
    }
}