package com.nival.chit.config;

import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Funds;
import com.nival.chit.entity.GroupChatMessage;
import com.nival.chit.entity.Ledger;
import com.nival.chit.entity.MemberLoan;
import com.nival.chit.entity.Membership;
import com.nival.chit.entity.Notification;
import com.nival.chit.entity.Payments;
import com.nival.chit.entity.User;
import com.nival.chit.enums.ChitGroupStatus;
import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.AuctionRepository;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.FundsRepository;
import com.nival.chit.repository.GroupChatMessageRepository;
import com.nival.chit.repository.GroupMediaRepository;
import com.nival.chit.repository.LedgerRepository;
import com.nival.chit.repository.MemberLoanRepository;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.NotificationConfigRepository;
import com.nival.chit.repository.NotificationRepository;
import com.nival.chit.repository.PaymentsRepository;
import com.nival.chit.repository.PollOptionRepository;
import com.nival.chit.repository.PollRepository;
import com.nival.chit.repository.UserRepository;
import com.nival.chit.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final AuctionRepository auctionRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final FundsRepository fundsRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final GroupMediaRepository groupMediaRepository;
    private final LedgerRepository ledgerRepository;
    private final MemberLoanRepository memberLoanRepository;
    private final MembershipRepository membershipRepository;
    private final NotificationConfigRepository notificationConfigRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentsRepository paymentsRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.reset-on-start:true}")
    private boolean resetOnStart;

    @Override
    @Transactional
    public void run(String... args) {
        if (!resetOnStart) {
            log.info("Seed reset disabled. Skipping deterministic data reset.");
            return;
        }

        log.info("Resetting application data and loading deterministic seed set...");
        wipeData();
        seedUsersAndGroups();
        log.info("Deterministic seed data loaded successfully.");
    }

    private void wipeData() {
        groupChatMessageRepository.deleteAllInBatch();
        groupMediaRepository.deleteAllInBatch();
        voteRepository.deleteAllInBatch();
        pollOptionRepository.deleteAllInBatch();
        pollRepository.deleteAllInBatch();
        paymentsRepository.deleteAllInBatch();
        ledgerRepository.deleteAllInBatch();
        memberLoanRepository.deleteAllInBatch();
        auctionRepository.deleteAllInBatch();
        notificationConfigRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
        membershipRepository.deleteAllInBatch();

        List<ChitGroup> groups = chitGroupRepository.findAll();
        groups.forEach(group -> group.setFundId(null));
        chitGroupRepository.saveAllAndFlush(groups);

        fundsRepository.deleteAllInBatch();
        chitGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private void seedUsersAndGroups() {
        User saasAdmin = createUser("SaaS Admin", "chitadmin", "admin@chitfund.saas", 9000000001L, "chitadmin", UserRoles.ADMIN);
        User anita = createUser("Anita Deshmukh", "anitaadmin", "anita@chitfund.local", 9000000002L, "demo123", UserRoles.MEMBER);
        User bharat = createUser("Bharat Kulkarni", "bharatlead", "bharat@chitfund.local", 9000000003L, "demo123", UserRoles.MEMBER);
        User charu = createUser("Charu Patil", "charumember", "charu@chitfund.local", 9000000004L, "demo123", UserRoles.MEMBER);
        User dev = createUser("Dev Nair", "devmember", "dev@chitfund.local", 9000000005L, "demo123", UserRoles.MEMBER);
        User meera = createUser("Meera Joshi", "acctmeera", "meera@chitfund.local", 9000000006L, "demo123", UserRoles.ACCOUNTANT);

        ChitGroup sunrise = createGroup("Sunrise Circle", 18, 5000, 10, LocalDateTime.now().minusMonths(2), ChitGroupStatus.ACTIVE, 140000);
        ChitGroup metro = createGroup("Metro Savers", 12, 7500, 8, LocalDateTime.now().minusMonths(1), ChitGroupStatus.ACTIVE, 95000);
        ChitGroup harbor = createGroup("Harbor Collective", 10, 6000, 6, LocalDateTime.now().minusWeeks(3), ChitGroupStatus.ACTIVE, 72000);

        createMembership(anita, sunrise, GroupRole.ADMIN, UserStatus.ACTIVE);
        createMembership(bharat, sunrise, GroupRole.MEMBER, UserStatus.ACTIVE);
        createMembership(charu, sunrise, GroupRole.MEMBER, UserStatus.ACTIVE);
        createMembership(dev, sunrise, GroupRole.MEMBER, UserStatus.PENDING);

        createMembership(bharat, metro, GroupRole.ADMIN, UserStatus.ACTIVE);
        createMembership(anita, metro, GroupRole.MEMBER, UserStatus.ACTIVE);
        createMembership(charu, metro, GroupRole.MEMBER, UserStatus.ACTIVE);
        createMembership(meera, metro, GroupRole.MEMBER, UserStatus.PENDING);

        createMembership(dev, harbor, GroupRole.ADMIN, UserStatus.ACTIVE);
        createMembership(meera, harbor, GroupRole.MEMBER, UserStatus.ACTIVE);
        createMembership(charu, harbor, GroupRole.MEMBER, UserStatus.ACTIVE);

        createNotification(anita, "Join request from Dev Nair for group Sunrise Circle", "JOIN_REQUEST", "UNREAD");
        createNotification(bharat, "Join request from Meera Joshi for group Metro Savers", "JOIN_REQUEST", "UNREAD");
        createNotification(charu, "Welcome back. You are active in 3 groups.", "INFO", "UNREAD");
        createNotification(dev, "You are the group admin for Harbor Collective.", "INFO", "READ");

        Payments sunrisePayment = createPayment(sunrise, bharat, "2026-03", 5000, "UPI", "COMPLETED", false, null);
        Payments sunrisePendingPayment = createPayment(sunrise, charu, "2026-03", 5000, "BANK_TRANSFER", "PENDING", false, null);
        Payments metroPayment = createPayment(metro, anita, "2026-03", 7500, "UPI", "COMPLETED", true, bharat);
        Payments harborPayment = createPayment(harbor, meera, "2026-03", 6000, "CASH", "COMPLETED", true, dev);

        MemberLoan bharatLoan = createLoan(sunrise, bharat, 30000, 12, 10, "ACTIVE", LocalDateTime.now().minusMonths(1));
        MemberLoan meeraLoan = createLoan(harbor, meera, 25000, 10, 8, "ACTIVE", LocalDateTime.now().minusWeeks(2));

        createLedgerEntry(sunrise, bharat, "CREDIT", 5000, "Monthly contribution received", "PAY-" + sunrisePayment.getId());
        createLedgerEntry(sunrise, charu, "CREDIT", 5000, "Pending contribution awaiting verification", "PAY-" + sunrisePendingPayment.getId());
        createLedgerEntry(metro, anita, "CREDIT", 7500, "Metro contribution verified", "PAY-" + metroPayment.getId());
        createLedgerEntry(harbor, meera, "CREDIT", 6000, "Harbor contribution verified", "PAY-" + harborPayment.getId());
        createLedgerEntry(sunrise, bharat, "DEBIT", -30000, "Member loan disbursed", "LOAN-" + bharatLoan.getId());
        createLedgerEntry(harbor, meera, "DEBIT", -25000, "Member loan disbursed", "LOAN-" + meeraLoan.getId());

        createChatMessage(sunrise, anita, "Welcome to Sunrise Circle. Use this room for group-only updates.");
        createChatMessage(sunrise, bharat, "Received. I can see only Sunrise from this workspace unless I open another joined group.");
        createChatMessage(metro, bharat, "Metro Savers is live. Anita is a member here, but admin access stays with me.");
        createChatMessage(metro, anita, "Confirmed. I can participate here without seeing admin controls.");
        createChatMessage(harbor, dev, "Harbor Collective chat is ready for active members only.");
    }

    private User createUser(String name, String username, String email, long phone, String password, UserRoles role) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    private ChitGroup createGroup(
            String name,
            int duration,
            double monthlyAmount,
            int totalMembers,
            LocalDateTime startDate,
            ChitGroupStatus status,
            double seededFundBalance
    ) {
        ChitGroup group = new ChitGroup();
        group.setName(name);
        group.setDuration(duration);
        group.setMonthlyAmount(monthlyAmount);
        group.setTotalMembers(totalMembers);
        group.setStartDate(startDate);
        group.setStatus(status);
        group = chitGroupRepository.save(group);

        Funds fund = new Funds();
        fund.setName(name + " Fund");
        fund.setTotalAmount(seededFundBalance);
        fund.setCyclePeriod(duration);
        fund.setInterestRate(9.5);
        fund.setChitGroup(group);
        fund = fundsRepository.save(fund);

        group.setFundId(fund);
        return chitGroupRepository.save(group);
    }

    private Membership createMembership(User user, ChitGroup group, GroupRole role, UserStatus status) {
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setChitGroup(group);
        membership.setRole(role);
        membership.setStatus(status);
        return membershipRepository.save(membership);
    }

    private Notification createNotification(User user, String message, String type, String status) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus(status);
        return notificationRepository.save(notification);
    }

    private Payments createPayment(
            ChitGroup group,
            User user,
            String month,
            double amountPaid,
            String mode,
            String status,
            boolean verified,
            User verifiedBy
    ) {
        Payments payment = new Payments();
        payment.setChitGroup(group);
        payment.setUser(user);
        payment.setMonth(month);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentDate(LocalDateTime.now().minusDays(5));
        payment.setMode(mode);
        payment.setStatus(status);
        payment.setVerified(verified);
        payment.setVerifiedBy(verifiedBy);
        payment.setVerifiedAt(verified ? LocalDateTime.now().minusDays(4) : null);
        return paymentsRepository.save(payment);
    }

    private MemberLoan createLoan(
            ChitGroup group,
            User user,
            double principle,
            double interest,
            double tenure,
            String status,
            LocalDateTime startDate
    ) {
        MemberLoan loan = new MemberLoan();
        loan.setChitGroup(group);
        loan.setUser(user);
        loan.setPrinciple(principle);
        loan.setInterest(interest);
        loan.setTenure(tenure);
        loan.setCurrentPayable(principle);
        loan.setStartDate(startDate);
        loan.setEndDate(startDate.plusMonths((long) tenure));
        loan.setStatus(status);
        return memberLoanRepository.save(loan);
    }

    private Ledger createLedgerEntry(
            ChitGroup group,
            User user,
            String entryType,
            double amount,
            String remark,
            String referenceId
    ) {
        Ledger ledger = new Ledger();
        ledger.setChitGroup(group);
        ledger.setUser(user);
        ledger.setEntryType(entryType);
        ledger.setAmount(amount);
        ledger.setEntryDate(LocalDateTime.now().minusDays(3));
        ledger.setRemark(remark);
        ledger.setReferenceId(referenceId);
        return ledgerRepository.save(ledger);
    }

    private GroupChatMessage createChatMessage(ChitGroup group, User sender, String content) {
        GroupChatMessage message = new GroupChatMessage();
        message.setChitGroup(group);
        message.setSender(sender);
        message.setContent(content);
        message.setMessageType("TEXT");
        return groupChatMessageRepository.save(message);
    }
}
