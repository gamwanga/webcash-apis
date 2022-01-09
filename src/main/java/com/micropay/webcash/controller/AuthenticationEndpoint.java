package com.micropay.webcash.controller;

import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.model.AuthRequest;
import com.micropay.webcash.model.PasswordChangeRequest;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.services.AuthenticationService;
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

@RestController
@RequestMapping("/api/v1/security/authentication")
@Api(tags = "Authentication services")
public class AuthenticationEndpoint {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/loginUser")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Manages user authentication in the system")
    public TxnResult authentication(@RequestBody Customer request) {
        try {
            return customerService.authentication(request);
        } catch (Exception e) {
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

    @PostMapping("/changePassword")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Manages user authentication in the system")
    public TxnResult changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            return service.changePassword(request);
        } catch (Exception e) {
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

    @PostMapping("/logoutUser")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @Operation(summary = "Manages user authentication in the system")
    public TxnResult logoutUser(@RequestBody AuthRequest request) {
        try {
            return service.logoutUser(request);
        } catch (Exception e) {
            logError(e);
            return CommonResponse.getUndefinedError();
        }
    }

}
