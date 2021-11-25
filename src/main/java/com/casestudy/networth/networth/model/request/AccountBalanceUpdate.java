package com.casestudy.networth.networth.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountBalanceUpdate {
    private Long accountId;
    private BigDecimal amount;
}
