package com.nival.chit.services;

import com.nival.chit.entity.Ledger;
import com.nival.chit.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public List<Ledger> getAllLedgerRecord() {
        return ledgerRepository.findAll();
    }
}
