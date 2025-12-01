package com.nival.chit.services;

import com.nival.chit.repository.FundsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundsService {

    private final FundsRepository fundsRepository;
}
