package test;

import org.junit.Before;
import org.junit.Test;
import core.Account;

import static org.junit.Assert.*;

/**
 * Unit Test for Account Withdrawal Logic
 * Tests withdrawal validation, daily limits, fees, and balance updates
 */
public class AccountWithdrawalTest {
    private Account account;
    private static final double WITHDRAWAL_FEE = 2.50;
    private static final double INITIAL_BALANCE = 1000.0;

    @Before
    public void setUp() {
        account = new Account("4532123456789012", "1234", INITIAL_BALANCE);
    }

    /**
     * Test Case 1: Successful withdrawal within limits
     * Verifies that a valid withdrawal reduces balance correctly (including fee)
     */
    @Test
    public void testWithdrawalSuccessfulWithinLimits() {
        double withdrawAmount = 100.0;
        boolean result = account.withdraw(withdrawAmount);

        assertTrue("Withdrawal should succeed with sufficient balance and within limit", result);
        double expectedBalance = INITIAL_BALANCE - withdrawAmount - WITHDRAWAL_FEE;
        assertEquals("Balance should be reduced by withdrawal amount plus fee", 
            expectedBalance, account.getBalance(), 0.01);
    }

    /**
     * Test Case 2: Withdrawal fails due to insufficient balance
     * Verifies that withdrawal is rejected when account has insufficient funds
     */
    @Test
    public void testWithdrawalFailsWithInsufficientBalance() {
        double withdrawAmount = 999.0; // Would need 999 + 2.50 = 1001.50
        boolean result = account.withdraw(withdrawAmount);

        assertFalse("Withdrawal should fail when balance is insufficient (including fee)", result);
        assertEquals("Balance should remain unchanged after failed withdrawal", 
            INITIAL_BALANCE, account.getBalance(), 0.01);
    }

    /**
     * Test Case 3: Withdrawal fails due to exceeding daily limit
     * Verifies that daily limit constraint is enforced
     */
    @Test
    public void testWithdrawalFailsWhenExceedingDailyLimit() {
        double withdrawAmount = 501.0; // Exceeds daily limit of 500.0
        boolean result = account.withdraw(withdrawAmount);

        assertFalse("Withdrawal should fail when exceeding daily limit", result);
        assertEquals("Balance should remain unchanged when daily limit exceeded", 
            INITIAL_BALANCE, account.getBalance(), 0.01);
    }

    /**
     * Test Case 4: Multiple withdrawals up to daily limit succeed
     * Verifies that daily limit is tracked across multiple withdrawals
     */
    @Test
    public void testMultipleWithdrawalsWithinDailyLimit() {
        // First withdrawal: 200
        boolean result1 = account.withdraw(200.0);
        assertTrue("First withdrawal should succeed", result1);

        // Second withdrawal: 200
        boolean result2 = account.withdraw(200.0);
        assertTrue("Second withdrawal should succeed", result2);

        // Third withdrawal: 100 (total = 500, at limit)
        boolean result3 = account.withdraw(100.0);
        assertTrue("Third withdrawal should succeed (at daily limit)", result3);

        // Fourth withdrawal: 1.0 (would exceed limit)
        boolean result4 = account.withdraw(1.0);
        assertFalse("Fourth withdrawal should fail (exceeds daily limit)", result4);

        // Balance should reflect first 3 withdrawals minus 3 fees
        double expectedBalance = INITIAL_BALANCE - 500.0 - (WITHDRAWAL_FEE * 3);
        assertEquals("Balance should reflect 3 successful withdrawals", 
            expectedBalance, account.getBalance(), 0.01);
    }

    /**
     * Test Case 5: Withdrawal fee is correctly applied
     * Verifies that the withdrawal fee is properly deducted from balance
     */
    @Test
    public void testWithdrawalFeeCorrectlyApplied() {
        double withdrawAmount = 50.0;
        account.withdraw(withdrawAmount);

        double expectedBalance = INITIAL_BALANCE - withdrawAmount - WITHDRAWAL_FEE;
        assertEquals("Balance should reflect withdrawal amount plus fee", 
            expectedBalance, account.getBalance(), 0.01);
    }

    /**
     * Test Case 6: Negative withdrawal amount is rejected
     * Verifies that withdrawal validation prevents negative amounts
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawalWithNegativeAmount() {
        account.withdraw(-50.0);
    }

    /**
     * Test Case 7: Zero withdrawal amount is rejected
     * Verifies that withdrawal validation prevents zero amounts
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawalWithZeroAmount() {
        account.withdraw(0.0);
    }
}
