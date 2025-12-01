package com.nival.chit.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Payment collection and tracking endpoints.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>Record monthly contributions for members</li>
 *     <li>Track loan repayments and late payments</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/payments/")
@Tag(name = "Payments", description = "Record and view contributions and loan repayments.")
public class PaymentsController {
}
