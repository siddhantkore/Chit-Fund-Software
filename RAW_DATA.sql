-- ============================================================================
-- Chit Fund Management System - Sample Data SQL Script
-- ============================================================================
-- This script populates the database with sample data for testing.
-- 
-- Execution Order:
-- 1. Users (no dependencies)
-- 2. Chit Groups (no dependencies)
-- 3. Funds (depends on chit_group)
-- 4. Memberships (depends on users and chit_group)
-- 5. Auctions (depends on users and chit_group)
-- 6. Payments (depends on users and chit_group)
-- 7. Member Loans (depends on users and chit_group)
-- 8. Ledger (depends on users and chit_group)
-- 9. Notification Config (depends on chit_group)
-- 10. Notifications (depends on users)
-- 11. Group Chat Messages (depends on users and chit_group)
--
-- Note: All passwords are set to 'password123' using {noop} prefix (plain text for testing)
-- For login, use username and password: password123
-- 
-- IMPORTANT: This uses plain text passwords ({noop}) suitable for testing only.
-- For production, replace with properly BCrypt encoded passwords.
-- ============================================================================

-- Disable foreign key checks temporarily (MySQL)
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data (optional - comment out if you want to keep existing data)
-- TRUNCATE TABLE group_chat_message;
-- TRUNCATE TABLE notification;
-- TRUNCATE TABLE notification_config;
-- TRUNCATE TABLE ledger;
-- TRUNCATE TABLE member_loan;
-- TRUNCATE TABLE payments;
-- TRUNCATE TABLE auction;
-- TRUNCATE TABLE membership;
-- TRUNCATE TABLE funds;
-- TRUNCATE TABLE chit_group;
-- TRUNCATE TABLE users;

-- ============================================================================
-- 1. USERS TABLE
-- ============================================================================
-- Password for all users: password123
-- Using {noop} prefix for plain text password (Spring Security no-op encoder)
-- This is suitable for testing. For production, use proper BCrypt encoding.
-- ============================================================================

INSERT INTO users (name, username, phone, email, password, role, created_at, updated_at) VALUES
('Admin User', 'admin', 9876543210, 'admin@chitfund.com', '{noop}password123', 'ADMIN', NOW(), NOW()),
('Accountant One', 'accountant1', 9876543211, 'accountant1@chitfund.com', '{noop}password123', 'ACCOUNTANT', NOW(), NOW()),
('John Doe', 'johndoe', 9876543212, 'john.doe@email.com', '{noop}password123', 'MEMBER', NOW(), NOW()),
('Jane Smith', 'janesmith', 9876543213, 'jane.smith@email.com', '{noop}password123', 'MEMBER', NOW(), NOW()),
('Bob Johnson', 'bobjohnson', 9876543214, 'bob.johnson@email.com', '{noop}password123', 'MEMBER', NOW(), NOW()),
('Alice Williams', 'alicewilliams', 9876543215, 'alice.williams@email.com', '{noop}password123', 'MEMBER', NOW(), NOW()),
('Charlie Brown', 'charliebrown', 9876543216, 'charlie.brown@email.com', '{noop}password123', 'MEMBER', NOW(), NOW());

-- ============================================================================
-- 2. CHIT_GROUP TABLE
-- ============================================================================

INSERT INTO chit_group (group_code, name, duration, monthly_amount, total_members, start_date, status, created_at, updated_at, fund_id) VALUES
('CHIT2024001', 'Monthly Premium Chit 2024', 12, 10000.00, 5, '2024-01-01 10:00:00', 'ACTIVE', NOW(), NOW(), NULL),
('CHIT2024002', 'Annual Savings Group', 24, 5000.00, 5, '2024-02-01 10:00:00', 'ACTIVE', NOW(), NOW(), NULL),
('CHIT2024003', 'Weekly Investment Plan', 12, 2000.00, 5, '2024-03-01 10:00:00', 'PENDING', NOW(), NOW(), NULL),
('CHIT2024004', 'Family Chit Fund 2024', 18, 15000.00, 5, '2024-01-15 10:00:00', 'ACTIVE', NOW(), NOW(), NULL),
('CHIT2024005', 'Corporate Savings Group', 36, 25000.00, 5, '2023-12-01 10:00:00', 'COMPLETED', NOW(), NOW(), NULL);

-- ============================================================================
-- 3. FUNDS TABLE
-- ============================================================================
-- One-to-one relationship with chit_group
-- ============================================================================

INSERT INTO funds (name, total_amount, cycle_period, interest_rate, created_at, updated_at, chit_group_id) VALUES
('Fund for Monthly Premium Chit 2024', 600000.00, 30, 12.50, NOW(), NOW(), 1),
('Fund for Annual Savings Group', 600000.00, 30, 11.00, NOW(), NOW(), 2),
('Fund for Weekly Investment Plan', 120000.00, 7, 10.50, NOW(), NOW(), 3),
('Fund for Family Chit Fund 2024', 1350000.00, 30, 13.00, NOW(), NOW(), 4),
('Fund for Corporate Savings Group', 4500000.00, 30, 12.00, NOW(), NOW(), 5);

-- Update chit_group to link to funds
UPDATE chit_group SET fund_id = 1 WHERE id = 1;
UPDATE chit_group SET fund_id = 2 WHERE id = 2;
UPDATE chit_group SET fund_id = 3 WHERE id = 3;
UPDATE chit_group SET fund_id = 4 WHERE id = 4;
UPDATE chit_group SET fund_id = 5 WHERE id = 5;

-- ============================================================================
-- 4. MEMBERSHIP TABLE
-- ============================================================================
-- Links users to chit groups
-- ============================================================================

INSERT INTO membership (user_id, chit_group_id, join_date, status) VALUES
-- Group 1 members
(3, 1, '2024-01-01 10:00:00', 'ACTIVE'),
(4, 1, '2024-01-01 10:00:00', 'ACTIVE'),
(5, 1, '2024-01-01 10:00:00', 'ACTIVE'),
(6, 1, '2024-01-01 10:00:00', 'ACTIVE'),
(7, 1, '2024-01-01 10:00:00', 'ACTIVE'),

-- Group 2 members
(3, 2, '2024-02-01 10:00:00', 'ACTIVE'),
(4, 2, '2024-02-01 10:00:00', 'ACTIVE'),
(5, 2, '2024-02-01 10:00:00', 'ACTIVE'),
(6, 2, '2024-02-01 10:00:00', 'ACTIVE'),
(7, 2, '2024-02-01 10:00:00', 'ACTIVE'),

-- Group 3 members
(3, 3, '2024-03-01 10:00:00', 'ACTIVE'),
(4, 3, '2024-03-01 10:00:00', 'ACTIVE'),
(5, 3, '2024-03-01 10:00:00', 'ACTIVE'),

-- Group 4 members
(3, 4, '2024-01-15 10:00:00', 'ACTIVE'),
(4, 4, '2024-01-15 10:00:00', 'ACTIVE'),
(5, 4, '2024-01-15 10:00:00', 'ACTIVE'),
(6, 4, '2024-01-15 10:00:00', 'ACTIVE'),
(7, 4, '2024-01-15 10:00:00', 'ACTIVE'),

-- Group 5 members (completed group)
(3, 5, '2023-12-01 10:00:00', 'ACTIVE'),
(4, 5, '2023-12-01 10:00:00', 'ACTIVE'),
(5, 5, '2023-12-01 10:00:00', 'ACTIVE'),
(6, 5, '2023-12-01 10:00:00', 'ACTIVE'),
(7, 5, '2023-12-01 10:00:00', 'ACTIVE');

-- ============================================================================
-- 5. AUCTION TABLE
-- ============================================================================
-- Records auction winners for chit groups
-- ============================================================================

INSERT INTO auction (month, winning_amount, commission, auction_date, remark, winning_member_id, chit_group_id, created_at, updated_at) VALUES
-- Group 1 auctions
('1', 95000.00, 5000.00, '2024-01-15 14:00:00', 'First month auction - John won', 3, 1, NOW(), NOW()),
('2', 92000.00, 8000.00, '2024-02-15 14:00:00', 'Second month auction - Jane won', 4, 1, NOW(), NOW()),
('3', 98000.00, 2000.00, '2024-03-15 14:00:00', 'Third month auction - Bob won', 5, 1, NOW(), NOW()),
('4', 94000.00, 6000.00, '2024-04-15 14:00:00', 'Fourth month auction - Alice won', 6, 1, NOW(), NOW()),
('5', 96000.00, 4000.00, '2024-05-15 14:00:00', 'Fifth month auction - Charlie won', 7, 1, NOW(), NOW()),

-- Group 2 auctions
('1', 45000.00, 5000.00, '2024-02-20 14:00:00', 'First auction in Annual Savings', 3, 2, NOW(), NOW()),
('2', 43000.00, 7000.00, '2024-03-20 14:00:00', 'Second auction', 4, 2, NOW(), NOW()),

-- Group 4 auctions
('1', 140000.00, 10000.00, '2024-02-01 14:00:00', 'Family chit first auction', 3, 4, NOW(), NOW()),
('2', 135000.00, 15000.00, '2024-03-01 14:00:00', 'Second auction in family chit', 4, 4, NOW(), NOW()),
('3', 142000.00, 8000.00, '2024-04-01 14:00:00', 'Third auction', 5, 4, NOW(), NOW()),

-- Group 5 auctions (completed group)
('1', 230000.00, 20000.00, '2023-12-15 14:00:00', 'Corporate group first auction', 3, 5, NOW(), NOW()),
('2', 225000.00, 25000.00, '2024-01-15 14:00:00', 'Second auction', 4, 5, NOW(), NOW()),
('3', 240000.00, 10000.00, '2024-02-15 14:00:00', 'Third auction', 5, 5, NOW(), NOW()),
('4', 235000.00, 15000.00, '2024-03-15 14:00:00', 'Fourth auction', 6, 5, NOW(), NOW()),
('5', 238000.00, 12000.00, '2024-04-15 14:00:00', 'Fifth auction', 7, 5, NOW(), NOW());

-- ============================================================================
-- 6. PAYMENTS TABLE
-- ============================================================================
-- Payment records for members
-- ============================================================================

INSERT INTO payments (month, amount_paid, payment_date, mode, status, user_id, chit_group_id, created_at, updated_at) VALUES
-- Group 1 payments
('1', 10000.00, '2024-01-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 3, 1, NOW(), NOW()),
('2', 10000.00, '2024-02-05 10:00:00', 'UPI', 'COMPLETED', 3, 1, NOW(), NOW()),
('1', 10000.00, '2024-01-05 10:00:00', 'CASH', 'COMPLETED', 4, 1, NOW(), NOW()),
('2', 10000.00, '2024-02-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 4, 1, NOW(), NOW()),
('3', 10000.00, '2024-03-05 10:00:00', 'UPI', 'PENDING', 4, 1, NOW(), NOW()),
('1', 10000.00, '2024-01-05 10:00:00', 'CHEQUE', 'COMPLETED', 5, 1, NOW(), NOW()),
('2', 10000.00, '2024-02-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 5, 1, NOW(), NOW()),
('1', 10000.00, '2024-01-05 10:00:00', 'UPI', 'COMPLETED', 6, 1, NOW(), NOW()),
('1', 10000.00, '2024-01-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 7, 1, NOW(), NOW()),

-- Group 2 payments
('1', 5000.00, '2024-02-10 10:00:00', 'UPI', 'COMPLETED', 3, 2, NOW(), NOW()),
('2', 5000.00, '2024-03-10 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 3, 2, NOW(), NOW()),
('1', 5000.00, '2024-02-10 10:00:00', 'CASH', 'COMPLETED', 4, 2, NOW(), NOW()),
('1', 5000.00, '2024-02-10 10:00:00', 'UPI', 'COMPLETED', 5, 2, NOW(), NOW()),
('1', 5000.00, '2024-02-10 10:00:00', 'BANK_TRANSFER', 'PENDING', 6, 2, NOW(), NOW()),

-- Group 4 payments
('1', 15000.00, '2024-01-20 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 3, 4, NOW(), NOW()),
('2', 15000.00, '2024-02-20 10:00:00', 'UPI', 'COMPLETED', 3, 4, NOW(), NOW()),
('1', 15000.00, '2024-01-20 10:00:00', 'CHEQUE', 'COMPLETED', 4, 4, NOW(), NOW()),
('2', 15000.00, '2024-02-20 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 4, 4, NOW(), NOW()),
('1', 15000.00, '2024-01-20 10:00:00', 'UPI', 'COMPLETED', 5, 4, NOW(), NOW()),
('1', 15000.00, '2024-01-20 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 6, 4, NOW(), NOW()),
('1', 15000.00, '2024-01-20 10:00:00', 'CASH', 'FAILED', 7, 4, NOW(), NOW()),

-- Group 5 payments (completed group)
('1', 25000.00, '2023-12-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 3, 5, NOW(), NOW()),
('2', 25000.00, '2024-01-05 10:00:00', 'UPI', 'COMPLETED', 3, 5, NOW(), NOW()),
('1', 25000.00, '2023-12-05 10:00:00', 'CHEQUE', 'COMPLETED', 4, 5, NOW(), NOW()),
('1', 25000.00, '2023-12-05 10:00:00', 'BANK_TRANSFER', 'COMPLETED', 5, 5, NOW(), NOW()),
('1', 25000.00, '2023-12-05 10:00:00', 'UPI', 'COMPLETED', 6, 5, NOW(), NOW()),
('1', 25000.00, '2023-12-05 10:00:00', 'CASH', 'COMPLETED', 7, 5, NOW(), NOW());

-- ============================================================================
-- 7. MEMBER_LOAN TABLE
-- ============================================================================
-- Loans taken by members (when they win auctions)
-- ============================================================================

INSERT INTO member_loan (principle, interest, tenure, current_payable, start_date, end_date, status, chit_group_id, user_id, created_at, updated_at) VALUES
-- Loans for Group 1 (based on auctions won)
(95000.00, 5700.00, 11, 95000.00, '2024-01-15 14:00:00', '2024-12-15 14:00:00', 'ACTIVE', 1, 3, NOW(), NOW()),
(92000.00, 10120.00, 10, 88000.00, '2024-02-15 14:00:00', '2024-12-15 14:00:00', 'ACTIVE', 1, 4, NOW(), NOW()),
(98000.00, 2156.00, 9, 98000.00, '2024-03-15 14:00:00', '2024-12-15 14:00:00', 'ACTIVE', 1, 5, NOW(), NOW()),
(94000.00, 5640.00, 8, 90000.00, '2024-04-15 14:00:00', '2024-12-15 14:00:00', 'ACTIVE', 1, 6, NOW(), NOW()),
(96000.00, 3840.00, 7, 94000.00, '2024-05-15 14:00:00', '2024-12-15 14:00:00', 'ACTIVE', 1, 7, NOW(), NOW()),

-- Loans for Group 2
(45000.00, 4950.00, 23, 45000.00, '2024-02-20 14:00:00', '2026-01-20 14:00:00', 'ACTIVE', 2, 3, NOW(), NOW()),
(43000.00, 4730.00, 22, 42000.00, '2024-03-20 14:00:00', '2026-01-20 14:00:00', 'ACTIVE', 2, 4, NOW(), NOW()),

-- Loans for Group 4
(140000.00, 18200.00, 17, 140000.00, '2024-02-01 14:00:00', '2025-07-01 14:00:00', 'ACTIVE', 4, 3, NOW(), NOW()),
(135000.00, 17550.00, 16, 130000.00, '2024-03-01 14:00:00', '2025-07-01 14:00:00', 'ACTIVE', 4, 4, NOW(), NOW()),
(142000.00, 18460.00, 15, 142000.00, '2024-04-01 14:00:00', '2025-07-01 14:00:00', 'ACTIVE', 4, 5, NOW(), NOW()),

-- Loans for Group 5 (completed group - some may be closed)
(230000.00, 27600.00, 35, 0.00, '2023-12-15 14:00:00', '2026-11-15 14:00:00', 'CLOSED', 5, 3, NOW(), NOW()),
(225000.00, 24750.00, 34, 0.00, '2024-01-15 14:00:00', '2026-11-15 14:00:00', 'CLOSED', 5, 4, NOW(), NOW()),
(240000.00, 28800.00, 33, 0.00, '2024-02-15 14:00:00', '2026-11-15 14:00:00', 'CLOSED', 5, 5, NOW(), NOW()),
(235000.00, 28200.00, 32, 0.00, '2024-03-15 14:00:00', '2026-11-15 14:00:00', 'CLOSED', 5, 6, NOW(), NOW()),
(238000.00, 28560.00, 31, 0.00, '2024-04-15 14:00:00', '2026-11-15 14:00:00', 'CLOSED', 5, 7, NOW(), NOW());

-- ============================================================================
-- 8. LEDGER TABLE
-- ============================================================================
-- Financial ledger entries for tracking credits and debits
-- ============================================================================

INSERT INTO ledger (entry_type, amount, entry_date, remark, reference_id, user_id, chit_group_id, created_at, updated_at) VALUES
-- Group 1 ledger entries
('CREDIT', 10000.00, '2024-01-05 10:00:00', 'Monthly contribution from John', 'PAYMENT_1', 3, 1, NOW(), NOW()),
('CREDIT', 10000.00, '2024-02-05 10:00:00', 'Monthly contribution from John', 'PAYMENT_2', 3, 1, NOW(), NOW()),
('DEBIT', 95000.00, '2024-01-15 14:00:00', 'Auction payout to John', 'AUCTION_1', 3, 1, NOW(), NOW()),
('CREDIT', 10000.00, '2024-01-05 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_3', 4, 1, NOW(), NOW()),
('CREDIT', 10000.00, '2024-02-05 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_4', 4, 1, NOW(), NOW()),
('CREDIT', 10000.00, '2024-03-05 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_5', 4, 1, NOW(), NOW()),
('DEBIT', 92000.00, '2024-02-15 14:00:00', 'Auction payout to Jane', 'AUCTION_2', 4, 1, NOW(), NOW()),

-- Group 2 ledger entries
('CREDIT', 5000.00, '2024-02-10 10:00:00', 'Monthly contribution from John', 'PAYMENT_10', 3, 2, NOW(), NOW()),
('CREDIT', 5000.00, '2024-03-10 10:00:00', 'Monthly contribution from John', 'PAYMENT_11', 3, 2, NOW(), NOW()),
('DEBIT', 45000.00, '2024-02-20 14:00:00', 'Auction payout to John', 'AUCTION_6', 3, 2, NOW(), NOW()),
('CREDIT', 5000.00, '2024-02-10 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_12', 4, 2, NOW(), NOW()),
('DEBIT', 43000.00, '2024-03-20 14:00:00', 'Auction payout to Jane', 'AUCTION_7', 4, 2, NOW(), NOW()),

-- Group 4 ledger entries
('CREDIT', 15000.00, '2024-01-20 10:00:00', 'Monthly contribution from John', 'PAYMENT_15', 3, 4, NOW(), NOW()),
('CREDIT', 15000.00, '2024-02-20 10:00:00', 'Monthly contribution from John', 'PAYMENT_16', 3, 4, NOW(), NOW()),
('DEBIT', 140000.00, '2024-02-01 14:00:00', 'Auction payout to John', 'AUCTION_8', 3, 4, NOW(), NOW()),
('CREDIT', 15000.00, '2024-01-20 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_17', 4, 4, NOW(), NOW()),
('CREDIT', 15000.00, '2024-02-20 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_18', 4, 4, NOW(), NOW()),
('DEBIT', 135000.00, '2024-03-01 14:00:00', 'Auction payout to Jane', 'AUCTION_9', 4, 4, NOW(), NOW()),

-- Group 5 ledger entries (completed group)
('CREDIT', 25000.00, '2023-12-05 10:00:00', 'Monthly contribution from John', 'PAYMENT_19', 3, 5, NOW(), NOW()),
('CREDIT', 25000.00, '2024-01-05 10:00:00', 'Monthly contribution from John', 'PAYMENT_20', 3, 5, NOW(), NOW()),
('DEBIT', 230000.00, '2023-12-15 14:00:00', 'Auction payout to John', 'AUCTION_10', 3, 5, NOW(), NOW()),
('CREDIT', 25000.00, '2023-12-05 10:00:00', 'Monthly contribution from Jane', 'PAYMENT_21', 4, 5, NOW(), NOW()),
('DEBIT', 225000.00, '2024-01-15 14:00:00', 'Auction payout to Jane', 'AUCTION_11', 4, 5, NOW(), NOW());

-- ============================================================================
-- 9. NOTIFICATION_CONFIG TABLE
-- ============================================================================
-- Configuration for notification rules per chit group
-- ============================================================================

INSERT INTO notification_config (chit_group_id, notification_type, advance_days, enabled, message_template, recurring_day, created_at, updated_at) VALUES
(1, 'PAYMENT_REMINDER', 2, true, 'Reminder: Your payment of {amount} for {groupName} is due on {date}', 5, NOW(), NOW()),
(1, 'AUCTION_UPCOMING', 1, true, 'Auction for {groupName} is scheduled for {date}', NULL, NOW(), NOW()),
(1, 'LOAN_DUE', 7, true, 'Your loan payment of {amount} is due on {date}', NULL, NOW(), NOW()),
(2, 'PAYMENT_REMINDER', 3, true, 'Reminder: Payment for {groupName} due soon', 10, NOW(), NOW()),
(2, 'LOAN_DUE', 5, true, 'Loan payment reminder for {groupName}', NULL, NOW(), NOW()),
(3, 'PAYMENT_REMINDER', 2, true, 'Weekly payment reminder for {groupName}', NULL, NOW(), NOW()),
(4, 'PAYMENT_REMINDER', 2, true, 'Family chit payment reminder', 20, NOW(), NOW()),
(4, 'AUCTION_UPCOMING', 2, true, 'Upcoming auction notification', NULL, NOW(), NOW()),
(5, 'PAYMENT_REMINDER', 5, true, 'Corporate group payment reminder', 5, NOW(), NOW()),
(5, 'GROUP_COMPLETED', 0, true, 'Congratulations! Your chit group {groupName} has been completed', NULL, NOW(), NOW());

-- ============================================================================
-- 10. NOTIFICATION TABLE
-- ============================================================================
-- Actual notifications sent to users
-- ============================================================================

INSERT INTO notification (message, type, status, user_id, created_at) VALUES
('Reminder: Your payment of ₹10000 for Monthly Premium Chit 2024 is due on 2024-06-05', 'PAYMENT_REMINDER', 'UNREAD', 3, NOW()),
('Auction for Monthly Premium Chit 2024 is scheduled for 2024-06-15', 'AUCTION_UPCOMING', 'READ', 3, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('Your loan payment of ₹5000 is due on 2024-06-20', 'LOAN_DUE', 'UNREAD', 3, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('Reminder: Your payment of ₹10000 for Monthly Premium Chit 2024 is due on 2024-06-05', 'PAYMENT_REMINDER', 'UNREAD', 4, NOW()),
('Auction for Monthly Premium Chit 2024 is scheduled for 2024-06-15', 'AUCTION_UPCOMING', 'READ', 4, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('Reminder: Payment for Annual Savings Group due soon', 'PAYMENT_REMINDER', 'READ', 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
('Loan payment reminder for Annual Savings Group', 'LOAN_DUE', 'UNREAD', 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('Weekly payment reminder for Weekly Investment Plan', 'PAYMENT_REMINDER', 'UNREAD', 6, NOW()),
('Family chit payment reminder', 'PAYMENT_REMINDER', 'UNREAD', 7, NOW()),
('Congratulations! Your chit group Corporate Savings Group has been completed', 'GROUP_COMPLETED', 'READ', 3, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('Congratulations! Your chit group Corporate Savings Group has been completed', 'GROUP_COMPLETED', 'READ', 4, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('Upcoming auction notification', 'AUCTION_UPCOMING', 'UNREAD', 6, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('Your loan payment of ₹8000 is due on 2024-06-25', 'LOAN_DUE', 'UNREAD', 7, NOW()),
('Reminder: Your payment of ₹15000 for Family Chit Fund 2024 is due on 2024-05-20', 'PAYMENT_REMINDER', 'READ', 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),
('Auction for Family Chit Fund 2024 is scheduled for 2024-05-01', 'AUCTION_UPCOMING', 'UNREAD', 5, NOW());

-- ============================================================================
-- 11. GROUP_CHAT_MESSAGE TABLE
-- ============================================================================
-- Chat messages in chit group discussions
-- ============================================================================

INSERT INTO group_chat_message (chit_group_id, sender_id, content, message_type, created_at) VALUES
-- Group 1 chat messages
(1, 3, 'Hello everyone! Looking forward to this chit group.', 'TEXT', DATE_SUB(NOW(), INTERVAL 150 DAY)),
(1, 4, 'Hi John! Yes, excited to be part of this group.', 'TEXT', DATE_SUB(NOW(), INTERVAL 149 DAY)),
(1, 5, 'Great to have you all here!', 'TEXT', DATE_SUB(NOW(), INTERVAL 148 DAY)),
(1, 3, 'Just completed my first payment. Please confirm.', 'TEXT', DATE_SUB(NOW(), INTERVAL 145 DAY)),
(1, 4, 'Confirmed! Received your payment.', 'TEXT', DATE_SUB(NOW(), INTERVAL 144 DAY)),
(1, 6, 'When is the next auction?', 'TEXT', DATE_SUB(NOW(), INTERVAL 120 DAY)),
(1, 3, 'Next auction is on 15th of this month.', 'TEXT', DATE_SUB(NOW(), INTERVAL 119 DAY)),
(1, 7, 'Thank you for the update!', 'TEXT', DATE_SUB(NOW(), INTERVAL 118 DAY)),
(1, 4, 'Congratulations to Bob for winning the last auction!', 'TEXT', DATE_SUB(NOW(), INTERVAL 75 DAY)),
(1, 5, 'Thank you Jane!', 'TEXT', DATE_SUB(NOW(), INTERVAL 74 DAY)),

-- Group 2 chat messages
(2, 3, 'Welcome to Annual Savings Group!', 'TEXT', DATE_SUB(NOW(), INTERVAL 120 DAY)),
(2, 4, 'Thank you! Happy to be here.', 'TEXT', DATE_SUB(NOW(), INTERVAL 119 DAY)),
(2, 5, 'This is going to be a great savings journey.', 'TEXT', DATE_SUB(NOW(), INTERVAL 118 DAY)),
(2, 3, 'First auction completed successfully.', 'TEXT', DATE_SUB(NOW(), INTERVAL 100 DAY)),
(2, 6, 'Great! When is the next one?', 'TEXT', DATE_SUB(NOW(), INTERVAL 99 DAY)),

-- Group 4 chat messages
(4, 3, 'Family Chit Fund - Let\'s make this successful!', 'TEXT', DATE_SUB(NOW(), INTERVAL 135 DAY)),
(4, 4, 'Absolutely! Family first.', 'TEXT', DATE_SUB(NOW(), INTERVAL 134 DAY)),
(4, 5, 'Looking forward to this journey together.', 'TEXT', DATE_SUB(NOW(), INTERVAL 133 DAY)),
(4, 6, 'First auction was great!', 'TEXT', DATE_SUB(NOW(), INTERVAL 90 DAY)),
(4, 7, 'Yes, everything went smoothly.', 'TEXT', DATE_SUB(NOW(), INTERVAL 89 DAY)),

-- Group 5 chat messages (completed group)
(5, 3, 'Welcome to Corporate Savings Group!', 'TEXT', DATE_SUB(NOW(), INTERVAL 180 DAY)),
(5, 4, 'Thank you! Excited to start.', 'TEXT', DATE_SUB(NOW(), INTERVAL 179 DAY)),
(5, 5, 'This will be a productive group.', 'TEXT', DATE_SUB(NOW(), INTERVAL 178 DAY)),
(5, 3, 'First auction completed!', 'TEXT', DATE_SUB(NOW(), INTERVAL 165 DAY)),
(5, 6, 'Congratulations to the winner!', 'TEXT', DATE_SUB(NOW(), INTERVAL 164 DAY)),
(5, 7, 'Group is progressing well!', 'TEXT', DATE_SUB(NOW(), INTERVAL 120 DAY)),
(5, 3, 'We are approaching completion!', 'TEXT', DATE_SUB(NOW(), INTERVAL 60 DAY)),
(5, 4, 'Great work everyone!', 'TEXT', DATE_SUB(NOW(), INTERVAL 59 DAY)),
(5, 5, 'Thank you all for a successful chit group!', 'TEXT', DATE_SUB(NOW(), INTERVAL 30 DAY)),
(5, 3, 'Group completed successfully! 🎉', 'TEXT', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- ============================================================================
-- Re-enable foreign key checks
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- VERIFICATION QUERIES (Optional - Run these to verify data)
-- ============================================================================

-- SELECT COUNT(*) as user_count FROM users;
-- SELECT COUNT(*) as group_count FROM chit_group;
-- SELECT COUNT(*) as fund_count FROM funds;
-- SELECT COUNT(*) as membership_count FROM membership;
-- SELECT COUNT(*) as auction_count FROM auction;
-- SELECT COUNT(*) as payment_count FROM payments;
-- SELECT COUNT(*) as loan_count FROM member_loan;
-- SELECT COUNT(*) as ledger_count FROM ledger;
-- SELECT COUNT(*) as notification_config_count FROM notification_config;
-- SELECT COUNT(*) as notification_count FROM notification;
-- SELECT COUNT(*) as chat_message_count FROM group_chat_message;

-- ============================================================================
-- SUMMARY
-- ============================================================================
-- Users: 7 (1 Admin, 1 Accountant, 5 Members)
-- Chit Groups: 5 (3 ACTIVE, 1 PENDING, 1 COMPLETED)
-- Funds: 5 (one for each group)
-- Memberships: 23 (various members in different groups)
-- Auctions: 14 (across different groups)
-- Payments: 26 (various payment records)
-- Loans: 14 (corresponding to auction winners)
-- Ledger Entries: 17 (credit and debit entries)
-- Notification Configs: 10 (notification rules per group)
-- Notifications: 15 (sample notifications to users)
-- Chat Messages: 25 (group discussions)
-- ============================================================================

