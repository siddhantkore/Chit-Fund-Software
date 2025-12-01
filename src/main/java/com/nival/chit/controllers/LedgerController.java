package com.nival.chit.controllers;

import com.nival.chit.services.LedgerPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chit/ledger/")
public class LedgerController {

    private final LedgerPdfService ledgerPdfService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        return null;
    }
}
