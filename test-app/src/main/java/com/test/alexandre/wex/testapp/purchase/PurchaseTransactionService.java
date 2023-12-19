package com.test.alexandre.wex.testapp.purchase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRate;
import com.test.alexandre.wex.testapp.exchangecurrency.ExchangeRateDataCache;
import com.test.alexandre.wex.testapp.util.AppUtils;

/**
 * Main responsible for saving the returning data related to transaction purchases.
 */
@Service
public class PurchaseTransactionService {

    private final PurchaseTransactionRepository transactionRepository;

    @Autowired
    public PurchaseTransactionService(PurchaseTransactionRepository transactionRepository) {
	this.transactionRepository = transactionRepository;
    }

    /**
     * Saves a purchase transaction into the repository.
     * 
     * @param transaction purchase transaction that should be saved.
     * @return the saved transaction, in general, is the same object with an added
     *         Id.
     */
    public PurchaseTransaction saveTransaction(PurchaseTransaction transaction) {
	return transactionRepository.save(transaction);
    }

    /**
     * Returns all the purchase transactions stored in the repository.
     * 
     * @return all the purchase transactions stored in the repository.
     */
    public List<PurchaseTransaction> getAllTransactions() {
	return transactionRepository.findAll();
    }

    /**
     * Returns all the purchase transactions stores in the repository with added
     * data based on a given currency.
     * 
     * @param currency currency to convert the data from the purchase transaction
     *                 into. Can be {@code null}, in this case the method returns
     *                 all the transactions with no added data.
     * @return all the purchase transactions stores in the repository, converted or
     *         not.
     */
    public List<PurchaseTransaction> getAllTransactionsConverted(String currency) {
	// If there's no currency param, return the stored objects
	final List<PurchaseTransaction> allTransactions = getAllTransactions();
	if (currency == null || "".equals(currency)) {
	    return allTransactions;
	}
	final List<PurchaseTransaction> allTransactionsConverted = new ArrayList<PurchaseTransaction>(
		allTransactions.size());
	for (PurchaseTransaction transaction : allTransactions) {
	    // Search the rate with the date of the transaction
	    Date transactionDate = transaction.getTransactionDate();
	    // If the transaction is olden than 6 months, we won't be able to convert it
	    if (!isWithinMinDate(transactionDate)) {
		allTransactionsConverted.add(transaction);
		continue;
	    }
	    ExchangeRate exchangeRateForCurrency = getExchangeRateForCurrency(transaction.getTransactionDate(),
		    currency);
	    // If there's no best/any rate, we won't be able to conver it
	    if (exchangeRateForCurrency == null) {
		allTransactionsConverted.add(transaction);
		continue;
	    }
	    PurchaseTransaction transactionConverted = convertPurchaseTransaction(transaction, exchangeRateForCurrency);
	    allTransactionsConverted.add(transactionConverted);
	}
	return allTransactionsConverted;
    }

    /**
     * Returns a particular purchase transaction related to it's Id.
     * 
     * @param transactionId purchase transaction's Id.
     * @return the purchased returned from the repository of {@code null} if there's
     *         none with the given Id.
     */
    public PurchaseTransaction getTransactionById(Long transactionId) {
	return transactionRepository.findById(transactionId).orElse(null);
    }

    /**
     * Returns a particular purchase transaction related to it's Id with added data
     * based on a given currency.
     * 
     * @param id       purchase transaction's Id.
     * @param currency currency to convert the data from the purchase transaction
     *                 into. Can be {@code null}, in this case the method returns
     *                 the purchase with no added data.
     * @return a particular purchase transaction, converted or not.
     */
    public PurchaseTransaction getPurchaseTransaction(long id, String currency) {
	// If currency is not null, we return the transaction converted, otherwise, we
	// return the original one
	PurchaseTransaction transactionById = getTransactionById(id);
	if (transactionById == null) {
	    return null;
	}
	if (currency == null || "".equals(currency)) {
	    return transactionById;
	}

	// Search the rate with the date of the transaction
	Date transactionDate = transactionById.getTransactionDate();
	// If the transaction is olden than 6 months, we won't be able to convert it
	if (!isWithinMinDate(transactionDate)) {
	    return transactionById;
	}

	ExchangeRate exchangeRateForCurrency = getExchangeRateForCurrency(transactionById.getTransactionDate(),
		currency);
	// If there's no rate available, we simply return the original transaction
	if (exchangeRateForCurrency == null) {
	    return transactionById;
	}
	return convertPurchaseTransaction(transactionById, exchangeRateForCurrency);
    }

    private PurchaseTransaction convertPurchaseTransaction(PurchaseTransaction transaction, ExchangeRate bestRate) {
	// Calculate the converted amount
	double convertedAmount = transaction.getPurchaseAmount() * AppUtils.convertValue(bestRate);
	// Let's assure there's only 2 digits here...
	convertedAmount = AppUtils.roundToCents(convertedAmount);

	ConvertedCurrencyPurchaseTransaction convertedTransaction = new ConvertedCurrencyPurchaseTransaction(
		transaction);
	convertedTransaction.setTransaction_id(transaction.getTransactionId());
	convertedTransaction.setExchangeRate(bestRate.getExchange_rate());
	convertedTransaction.setConvertedPurchaseAmount(convertedAmount);
	return convertedTransaction;
    }

    private ExchangeRate getExchangeRateForCurrency(Date referenceDate, String currency) {
	List<ExchangeRate> ratesForThisCurrency = new ArrayList<ExchangeRate>(
		ExchangeRateDataCache.getInstance().getByCurrency(currency));
	// If there's no rate for the given currency, return null - the caller must deal
	// with it
	if (ratesForThisCurrency.isEmpty()) {
	    return null;
	}
	Collections.sort(ratesForThisCurrency, new Comparator<ExchangeRate>() {
	    @Override
	    public int compare(ExchangeRate o1, ExchangeRate o2) {
		try {
		    Date rateDateObj1 = AppUtils.parseCommonCurrencyDate(o1.getRecord_date());
		    Date rateDateObj2 = AppUtils.parseCommonCurrencyDate(o2.getRecord_date());
		    return rateDateObj2.compareTo(rateDateObj1); // This will make the order desc
		} catch (ParseException e) {
		    return 0;
		}
	    }
	});
	ExchangeRate bestRate = null;
	for (ExchangeRate rate : ratesForThisCurrency) {
	    try {
		Date rateDate = AppUtils.parseCommonCurrencyDate(rate.getRecord_date());
		// If it's the same date, we've found it!
		if (rateDate.equals(referenceDate)) {
		    bestRate = rate;
		    break;
		}
		// If it's above the purchase date, we ignore it
		if (rateDate.after(referenceDate)) {
		    continue;
		}
		bestRate = rate; // So, it's the first one just below the transaction rate
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}
	return bestRate;
    }

    private static boolean isWithinMinDate(Date exchangeDate) {
	return exchangeDate.after(AppUtils.getCurrencyDataMinDate());
    }

}
