package com.test.alexandre.wex.testapp.exchangecurrency;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * All the units of exchange currency data available.
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateData {

    private List<ExchangeRate> data;

    public List<ExchangeRate> getData() {
	return data;
    }

    public void setData(List<ExchangeRate> data) {
	this.data = data;
    }

}
