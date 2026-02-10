package com.nival.chit.services;

import com.nival.chit.dto.GroupMediaDTO;
import com.nival.chit.dto.UploadMediaDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.GroupMedia;
import com.nival.chit.entity.User;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.GroupMediaRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupMediaService {

    private final GroupMediaRepository groupMediaRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupMediaDTO uploadMedia(UploadMediaDTO uploadDTO) {
        ChitGroup group = chitGroupRepository.findById(uploadDTO.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        User uploader = userRepository.findById(uploadDTO.getUploaderId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMedia media = new GroupMedia();
        media.setChitGroup(group);
        media.setUploader(uploader);
        media.setUrl(uploadDTO.getUrl());
        media.setCaption(uploadDTO.getCaption());
        media.setMediaType(uploadDTO.getMediaType());

        media = groupMediaRepository.save(media);
        log.info("Media uploaded for group {}: {}", group.getId(), media.getId());

        return convertToDTO(media);
    }

    @Transactional(readOnly = true)
    public List<GroupMediaDTO> getGroupMedia(Long groupId) {
        return groupMediaRepository.findByChitGroupIdOrderByCreatedAtDesc(groupId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMedia(Long mediaId) {
        groupMediaRepository.deleteById(mediaId);
    }

    private GroupMediaDTO convertToDTO(GroupMedia media) {
        return GroupMediaDTO.builder()
                .id(media.getId())
                .groupId(media.getChitGroup().getId())
                .uploaderId(media.getUploader().getId())
                .uploaderName(media.getUploader().getName())
                .url(media.getUrl())
                .caption(media.getCaption())
                .mediaType(media.getMediaType())
                .createdAt(media.getCreatedAt())
                .build();
    }
}
