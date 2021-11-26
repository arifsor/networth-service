package com.casestudy.networth.networth.model.currencyExchange;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString
public class CurrencyExchangeRate {
    private Map<String, BigDecimal> rates;
}
