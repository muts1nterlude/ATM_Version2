package services;

import core.ATMState;
import interfaces.Persistence;

public class V2Technician {

    private Persistence persistence;

    public V2Technician(Persistence persistence) {
        this.persistence = persistence;
    }

    public void addCash(int denomination, int count) {
        ATMState state = persistence.loadATMState();
        state.addCash(denomination, count);
        persistence.saveATMState(state);
    }

    public void collectCash() {
        System.out.println("Cash collected.");
    }

    public void refillPaper() {
        System.out.println("Paper refilled.");
    }

    public void updateFirmware() {
        System.out.println("Firmware updated.");
    }

    public void displayStatus() {
    }

    public void addCash(double amount) {
    }

    public void viewEventLog() {
    }

    public void performMaintenance() {
    }
}
