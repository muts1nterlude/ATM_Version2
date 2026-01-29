package test;

import org.junit.Before;
import org.junit.Test;
import services.AuthService;

import static org.junit.Assert.*;

/**
 * Unit Test for AuthService
 * Tests the authentication logic for valid and invalid credentials
 */
public class AuthServiceTest {
    private AuthService authService;

    @Before
    public void setUp() {
        authService = new AuthService();
    }

    /**
     * Test Case 1: Valid credentials should return true
     * Verifies that the correct username and password authenticate successfully
     */
    @Test
    public void testAuthenticateWithValidCredentials() {
        boolean result = authService.authenticate("admin", "1234");
        assertTrue("Authentication should succeed with valid credentials", result);
    }

    /**
     * Test Case 2: Invalid username should return false
     * Verifies that incorrect username fails authentication
     */
    @Test
    public void testAuthenticateWithInvalidUsername() {
        boolean result = authService.authenticate("wronguser", "1234");
        assertFalse("Authentication should fail with invalid username", result);
    }

    /**
     * Test Case 3: Invalid password should return false
     * Verifies that incorrect password fails authentication
     */
    @Test
    public void testAuthenticateWithInvalidPassword() {
        boolean result = authService.authenticate("admin", "wrongpass");
        assertFalse("Authentication should fail with invalid password", result);
    }

    /**
     * Test Case 4: Both invalid should return false
     * Verifies that neither valid username nor password is a substitute
     */
    @Test
    public void testAuthenticateWithBothInvalid() {
        boolean result = authService.authenticate("wronguser", "wrongpass");
        assertFalse("Authentication should fail when both username and password are wrong", result);
    }

    /**
     * Test Case 5: Empty strings should return false
     * Verifies that empty credentials do not authenticate
     */
    @Test
    public void testAuthenticateWithEmptyCredentials() {
        boolean result = authService.authenticate("", "");
        assertFalse("Authentication should fail with empty credentials", result);
    }
}
