package com.casestudy.networth.networth.service;

import com.casestudy.networth.networth.model.*;
import com.casestudy.networth.networth.model.Currency;
import com.casestudy.networth.networth.model.currencyExchange.CurrencyExchangeRate;
import com.casestudy.networth.networth.model.request.AccountBalanceUpdate;
import com.casestudy.networth.networth.model.request.BalanceUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class NetworthService {

    private Networth networth;
    private List<Currency> currencies = new ArrayList<>();

    private CurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    public NetworthService(CurrencyExchangeRateService currencyExchangeRateService) {

        this.currencyExchangeRateService = currencyExchangeRateService;

        currencies.add(new Currency("EUR", "€"));
        currencies.add(new Currency("CAD", "$"));
        currencies.add(new Currency("USD", "$"));
        currencies.add(new Currency("GBP", "£"));

        networth = new Networth();
        networth.setCurrency(new Currency("CAD", "$"));
        List<Account> assetsAccounts = new ArrayList<>();
        List<Account> liabilitiesAccounts = new ArrayList<>();

        Account cashAndInvestments = new Account();
        cashAndInvestments.setAccountId(1000L);
        cashAndInvestments.setAccountName("Cash and Investments");
        cashAndInvestments.setIsControl(true);
        cashAndInvestments.setSuperType(AccountSuperType.ASSETS);

        Account chequing = new Account();
        chequing.setAccountId(1001L);
        chequing.setSuperType(cashAndInvestments.getSuperType());
        chequing.setAccountName("Chequing");
        chequing.setIsControl(false);
        chequing.setAmount(new AccountBalance(new BigDecimal(2000)));
        cashAndInvestments.addSubAccount(chequing);

        Account savingAndTaxes = new Account();
        savingAndTaxes.setAccountId(1002L);
        savingAndTaxes.setSuperType(cashAndInvestments.getSuperType());
        savingAndTaxes.setAccountName("Saving and Taxes");
        savingAndTaxes.setIsControl(false);
        savingAndTaxes.setAmount(new AccountBalance(new BigDecimal(4500)));
        cashAndInvestments.addSubAccount(savingAndTaxes);

        Account rainingDayFunds = new Account();
        rainingDayFunds.setAccountId(1003L);
        rainingDayFunds.setSuperType(cashAndInvestments.getSuperType());
        rainingDayFunds.setAccountName("Rainy Day Fund");
        rainingDayFunds.setIsControl(false);
        rainingDayFunds.setAmount(new AccountBalance(new BigDecimal(500)));
        cashAndInvestments.addSubAccount(rainingDayFunds);

        Account savingForTravel = new Account();
        savingForTravel.setAccountId(1004L);
        savingForTravel.setSuperType(cashAndInvestments.getSuperType());
        savingForTravel.setAccountName("Saving for Travel");
        savingForTravel.setIsControl(false);
        savingForTravel.setAmount(new AccountBalance(new BigDecimal(6000)));
        cashAndInvestments.addSubAccount(savingForTravel);

        assetsAccounts.add(cashAndInvestments);

        Account longTermAssets = new Account();
        longTermAssets.setAccountId(1005L);
        longTermAssets.setAccountName("Long Term Assets");
        longTermAssets.setIsControl(true);
        longTermAssets.setSuperType(AccountSuperType.ASSETS);

        Account primaryHome = new Account();
        primaryHome.setAccountId(1006L);
        primaryHome.setSuperType(longTermAssets.getSuperType());
        primaryHome.setAccountName("Primary Home");
        primaryHome.setIsControl(false);
        primaryHome.setAmount(new AccountBalance(new BigDecimal(12000)));
        longTermAssets.addSubAccount(primaryHome);

        Account secondHome = new Account();
        secondHome.setAccountId(1007L);
        secondHome.setSuperType(longTermAssets.getSuperType());
        secondHome.setAccountName("Second Home");
        secondHome.setIsControl(false);
        secondHome.setAmount(new AccountBalance(new BigDecimal(10000)));
        longTermAssets.addSubAccount(secondHome);

        assetsAccounts.add(longTermAssets);

        Account shortTermLiabilities = new Account();
        shortTermLiabilities.setAccountId(2000L);
        shortTermLiabilities.setAccountName("Short Term Liabilities");
        shortTermLiabilities.setIsControl(true);
        shortTermLiabilities.setSuperType(AccountSuperType.LIABILITIES);

        Account creditCard1 = new Account();
        creditCard1.setAccountId(2001L);
        creditCard1.setSuperType(shortTermLiabilities.getSuperType());
        creditCard1.setAccountName("Credit Card 1");
        creditCard1.setIsControl(false);
        creditCard1.setAmount(new AccountBalance(new BigDecimal(2000)));
        shortTermLiabilities.addSubAccount(creditCard1);

        Account creditCard2 = new Account();
        creditCard2.setAccountId(2002L);
        creditCard2.setSuperType(shortTermLiabilities.getSuperType());
        creditCard2.setAccountName("Credit Card 1");
        creditCard2.setIsControl(false);
        creditCard2.setAmount(new AccountBalance(new BigDecimal(2000)));
        shortTermLiabilities.addSubAccount(creditCard2);

        liabilitiesAccounts.add(shortTermLiabilities);

        Account longTermDebts = new Account();
        longTermDebts.setAccountId(2003L);
        longTermDebts.setAccountName("Long Term Debts");
        longTermDebts.setIsControl(true);
        longTermDebts.setSuperType(AccountSuperType.LIABILITIES);

        Account mortgage1 = new Account();
        mortgage1.setAccountId(2004L);
        mortgage1.setSuperType(longTermDebts.getSuperType());
        mortgage1.setAccountName("Mortgage 1");
        mortgage1.setIsControl(false);
        mortgage1.setAmount(new AccountBalance(new BigDecimal(2000)));
        longTermDebts.addSubAccount(mortgage1);

        Account mortgage2 = new Account();
        mortgage2.setAccountId(2005L);
        mortgage2.setSuperType(longTermDebts.getSuperType());
        mortgage2.setAccountName("Mortgage 2");
        mortgage2.setIsControl(false);
        mortgage2.setAmount(new AccountBalance(new BigDecimal(2000)));
        longTermDebts.addSubAccount(mortgage2);

        liabilitiesAccounts.add(longTermDebts);

        networth.setAssets(new AccountInfo(null, assetsAccounts));
        networth.setLiabilities(new AccountInfo(null, liabilitiesAccounts));

    }

    public Networth getNetworth(String currencyIsoCode) {
        return calculateNetworth(currencyIsoCode);
    }

    public Networth updateBalance(BalanceUpdateRequest balanceUpdateRequest) {
        for(AccountBalanceUpdate accountModel: balanceUpdateRequest.getAccounts()) {
            Account account = findAccountInNetworth(networth, accountModel.getAccountId());
            if(Objects.nonNull(account)) {
                account.setAmount(new AccountBalance(accountModel.getAmount()));
            }
        }
        return calculateNetworth(balanceUpdateRequest.getCurrencyIsoCode());
    }

    /** Private Methods **/

    private Networth calculateNetworth(String currencyIsoCode) {
        Currency newCurrency = findCurrency(currencyIsoCode);
        List<Account> assetsAccounts = networth.getAssets().getAccounts();
        List<Account> liabilitiesAccounts = networth.getLiabilities().getAccounts();

        /**
         * If current networth currency is not equal to the new currency then
         * call method recursively and update existing balance of all subsidiary accounts
         * */
        if (!networth.getCurrency().getIsoCode().equals(currencyIsoCode)) {
            BigDecimal currencyFactor = getCurrencyFactor(networth.getCurrency(), newCurrency);
            updateAllAccountBalance(currencyFactor, assetsAccounts);
            updateAllAccountBalance(currencyFactor, liabilitiesAccounts);
        }

        BigDecimal assetsTotal = calculateAccountsTotal(assetsAccounts);
        BigDecimal liabilitiesTotal = calculateAccountsTotal(liabilitiesAccounts);

        networth.setAssets(new AccountInfo(assetsTotal, assetsAccounts));
        networth.setLiabilities(new AccountInfo(liabilitiesTotal, liabilitiesAccounts));
        networth.setNetworthValue(assetsTotal.add(liabilitiesTotal));
        networth.setCurrency(newCurrency);

        return  networth;
    }

    private Account findAccountInNetworth(Networth networth, Long accountId) {
        Account account = null;
        account = findAccount(networth.getAssets().getAccounts(), accountId);
        if(Objects.isNull(account)) {
            account = findAccount(networth.getLiabilities().getAccounts(), accountId);
        }
        return account;

    }

    private Account findAccount(List<Account> accounts, Long accountId) {

        for(Account account: accounts) {
            if(accountId.equals(account.getAccountId())) {
                return account;
            }
            if(!CollectionUtils.isEmpty(account.getAccounts())) {
                Account _account =  findAccount(account.getAccounts(), accountId);
                if(Objects.nonNull(_account)) {
                    return _account;
                }
            }
        }
        return null;

    }

    private BigDecimal calculateAccountsTotal(List<Account> accounts) {
        BigDecimal total = BigDecimal.ZERO;
        for(Account account : accounts) {
            if(Objects.nonNull(account.getAmount()) && Objects.nonNull(account.getAmount().getBalance())) {
                total = total.add(account.getAmount().getBalance());
            }
            if(!CollectionUtils.isEmpty(account.getAccounts())) {
                total = total.add(calculateAccountsTotal(account.getAccounts()));
            }
        }
        return total;
    }

    private Currency findCurrency(String currencyIsoCode) {
        return currencies
                .stream()
                .filter(currency -> currencyIsoCode.equals(currency.getIsoCode()))
                .findFirst().orElse(null);
    }

    private BigDecimal getCurrencyFactor(Currency fromCurrency, Currency toCurrency) {
        BigDecimal currencyFactor = BigDecimal.ONE;
        CurrencyExchangeRate exchangeRate = currencyExchangeRateService.getExchangeRate(fromCurrency.getIsoCode());
        if(Objects.nonNull(exchangeRate)  && Objects.nonNull(exchangeRate.getRates())) {
            BigDecimal factor = exchangeRate.getRates().get(toCurrency.getIsoCode());
            if(Objects.nonNull(factor)) {
                currencyFactor = factor;
            }
        }
        return  currencyFactor;
    }

    private void updateAllAccountBalance(BigDecimal currencyFactor, List<Account> accounts) {
        for(Account account : accounts) {
            AccountBalance accountBalance = account.getAmount();
            if(Objects.nonNull(accountBalance) && Objects.nonNull(accountBalance.getBalance())) {
                accountBalance.setBalance(accountBalance.getBalance().multiply(currencyFactor).setScale(2, RoundingMode.HALF_UP));
            }
            if(!CollectionUtils.isEmpty(account.getAccounts())) {
                updateAllAccountBalance(currencyFactor, account.getAccounts());
            }
        }
    }
}
