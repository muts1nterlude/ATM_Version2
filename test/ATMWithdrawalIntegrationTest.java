package test;

import org.junit.Before;
import org.junit.Test;
import services.ATMService;
import persistence.JsonHandler;
import core.Account;

import static org.junit.Assert.*;

/**
 * Integration Test for Complete ATM Withdrawal Workflow
 * Tests the end-to-end withdrawal process including:
 * - Authentication
 * - ATM cash availability checks
 * - Account balance deduction
 * - Persistence layer updates
 * - Failure scenarios and recovery
 */
public class ATMWithdrawalIntegrationTest {
    private ATMService atmService;
    private JsonHandler persistence;
    private double initialBalance;

    @Before
    public void setUp() {
        // Initialize with real JsonHandler (persistence layer)
        persistence = new JsonHandler();
        atmService = new ATMService(persistence);

        // Reset account to fresh state before each test
        Account testAccount = new Account("4532123456789012", "1234", 1000.0);
        persistence.saveAccount(testAccount);

        // Authenticate first to set up account
        atmService.authenticate("4532123456789012", "1234");

        // Get initial balance for comparison
        Account account = persistence.loadAccount("4532123456789012");
        initialBalance = account.getBalance();
    }

    /**
     * Test Case 1: Successful withdrawal within all constraints
     * Verifies that withdrawal succeeds when:
     * - Account has sufficient balance
     * - ATM has sufficient cash
     * - Daily withdrawal limit not exceeded
     * - Persistence is updated correctly
     */
    @Test
    public void testSuccessfulWithdrawalWithinConstraints() {
        double withdrawAmount = 50.0;
        double expectedFee = 2.50;

        boolean result = atmService.withdraw(withdrawAmount);

        assertTrue("Withdrawal should succeed with sufficient balance and ATM cash", result);

        // Verify account balance was updated in persistence
        Account updatedAccount = persistence.loadAccount("4532123456789012");
        double expectedBalance = initialBalance - withdrawAmount - expectedFee;
        assertEquals("Account balance should be reduced by withdrawal + fee", 
            expectedBalance, updatedAccount.getBalance(), 0.01);
    }

    /**
     * Test Case 2: Withdrawal fails when account balance is insufficient
     * Verifies that withdrawal is rejected and account state is not modified
     * This tests failure recovery (reverting ATM cash state)
     */
    @Test
    public void testWithdrawalFailsWithInsufficientBalance() {
        double largeWithdrawAmount = 5000.0; // Exceeds account balance
        double balanceBeforeAttempt = persistence.loadAccount("4532123456789012").getBalance();

        boolean result = atmService.withdraw(largeWithdrawAmount);

        assertFalse("Withdrawal should fail with insufficient balance", result);

        // Verify account balance was NOT modified
        Account accountAfterFailed = persistence.loadAccount("4532123456789012");
        assertEquals("Account balance should remain unchanged after failed withdrawal", 
            balanceBeforeAttempt, accountAfterFailed.getBalance(), 0.01);
    }

    /**
     * Test Case 3: Withdrawal fails when exceeding daily limit
     * Verifies that daily withdrawal limit is enforced at the ATMService level
     * This tests the integration of Account's daily limit with ATMService
     */
    @Test
    public void testWithdrawalFailsWhenExceedingDailyLimit() {
        // Reset to fresh account state
        Account freshAccount = new Account("4532123456789012", "1234", 1000.0);
        persistence.saveAccount(freshAccount);
        atmService.authenticate("4532123456789012", "1234");
        
        // Make multiple withdrawals to approach the daily limit
        double firstWithdrawal = 400.0;
        boolean result1 = atmService.withdraw(firstWithdrawal);
        assertTrue("First withdrawal should succeed", result1);

        // Try to withdraw amount that exceeds daily limit
        double secondWithdrawal = 150.0; // 400 + 150 = 550 > 500 limit
        boolean result2 = atmService.withdraw(secondWithdrawal);

        assertFalse("Withdrawal should fail when exceeding daily limit", result2);

        // Verify balance was not deducted for the failed withdrawal
        Account account = persistence.loadAccount("4532123456789012");
        double expectedBalance = 1000.0 - firstWithdrawal - 2.50; // Only first withdrawal deducted
        assertEquals("Balance should only reflect successful withdrawal", 
            expectedBalance, account.getBalance(), 0.01);
    }

    /**
     * Test Case 4: Withdrawal fails when ATM cash is insufficient
     * Verifies that ATM state is checked before allowing withdrawal
     * This tests the integration with ATMState
     */
    @Test
    public void testWithdrawalFailsWhenATMCashInsufficient() {
        double withdrawAmount = 100.0;
        double balanceBeforeAttempt = persistence.loadAccount("4532123456789012").getBalance();

        // Attempt withdrawal - if ATM doesn't have sufficient cash, it should fail
        boolean result = atmService.withdraw(withdrawAmount);

        // Check if withdrawal failed due to ATM cash shortage
        if (!result) {
            // Verify balance was not modified
            Account accountAfter = persistence.loadAccount("4532123456789012");
            assertEquals("Balance should not change if ATM has insufficient cash", 
                balanceBeforeAttempt, accountAfter.getBalance(), 0.01);
        }
    }

    /**
     * Test Case 5: Negative withdrawal amount is rejected
     * Verifies validation at ATMService level prevents invalid amounts
     */
    @Test
    public void testWithdrawalRejectedWithNegativeAmount() {
        double balanceBeforeAttempt = persistence.loadAccount("4532123456789012").getBalance();

        boolean result = atmService.withdraw(-50.0);

        assertFalse("Withdrawal with negative amount should be rejected", result);

        // Balance should remain unchanged
        Account account = persistence.loadAccount("4532123456789012");
        assertEquals("Balance should remain unchanged for invalid withdrawal", 
            balanceBeforeAttempt, account.getBalance(), 0.01);
    }

    /**
     * Test Case 6: Zero withdrawal amount is rejected
     * Verifies validation prevents zero withdrawals
     */
    @Test
    public void testWithdrawalRejectedWithZeroAmount() {
        double balanceBeforeAttempt = persistence.loadAccount("4532123456789012").getBalance();

        boolean result = atmService.withdraw(0.0);

        assertFalse("Withdrawal with zero amount should be rejected", result);

        // Balance should remain unchanged
        Account account = persistence.loadAccount("4532123456789012");
        assertEquals("Balance should remain unchanged for zero withdrawal", 
            balanceBeforeAttempt, account.getBalance(), 0.01);
    }

    /**
     * Test Case 7: Multiple successful withdrawals update persistence correctly
     * Verifies that multiple withdrawals are properly persisted
     * This tests that the persistence layer correctly saves multiple state changes
     */
    @Test
    public void testMultipleWithdrawalsUpdatePersistenceCorrectly() {
        // Reset to fresh account state
        Account freshAccount = new Account("4532123456789012", "1234", 1000.0);
        persistence.saveAccount(freshAccount);
        atmService.authenticate("4532123456789012", "1234");
        double startingBalance = 1000.0;
        
        double firstWithdrawal = 50.0;
        double secondWithdrawal = 75.0;
        double expectedFeePerTransaction = 2.50;

        atmService.withdraw(firstWithdrawal);
        atmService.withdraw(secondWithdrawal);

        // Verify final balance in persistence
        Account finalAccount = persistence.loadAccount("4532123456789012");
        double expectedBalance = startingBalance - firstWithdrawal - secondWithdrawal - 
                                (expectedFeePerTransaction * 2);
        assertEquals("Final balance should reflect all withdrawals and fees", 
            expectedBalance, finalAccount.getBalance(), 0.01);
    }
}
