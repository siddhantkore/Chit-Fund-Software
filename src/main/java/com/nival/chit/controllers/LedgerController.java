package com.nival.chit.controllers;

import com.nival.chit.services.LedgerPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ledger and statement endpoints.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chit/ledger/")
@Tag(name = "Ledger", description = "Download and inspect group ledgers.")
public class LedgerController {

    private final LedgerPdfService ledgerPdfService;

    /**
     * Download a PDF representation of the ledger.
     *
     * <p>Implementation is pending; this endpoint is documented for future use.</p>
     */
    @GetMapping("/pdf")
    @Operation(summary = "Download ledger PDF", description = "Returns a PDF snapshot of the ledger for the current context (implementation pending).")
    public ResponseEntity<byte[]> downloadPdf() {
        return null;
    }
}
