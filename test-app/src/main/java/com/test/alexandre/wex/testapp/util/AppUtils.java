package com.test.alexandre.wex.testapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRate;

/**
 * Some commonly convenient methods used by this application.
 */
public class AppUtils {

    // This is static because always creating a SimpleDateFormat may be expensive
    private static SimpleDateFormat COMMON_CURRENCY_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");

    /**
     * Round a given {@code double} value to two decimal places (i.e., cent).
     * 
     * @param value original value, not rounded at all.
     * @return value rounded.
     */
    public static double roundToCents(double value) {
	return Double.valueOf(String.format("%.2f", value).replace(',', '.'));
    }

    /**
     * Extract and convert the exchange rate value to a {@code double}.
     * 
     * @param rate exchange rate that holds the exchangeable value.
     * @return converted value.
     */
    public static double convertValue(ExchangeRate rate) {
	return Double.parseDouble(rate.getExchange_rate());
    }

    /**
     * Parse a string on the format <b>YYYY-MM-dd</b> to a {@code Date} object.
     * 
     * @param date date as a string.
     * @return date converted.
     * @throws ParseException ir the given string doesn't contains the expected
     *                        format.
     */
    public static Date parseCommonCurrencyDate(String date) throws ParseException {
	return COMMON_CURRENCY_DATE_FORMAT.parse(date);
    }

    /**
     * Format a {@code Date} into a string like <b>YYYY-MM-dd</b>.
     * 
     * @param date object to be formatted
     * @return date formatted as a string.
     */
    public static String formatIntoCommonCurrencyDate(Date date) {
	return COMMON_CURRENCY_DATE_FORMAT.format(date);
    }

    /**
     * Returns the minimum date on which the currency data is valid for this
     * application.
     * 
     * @return the minimum date on which the currency data is valid for this
     *         application.
     */
    public static Date getCurrencyDataMinDate() {
	Date referenceDate = new Date();
	Calendar c = Calendar.getInstance();
	c.setTime(referenceDate);
	c.add(Calendar.MONTH, -6); // We only want the last 6 months of data from the government
	return c.getTime();
    }

}
