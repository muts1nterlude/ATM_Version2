import services.*;
import persistence.JsonHandler;
import java.util.Scanner;

public class Main {
    private static ATMService atm;
    private static Scanner sc;
    private static boolean isLoggedIn = false;

    public static void main(String[] args) {
        try {
            sc = new Scanner(System.in);
            JsonHandler persistence = new JsonHandler();
            atm = new ATMService(persistence);

            displayWelcomeScreen();

            while (!isLoggedIn && sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.equalsIgnoreCase("1")) {
                    customerLogin();
                } else if (input.equalsIgnoreCase("2")) {
                    technicianLogin(persistence);
                } else if (input.equalsIgnoreCase("3")) {
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                    displayWelcomeScreen();
                }
            }

            if (isLoggedIn) {
                showCustomerMenu();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }
    }

    private static void displayWelcomeScreen() {
        System.out.println("\n");
        System.out.println("====================================");
        System.out.println("   WELCOME TO PROFESSIONAL ATM      ");
        System.out.println("        Version 2.0                 ");
        System.out.println("====================================");
        System.out.println("\nPlease select an option:");
        System.out.println("[1] Customer Login");
        System.out.println("[2] Technician Login");
        System.out.println("[3] Exit");
        System.out.print("Enter choice: ");
    }

    private static void customerLogin() {
        System.out.println("\n========== CUSTOMER LOGIN ==========");
        System.out.print("Insert Card Number: ");
        String card = sc.nextLine().trim();

        System.out.print("Enter PIN (4 digits): ");
        String pin = sc.nextLine().trim();

        if (atm.authenticate(card, pin)) {
            isLoggedIn = true;
            atm.checkBalance();
        } else {
            System.out.println("Login failed. Please try again.\n");
            displayWelcomeScreen();
        }
    }

    private static void technicianLogin(JsonHandler persistence) {
        System.out.println("\n========== TECHNICIAN LOGIN ==========");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        AuthService auth = new AuthService();
        if (auth.authenticate(username, password)) {
            System.out.println("Technician login successful!");
            showTechnicianMenu(persistence);
        } else {
            System.out.println("Invalid credentials.\n");
            displayWelcomeScreen();
        }
    }

    private static void showCustomerMenu() {
        while (isLoggedIn) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("[1] Check Balance");
            System.out.println("[2] Withdraw Cash");
            System.out.println("[3] Deposit Cash");
            System.out.println("[4] Transfer Funds");
            System.out.println("[5] View Transaction History");
            System.out.println("[6] Change PIN");
            System.out.println("[7] Logout");
            System.out.print("Select option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    atm.checkBalance();
                    break;
                case "2":
                    handleWithdraw();
                    break;
                case "3":
                    handleDeposit();
                    break;
                case "4":
                    handleTransfer();
                    break;
                case "5":
                    atm.viewTransactionHistory();
                    break;
                case "6":
                    handlePinChange();
                    break;
                case "7":
                    System.out.println("Thank you for using our ATM. Please take your card.");
                    isLoggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleWithdraw() {
        System.out.println("\n========== WITHDRAWAL ==========");
        System.out.println("Quick withdraw options:");
        System.out.println("[1] $20   [2] $50   [3] $100");
        System.out.println("[4] $200  [5] $500  [6] Custom Amount");
        System.out.print("Select option: ");

        String choice = sc.nextLine().trim();
        double amount = 0;

        switch (choice) {
            case "1": amount = 20; break;
            case "2": amount = 50; break;
            case "3": amount = 100; break;
            case "4": amount = 200; break;
            case "5": amount = 500; break;
            case "6":
                System.out.print("Enter amount: $");
                try {
                    amount = Double.parseDouble(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        atm.withdraw(amount);
    }

    private static void handleDeposit() {
        System.out.println("\n========== DEPOSIT ==========");
        System.out.print("Enter deposit amount: $");
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            atm.deposit(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    private static void handleTransfer() {
        System.out.println("\n========== TRANSFER FUNDS ==========");
        System.out.print("Recipient Card Number: ");
        String recipientCard = sc.nextLine().trim();

        System.out.print("Transfer Amount: $");
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            atm.transfer(recipientCard, amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    private static void handlePinChange() {
        System.out.println("\n========== CHANGE PIN ==========");
        System.out.print("Enter new PIN (4 digits): ");
        String newPin = sc.nextLine().trim();
        atm.changePin(newPin);
    }

    private static void showTechnicianMenu(JsonHandler persistence) {
        V2Technician tech = new V2Technician(persistence.loadATMState(), persistence);
        boolean inTechMenu = true;

        while (inTechMenu) {
            System.out.println("\n========== TECHNICIAN MENU ==========");
            System.out.println("[1] Display ATM Status");
            System.out.println("[2] Refill Paper");
            System.out.println("[3] Add Cash");
            System.out.println("[4] Collect Cash");
            System.out.println("[5] Update Firmware");
            System.out.println("[6] View Event Log");
            System.out.println("[7] Perform Maintenance");
            System.out.println("[8] Logout");
            System.out.print("Select option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    tech.displayStatus();
                    break;
                case "2":
                    tech.refillPaper();
                    System.out.println("Paper refilled successfully!");
                    break;
                case "3":
                    System.out.print("Enter cash amount to add: $");
                    try {
                        double amount = Double.parseDouble(sc.nextLine().trim());
                        tech.addCash(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                    }
                    break;
                case "4":
                    tech.collectCash();
                    break;
                case "5":
                    tech.updateFirmware();
                    break;
                case "6":
                    tech.viewEventLog();
                    break;
                case "7":
                    tech.performMaintenance();
                    System.out.println("Maintenance completed!");
                    break;
                case "8":
                    System.out.println("Technician logged out.");
                    inTechMenu = false;
                    displayWelcomeScreen();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
