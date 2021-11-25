package com.casestudy.networth.networth.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Account {
    private Long accountId;
    private String accountName;
    private Boolean isControl;
    private AccountBalance amount;
    private List<Account> accounts;
    private AccountSuperType superType;

    public void addSubAccount(Account subAccount) {
        if(Objects.isNull(accounts)) {
            accounts = new ArrayList<>();
        }
        accounts.add(subAccount);
    }
}
