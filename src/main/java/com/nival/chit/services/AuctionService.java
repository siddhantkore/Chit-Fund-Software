package com.nival.chit.services;

import com.nival.chit.dto.AuctionDTO;
import com.nival.chit.dto.CreateAuctionDTO;
import com.nival.chit.entity.Auction;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.User;
import com.nival.chit.repository.AuctionRepository;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing auctions.
 * Handles auction creation, winner selection, and auction history.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final UserRepository userRepository;

    /**
     * Create a new auction record.
     *
     * @param createDTO the auction data
     * @return the created auction DTO
     * @throws IllegalArgumentException if group/user not found or auction already exists for month
     */
    @Transactional
    public AuctionDTO createAuction(CreateAuctionDTO createDTO) {
        ChitGroup group = chitGroupRepository.findById(createDTO.getChitGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found: " + createDTO.getChitGroupId()));

        User winningMember = userRepository.findById(createDTO.getWinningMemberId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + createDTO.getWinningMemberId()));

        // Check if auction already exists for this month and group
        if (auctionRepository.findByMonthAndChitGroupId(createDTO.getMonth(), createDTO.getChitGroupId()).isPresent()) {
            throw new IllegalArgumentException("Auction already exists for month " + createDTO.getMonth() + " in this group");
        }

        Auction auction = new Auction();
        auction.setMonth(createDTO.getMonth());
        auction.setWinningAmount(createDTO.getWinningAmount());
        auction.setCommission(createDTO.getCommission());
        auction.setAuctionDate(createDTO.getAuctionDate());
        auction.setRemark(createDTO.getRemark());
        auction.setWinningMemberId(winningMember);
        auction.setChitGroup(group);

        auction = auctionRepository.save(auction);
        log.info("Auction created for group {} in month {}", createDTO.getChitGroupId(), createDTO.getMonth());

        return convertToDTO(auction);
    }

    /**
     * Get all auctions for a chit group.
     *
     * @param groupId the chit group ID
     * @return list of auction DTOs
     */
    @Transactional(readOnly = true)
    public List<AuctionDTO> getAuctionsByGroup(Long groupId) {
        List<Auction> auctions = auctionRepository.findByChitGroupId(groupId);
        return auctions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all auctions won by a user.
     *
     * @param userId the user ID
     * @return list of auction DTOs
     */
    @Transactional(readOnly = true)
    public List<AuctionDTO> getAuctionsByWinningMember(Long userId) {
        List<Auction> auctions = auctionRepository.findByWinningMemberId(userId);
        return auctions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get auction by ID.
     *
     * @param auctionId the auction ID
     * @return auction DTO or null if not found
     */
    @Transactional(readOnly = true)
    public AuctionDTO getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Delete an auction.
     *
     * @param auctionId the auction ID
     * @throws IllegalArgumentException if auction not found
     */
    @Transactional
    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        auctionRepository.delete(auction);
        log.info("Auction deleted: {}", auctionId);
    }

    /**
     * Convert Auction entity to AuctionDTO.
     */
    private AuctionDTO convertToDTO(Auction auction) {
        return AuctionDTO.builder()
                .id(auction.getId())
                .month(auction.getMonth())
                .winningAmount(auction.getWinningAmount())
                .commission(auction.getCommission())
                .auctionDate(auction.getAuctionDate())
                .remark(auction.getRemark())
                .winningMemberId(auction.getWinningMemberId().getId())
                .winningMemberName(auction.getWinningMemberId().getName())
                .chitGroupId(auction.getChitGroup().getId())
                .chitGroupName(auction.getChitGroup().getName())
                .createdAt(auction.getCreatedAt())
                .updatedAt(auction.getUpdatedAt())
                .build();
    }
}

