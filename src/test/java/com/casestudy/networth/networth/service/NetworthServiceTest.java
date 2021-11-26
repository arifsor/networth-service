package com.casestudy.networth.networth.service;

import com.casestudy.networth.networth.model.Account;
import com.casestudy.networth.networth.model.Networth;
import com.casestudy.networth.networth.model.currencyExchange.CurrencyExchangeRate;
import com.casestudy.networth.networth.model.request.AccountBalanceUpdate;
import com.casestudy.networth.networth.model.request.BalanceUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class NetworthServiceTest {

    @Mock
    private CurrencyExchangeRateService currencyExchangeRateService;

    private NetworthService networthService;

    @BeforeEach
    public void setup() {
        networthService = new NetworthService(currencyExchangeRateService);
    }

    @Test
    public void getNetworth_WithDefaultValue() {
        Networth networth = networthService.getNetworth("CAD");
        assertNotNull(networth);
        assertEquals(2, networth.getAssets().getAccounts().size());
        assertEquals(2, networth.getLiabilities().getAccounts().size());
        assertEquals(new BigDecimal(35000), networth.getAssets().getTotal());
        assertEquals(new BigDecimal(8000), networth.getLiabilities().getTotal());
        assertEquals(new BigDecimal(43000), networth.getNetworthValue());
    }

    @Test
    public void getNetworth_WithChangedCurrency() {
        CurrencyExchangeRate exchangeRates = new CurrencyExchangeRate();
        HashMap<String, BigDecimal> exchangeRateMap = new HashMap<>();
        exchangeRateMap.put("USD", BigDecimal.TEN);
        exchangeRateMap.put("CAD", BigDecimal.ONE);
        exchangeRates.setRates(exchangeRateMap);

        when(currencyExchangeRateService.getExchangeRate(eq("CAD"))).thenReturn(exchangeRates);
        Networth networth = networthService.getNetworth("USD");
        assertNotNull(networth);
        assertEquals(new BigDecimal(35000).multiply(BigDecimal.TEN).setScale(2), networth.getAssets().getTotal());
        assertEquals(new BigDecimal(8000).multiply(BigDecimal.TEN).setScale(2), networth.getLiabilities().getTotal());
        assertEquals(new BigDecimal(43000).multiply(BigDecimal.TEN).setScale(2), networth.getNetworthValue());
        assertEquals("USD", networth.getCurrency().getIsoCode());
    }

    @Test
    public void updateBalance_Success() {

        AccountBalanceUpdate accountBalanceUpdate = new AccountBalanceUpdate();
        accountBalanceUpdate.setAccountId(1001L);
        accountBalanceUpdate.setAmount(new BigDecimal(1000));

        BalanceUpdateRequest request = new BalanceUpdateRequest();
        request.setCurrencyIsoCode("CAD");
        request.setAccounts(Collections.singletonList(accountBalanceUpdate));

        Networth networth = networthService.updateBalance(request);
        assertNotNull(networth);
        assertEquals(new BigDecimal(34000), networth.getAssets().getTotal());
        assertEquals(new BigDecimal(8000), networth.getLiabilities().getTotal());
        assertEquals(new BigDecimal(42000), networth.getNetworthValue());

        Account chequingAccount = networth.getAssets().getAccounts().get(0).getAccounts().get(0);
        assertEquals(new BigDecimal(1000), chequingAccount.getAmount().getBalance());
    }

}
