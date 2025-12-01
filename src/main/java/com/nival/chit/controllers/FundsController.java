package com.nival.chit.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * High‑level view over funds managed by chit groups.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>Summaries of group funds and balances</li>
 *     <li>Administrative views of inflows and outflows</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/funds/")
@Tag(name = "Funds", description = "View overall funds managed by chit groups.")
public class FundsController {
}
