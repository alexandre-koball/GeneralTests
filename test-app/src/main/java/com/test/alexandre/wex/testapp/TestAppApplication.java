package com.test.alexandre.wex.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateDataCache;

@SpringBootApplication
public class TestAppApplication {

    public static void main(String[] args) {
	SpringApplication.run(TestAppApplication.class, args);
	// init exchange rate cache
	ExchangeRateDataCache.getInstance().init();
    }

}
