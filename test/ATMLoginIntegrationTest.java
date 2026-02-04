import services.ATMService;
import persistence.JsonHandler;

public class ATMLoginIntegrationTest {

    public static void main(String[] args) {

        ATMService atm = new ATMService(new JsonHandler());

        boolean loggedIn = atm.authenticate("1234567890", "1234");

        if (loggedIn) {
            System.out.println("✅ ATMLoginIntegrationTest PASSED");
        } else {
            System.out.println("❌ ATMLoginIntegrationTest FAILED");
        }
    }
}
