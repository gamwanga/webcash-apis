package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanAccountRepo extends CrudRepository<LoanAccount, Integer> {
    @Query(value = "select * from loan_account u where u.cust_id = :cust_id", nativeQuery = true)
    List<LoanAccount> findAll(@Param("cust_id") Integer custId);
}