package com.test.alexandre.wex.testapp.exchangecurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.alexandre.wex.testapp.util.AppUtils;

/**
 * Implementation of {@code ExchangeDataCurrency} for the U.S. government's
 * Treasury Reporting Rates of Exchange. More info on
 * {@link https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange}.
 */
public class FiscalDataTreasuryExchangeDataCurrency implements ExchangeDataCurrency {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    // Endpoint with all relevant filters and fields
    private static final String FISCAL_DATA_TREASURY_ENDPOINT = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?filter=record_date:gt:%s&fields=record_date,country,currency,country_currency_desc,exchange_rate";

    /**
     * {@inheritDoc}
     */
    @Override
    public ExchangeRateData getExchangeRateData() {
	HttpURLConnection connection = null;
	try {
	    URL url = new URL(String.format(FISCAL_DATA_TREASURY_ENDPOINT, getCurrencyDataMinDate()));
	    logger.trace("Calling Treasury Reporting Rates of Exchange's API on URL " + url);
	    connection = (HttpURLConnection) url.openConnection();

	    // Set request method to GET
	    connection.setRequestMethod("GET");

	    // Get the response code
	    int responseCode = connection.getResponseCode();

	    if (responseCode == HttpURLConnection.HTTP_OK) {
		// Read the response
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder response = new StringBuilder();

		while ((line = reader.readLine()) != null) {
		    response.append(line);
		}

		reader.close();

		ObjectMapper objectMapper = new ObjectMapper();
		ExchangeRateData exchangeRateData = objectMapper.readValue(response.toString(), ExchangeRateData.class);

		return exchangeRateData;

	    } else {
		logger.error("Failed to retrieve data. Response code: " + responseCode);
	    }

	} catch (IOException e) {
	    logger.error("Exception: " + e.getMessage(), e);
	} finally {
	    if (connection != null) {
		connection.disconnect();
	    }
	}
	return null;
    }

    private static String getCurrencyDataMinDate() {
	return AppUtils.formatIntoCommonCurrencyDate(AppUtils.getCurrencyDataMinDate());
    }

}
