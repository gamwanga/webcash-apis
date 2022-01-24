package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.CreditApp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditAppRepo extends CrudRepository<CreditApp, Integer> {
    @Query(value = "select * from mbl_credit_app u where u.cust_id = :cust_id", nativeQuery = true)
    List<CreditApp> findAll(@Param("cust_id") Integer custId);

    @Query(value = "select * from mbl_credit_app u order by u.credit_app_id desc", nativeQuery = true)
    List<CreditApp> findAll();
}