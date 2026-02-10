package com.nival.chit.services;

import com.nival.chit.dto.CreatePollDTO;
import com.nival.chit.dto.PollDTO;
import com.nival.chit.dto.PollOptionDTO;
import com.nival.chit.dto.VoteDTO;
import com.nival.chit.entity.*;
import com.nival.chit.enums.PollStatus;
import com.nival.chit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final VoteRepository voteRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final UserRepository userRepository;

    @Transactional
    public PollDTO createPoll(CreatePollDTO createDTO) {
        ChitGroup group = chitGroupRepository.findById(createDTO.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        User creator = userRepository.findById(createDTO.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Poll poll = new Poll();
        poll.setChitGroup(group);
        poll.setCreator(creator);
        poll.setQuestion(createDTO.getQuestion());
        poll.setStatus(PollStatus.OPEN);
        poll.setExpiresAt(createDTO.getExpiresAt());

        poll = pollRepository.save(poll);

        final Poll savedPoll = poll;
        List<PollOption> options = createDTO.getOptions().stream()
                .map(text -> {
                    PollOption option = new PollOption();
                    option.setPoll(savedPoll);
                    option.setOptionText(text);
                    return option;
                })
                .collect(Collectors.toList());

        pollOptionRepository.saveAll(options);
        poll.setOptions(options);

        log.info("Poll created for group {}: {}", group.getId(), poll.getId());
        return convertToDTO(poll);
    }

    @Transactional
    public void castVote(VoteDTO voteDTO) {
        Poll poll = pollRepository.findById(voteDTO.getPollId())
                .orElseThrow(() -> new IllegalArgumentException("Poll not found"));
        
        if (poll.getStatus() == PollStatus.CLOSED || 
            (poll.getExpiresAt() != null && poll.getExpiresAt().isBefore(LocalDateTime.now()))) {
            throw new IllegalStateException("Poll is closed");
        }

        User user = userRepository.findById(voteDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        PollOption option = pollOptionRepository.findById(voteDTO.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        if (option.getPoll().getId() != poll.getId()) {
            throw new IllegalArgumentException("Option does not belong to this poll");
        }

        // Check if already voted
        voteRepository.findByPollIdAndUserId(poll.getId(), user.getId())
                .ifPresent(v -> { throw new IllegalStateException("Already voted in this poll"); });

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setOption(option);
        vote.setUser(user);

        voteRepository.save(vote);
        log.info("User {} voted in poll {}", user.getId(), poll.getId());
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getGroupPolls(Long groupId) {
        return pollRepository.findByChitGroupIdOrderByCreatedAtDesc(groupId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PollDTO convertToDTO(Poll poll) {
        List<PollOptionDTO> optionDTOs = poll.getOptions().stream()
                .map(opt -> PollOptionDTO.builder()
                        .id(opt.getId())
                        .optionText(opt.getOptionText())
                        .voteCount(opt.getVotes() != null ? opt.getVotes().size() : 0)
                        .build())
                .collect(Collectors.toList());

        return PollDTO.builder()
                .id(poll.getId())
                .groupId(poll.getChitGroup().getId())
                .groupName(poll.getChitGroup().getName())
                .creatorId(poll.getCreator().getId())
                .creatorName(poll.getCreator().getName())
                .question(poll.getQuestion())
                .status(poll.getStatus())
                .expiresAt(poll.getExpiresAt())
                .createdAt(poll.getCreatedAt())
                .options(optionDTOs)
                .build();
    }
}
