package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanAccount;
import com.micropay.webcash.entity.LoanRepaymentInfo;
import com.micropay.webcash.entity.LoanSchedule;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.LoanScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoanManagerService {

    @Autowired
    private LoanAccountService loanAccountService;
    @Autowired
    private LoanScheduleService loanScheduleService;
    @Autowired
    private LoanScheduleRepo loanScheduleRepo;
    @Autowired
    private CreditAppRepo creditAppRepo;

    Double totalInterest = 0D;
    Double totalPrincipal = 0D;
    Date maturityDate;

    private List<LoanSchedule> computeFlatAmountSchedule(CreditApp request, LoanAccount loanAccount, LoanRepaymentInfo loanRepaymentInfo) {
        List<LoanSchedule> loanSchedulesList = new ArrayList<>();
        int intCount;
        Double dblPrinciple;
        Double dblTempPrinciple = 0d;
        Double dblTotal;
        int intTotMonths;
        Double dblInterest;
        Double dblInterestRate;
        Double dblTotalAmount = 0D;
        String strInterval;
        int intpnPeriods = 0;
        Double dblnPayments;
        Double dblnPvRate = 0D;
        Date payDueDate = request.getStartDate();
        intTotMonths = request.getRepayTerm();
        strInterval = request.getRepayPeriod();
        dblTotal = request.getApplAmt().doubleValue();
        dblPrinciple = (dblTotal / intTotMonths);
        // USING SIMPLE INTEREST
        dblInterest = (dblPrinciple
                * (loanRepaymentInfo.getInterestRate() / 100));
        for (intCount = 0; (intCount
                <= (intTotMonths - 1)); intCount++) {
            int serial = (intCount + 1);
            dblInterest = dblInterest;
            dblPrinciple = dblPrinciple;
            dblTempPrinciple = (dblTempPrinciple - dblPrinciple);
            dblTotalAmount = dblPrinciple + dblInterest;

            loanSchedulesList.add(new LoanSchedule(
                    serial,
                    loanAccount.getLoanId(),
                    dblPrinciple,
                    dblInterest,
                    0D,
                    0D,
                    dblPrinciple,
                    dblInterest,
                    payDueDate,
                    null,
                    "NOT_PAID",
                    loanAccount.getCreatedBy(),
                    loanAccount.getCreateDate(),
                    0));

            maturityDate = payDueDate;
            Calendar cal = Calendar.getInstance();
            cal.setTime(payDueDate);
            switch (strInterval) {
                case "DAY":
                    cal.add(Calendar.DATE, 1);
                    payDueDate = cal.getTime();
                    break;
                case "WEEK":
                    cal.add(Calendar.DATE, 7);
                    payDueDate = cal.getTime();
                    break;
                case "MONTH":
                    cal.add(Calendar.MONTH, 1);
                    payDueDate = cal.getTime();
                    break;
                case "Quarter":
                    cal.add(Calendar.MONTH, 3);
                    payDueDate = cal.getTime();
                    break;
                case "Half-Year":
                    cal.add(Calendar.MONTH, 6);
                    payDueDate = cal.getTime();
                    break;
                case "Year":
                    cal.add(Calendar.MONTH, 12);
                    payDueDate = cal.getTime();
                    break;
            }
        }
        // Get the total Interest to be paid.
        totalInterest = 0D;
        totalPrincipal = 0D;
        for (LoanSchedule lsr : loanSchedulesList) {
            totalInterest = totalInterest + lsr.getInterestAmount();
            totalPrincipal = totalPrincipal + lsr.getPrincipalAmount();
        }
        return loanSchedulesList;
    }


    private List<LoanSchedule> computeReducingBalanceSchedule(CreditApp request, LoanAccount loanAccount, LoanRepaymentInfo loanRepaymentInfo) {
        List<LoanSchedule> loanSchedulesList = new ArrayList<>();
        int intCount;
        Double dblPrinciple;
        Double dblTempPrinciple = 0d;
        Double totalLoanAmount;
        int intTotMonths;
        Double dblInterest;
        Double dblInterestRate;
        Double dblTotalAmount = 0D;
        String strInterval;
        int intpnPeriods = 0;
        Double dblnPayments;
        Double dblnPvRate = 0D;
        Date payDueDate = request.getStartDate();

        intTotMonths = request.getRepayTerm();
        strInterval = request.getRepayPeriod();
        totalLoanAmount = request.getApplAmt().doubleValue();
        dblPrinciple = (totalLoanAmount / intTotMonths);
        if ((loanRepaymentInfo.getRepaymentType().equals("EQUAL_TOTAL_PAYMENT"))) // Using  equal total payments
        {
            dblInterestRate = (loanRepaymentInfo.getInterestRate() / 100);
            dblTempPrinciple = totalLoanAmount;
            switch (strInterval) {
                case "DAY":
                    intpnPeriods = 365;
                    break;
                case "WEEK":
                    intpnPeriods = 52;
                    break;
                case "MONTH":
                    intpnPeriods = 12;
                    break;
                case "Quarter":
                    intpnPeriods = 4;
                    break;
                case "Half-Year":
                    intpnPeriods = 2;
                    break;
                case "Year":
                    intpnPeriods = 1;
                    break;
            }
            intCount = 1;
            dblnPvRate = 0D;
            while ((intCount <= intTotMonths)) {
                dblnPvRate = (dblnPvRate + ((1 / (1 + (dblInterestRate / intpnPeriods)))));
                intCount = (intCount + 1);
            }
            dblnPayments = (dblTempPrinciple / dblnPvRate);
            for (intCount = 0; (intCount
                    <= (intTotMonths - 1)); intCount++) {
                int serial = (intCount + 1);
                // calculate interest part of Month(s) repayment
                dblInterest = (dblTempPrinciple * (dblInterestRate / intpnPeriods));
                // calculate principal part of Month(s) repayment
                dblPrinciple = (dblnPayments - dblInterest);
                dblTempPrinciple = (dblTempPrinciple - dblPrinciple);
                dblTotalAmount = (dblPrinciple + dblInterest);
                loanSchedulesList.add(new LoanSchedule(
                        serial,
                        loanAccount.getLoanId(),
                        dblPrinciple,
                        dblInterest,
                        0D,
                        0D,
                        dblPrinciple,
                        dblInterest,
                        payDueDate,
                        null,
                        "NOT_PAID",
                        loanAccount.getCreatedBy(),
                        loanAccount.getCreateDate(),
                        0)
                );
                maturityDate = payDueDate;

                Calendar cal = Calendar.getInstance();
                cal.setTime(payDueDate);
                switch (strInterval) {
                    case "DAY":
                        cal.add(Calendar.DATE, 1);
                        payDueDate = cal.getTime();
                        break;
                    case "WEEK":
                        cal.add(Calendar.DATE, 7);
                        payDueDate = cal.getTime();
                        break;
                    case "MONTH":
                        cal.add(Calendar.MONTH, 1);
                        payDueDate = cal.getTime();
                        break;
                    case "Quarter":
                        cal.add(Calendar.MONTH, 3);
                        payDueDate = cal.getTime();
                        break;
                    case "Half-Year":
                        cal.add(Calendar.MONTH, 6);
                        payDueDate = cal.getTime();
                        break;
                    case "Year":
                        cal.add(Calendar.MONTH, 12);
                        payDueDate = cal.getTime();
                        break;
                }
            }
        } else {
            for (intCount = 0; (intCount
                    <= (intTotMonths - 1)); intCount++) {
                dblInterest = (totalLoanAmount * ((loanRepaymentInfo.getInterestRate() / 100)));
                int serial = (intCount + 1);
                dblTempPrinciple = (dblTempPrinciple - dblPrinciple);
                dblTotalAmount = (dblPrinciple + dblInterest);
                loanSchedulesList.add(new LoanSchedule(
                        serial,
                        loanAccount.getLoanId(),
                        dblPrinciple,
                        dblInterest,
                        0D,
                        0D,
                        dblPrinciple,
                        dblInterest,
                        payDueDate,
                        null,
                        "NOT_PAID",
                        loanAccount.getCreatedBy(),
                        loanAccount.getCreateDate(),
                        0));
                maturityDate = payDueDate;
                Calendar cal = Calendar.getInstance();
                cal.setTime(payDueDate);
                switch (strInterval) {
                    case "DAY":
                        cal.add(Calendar.DATE, 1);
                        payDueDate = cal.getTime();
                        break;
                    case "WEEK":
                        cal.add(Calendar.DATE, 7);
                        payDueDate = cal.getTime();
                        break;
                    case "MONTH":
                        cal.add(Calendar.MONTH, 1);
                        payDueDate = cal.getTime();
                        break;
                    case "Quarter":
                        cal.add(Calendar.MONTH, 3);
                        payDueDate = cal.getTime();
                        break;
                    case "Half-Year":
                        cal.add(Calendar.MONTH, 6);
                        payDueDate = cal.getTime();
                        break;
                    case "Year":
                        cal.add(Calendar.MONTH, 12);
                        payDueDate = cal.getTime();
                        break;
                }
                totalLoanAmount = (totalLoanAmount - dblPrinciple);
            }
        }

        // Get the total Interest to be paid.
        totalInterest = 0D;
        totalPrincipal = 0D;
        for (LoanSchedule lsr : loanSchedulesList) {
            totalInterest = totalInterest + lsr.getInterestAmount();
            totalPrincipal = totalPrincipal + lsr.getPrincipalAmount();
        }
        return loanSchedulesList;
    }

    @Transactional
    public TxnResult approveLoan(CreditApp request) {
        creditAppRepo.save(request);
        if (!request.getStatus().equals("ACTIVE"))
            return TxnResult.builder().message("approved").
                    code("00").data(request).build();

        DateFormat dateFormat = new SimpleDateFormat("yymmddhhmmss");
        String loanNumber = dateFormat.format(Calendar.getInstance().getTime());
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setCreditApplId(request.getId());
        loanAccount.setApprovedAmount(request.getApplAmt());
        loanAccount.setLedgerBal(request.getApplAmt());
        loanAccount.setStartDate(request.getStartDate());
        loanAccount.setEndDate(request.getEndDate());
        loanAccount.setLoanNumber(loanNumber);
        loanAccount.setStatus("PENDING");
        loanAccount.setRepayPeriod(request.getRepayPeriod());
        loanAccount.setTerm(request.getRepayTerm());
        loanAccount.setCreatedBy(request.getCreatedBy());
        loanAccount.setCreateDate(request.getCreateDate());
        loanAccount.setRowVersion(0);
        Integer loanId = loanAccountService.save(loanAccount);
        loanAccount.setLoanId(loanId);

        // Get the Loan Repayment Information
        LoanRepaymentInfo loanRepaymentInfo = new LoanRepaymentInfo();
        loanRepaymentInfo.setRepaymentType("REDUCING_BALANCE");
        loanRepaymentInfo.setIntCalcOption("REDUCING_BALANCE");
        loanRepaymentInfo.setInterestRate(8D);
        List<LoanSchedule> loanScheduleList = null;
        if (loanRepaymentInfo.getIntCalcOption().equals("REDUCING_BALANCE"))
            loanScheduleList = computeReducingBalanceSchedule(request, loanAccount, loanRepaymentInfo);
        else if (loanRepaymentInfo.getIntCalcOption().equals("FLAT_AMOUNT")) {
            loanScheduleList = computeFlatAmountSchedule(request, loanAccount, loanRepaymentInfo);
        }


        if (loanScheduleList.isEmpty() || loanScheduleList == null)
            return TxnResult.builder().message("System failed to generate schedules").
                    code("-99").data(request).build();
        // Save the Loan Repayment Schedules
        for (LoanSchedule schedule : loanScheduleList) {
            loanScheduleRepo.save(schedule);
        }
        // Update the loan account information
        loanAccount.setRepayInterest(BigDecimal.valueOf(totalInterest));
        loanAccount.setRepayPrincipal(BigDecimal.valueOf(totalPrincipal));
        loanAccount.setEndDate(maturityDate);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }




}
