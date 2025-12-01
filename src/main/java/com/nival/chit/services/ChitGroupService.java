package com.nival.chit.services;

import com.nival.chit.entity.ChitGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChitGroupService {

//    private final ChitGroupRepository chitGroupRepository;

    public ChitGroup createGroup(ChitGroup group) {
        group.setGroupCode(generateGroupCode());
//        return chitGroupRepository.save(group);
        return null;
    }

    private String generateGroupCode() {
//        long count = chitGroupRepository.count() + 1;
//        return String.format("CHIT%05d", count); // CHIT00001, CHIT00002
        return null;
    }
}

