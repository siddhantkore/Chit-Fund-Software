package com.nival.chit.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notification configuration and delivery endpoints.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>Configure notification rules for contributions, loans, and auctions</li>
 *     <li>Allow admins and members to manage their notification preferences</li>
 *     <li>Expose notification history per user and per group</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/notifications/")
@Tag(name = "Notifications", description = "Configure and inspect notifications sent by the platform.")
public class NotificationController {
}
