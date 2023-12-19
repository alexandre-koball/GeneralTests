package com.test.alexandre.wex.testapp.purchase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRate;
import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateDataCache;
import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateService;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;
    
    @Test
    void getCurrentExchangeRates() {
	// Init cache
	ExchangeRateDataCache cache = ExchangeRateDataCache.getInstance();
	cache.init();

	// We can proceed only if the cache was filled
	assertNotNull(cache.getAll());
	assertFalse(cache.getAll().isEmpty());
	
	List<ExchangeRate> currentExchangeRates = exchangeRateService.getCurrentExchangeRates();
	assertNotNull(currentExchangeRates);
	assertFalse(currentExchangeRates.isEmpty());
	
	// Check if there are no duplicated itens
	List<String> checkedCurrencies = new ArrayList<String>();
	for (ExchangeRate rate : currentExchangeRates) {
	    assertFalse(checkedCurrencies.contains(rate.getCurrency()));
	    checkedCurrencies.add(rate.getCurrency());
	}
    }
    
}
