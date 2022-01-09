package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanRepayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepaymentRepo extends CrudRepository<LoanRepayment, Integer> {

    @Query(value = "select * from loan_repayment u where u.acct_no = :acct_no", nativeQuery = true)
    List<LoanRepayment> findAll(@Param("acct_no") String custId);
}