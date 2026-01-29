package services;

import core.ATMState;
import interfaces.TechActions;

public class V1Technician implements TechActions {

    protected ATMState state;

    public V1Technician(ATMState state) {
        this.state = state;
    }

    @Override
    public void displayStatus() {
        System.out.println("\n========== ATM STATUS (V1) ==========");
        System.out.println("Cash Available: $" + String.format("%.2f", state.getCashAvailable()));
        System.out.println("Paper Tank: " + state.getPaperTank().getSheets() + " sheets");
        System.out.println("Firmware Version: " + state.getFirmwareVersion());
        System.out.println("Operational: " + (state.isOperational() ? "YES" : "NO"));
        System.out.println("====================================\n");
    }

    @Override
    public void collectCash() {
        System.out.println("ERROR: Cash collection not allowed in V1.");
    }

    @Override
    public void refillPaper() {
        System.out.println("ERROR: Paper refill not allowed in V1.");
    }

    @Override
    public void updateFirmware() {
        System.out.println("ERROR: Firmware updates not allowed in V1.");
    }

    public void performDiagnostics() {
        state.displayDiagnostics();
    }

    public void reportIssue(String issue) {
        System.out.println("[V1 REPORT] Issue: " + issue);
    }
}
