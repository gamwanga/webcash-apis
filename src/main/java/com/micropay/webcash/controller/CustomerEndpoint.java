package com.micropay.webcash.controller;


import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.services.CustomerService;
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
@RequestMapping("/api/v1/customer")
@Api(tags = "Customer Service")
public class CustomerEndpoint {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/findCustomerById")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Retrieves all customer addresses in the system")
    public TxnResult findCustById(@RequestBody Customer request) {
        try {
            logInfo(request);
            return customerService.findCustById(request);
        }catch (Exception e){
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

    @PostMapping("/signUp")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Creates a customer address in the system")
    public TxnResult signUp(@RequestBody Customer request) {
        try {
            logInfo(request);
            if (request.getEdit())
                return customerService.update(request);
            else
                return customerService.save(request);
        }catch (Exception e){
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }
}
