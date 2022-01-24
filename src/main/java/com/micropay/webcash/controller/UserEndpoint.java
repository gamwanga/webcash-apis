package com.micropay.webcash.controller;

import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.entity.LoanRepayment;
import com.micropay.webcash.entity.SysUser;
import com.micropay.webcash.model.AuthRequest;
import com.micropay.webcash.model.PasswordChangeRequest;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.services.AuthenticationService;
import com.micropay.webcash.services.CustomerService;
import com.micropay.webcash.services.UserService;
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
@RequestMapping("/api/v1/user")
@Api(tags = "User services")
public class UserEndpoint {

    @Autowired
    private UserService service;

    @PostMapping("/findUsers")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Retrieves all customer addresses in the system")
    public TxnResult findUsers(@RequestBody SysUser request) {
        try {
            logInfo(request);
            return service.findAll(request);
        }catch (Exception e){
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

    @PostMapping("/maintainUser")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Manages user authentication in the system")
    public TxnResult maintainUser(@RequestBody SysUser request) {
        try {
            if (request.getId() != null)
                return service.update(request);
            else
                return service.update(request);
        } catch (Exception e) {
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }
}
