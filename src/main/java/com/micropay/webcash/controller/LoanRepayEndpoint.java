package com.micropay.webcash.controller;


import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.LoanRepayment;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.services.CreditService;
import com.micropay.webcash.services.RepaymentService;
import com.micropay.webcash.utils.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.micropay.webcash.utils.Logger.logError;
import static com.micropay.webcash.utils.Logger.logInfo;

@RestController
@RequestMapping("/api/v1/transaction")
@Api(tags = "Transaction Service")
public class LoanRepayEndpoint {
    @Autowired
    private RepaymentService customerService;
    @PostMapping("/findTransHistory")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Retrieves all customer addresses in the system")
    public TxnResult findTransHistory(@RequestBody LoanRepayment request) {
        try {
            logInfo(request);
            return customerService.findAll(request);
        }catch (Exception e){
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

    @PostMapping("/postLoanRepayment")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Creates a customer address in the system")
    public TxnResult postLoanRepayment(@RequestBody LoanRepayment request) {
        try {
            logInfo(request);
            return customerService.save(request);
        }catch (Exception e){
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }
}
