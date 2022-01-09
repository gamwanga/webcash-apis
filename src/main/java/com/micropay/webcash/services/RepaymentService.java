package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanRepayment;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.LoanRepaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RepaymentService {

    @Autowired
    private LoanRepaymentRepo loanRepaymentRepo;

    public TxnResult findAll(LoanRepayment request) {
        List<LoanRepayment> charges = loanRepaymentRepo.findAll(request.getAcctNo());
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }


    public TxnResult save(LoanRepayment request) {
        request.setReferenceNo(String.valueOf(System.currentTimeMillis()));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        request.setTransDate(timestamp);
        loanRepaymentRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }
}
