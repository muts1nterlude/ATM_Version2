package services;

import core.ATMState;
import interfaces.Persistence;

public class V2Technician {

    private Persistence persistence;

    public V2Technician(Persistence persistence) {
        this.persistence = persistence;
    }

    // This method handles the display logic for Option [1]
    public void displayStatus() {
        ATMState state = persistence.loadATMState();
        System.out.println("\n--- ATM HARDWARE STATUS ---");
        System.out.println("Firmware Version: " + state.getFirmwareVersion());
        System.out.println("Paper Level:      " + state.getPaperTank().getSheets() + " sheets");
        System.out.println("Cash Inventory:");
        System.out.println("Ink Level:        " + state.getInkLevel() + "%");
        System.out.println(" - $100 bills: " + state.getCount100());
        System.out.println(" - $50 bills:  " + state.getCount50());
        System.out.println(" - $20 bills:  " + state.getCount20());
        System.out.println("---------------------------\n");
    }

    // Handles Option [2]
    public void refillPaper() {
        ATMState state = persistence.loadATMState();
        state.getPaperTank().refill(); // Assuming your PaperTank has a refill() method
        persistence.saveATMState(state);
        System.out.println("Paper refilled successfully.");
    }

    // Handles Option [3] using the double amount from your Main.java
    public void addCash(double amount) {
        ATMState state = persistence.loadATMState();
        // Since your hardware logic uses specific bills, we'll split the amount
        // For the demo, let's assume we add it all in $50 bills
        int count = (int) amount / 50;
        state.addCash(50, count);
        persistence.saveATMState(state);
        System.out.println("Added " + count + " x $50 bills to the inventory.");
    }

    // Handles Option [5]
    public void updateFirmware() {
        ATMState state = persistence.loadATMState();
        state.updateFirmware("v2.1.0"); // Update the string in state
        persistence.saveATMState(state);
        System.out.println("Firmware updated to v2.1.0.");
    }

    public void collectCash() {
        System.out.println("Cash collected from deposit bin.");
    }

    public void viewEventLog() {
        System.out.println("--- SYSTEM EVENT LOG ---");
        System.out.println("[INFO] Boot sequence completed.");
        System.out.println("[INFO] Technician Login: admin");
    }

    public void performMaintenance() {
        // 1. Load the current state from the file
        ATMState state = persistence.loadATMState();

        // 2. Perform the 'repairs'
        state.setInkLevel(100);
        state.getPaperTank().refill();
        state.setOperational(true);

        persistence.saveATMState(state);

        System.out.println("Maintenance Complete: Ink and Paper are now at 100%.");
    }
}