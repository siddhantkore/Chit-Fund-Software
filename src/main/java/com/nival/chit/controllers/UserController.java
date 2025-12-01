package com.nival.chit.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User and profile related endpoints.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>CRUD operations for users (admin only)</li>
 *     <li>Self‑service profile endpoints for members</li>
 *     <li>Integration with authentication layer</li>
 * </ul>
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/chit/user/")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Manage users, profiles, and roles in the chit platform.")
public class UserController {

}
