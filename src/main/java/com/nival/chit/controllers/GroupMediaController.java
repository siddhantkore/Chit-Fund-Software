package com.nival.chit.controllers;

import com.nival.chit.dto.GroupMediaDTO;
import com.nival.chit.dto.UploadMediaDTO;
import com.nival.chit.services.GroupMediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chit/media")
@RequiredArgsConstructor
@Tag(name = "Group Media", description = "Poster area for groups - photos and videos.")
public class GroupMediaController {

    private final GroupMediaService groupMediaService;

    @PostMapping("/upload")
    @Operation(summary = "Upload media to group poster area")
    public ResponseEntity<GroupMediaDTO> uploadMedia(@RequestBody UploadMediaDTO uploadDTO) {
        return ResponseEntity.ok(groupMediaService.uploadMedia(uploadDTO));
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Get all media for a group")
    public ResponseEntity<List<GroupMediaDTO>> getGroupMedia(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupMediaService.getGroupMedia(groupId));
    }

    @DeleteMapping("/{mediaId}")
    @Operation(summary = "Delete media")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long mediaId) {
        groupMediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}
