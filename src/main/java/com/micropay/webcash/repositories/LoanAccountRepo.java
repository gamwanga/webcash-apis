package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanAccountRepo extends CrudRepository<LoanAccount, Integer> {
    @Query(value = "select * from loan_account u where u.credit_appl_id in (select credit_app_id from public.mbl_credit_app where cust_id = :cust_id)", nativeQuery = true)
    List<LoanAccount> findAll(@Param("cust_id") Integer custId);

    @Query(value = "select * from loan_account u where u.status = :status", nativeQuery = true)
    List<LoanAccount> findLoansByStatus(@Param("status") String status);

    @Query(value = "select * from loan_account u where u.loan_number = :loan_number", nativeQuery = true)
    LoanAccount findLoanAccount(@Param("loan_number") String loanNumber);

    @Query(value = "select * from loan_account u order by u.loan_id desc", nativeQuery = true)
    List<LoanAccount> findAll();
}