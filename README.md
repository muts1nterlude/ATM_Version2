# Professional ATM Machine System

A sophisticated, real-world ATM application built with professional-grade features including user authentication, transaction management, cash handling, and technician maintenance capabilities.

## Overview

This ATM system provides a complete banking experience with:
- **Customer Features**: Balance checking, withdrawals, deposits, transfers, transaction history
- **Security Features**: Card expiration, PIN authentication with attempt limiting, card masking
- **Transaction Management**: Transaction fees, daily withdrawal limits, comprehensive history tracking
- **Technician Tools**: System diagnostics, cash management, paper refills, firmware updates, event logging
- **Professional Design**: Menu-driven interface, comprehensive error handling, data persistence

---

## Project Structure

```
ATMMachine/
├── src/
│   ├── Main.java                    # Application entry point with menu system
│   ├── core/
│   │   ├── Account.java             # Customer account with transaction history
│   │   ├── ATMState.java            # ATM system state and diagnostics
│   │   └── PaperTank.java           # Receipt paper management
│   ├── interfaces/
│   │   ├── Persistence.java         # Data persistence interface
│   │   └── TechActions.java         # Technician interface
│   ├── persistence/
│   │   └── JsonHandler.java         # JSON file-based persistence
│   └── services/
│       ├── ATMService.java          # Main ATM operations
│       ├── AuthService.java         # Authentication management
│       ├── V1Technician.java        # Basic technician features
│       └── V2Technician.java        # Advanced technician features
├── lib/
│   └── json-20231013.jar            # JSON library
├── account.json                     # Customer account data (persisted)
├── atm.json                         # ATM state data (persisted)
└── ATMMachine.iml                   # IntelliJ project file
```

---

## Getting Started

### Prerequisites
- Java 8 or higher
- Write permissions for JSON files in project directory

### Running the Application

#### Option 1: Using Command Line
```bash
cd c:\Code\2025-software-engineering\ATMMachine
javac -cp lib\json-20231013.jar -d bin src\*.java src\core\*.java src\interfaces\*.java src\persistence\*.java src\services\*.java
java -cp bin;lib\json-20231013.jar Main
```

#### Option 2: Using IDE
1. Open the project in your Java IDE (IntelliJ, Eclipse, VS Code)
2. Configure the project classpath to include `lib/json-20231013.jar`
3. Run `Main.java`

---

## Usage Guide

### Customer Login

**Default Credentials:**
- Card Number: `1234567890123456`
- PIN: `1234`

**Customer Menu:**
```
1. Check Balance
   - View account details
   - See daily withdrawal limit and usage
   - Check card expiration date

2. Withdraw Cash
   - Quick options: $20, $50, $100, $200, $500
   - Custom amount entry
   - Withdrawal fee: $2.50 per transaction
   - Subject to daily limit ($500)

3. Deposit Cash
   - Enter desired deposit amount
   - Funds credited immediately
   - Receipt printed if paper available

4. Transfer Funds
   - Specify recipient card number
   - Enter transfer amount
   - Transfer fee: $1.00 per transaction
   - Both parties receive transaction record

5. View Transaction History
   - Display last 10 transactions
   - Shows: Type, Amount, Time, Balance

6. Change PIN
   - Future enhancement for security

7. Logout
   - End session and return to main menu
```

### Technician Login

**Default Credentials:**
- Username: `admin`
- Password: `1234`

**Technician Menu:**
```
1. Display ATM Status
   - Full system diagnostics
   - Cash levels and capacity percentage
   - Paper tank status
   - Firmware version
   - Total transactions processed
   - Last maintenance and cash refill dates

2. Refill Paper
   - Fill paper tank to 500 sheets
   - Changes saved automatically
   - Useful when out of paper warnings appear

3. Add Cash
   - Manually add cash to ATM supply
   - Validates against capacity limits (max $50,000)
   - Automatically saved

4. Collect Cash
   - Remove cash from ATM for transport
   - Records collection transaction
   - Updates ATM balance

5. Update Firmware
   - Update ATM firmware to latest version
   - Logs update in event history

6. View Event Log
   - Display all timestamped system events
   - Includes ATM initialization, transactions, maintenance

7. Perform Maintenance
   - Record routine maintenance activity
   - Updates maintenance timestamp

8. Logout
   - Return to main menu
```

---

## Key Features

### Security & Authentication
- ✓ PIN authentication with 3-attempt limit
- ✓ Card expiration checking (5-year validity)
- ✓ Card number masking (shows only last 4 digits)
- ✓ Separate customer and technician access levels
- ✓ Username/password technician authentication

### Transaction Management
- ✓ Complete transaction history with timestamps
- ✓ Transaction fees (withdrawal: $2.50, transfer: $1.00)
- ✓ Daily withdrawal limits ($500 default)
- ✓ Automatic daily limit reset
- ✓ Transaction logging for audit trail

### Customer Services
- ✓ Balance checking with account details
- ✓ Cash withdrawal with multiple options
- ✓ Cash deposit functionality
- ✓ Fund transfers between accounts
- ✓ Receipt printing (with paper availability check)
- ✓ Transaction history viewing

### System Management
- ✓ Real-time ATM diagnostics
- ✓ Cash level monitoring (with warnings)
- ✓ Paper tank management
- ✓ Firmware version tracking and updates
- ✓ Event logging system
- ✓ Low resource alerts (cash, paper)

### Data Persistence
- ✓ JSON-based file storage
- ✓ Automatic state persistence
- ✓ Account data preservation
- ✓ Transaction history maintenance

---

## Professional Features Implemented

### 1. Account Management
The `Account` class has been enhanced with:
```java
- Transaction history with timestamped entries
- Card expiration dates (5-year validity)
- Daily withdrawal limits ($500 configurable)
- Transaction fee tracking
- Transfer capabilities
- Deposit functionality
```

### 2. ATM State Management
The `ATMState` class includes:
```java
- Event logging system
- Cash capacity management (max $50,000)
- Low cash threshold alerts ($500)
- Maintenance tracking
- Transaction counting
- System diagnostics
```

### 3. Enhanced Service Layer
`ATMService` provides:
```java
- PIN attempt limiting (3 attempts)
- Card expiration validation
- Comprehensive error handling
- Multiple transaction types
- Receipt generation
- Card number masking
```

### 4. Technician Tools
`V1Technician` and `V2Technician`:
```java
- System health checks
- Cash management (add, collect, track)
- Paper management
- Firmware updates
- Event log viewing
- Performance monitoring
```

---

## Fees & Limits

| Operation | Fee | Limit | Notes |
|-----------|-----|-------|-------|
| Withdrawal | $2.50 | $500/day | Resets daily |
| Deposit | Free | Unlimited | Subject to ATM capacity |
| Transfer | $1.00 | Unlimited | Per account balance |
| Card Validity | N/A | 5 years | Checked on login |

---

## Transaction Flow Examples

### Example 1: Customer Withdraws Money
```
1. Customer logs in with card and PIN
2. System displays account details
3. Customer selects withdrawal amount
4. ATM checks:
   - Sufficient ATM cash available
   - Sufficient customer balance
   - Daily limit not exceeded
   - Paper for receipt
5. ATM dispenses cash
6. Deducts amount + $2.50 fee from account
7. Prints receipt and logs transaction
8. Updates both ATM and account state
```

### Example 2: Technician Refills ATM
```
1. Technician logs in with credentials
2. Checks ATM status and diagnostics
3. Identifies low cash level
4. Selects "Add Cash"
5. Enters amount to add
6. System validates capacity limits
7. Updates ATM cash state
8. Saves changes to JSON
9. Logs event in audit trail
```

---

## Error Handling

The system includes comprehensive error handling for:
- ✓ Invalid PIN entries
- ✓ Expired cards
- ✓ Insufficient balance
- ✓ Exceeded daily limits
- ✓ Out of paper scenarios
- ✓ ATM low on cash warnings
- ✓ Invalid input values
- ✓ File I/O errors

---

## Data Persistence

### account.json
```json
{
  "card": "1234567890123456",
  "pin": "1234",
  "balance": 1000.00
}
```

### atm.json
```json
{
  "cash": 2000.00,
  "firmware": "v2.0.1",
  "paper": 450
}
```

---

## Compilation & Execution

### Compile
```bash
javac -cp lib\json-20231013.jar -d bin src\*.java src\core\*.java src\interfaces\*.java src\persistence\*.java src\services\*.java
```

### Run
```bash
java -cp bin;lib\json-20231013.jar Main
```

### On Linux/Mac
```bash
# Compile
javac -cp lib/json-20231013.jar -d bin src/*.java src/core/*.java src/interfaces/*.java src/persistence/*.java src/services/*.java

# Run
java -cp bin:lib/json-20231013.jar Main
```

---

## Testing Scenarios

### Customer Scenario
1. Login with card: `1234567890123456`, PIN: `1234`
2. Check balance
3. Withdraw $100
4. View transaction history
5. Transfer $50 to another account
6. Logout

### Technician Scenario
1. Login with admin/1234
2. View system diagnostics
3. Check system health
4. Add $1000 cash
5. Refill paper
6. View event log
7. Logout

---

## Code Quality Features

- ✓ Object-oriented design principles
- ✓ Clear separation of concerns
- ✓ Interface-based architecture
- ✓ Comprehensive error handling
- ✓ Professional formatting and logging
- ✓ Detailed comments and documentation
- ✓ Type safety and validation
- ✓ Consistent naming conventions

---

## Future Enhancements

Potential features for future versions:
- Multi-language support
- Biometric authentication
- Mobile app integration
- Email/SMS notifications
- Advanced reporting and analytics
- Network ATM support
- Encrypted password storage
- Interest calculation for savings
- Loyalty rewards program
- Transaction dispute resolution

---

## Troubleshooting

### Compilation Issues
**Problem:** `package org.json does not exist`
- **Solution:** Ensure `lib/json-20231013.jar` is in classpath: `-cp lib\json-20231013.jar`

**Problem:** Class not found
- **Solution:** Verify all source files are in correct package directories

### Runtime Issues
**Problem:** `account.json not found`
- **Solution:** Run from project root directory, or ensure JSON files exist

**Problem:** "Card blocked" after 3 PIN attempts
- **Solution:** This is expected security behavior. Restart program to retry.

---

## License & Credits

This is an educational project demonstrating professional ATM system design and implementation.

---

## Support & Documentation

Refer to the following documentation files:
- [FEATURES_ADDED.md](FEATURES_ADDED.md) - Detailed feature descriptions
- [QUICK_START.md](QUICK_START.md) - Quick reference guide

---

**Version:** 2.0.1  
**Last Updated:** January 28, 2026  
**Status:** Production Ready
