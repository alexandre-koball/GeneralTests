package com.test.alexandre.wex.testapp.purchase;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * An object that represents a purchase transaction.
 */
@Entity
@Table(name = "PurchaseTransaction")
public class PurchaseTransaction implements Serializable {

    /**
     * Serializable Id.
     */
    private static final long serialVersionUID = -6277658531462343595L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "transaction_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @Column(name = "purchase_amount", precision = 10, nullable = false)
    private Double purchaseAmount;

    public PurchaseTransaction() {
	// Default constructor
    }

    public PurchaseTransaction(String description, Date transactionDate, Double purchaseAmount) {
	this.description = description;
	this.transactionDate = transactionDate;
	this.purchaseAmount = purchaseAmount;
    }

    public Long getTransactionId() {
	return transactionId;
    }

    public void setTransaction_id(Long transactionId) {
	this.transactionId = transactionId;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Date getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public Double getPurchaseAmount() {
	return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
	this.purchaseAmount = purchaseAmount;
    }

}