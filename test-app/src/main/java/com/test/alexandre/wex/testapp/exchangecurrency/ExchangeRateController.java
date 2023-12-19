package com.test.alexandre.wex.testapp.exchangecurrency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Holds the API responsible for exchange rate data.
 */
@RestController
@RequestMapping("/exchange-rate")
@CrossOrigin
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService service;
    
    @GetMapping("/get-available-currencies")
    public List<String> getAvailableCurrencies() {
	return service.getAvailableCurrencies();
    }

    @GetMapping("/get-current-exchange-rates")
    public List<ExchangeRate> getCurrentExchangeRates() {
	return service.getCurrentExchangeRates();
    }
    
}
