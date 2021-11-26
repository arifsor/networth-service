package com.casestudy.networth.networth.service;

import com.casestudy.networth.networth.config.ApplicationProperties;
import com.casestudy.networth.networth.model.currencyExchange.CurrencyExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CurrencyExchangeRateService {

    private RestTemplate restTemplate;
    private ApplicationProperties properties;
    private Map<String, CurrencyExchangeRate> exchangeRateCache = new HashMap<>();

    @Autowired
    public  CurrencyExchangeRateService(RestTemplate restTemplate, ApplicationProperties properties) {
        this. restTemplate = restTemplate;
        this.properties = properties;
    }

    public CurrencyExchangeRate getExchangeRate(String baseCurrencyIsoCode) {
        CurrencyExchangeRate currencyExchangeRate = exchangeRateCache.get(baseCurrencyIsoCode);
        if(Objects.nonNull(currencyExchangeRate)) {
            return currencyExchangeRate;
        }
        final String url = properties.getCurrencyExchangeUrl() + "?access_key=" + properties.getCurrencyExchangeApiKey() + "&base="+baseCurrencyIsoCode;
        log.info("exchangeRateCall url={}", url);
        currencyExchangeRate = restTemplate.exchange(url, HttpMethod.GET, null, CurrencyExchangeRate.class).getBody();

        /** add exchange rate in cache, this cache could be update hourly based or base on specific requirement */
        exchangeRateCache.put(baseCurrencyIsoCode, currencyExchangeRate);

        return currencyExchangeRate;
    }

    public Map<String, CurrencyExchangeRate> exchangeRateCache() {
        return exchangeRateCache;
    }
}
