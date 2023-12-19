package com.test.alexandre.wex.testapp.exchangecurrency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A unit of currency exchange data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {

    private String record_date;
    private String country;
    private String currency;
    private String country_currency_desc;
    private String exchange_rate;

    public String getRecord_date() {
	return record_date;
    }

    public void setRecord_date(String record_date) {
	this.record_date = record_date;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getCurrency() {
	return currency;
    }

    public void setCurrency(String currency) {
	this.currency = currency;
    }

    public String getCountry_currency_desc() {
	return country_currency_desc;
    }

    public void setCountry_currency_desc(String country_currency_desc) {
	this.country_currency_desc = country_currency_desc;
    }

    public String getExchange_rate() {
	return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
	this.exchange_rate = exchange_rate;
    }

}
