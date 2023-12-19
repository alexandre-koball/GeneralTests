package com.test.alexandre.wex.testapp.purchase;

/**
 * A {@code PurchaseTransaction} with a few added conversion rate fields.
 */
public class ConvertedCurrencyPurchaseTransaction extends PurchaseTransaction {

    /**
     * Serializable Id.
     */
    private static final long serialVersionUID = -8486333796113125581L;
    private String exchangeRate;
    private Double convertedPurchaseAmount;

    public ConvertedCurrencyPurchaseTransaction() {
	// Default constructor
    }

    public ConvertedCurrencyPurchaseTransaction(PurchaseTransaction basePurchaseTransaction) {
	super(basePurchaseTransaction.getDescription(), basePurchaseTransaction.getTransactionDate(),
		basePurchaseTransaction.getPurchaseAmount());
    }

    public ConvertedCurrencyPurchaseTransaction(String exchangeRate, Double convertedPurchaseAmount) {
	super();
	this.exchangeRate = exchangeRate;
	this.convertedPurchaseAmount = convertedPurchaseAmount;
    }

    public String getExchangeRate() {
	return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
	this.exchangeRate = exchangeRate;
    }

    public Double getConvertedPurchaseAmount() {
	return convertedPurchaseAmount;
    }

    public void setConvertedPurchaseAmount(Double convertedPurchaseAmount) {
	this.convertedPurchaseAmount = convertedPurchaseAmount;
    }

}
