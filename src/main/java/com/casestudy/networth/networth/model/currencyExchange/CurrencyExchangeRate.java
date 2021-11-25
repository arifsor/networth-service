package com.casestudy.networth.networth.model.currencyExchange;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class CurrencyExchangeRate {
    private Map<String, BigDecimal> rates;
}
