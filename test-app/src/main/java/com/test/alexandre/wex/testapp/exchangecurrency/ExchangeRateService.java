package com.test.alexandre.wex.testapp.exchangecurrency;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.test.alexandre.wex.testapp.util.AppUtils;

/**
 * Main responsible for exchange rate data.
 */
@Service
public class ExchangeRateService {

    /**
     * Returns all available currencies supported at this moment by this
     * application. If a currency is supported or not, it depends on the data
     * available from an the external source. The list is sent ordered.
     * 
     * @return all available currencies supported at this moment by this
     *         application.
     */
    public List<String> getAvailableCurrencies() {
	List<ExchangeRate> allExchangeRates = ExchangeRateDataCache.getInstance().getAll();
	List<String> allCurrencies = new ArrayList<String>();
	for (ExchangeRate rate : allExchangeRates) {
	    if (!allCurrencies.contains(rate.getCurrency())) {
		allCurrencies.add(rate.getCurrency());
	    }
	}
	Collections.sort(allCurrencies);
	return allCurrencies;
    }

    /**
     * Returns a list ordered by currency name of all available exchange rates from
     * the application. Only the most recent rate for each currency is returned.
     * 
     * @return all available exchange rates from the application.
     */
    public List<ExchangeRate> getCurrentExchangeRates() {
	final List<ExchangeRate> allExchangeRates = ExchangeRateDataCache.getInstance().getAll();
	final Map<String, ExchangeRate> currentExchangeRates = new HashMap<String, ExchangeRate>();
	for (ExchangeRate rate : allExchangeRates) {
	    String currency = rate.getCurrency();
	    // If it doesn't exist, add it anyway
	    if (!currentExchangeRates.containsKey(currency)) {
		currentExchangeRates.put(currency, rate);
	    } else {
		// If it exists, check if it is newer than the existing one
		ExchangeRate priorRate = currentExchangeRates.get(currency);
		try {
		    Date priorDate = AppUtils.parseCommonCurrencyDate(priorRate.getRecord_date());
		    Date rateDate = AppUtils.parseCommonCurrencyDate(rate.getRecord_date());

		    if (rateDate.after(priorDate)) {
			currentExchangeRates.put(currency, rate);
		    }

		} catch (ParseException e) {
		    // Invalid rate data, so we just keep going...
		    continue;
		}
	    }
	}
	final List<ExchangeRate> selectedRates = new ArrayList<ExchangeRate>(currentExchangeRates.values());
	Collections.sort(selectedRates, new Comparator<ExchangeRate>() {
	    @Override
	    public int compare(ExchangeRate o1, ExchangeRate o2) {
		return o1.getCurrency().compareTo(o2.getCurrency());
	    }
	});
	return selectedRates;
    }
    
}
