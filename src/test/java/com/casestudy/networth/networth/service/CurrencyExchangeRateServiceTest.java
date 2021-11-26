package com.casestudy.networth.networth.service;

import com.casestudy.networth.networth.config.ApplicationProperties;
import com.casestudy.networth.networth.model.currencyExchange.CurrencyExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeRateServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApplicationProperties properties;

    private CurrencyExchangeRateService currencyExchangeRateService;
    private CurrencyExchangeRate exchangeRates = new CurrencyExchangeRate();

    @BeforeEach
    public void setup() {
        currencyExchangeRateService = new CurrencyExchangeRateService(restTemplate, properties);
        HashMap<String, BigDecimal> exchangeRateMap = new HashMap<>();
        exchangeRateMap.put("USD", BigDecimal.ONE);
        exchangeRateMap.put("CAD", BigDecimal.TEN);
        exchangeRates.setRates(exchangeRateMap);
    }

    @Test
    public void getExchangeRate_Success() {
        CurrencyExchangeRate actualResult = getCurrencyExchangeRate("USD");
        assertEquals(BigDecimal.ONE, actualResult.getRates().get("USD"));
        assertEquals(BigDecimal.TEN, actualResult.getRates().get("CAD"));
        assertEquals(2, exchangeRates.getRates().size());
        assertEquals(1, currencyExchangeRateService.exchangeRateCache().size());
    }

    @Test
    public void getExchangeRate_Cache() {
        assertEquals(0, currencyExchangeRateService.exchangeRateCache().size());
        getCurrencyExchangeRate("USD");
        getCurrencyExchangeRate("CAD");
        assertEquals(2, currencyExchangeRateService.exchangeRateCache().size());
    }

    private CurrencyExchangeRate getCurrencyExchangeRate(String baseCurrency) {
        when(properties.getCurrencyExchangeUrl()).thenReturn("http://localhost");
        when(properties.getCurrencyExchangeApiKey()).thenReturn("rookKillsQueen");
        String url = "http://localhost?access_key=rookKillsQueen&base=" + baseCurrency;
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(null), eq(CurrencyExchangeRate.class)))
                .thenReturn(new ResponseEntity<>(exchangeRates, HttpStatus.OK));
        return currencyExchangeRateService.getExchangeRate(baseCurrency);
    }
}
