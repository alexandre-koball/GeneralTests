package com.test.alexandre.wex.testapp.purchase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRate;
import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateDataCache;
import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateService;

@ExtendWith(MockitoExtension.class)
public class PurchaseTransactionServiceTest {

    @Mock
    private PurchaseTransactionRepository purchaseTransactionRepository;

    @InjectMocks
    private PurchaseTransactionService purchaseTransactionService;
    
    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void saveTransaction() {
	PurchaseTransaction transaction = new PurchaseTransaction();
	transaction.setDescription("Some stuff");
	transaction.setPurchaseAmount(1000.0);
	transaction.setTransactionDate(new Date());
	transaction.setTransaction_id((long) 1);

	when(purchaseTransactionRepository.save(transaction)).thenReturn(transaction);
	PurchaseTransaction savedTransaction = purchaseTransactionService.saveTransaction(transaction);
	assertNotNull(savedTransaction);
	assertTrue(savedTransaction.getDescription().equals(transaction.getDescription()));
	assertTrue(savedTransaction.getPurchaseAmount().equals(transaction.getPurchaseAmount()));
	assertTrue(savedTransaction.getTransactionDate().equals(transaction.getTransactionDate()));
	assertTrue(savedTransaction.getTransactionId() != 0);
    }

    @Test
    void getAllTransactions() {
	List<PurchaseTransaction> fakeTransactions = new ArrayList<PurchaseTransaction>();
	for (int i = 0; i < 10; i++) {
	    PurchaseTransaction fakeTransaction = new PurchaseTransaction();
	    fakeTransaction.setDescription("Stuff " + i);
	    fakeTransaction.setPurchaseAmount(13.0 * i);
	    fakeTransaction.setTransactionDate(new Date());
	    fakeTransaction.setTransaction_id((long) i + 1);

	    fakeTransactions.add(fakeTransaction);
	}

	// When calling the mocked repository method
	when(purchaseTransactionRepository.findAll()).thenReturn(fakeTransactions);
	List<PurchaseTransaction> transactions = purchaseTransactionService.getAllTransactions();

	// Then
	assertEquals(fakeTransactions, transactions);
    }

    @Test
    void getPurchaseTransactionNoCurrencyParam() {
	PurchaseTransaction transaction = new PurchaseTransaction();
	transaction.setDescription("Some stuff");
	transaction.setPurchaseAmount(1000.0);
	transaction.setTransactionDate(new Date());
	transaction.setTransaction_id((long) 1);

	when(purchaseTransactionRepository.findById((long) 1)).thenReturn(Optional.of(transaction));
	PurchaseTransaction purchaseTransaction = purchaseTransactionService.getPurchaseTransaction(1, null);

	assertNotNull(purchaseTransaction);
	assertFalse(purchaseTransaction instanceof ConvertedCurrencyPurchaseTransaction); // We didn't give a currency,
											  // so whe don't receive a
											  // converted transaction
	assertEquals(transaction, purchaseTransaction);
	assertNull(purchaseTransactionService.getPurchaseTransaction(2, null)); // Invalid ID, so we get back a null
										// transaction
    }

    @Test
    void getPurchaseTransactionWithCurrencyParam() {
	// Init cache
	ExchangeRateDataCache cache = ExchangeRateDataCache.getInstance();
	cache.init();

	// We can proceed only if the cache was filled
	assertNotNull(cache.getAll());
	assertFalse(cache.getAll().isEmpty());

	PurchaseTransaction transaction = new PurchaseTransaction();
	transaction.setDescription("Some stuff");
	transaction.setPurchaseAmount(1000.0);
	transaction.setTransactionDate(new Date());
	transaction.setTransaction_id((long) 1);

	// Let's use a valid currency in order to not break our tests
	String currency = exchangeRateService.getAvailableCurrencies().get(0);

	when(purchaseTransactionRepository.findById((long) 1)).thenReturn(Optional.of(transaction));
	PurchaseTransaction purchaseTransaction = purchaseTransactionService.getPurchaseTransaction(1, currency);
	assertNotNull(purchaseTransaction);
	assertTrue(purchaseTransaction instanceof ConvertedCurrencyPurchaseTransaction);
	assertNotEquals(transaction, purchaseTransaction);

	ConvertedCurrencyPurchaseTransaction convertedTransaction = (ConvertedCurrencyPurchaseTransaction) purchaseTransaction;
	List<ExchangeRate> allRates = cache.getByCurrency(currency);
	boolean foundExactConversion = false; // We don't know which rate will be used, so we have to test against all
					      // of them

	for (ExchangeRate rate : allRates) {
	    double exchangeRate = Double.parseDouble(rate.getExchange_rate());
	    if (convertedTransaction.getConvertedPurchaseAmount() == convertedTransaction.getPurchaseAmount()
		    * exchangeRate) {
		foundExactConversion = true;
		break;
	    }
	}

	assertTrue(foundExactConversion);
    }

}
