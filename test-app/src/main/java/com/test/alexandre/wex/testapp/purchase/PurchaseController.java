package com.test.alexandre.wex.testapp.purchase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Holds the API responsible for saving and returning data releate to purchases.
 */
@RestController
@RequestMapping("/purchase")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private PurchaseTransactionService service;

    @PostMapping("/add")
    public ResponseEntity<Object> addPurchaseTransaction(@RequestBody PurchaseTransaction purchaseTransaction) {
	try {
	    PurchaseTransaction transaction = service.saveTransaction(purchaseTransaction);
	    return ResponseEntity.status(HttpStatus.CREATED).body(String.format("New purchase transaction with Id %d added.", transaction.getTransactionId()));
	} catch (Exception e) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Error on adding new purchase transaction. %s", e.getMessage()));
	}
    }

    @GetMapping("/get")
    public PurchaseTransaction getPurchaseTransaction(@RequestParam long id,
	    @RequestParam(required = false) String currency) {
	PurchaseTransaction purchaseTransaction = service.getPurchaseTransaction(id, currency);
	// If the transaction is null, the response depends on the currency param
	if (purchaseTransaction == null) {
	    String response = "";
	    if (currency == null || "".equals(currency)) {
		response = String.format("No available purchase data for ID %d.", id);
	    } else if (currency != null) {
		response = String.format("No available currency data for %s or no available purchase data for ID %d.",
			currency, id);
	    }
	    throw new ResponseStatusException(HttpStatusCode.valueOf(404), response);
	}
	return purchaseTransaction;
    }

    @GetMapping("/get-all")
    public List<PurchaseTransaction> getAllPurchaseTransactions() {
	return service.getAllTransactions();
    }

    @GetMapping("/get-all-converted")
    public List<PurchaseTransaction> getAllPurchaseTransactionsConverted(
	    @RequestParam(required = false) String currency) {
	return service.getAllTransactionsConverted(currency);
    }

}
