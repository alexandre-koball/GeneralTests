package com.test.alexandre.wex.testapp.exchangecurrency;

import org.springframework.stereotype.Component;

/**
 * Common interface to all classes that obtain and keep exchanging currency
 * data from a particular source.
 */
@Component
public interface ExchangeDataCurrency {

    /**
     * Get all available exchange currency data.
     * 
     * @return available exchange currency data.
     */
    ExchangeRateData getExchangeRateData();

}
