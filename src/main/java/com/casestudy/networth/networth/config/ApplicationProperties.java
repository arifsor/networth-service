package com.casestudy.networth.networth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class ApplicationProperties {
    @Value("${rest.currencyExchange.url}")
    private String currencyExchangeUrl;
    @Value("${rest.currencyExchange.apiKey}")
    private String currencyExchangeApiKey;
}
