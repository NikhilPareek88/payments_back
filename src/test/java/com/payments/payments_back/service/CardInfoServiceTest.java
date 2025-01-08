package com.payments.payments_back.service;

import com.payments.payments_back.clients.BinlistClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardInfoServiceTest {

    @Mock
    private BinlistClient binlistClient;

    @InjectMocks
    private CardInfoService cardInfoService;

    @Test
    void testGetCardCountry_Success() throws Exception {
        String cardNumber = "371240328956";
        String expectedCountry = "US";

        when(binlistClient.getCardCountry(cardNumber)).thenReturn(expectedCountry);

        String actualCountry = cardInfoService.getCardCountry(cardNumber);

        assertEquals(expectedCountry, actualCountry, "The card country should be 'US'");
        verify(binlistClient, times(1)).getCardCountry(cardNumber);
    }

    @Test
    void testGetCardCountry_ExceptionThrown() throws Exception {
        String cardNumber = "371240328956";

        when(binlistClient.getCardCountry(cardNumber)).thenThrow(new Exception("API Error"));

        Exception exception = assertThrows(Exception.class, () -> {
            cardInfoService.getCardCountry(cardNumber);
        });

        assertEquals("API Error", exception.getMessage(), "The exception message should match the expected error");
    }
}