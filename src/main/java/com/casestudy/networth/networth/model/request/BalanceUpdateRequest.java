package com.casestudy.networth.networth.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BalanceUpdateRequest {
    private String currencyIsoCode;
    private List<AccountBalanceUpdate> accounts;
}
