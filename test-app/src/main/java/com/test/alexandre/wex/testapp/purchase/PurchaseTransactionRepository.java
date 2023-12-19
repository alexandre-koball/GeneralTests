package com.test.alexandre.wex.testapp.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {
    // No custom query methods so far
}
