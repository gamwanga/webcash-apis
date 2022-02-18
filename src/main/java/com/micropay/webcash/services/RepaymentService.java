package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanRepayment;
import com.micropay.webcash.entity.LoanSchedule;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.LoanAccountRepo;
import com.micropay.webcash.repositories.LoanRepaymentRepo;
import com.micropay.webcash.repositories.LoanScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RepaymentService {

    @Autowired
    private LoanRepaymentRepo loanRepaymentRepo;
    @Autowired
    private LoanScheduleRepo loanScheduleRepo;
    @Autowired
    private LoanAccountRepo loanAccountRepo;

    public TxnResult findAll(LoanRepayment request) {
        List<LoanRepayment> charges = loanRepaymentRepo.findAll(request.getAcctNo());
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }


    @Transactional
    public TxnResult save(LoanRepayment request) {
        LoanAccount loanAccount = loanAccountRepo.findLoanAccount(request.getAcctNo());
        if (loanAccount.getLoanId() == null)
            return TxnResult.builder().message("Invalid Loan account specified").
                    code("404").data(request).build();

        request.setReferenceNo(String.valueOf(System.currentTimeMillis()));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        request.setTransDate(timestamp);
        LoanRepayment response = loanRepaymentRepo.save(request);

        // Get Loan account schedules
        Double paymentAmount = request.getAmount().doubleValue();
        Double totalPayment = 0D;
        List<LoanSchedule> scheduleList = loanScheduleRepo.findPendingSchedules(loanAccount.getLoanId());
        for (LoanSchedule schedule : scheduleList) {
            totalPayment = schedule.getInterestAmount() + schedule.getPrincipalAmount();
            if (totalPayment <= paymentAmount) {
                schedule.setInterestPaid(schedule.getInterestAmount());
                schedule.setInterestUnpaid(0d);
                schedule.setPrincipalPaid(schedule.getPrincipalAmount());
                schedule.setPrincipalUnpaid(0d);
                schedule.setStatus("PAID");
                schedule.setPaymentDate(new Date());
                loanScheduleRepo.save(schedule);
                paymentAmount = paymentAmount - totalPayment;
                continue;
            }
            schedule.setInterestPaid(schedule.getInterestAmount());
            schedule.setInterestUnpaid(0d);
            schedule.setStatus("PARTIALLY_PAID");
            paymentAmount = paymentAmount - schedule.getInterestAmount();
            if (paymentAmount > 0) {
                schedule.setPrincipalPaid(paymentAmount);
                schedule.setPrincipalUnpaid(schedule.getPrincipalAmount() - paymentAmount);
            }
            schedule.setPaymentDate(new Date());
            loanScheduleRepo.save(schedule);
            break;
        }

        // Get the Next Schedule to be Serviced in Future
        LoanSchedule schedule = loanScheduleRepo.findMinimumUnPaidSchedule(loanAccount.getLoanId());
        loanAccount.setNextPmtAmount(schedule.getInterestUnpaid() + schedule.getPrincipalUnpaid());
        loanAccount.setLedgerBal(loanAccount.getLedgerBal() - request.getAmount());
        loanAccount.setNextPmtDate(schedule.getDueDate());
        loanAccountRepo.save(loanAccount);
        return TxnResult.builder().message("approved").code("00").data(request).build();
    }
}
