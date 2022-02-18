package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanSchedule;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.LoanAccountRepo;
import com.micropay.webcash.repositories.LoanScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanScheduleService {

    @Autowired
    private LoanScheduleRepo loanScheduleRepo;

    public TxnResult findAll(LoanSchedule request) {
        List<LoanSchedule> schedules = loanScheduleRepo.findAll(request.getLoanId());
        if (schedules == null || schedules.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        List<LoanSchedule> loanScheduleList = new ArrayList<>();
        for (LoanSchedule item : schedules){
            item.setTotalAmount(item.getPrincipalAmount() + item.getInterestAmount());
            item.setAmountUnPaid(item.getInterestUnpaid() + item.getPrincipalUnpaid());
            item.setAmountPaid(item.getInterestPaid() + item.getPrincipalPaid());
            loanScheduleList.add(item);
        }
        return TxnResult.builder().message("approved").
                code("00").data(loanScheduleList).build();
    }


    public TxnResult save(LoanSchedule request) {
        loanScheduleRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }

    public TxnResult update(LoanSchedule request) {
        loanScheduleRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }
}
