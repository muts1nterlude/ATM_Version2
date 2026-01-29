import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.AuthServiceTest;
import test.AccountWithdrawalTest;
import test.ATMAuthenticationIntegrationTest;
import test.ATMWithdrawalIntegrationTest;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("      ATM MACHINE - TEST SUITE RUNNER");
        System.out.println("=".repeat(60) + "\n");

        JUnitCore junit = new JUnitCore();

        // Run each test suite
        System.out.println("[1/4] Running AuthServiceTest...");
        Result result1 = junit.run(AuthServiceTest.class);
        printResults("AuthServiceTest", result1);

        System.out.println("\n[2/4] Running AccountWithdrawalTest...");
        Result result2 = junit.run(AccountWithdrawalTest.class);
        printResults("AccountWithdrawalTest", result2);

        System.out.println("\n[3/4] Running ATMAuthenticationIntegrationTest...");
        Result result3 = junit.run(ATMAuthenticationIntegrationTest.class);
        printResults("ATMAuthenticationIntegrationTest", result3);

        System.out.println("\n[4/4] Running ATMWithdrawalIntegrationTest...");
        Result result4 = junit.run(ATMWithdrawalIntegrationTest.class);
        printResults("ATMWithdrawalIntegrationTest", result4);

        // Summary
        int totalTests = result1.getRunCount() + result2.getRunCount() + result3.getRunCount() + result4.getRunCount();
        int totalFailures = result1.getFailureCount() + result2.getFailureCount() + result3.getFailureCount() + result4.getFailureCount();
        int totalPassed = totalTests - totalFailures;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    FINAL SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Total Tests Run:     " + totalTests);
        System.out.println("Passed:              " + totalPassed);
        System.out.println("Failed:              " + totalFailures);

        if (totalFailures == 0) {
            System.out.println("\n✓ ALL TESTS PASSED SUCCESSFULLY!");
        } else {
            System.out.println("\n✗ SOME TESTS FAILED - See details above");
        }
        System.out.println("=".repeat(60) + "\n");

        System.exit(totalFailures == 0 ? 0 : 1);
    }

    private static void printResults(String testName, Result result) {
        if (result.wasSuccessful()) {
            System.out.println("  ✓ PASSED - " + result.getRunCount() + " tests");
        } else {
            System.out.println("  ✗ FAILED - " + result.getFailureCount() + " failure(s)");
            for (Failure failure : result.getFailures()) {
                System.out.println("    - " + failure.getTestHeader());
                System.out.println("      " + failure.getMessage());
            }
        }
    }
}
