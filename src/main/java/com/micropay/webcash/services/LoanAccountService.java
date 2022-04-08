package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanRepaymentInfo;
import com.micropay.webcash.entity.LoanSchedule;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.LoanAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class LoanAccountService {

    @Autowired
    private LoanAccountRepo loanAccountRepo;

    public TxnResult findAll(LoanAccount request) {
        List<LoanAccount> loanAccountList;
        if(request.getStatus() != null)
            loanAccountList = loanAccountRepo.findLoansByStatus(request.getStatus());
        else if(request.getCustId() != null)
            loanAccountList = loanAccountRepo.findAll(request.getCustId());
        else
            loanAccountList = loanAccountRepo.findAll();
        if (loanAccountList == null || loanAccountList.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(loanAccountList).build();
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

    @Transactional
    public TxnResult disburseLoan(LoanAccount request) {
        request.setStatus("ACTIVE");
        loanAccountRepo.save(request);
            return TxnResult.builder().message("approved").
                    code("00").data(request).build();

    }
}
