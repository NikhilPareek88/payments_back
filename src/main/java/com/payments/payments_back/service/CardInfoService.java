package com.payments.payments_back.service;

import com.payments.payments_back.clients.BinlistClient;
import org.springframework.stereotype.Service;

@Service
public class CardInfoService {

    private final BinlistClient binlistClient;

    public CardInfoService(BinlistClient binlistClient) {
        this.binlistClient = binlistClient;
    }

    public String getCardCountry(String cardNumber) throws Exception {
        return binlistClient.getCardCountry(cardNumber);
    }
}
