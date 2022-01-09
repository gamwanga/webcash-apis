package com.micropay.webcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.webcash.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse extends User {
    private String processDate;
    private Integer employeeId;
    private String securityRoleDesc;
    private String branchDesc;
    private String employeeName;
    private String passwordExpiryFlag;
    private String passwordChanged;
    private String token;
    private Integer licenseDays;
    private String isPasswordExpired;
    private String failedLogins;
    private String loginLimits;
    private String minimumPasswordLength;
    private String isAdmin;
    private Integer userCurrencyId;
}
