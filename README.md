cd c:\Users\Kaushikan\Desktop\atmprocess
java Maincd c:\Users\Kaushikan\Desktop\atmprocess
java Maincd c:\Users\Kaushikan\Desktop\atmprocess
java Main# ATM Management System

A complete, production-ready ATM console application built in Java with real-world banking features.

## Features

### Authentication & Security
- 16-digit card number + 4-digit PIN authentication
- 3-strike lockout mechanism with account locking
- 5-minute session timeout on inactivity
- Transaction PIN required for amounts exceeding ₹5,000
- Secure PIN change functionality

### Account Operations
- **Check Balance** - View current balance and daily transaction limits
- **Withdraw Cash** - Multiples of ₹100, max ₹20,000/day, transaction limit ₹50,000
- **Deposit Money** - Multiples of ₹100, max ₹50,000/transaction, ₹1,00,000/day limit
- **Transfer Funds** - Inter-account transfers with ₹10 transfer fee
- **Transaction History** - View last 10 transactions with timestamps
- **PIN Management** - Change PIN with current PIN verification

### Admin Panel (Card: 0000000000000000, PIN: 0000)
- View all accounts with current balances and status
- View complete transaction logs (all accounts or specific)
- Unlock locked accounts after failed authentication attempts
- Reset customer PINs

### Transaction Management
- Automatic daily limit reset at midnight
- Transaction ID tracking (TXN format with auto-incrementing numbers)
- Detailed transaction history with date/time, type, amount, and balance
- Support for multiple transaction types: WITHDRAW, DEPOSIT, TRANSFER, PIN_CHANGE

## Sample Test Accounts

**Customer Accounts (Initial Balance: ₹25,000 each)**
```
Account 1: 1111111111111111 | PIN: 1234 | Name: Raj Kumar
Account 2: 2222222222222222 | PIN: 5678 | Name: Priya Singh
Account 3: 3333333333333333 | PIN: 9012 | Name: Amit Patel
```

**Admin Account**
```
Card: 0000000000000000 | PIN: 0000
```

## System Limits

| Limit | Amount |
|-------|--------|
| Card Number | 16 digits |
| PIN | 4 digits |
| Daily Withdrawal | ₹20,000 |
| Daily Deposit | ₹1,00,000 |
| Transaction Limit | ₹50,000 |
| Transfer Fee | ₹10 |
| Transaction PIN Threshold | ₹5,000+ |
| Session Timeout | 5 minutes |
| Wrong PIN Attempts | 3 (then account locks) |

## How to Run

### Requirements
- Java 8 or higher
- 64-bit operating system

### Compilation
```bash
javac Main.java
```

### Execution
```bash
java Main
```

## Flow Diagram

```
START
  ↓
Authentication (Card + PIN)
  ├─ Invalid Card → Error, Retry
  ├─ Locked Account → Cannot proceed
  ├─ Wrong PIN (3 attempts) → Account Locks
  └─ Success → Main Menu
      ↓
    ┌─ 1. Check Balance
    ├─ 2. Withdraw (validate limits, transaction PIN for >5000)
    ├─ 3. Deposit (validate limits)
    ├─ 4. Transfer (confirm recipient, deduct fee)
    ├─ 5. Transaction History (last 10)
    ├─ 6. Change PIN
    ├─ 7. Admin Panel (admin only)
    └─ 0. Logout
         ↓
    Session Timeout Check
      ├─ Timeout → Force Logout
      └─ Active → Continue
```

## Class Structure

### Account Class
```java
- cardNumber: String (16 digits)
- pin: String (4 digits)
- holderName: String
- balance: double
- dailyWithdrawal: double
- isLocked: boolean
- lastActivityTime: LocalDateTime
```

### Transaction Class
```java
- id: String (auto-generated TXN + counter)
- dateTime: LocalDateTime
- type: String (WITHDRAW, DEPOSIT, TRANSFER, PIN_CHANGE)
- amount: double
- balanceAfter: double
- toCard: String (for transfers)
- fee: double
```

## Data Storage

All data is stored in static Maps:
- `accounts`: Map<String, Account> - All customer and admin accounts
- `allTransactions`: Map<String, List<Transaction>> - Transaction history per account

**Note:** This is an in-memory system. Data resets when the application closes. For persistence, integrate a database like MySQL/PostgreSQL.

## Security Features

✅ Input validation (card number, PIN format)
✅ Daily transaction limits enforcement
✅ Account lockout after failed attempts
✅ Session timeout for inactive users
✅ Transaction PIN for high-value transactions
✅ Masked card numbers in display (show only last 4 digits)
✅ Transaction tracking and audit logs

## Production Enhancements (Recommended)

- [ ] Replace static maps with database (MySQL/PostgreSQL/MongoDB)
- [ ] Add SSL/TLS encryption for data transmission
- [ ] Implement password hashing with salt (bcrypt/Argon2)
- [ ] Add role-based access control (RBAC)
- [ ] Implement OTP for high-value transactions
- [ ] Add transaction approvals workflow
- [ ] Implement audit logging to file system
- [ ] Add email notifications for transactions
- [ ] Implement biometric authentication
- [ ] Add transaction reversal/cancellation features

## Technical Stack

- **Language:** Java 8+
- **UI:** Console-based with formatted tables
- **Date/Time:** java.time.LocalDateTime, DateTimeFormatter
- **Data Structures:** HashMap, ArrayList
- **Input:** Scanner class

## File Structure

```
atmprocess/
├── Main.java          # Complete ATM system (single file)
├── Main.class         # Compiled bytecode
└── README.md          # Documentation
```

## Author
Generated on March 22, 2026

## License
Open Source - Free to use and modify

---

**Note:** This is a demonstration/educational project. For real banking systems, additional security measures and compliance with banking regulations are required.
