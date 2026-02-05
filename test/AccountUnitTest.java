import core.Account;

public class AccountUnitTest {
    public static void main(String[] args) {
        // Initial balance 1000
        Account account = new Account("1234567890", "1234", 1000);

        account.deposit(500);               // balance = 1500
        // If your system uses banknotes, ensure this is a multiple of 20/50/100
        boolean withdrew = account.withdraw(200);

        // Assuming a $2.50 fee logic exists in your Account.java
        double expected = 1297.50;
        double actual = account.getBalance();

        if (withdrew && Math.abs(actual - expected) < 0.001) { // Use epsilon for double comparison
            System.out.println("✅ AccountUnitTest PASSED");
        } else {
            System.out.println("❌ AccountUnitTest FAILED");
            System.out.println("Expected: " + expected + ", Got: " + actual);
        }
    }
}