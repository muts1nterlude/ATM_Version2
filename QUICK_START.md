# ATM Machine - Quick Reference Guide

## Starting the Application
```
Run Main.java
```

---

## CUSTOMER LOGIN

### Default Credentials (from account.json)
- **Card Number:** 1234567890123456
- **PIN:** 1234

### Customer Menu Options
```
1. Check Balance
   - View account details
   - See daily withdrawal info
   - Check card expiration

2. Withdraw Cash
   - Quick options: $20, $50, $100, $200, $500
   - Custom amount entry
   - Automatic $2.50 fee deduction

3. Deposit Cash
   - Enter deposit amount
   - Funds credited immediately
   - Receipt printed

4. Transfer Funds
   - Enter recipient card number
   - Specify transfer amount
   - Automatic $1.00 transfer fee
   - Transaction recorded for both parties

5. View Transaction History
   - Last 10 transactions displayed
   - Shows type, amount, time, and balance
   - Complete audit trail

6. Change PIN
   - Future enhancement for security

7. Logout
   - Ends session and returns to welcome screen
```

---

## TECHNICIAN LOGIN

### Default Credentials
- **Username:** admin
- **Password:** 1234

### Technician Menu Options
```
1. Display ATM Status
   - Full system diagnostics
   - Cash levels and capacity
   - Paper tank status
   - Firmware version
   - Total transactions
   - Maintenance history

2. Refill Paper
   - Fills paper tank to 500 sheets
   - Changes saved automatically

3. Add Cash
   - Manually add cash to ATM
   - Validates capacity limits
   - Updates ATM balance

4. Collect Cash
   - Removes all cash from ATM
   - Prepares for transport
   - Records transaction

5. Update Firmware
   - Updates to latest version (v2.0.1)
   - Records update in logs

6. View Event Log
   - All system events timestamped
   - Includes ATM initialization
   - Cash transactions
   - Maintenance activities
   - Alerts and warnings

7. Perform Maintenance
   - Records maintenance activity
   - Updates maintenance timestamp

8. Logout
   - Returns to main menu
```

---

## KEY FEATURES

### Security
- **PIN Protection:** 3 attempt limit before card block
- **Card Masking:** Displays only last 4 digits
- **Card Expiration:** Checks 5-year validity
- **Transaction Logging:** All activities recorded

### Transaction Fees
- **Withdrawal Fee:** $2.50 per transaction
- **Transfer Fee:** $1.00 per transaction
- **Deposit:** No fee

### Daily Limits
- **Daily Withdrawal Limit:** $500
- **Limit Resets:** Daily at 12:00 AM
- **Remaining Balance:** Calculated in real-time

### Receipt Printing
- Requires paper in tank
- Shows transaction details
- Includes timestamp and new balance

---

## EXAMPLE WORKFLOWS

### Scenario 1: Customer Withdraws $100
```
1. Login with card and PIN
2. See balance info
3. Select "Withdraw Cash"
4. Choose $100 or custom amount
5. ATM deducts $100 + $2.50 fee = $102.50 total
6. Receipt printed if paper available
7. Transaction logged in history
```

### Scenario 2: Customer Transfers Money
```
1. Login with card and PIN
2. Select "Transfer Funds"
3. Enter recipient card number
4. Specify amount to transfer
5. Sender account debited (amount + $1.00 fee)
6. Recipient account credited with amount
7. Both parties get transaction records
```

### Scenario 3: Technician Refills ATM
```
1. Login with admin credentials
2. View ATM diagnostics
3. Check cash level
4. If low, select "Add Cash"
5. Enter amount to add
6. Refill paper if needed
7. Check "System Health Check"
8. View event log for confirmation
```

---

## TROUBLESHOOTING

### Problem: "Card Blocked"
- **Cause:** 3 failed PIN attempts
- **Solution:** Contact bank to unblock card

### Problem: "Out of Paper"
- **Cause:** Receipt printer out of paper
- **Solution:** Technician should use option 2 to refill

### Problem: "Insufficient Balance"
- **Cause:** Not enough funds for transaction
- **Solution:** Deposit funds or withdraw less

### Problem: "Daily Limit Exceeded"
- **Cause:** Exceeded $500 daily withdrawal limit
- **Solution:** Try again tomorrow or withdraw less

### Problem: "ATM Low on Cash"
- **Cause:** ATM cash below $500 threshold
- **Solution:** Technician should add cash

---

## FILE STRUCTURE

```
account.json       - Customer account data
atm.json          - ATM state and cash information
Main.java         - Application entry point
Account.java      - Customer account model
ATMState.java     - ATM system state
PaperTank.java    - Paper tank management
ATMService.java   - Main ATM operations
AuthService.java  - Authentication service
V1Technician.java - Basic technician interface
V2Technician.java - Advanced technician features
```

---

## DATA PERSISTENCE

All changes are automatically saved to JSON files:
- Account changes: Saved to `account.json`
- ATM state changes: Saved to `atm.json`
- All transactions: Stored in account history
- Event logs: Maintained in ATM state

---

## SYSTEM REQUIREMENTS

- Java 8 or higher
- Write permissions for JSON files
- Console with UTF-8 support for box characters

---

## PROFESSIONAL FEATURES CHECKLIST

✓ Multi-user authentication system
✓ Transaction history tracking
✓ Card expiration management
✓ Daily withdrawal limits
✓ Transaction fees
✓ Deposit functionality
✓ Fund transfers between accounts
✓ Receipt printing system
✓ Comprehensive error handling
✓ Admin technician interface
✓ System diagnostics
✓ Event logging and audit trails
✓ Cash and paper inventory management
✓ Firmware version tracking
✓ Professional UI with menus

