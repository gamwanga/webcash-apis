package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanSchedule;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.LoanAccountRepo;
import com.micropay.webcash.repositories.LoanScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanScheduleService {

    @Autowired
    private LoanScheduleRepo loanScheduleRepo;

    public TxnResult findAll(LoanSchedule request) {
        List<LoanSchedule> charges = loanScheduleRepo.findAll(request.getLoanId());
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
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
