package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.LoanSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanScheduleRepo extends CrudRepository<LoanSchedule, Integer> {
    @Query(value = "select * from loan_schedule u where u.loan_id = :loan_id order by u.schedule_no", nativeQuery = true)
    List<LoanSchedule> findAll(@Param("loan_id") Integer loanId);

    @Query(value = "select * from loan_schedule u where loan_id = :loan_id and loan_schedule_id in (select min(loan_schedule_id) from loan_schedule ls where loan_id = :loan_id and status in ('NOT_PAID','PARTIALLY_PAID'))", nativeQuery = true)
    LoanSchedule findMinimumUnPaidSchedule(@Param("loan_id") Integer loanId);

    @Query(value = "select * from loan_schedule u where u.loan_id = :loan_id order by u.schedule_no", nativeQuery = true)
    List<LoanSchedule> findPendingSchedules(@Param("loan_id") Integer loanId);
}