import core.Account;

public class AccountPinUnitTest {

    public static void main(String[] args) {

        Account account = new Account("1234567890", "1234", 500);

        boolean correct = account.validatePin("1234");
        boolean wrong = account.validatePin("9999");

        if (correct && !wrong) {
            System.out.println("✅ AccountPinUnitTest PASSED");
        } else {
            System.out.println("❌ AccountPinUnitTest FAILED");
        }
    }
}
