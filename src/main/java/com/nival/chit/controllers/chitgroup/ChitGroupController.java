package com.nival.chit.controllers.chitgroup;

import com.nival.chit.services.ChitGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chit group lifecycle operations.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>Create and delete chit groups</li>
 *     <li>Rename and update group metadata</li>
 *     <li>Search chit groups by id or name</li>
 * </ul>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/chit/chit/")
@Tag(name = "Chit Groups", description = "Create, manage, and search chit groups.")
public class ChitGroupController {
    private final ChitGroupService chitGroupService;

    public void createChitGroup() {

    }

    public void deleteChitGroup() {

    }

    public void searchChitGroupById() {

    }

    public void renameChitGroup() {

    }

    public void searchChitGroupByName() {

    }

}
