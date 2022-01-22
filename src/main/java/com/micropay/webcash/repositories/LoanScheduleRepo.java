package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanScheduleRepo extends JpaRepository<LoanSchedule, Integer> {
    @Query(value = "select * from loan_schedule u where u.cust_id = :cust_id", nativeQuery = true)
    List<LoanSchedule> findAll(@Param("cust_id") Integer custId);
}