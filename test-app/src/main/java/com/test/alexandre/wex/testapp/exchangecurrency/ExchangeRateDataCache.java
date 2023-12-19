package com.test.alexandre.wex.testapp.exchangecurrency;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Keeps in memory all the currency data available for use of the application.
 * In this cache, the data is kept up to 24 hours, then the next usage of the
 * cache reloads all data from the remote location.
 */
@Component
public class ExchangeRateDataCache {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExchangeDataCurrency exchangeDataCurrency = new FiscalDataTreasuryExchangeDataCurrency();

    private long lastCacheUpdate = -1; // Means it was not init at all

    // Each 24 hours we should renew the cache to get the latest rates
    private int EXPIRATION_TIME_IN_MILLIS = 1000 * 60 * 60 * 24; // 1 day

    @Autowired
    private ExchangeRateData exchangeRateDataCache;

    // We make it a Singleton in order to have just one per JVM
    private static ExchangeRateDataCache INSTANCE = null;

    /**
     * Get the unique point of access for this cache.
     * 
     * @return unique point of access for this cache.
     */
    public static ExchangeRateDataCache getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ExchangeRateDataCache();
	}
	return INSTANCE;
    }

    /**
     * Initializes this cache. If it's already initialized, it won't do it again
     * unless the data has expired.
     */
    public void init() {
	// Only initializes it if it wasn't already
	logger.trace("Init() called.");
	if (shouldReload()) {
	    logger.trace("Cache is empty or expired, let's fill it up!");
	    exchangeRateDataCache = exchangeDataCurrency.getExchangeRateData();
	    if (exchangeRateDataCache != null) {
		lastCacheUpdate = System.currentTimeMillis();
		logger.trace("Cache is now full. Size: " + exchangeRateDataCache.getData().size());
	    } else {
		// We use empty data in order to avoid NPE
		exchangeRateDataCache = new ExchangeRateData();
		exchangeRateDataCache.setData(Collections.emptyList());
		logger.trace("Failed to load cache. App will not be able to exchange rates.");
	    }
	}
    }

    /**
     * Returns the cached data related to a specific currency. Loads the cache if
     * necessary.
     * 
     * @param currency Currency to have the data returned on.
     * @return the cached data related to a specific currency.
     */
    public List<ExchangeRate> getByCurrency(String currency) {
	// Check if it's initialized already
	if (shouldReload()) {
	    init();
	}
	// Check if the currency exists within the cache
	List<ExchangeRate> data = exchangeRateDataCache.getData();
	return data.stream().filter(e -> e.getCurrency().equalsIgnoreCase(currency)).toList();
    }

    /**
     * Returns all the cached data. Loads the cache if necessary.
     * 
     * @return all the cached data.
     */
    public List<ExchangeRate> getAll() {
	// Check if it's initialized already
	if (shouldReload()) {
	    init();
	}
	return exchangeRateDataCache.getData();
    }

    private boolean shouldReload() {
	return ((lastCacheUpdate == -1) || (lastCacheUpdate + EXPIRATION_TIME_IN_MILLIS < System.currentTimeMillis()));
    }

}
