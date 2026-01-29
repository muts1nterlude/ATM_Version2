@echo off
REM Run all unit and integration tests for ATM Machine
REM This script compiles and runs all 4 test files

echo Compiling all Java files...
javac -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/json-20231013.jar" -d bin src/Main.java src/core/Account.java src/core/ATMState.java src/core/PaperTank.java src/interfaces/Persistence.java src/interfaces/TechActions.java src/persistence/JsonHandler.java src/services/ATMService.java src/services/AuthService.java src/services/V1Technician.java src/services/V2Technician.java test/AuthServiceTest.java test/AccountWithdrawalTest.java test/ATMAuthenticationIntegrationTest.java test/ATMWithdrawalIntegrationTest.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo ============================================
echo Running all 4 Test Suites...
echo ============================================
echo.

java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/json-20231013.jar;bin" org.junit.runner.JUnitCore test.AuthServiceTest test.AccountWithdrawalTest test.ATMAuthenticationIntegrationTest test.ATMWithdrawalIntegrationTest

if %ERRORLEVEL% equ 0 (
    echo.
    echo ============================================
    echo SUCCESS: All tests passed!
    echo ============================================
) else (
    echo.
    echo ============================================
    echo FAILURE: Some tests failed!
    echo ============================================
)

pause
