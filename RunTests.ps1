# Run all unit and integration tests for ATM Machine
# This script compiles and runs all 4 test files

Write-Host "Compiling all Java files..." -ForegroundColor Cyan

$compileCmd = 'javac -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/json-20231013.jar" -d bin src/Main.java src/core/Account.java src/core/ATMState.java src/core/PaperTank.java src/interfaces/Persistence.java src/interfaces/TechActions.java src/persistence/JsonHandler.java src/services/ATMService.java src/services/AuthService.java src/services/V1Technician.java src/services/V2Technician.java test/AuthServiceTest.java test/AccountWithdrawalTest.java test/ATMAuthenticationIntegrationTest.java test/ATMWithdrawalIntegrationTest.java'

Invoke-Expression $compileCmd

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nERROR: Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "Running all 4 Test Suites..." -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

$testCmd = 'java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/json-20231013.jar;bin" org.junit.runner.JUnitCore test.AuthServiceTest test.AccountWithdrawalTest test.ATMAuthenticationIntegrationTest test.ATMWithdrawalIntegrationTest'

Invoke-Expression $testCmd

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "SUCCESS: All tests passed!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Red
    Write-Host "FAILURE: Some tests failed!" -ForegroundColor Red
    Write-Host "============================================" -ForegroundColor Red
}
