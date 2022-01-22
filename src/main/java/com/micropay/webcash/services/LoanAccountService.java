package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.LoanAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanAccountService {

    @Autowired
    private LoanAccountRepo loanAccountRepo;

    public TxnResult findAll(LoanAccount request) {
        List<LoanAccount> charges = loanAccountRepo.findAll(request.getCustId());
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }


    public Integer save(LoanAccount request) {

        LoanAccount response = loanAccountRepo.save(request);
        return response.getLoanId();
    }

    public TxnResult update(LoanAccount request) {
        loanAccountRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }
}
