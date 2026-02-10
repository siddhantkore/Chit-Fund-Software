package com.nival.chit.controllers;

import com.nival.chit.dto.CreatePollDTO;
import com.nival.chit.dto.PollDTO;
import com.nival.chit.dto.VoteDTO;
import com.nival.chit.services.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chit/polls")
@RequiredArgsConstructor
@Tag(name = "Polls", description = "Group voting and decision making.")
public class PollController {

    private final PollService pollService;

    @PostMapping
    @Operation(summary = "Create a new poll in a group")
    public ResponseEntity<PollDTO> createPoll(@RequestBody CreatePollDTO createDTO) {
        return ResponseEntity.ok(pollService.createPoll(createDTO));
    }

    @PostMapping("/vote")
    @Operation(summary = "Cast a vote in a poll")
    public ResponseEntity<Void> castVote(@RequestBody VoteDTO voteDTO) {
        pollService.castVote(voteDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Get all polls for a group")
    public ResponseEntity<List<PollDTO>> getGroupPolls(@PathVariable Long groupId) {
        return ResponseEntity.ok(pollService.getGroupPolls(groupId));
    }
}
