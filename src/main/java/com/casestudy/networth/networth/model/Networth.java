package com.casestudy.networth.networth.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Networth {
    private Currency currency;
    private BigDecimal networthValue;
    private AccountInfo assets;
    private AccountInfo liabilities;

}
