import core.Account;

public class AccountUnitTest {

    public static void main(String[] args) {

        Account account = new Account("1234567890", "1234", 1000);

        account.deposit(500);               // balance = 1500
        boolean withdrew = account.withdraw(200); // + fee 2.50 → 1297.50

        double expected = 1297.50;
        double actual = account.getBalance();

        if (withdrew && actual == expected) {
            System.out.println("✅ AccountUnitTest PASSED");
        } else {
            System.out.println("❌ AccountUnitTest FAILED");
            System.out.println("Expected: " + expected + ", Got: " + actual);
        }
    }
}
