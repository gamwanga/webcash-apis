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
        Double totalPrincipal = 0D;
        Double totalRepayment = 0D;
        List<LoanSchedule> scheduleList = loanScheduleRepo.findPendingSchedules(loanAccount.getLoanId());

        for(LoanSchedule schedule: scheduleList){
            totalRepayment += schedule.getInterestUnpaid()+ schedule.getPrincipalUnpaid();
        }
        if (totalRepayment < paymentAmount) {
            return TxnResult.builder().message("Specified Payment amount [" + String.format("%.2f", paymentAmount) +  "] is greater than the expected repayment amount [" + String.format("%.2f", totalRepayment) +  "].").code("99").data(request).build();
        }

        for (LoanSchedule schedule : scheduleList) {
            totalPayment = schedule.getInterestUnpaid() + schedule.getPrincipalUnpaid();
            if (totalPayment <= paymentAmount) {
                schedule.setInterestPaid(schedule.getInterestAmount());
                schedule.setInterestUnpaid(0d);
                schedule.setPrincipalPaid(schedule.getPrincipalAmount());
                schedule.setPrincipalUnpaid(0d);
                schedule.setStatus("PAID");
                schedule.setPaymentDate(new Date());
                loanScheduleRepo.save(schedule);
                totalPrincipal += schedule.getPrincipalAmount();
                paymentAmount = paymentAmount - totalPayment;
                continue;
            }

            if (paymentAmount > schedule.getInterestUnpaid()) {
                paymentAmount = paymentAmount - schedule.getInterestUnpaid();
                schedule.setInterestPaid(schedule.getInterestUnpaid());
                schedule.setInterestUnpaid(0d);
            }else{
                schedule.setInterestPaid(paymentAmount);
                schedule.setInterestUnpaid(schedule.getInterestUnpaid() - paymentAmount);
                paymentAmount = 0D;
            }

            if (paymentAmount > 0) {
                totalPrincipal += paymentAmount;
                schedule.setPrincipalPaid(paymentAmount + schedule.getPrincipalPaid());
                schedule.setPrincipalUnpaid(schedule.getPrincipalUnpaid() - paymentAmount);
            }
            schedule.setStatus("PARTIALLY_PAID");
            schedule.setPaymentDate(new Date());
            loanScheduleRepo.save(schedule);
            break;
        }

        // Get the Next Schedule to be Serviced in Future
        LoanSchedule schedule = loanScheduleRepo.findMinimumUnPaidSchedule(loanAccount.getLoanId());
        if (schedule != null) {
            loanAccount.setNextPmtAmount(schedule.getInterestUnpaid() + schedule.getPrincipalUnpaid());
            loanAccount.setLedgerBal(loanAccount.getLedgerBal() - totalPrincipal);
            loanAccount.setNextPmtDate(schedule.getDueDate());
        }else{
            loanAccount.setNextPmtAmount(0D);
            loanAccount.setLedgerBal(0D);
            loanAccount.setNextPmtDate(null);
            loanAccount.setStatus("CLOSED");
        }
        loanAccountRepo.save(loanAccount);
        return TxnResult.builder().message("approved").code("00").data(request).build();
    }
}
