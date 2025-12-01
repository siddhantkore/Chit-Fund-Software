package com.nival.chit.controllers.chitgroup;

import com.nival.chit.services.ChitGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/chit/chit/")
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
