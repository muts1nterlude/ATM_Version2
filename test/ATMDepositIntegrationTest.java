import services.ATMService;
import persistence.JsonHandler;

public class ATMDepositIntegrationTest {
    public static void main(String[] args) {
        ATMService atm = new ATMService(new JsonHandler());

        // Must authenticate first to set the 'account' object in ATMService
        atm.authenticate("1234567890", "1234");

        boolean deposited = atm.deposit(100);

        if (deposited) {
            System.out.println("✅ ATMDepositIntegrationTest PASSED");
        } else {
            System.out.println("❌ ATMDepositIntegrationTest FAILED");
        }
    }
}