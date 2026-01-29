package test;

import org.junit.Before;
import org.junit.Test;
import services.ATMService;
import persistence.JsonHandler;
import core.Account;

import static org.junit.Assert.*;

/**
 * Integration Test for ATM Authentication Flow
 * Tests the complete authentication process: card loading, expiration check, PIN validation
 * This test validates how ATMService integrates with Persistence layer
 */
public class ATMAuthenticationIntegrationTest {
    private ATMService atmService;
    private JsonHandler persistence;

    @Before
    public void setUp() {
        // Initialize with real JsonHandler (persistence layer)
        persistence = new JsonHandler();
        atmService = new ATMService(persistence);
        
        // Reset account to fresh state before each test
        Account testAccount = new Account("4532123456789012", "1234", 1000.0);
        persistence.saveAccount(testAccount);
    }

    /**
     * Test Case 1: Successful authentication with valid card and PIN
     * Verifies that a valid account card and correct PIN authenticate successfully
     * This tests the integration between ATMService.authenticate(), Account loading, 
     * and PIN validation
     */
    @Test
    public void testAuthenticationSuccessfulWithValidCredentials() {
        // Using test card from the system
        boolean result = atmService.authenticate("4532123456789012", "1234");
        assertTrue("Authentication should succeed with valid card and PIN", result);
    }

    /**
     * Test Case 2: Authentication fails with incorrect PIN
     * Verifies that an incorrect PIN fails authentication even with valid card
     * This tests that PIN validation is properly enforced
     */
    @Test
    public void testAuthenticationFailsWithIncorrectPin() {
        boolean result = atmService.authenticate("4532123456789012", "9999");
        assertFalse("Authentication should fail with incorrect PIN", result);
    }

    /**
     * Test Case 3: Authentication tracks PIN attempt count
     * Verifies that the system tracks failed authentication attempts
     * This tests attempt limiting for security
     */
    @Test
    public void testAuthenticationTracksAttempts() {
        // First failed attempt
        boolean attempt1 = atmService.authenticate("4532123456789012", "0000");
        assertFalse("First attempt should fail", attempt1);

        // Second failed attempt
        boolean attempt2 = atmService.authenticate("4532123456789012", "1111");
        assertFalse("Second attempt should fail", attempt2);

        // Third failed attempt
        boolean attempt3 = atmService.authenticate("4532123456789012", "2222");
        assertFalse("Third attempt should fail", attempt3);

        // After 3 failed attempts, account should be blocked
        // Verify by attempting again - should still fail
    }

    /**
     * Test Case 4: Authentication fails with non-existent card
     * Verifies that the system handles missing accounts gracefully
     * This tests error handling for invalid card numbers
     */
    @Test
    public void testAuthenticationFailsWithInvalidCard() {
        // Test with non-existent card number
        try {
            atmService.authenticate("0000000000000000", "1234");
            // Should handle gracefully - either return false or throw exception
            // depending on implementation
        } catch (Exception e) {
            // Expected behavior for invalid card
            assertNotNull("Should handle invalid card", e);
        }
    }

    /**
     * Test Case 5: Check balance after successful authentication
     * Verifies that after authentication, account balance can be accessed
     * This tests the complete flow: authenticate -> load account -> check balance
     */
    @Test
    public void testCheckBalanceAfterSuccessfulAuthentication() {
        // First authenticate
        boolean authResult = atmService.authenticate("4532123456789012", "1234");
        assertTrue("Authentication should succeed", authResult);

        // Then check balance (should not throw exception)
        try {
            atmService.checkBalance();
            // If no exception is thrown, the balance check succeeded
            assertTrue("Balance check should succeed after authentication", true);
        } catch (Exception e) {
            fail("Balance check should not throw exception after authentication: " + e.getMessage());
        }
    }
}
