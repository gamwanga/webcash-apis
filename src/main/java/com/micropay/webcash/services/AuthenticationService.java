package com.micropay.webcash.services;

import com.micropay.webcash.config.SchemaConfig;
import com.micropay.webcash.entity.Password;
import com.micropay.webcash.entity.SysUser;
import com.micropay.webcash.model.AuthRequest;
import com.micropay.webcash.model.AuthResponse;
import com.micropay.webcash.model.PasswordChangeRequest;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.security.AuthenticationRepoImpl;
import com.micropay.webcash.repositories.security.UserRepo;
import com.micropay.webcash.security.Encryptor;
import com.micropay.webcash.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private SchemaConfig schemaConfig;

    @Autowired
    private AuthenticationRepoImpl pwdRepo;

    @Autowired
    Encryptor encryptor;

    @Transactional
    public TxnResult logoutUser(AuthRequest request) {
        SysUser data = repo.findUserByLoginId(request.getUsername());
        if (data != null) {
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }

        request.setStatus("success");
        request.setEmployeeId(data.getId());
        pwdRepo.logOutUser(request);
        return TxnResult.builder().message("approved").
                code("00").build();
    }


    @Transactional
    public TxnResult authentication(AuthRequest request) throws Exception {
        SysUser data = repo.findUserByLoginId(request.getUsername());
        if (data == null) {
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }

        Password password = pwdRepo.findActivePasswordByEmployeeId(data.getId());
        if (password.getEncryptedPswd() == null) {
            request.setStatus("failed");
            request.setEmployeeId(data.getId());
            pwdRepo.logAccessTrail(request);
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }
        String suppliedPassword = encryptor.encrypt(request.getPassword().trim());
        String savedPassword = password.getEncryptedPswd().trim();
        if (!suppliedPassword.trim().equals(savedPassword))
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        AuthResponse response = new AuthResponse();
        response.setEmployeeId(data.getId());
        response.setGender(data.getGender());
        response.setStartDate(data.getStartDate());
        response.setEndDate(data.getEndDate());
        response.setFailedLoginAttempt(data.getFailedLoginAttempt());
        response.setBranchId(data.getBranchId());
        response.setUserName(data.getUserName());
        response.setEmployeeNumber(data.getEmployeeNumber());
        response.setFirstName(data.getFirstName());
        response.setMiddleName(data.getMiddleName());
        response.setLastName(data.getLastName());
        response.setEmailAddress(data.getEmailAddress());
        response.setSecurityRoleId(data.getSecurityRoleId());
        response.setStatus(data.getStatus());
        response.setProcessDate(DateUtils.toString(DateUtils.getCurrentDate()));
        response.setSecurityRoleDesc(data.getFirstName());
        response.setBranchDesc(data.getFirstName());
        response.setEmployeeName(data.getFirstName());
        response.setPasswordChangeFlag(data.getPasswordChangeFlag());
        response.setPasswordExpiryFlag(data.getFirstName());
        response.setPasswordChanged(data.getFirstName());
        response.setToken(data.getFirstName());
        response.setLicenseDays(0);
        response.setFailedLogins(data.getFirstName());
        response.setMinimumPasswordLength(data.getFirstName());
        response.setLastLogonDate(data.getLastLogonDate());
        response.setUserCurrencyId(0);
        repo.updateLoginActivities(new Timestamp(new Date().getTime()), data.getId());

        request.setStatus("success");
        request.setEmployeeId(data.getId());
        String sessionToken = pwdRepo.logAccessTrail(request);
        response.setToken(sessionToken);
        return TxnResult.builder().message("approved").
                code("00").data(response).build();
    }


    @Transactional
    public TxnResult changePassword(PasswordChangeRequest request) throws Exception {

        SysUser employee = repo.findUserByLoginId(request.getUserName());
        if (employee == null)
            return TxnResult.builder().message("Invalid username specified").
                    code("403").build();

        Password password = pwdRepo.findActivePasswordByEmployeeId(employee.getId());
        if (password == null || password.getEncryptedPswd() == null)
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();

        String oldPassword = encryptor.encrypt(request.getOldPassword().trim());
        String newPassword = encryptor.encrypt(request.getNewPassword().trim());
        String confirmPassword = encryptor.encrypt(request.getConfirmPassword().trim());
        String savedPassword = password.getEncryptedPswd().trim();
        if (!oldPassword.trim().equals(savedPassword))
            return TxnResult.builder().message("Invalid old password specified").
                    code("403").build();

        if (!confirmPassword.trim().equals(newPassword))
            return TxnResult.builder().message("Passwords do not match").
                    code("403").build();

        Password pwdHistoryData = pwdRepo.findPasswordByEncryptedPassword(newPassword);
        if (pwdHistoryData.getEncryptedPswd() != null) {
            if (pwdHistoryData.getPasswordCycle() <= 3)
                return TxnResult.builder().message("You cannot reuse a password you have used recently.").
                        code("-99").build();
            else {
                pwdRepo.deactivateCurrentPassword(employee.getId());
                pwdRepo.reactivateOlPassword(employee.getId(), newPassword);
                return TxnResult.builder().message("approved").
                        code("00").build();
            }
        }

        pwdRepo.deactivateCurrentPassword(employee.getId());
        Password password1 = new Password();
        password1.setCreateDate(new Timestamp(new Date().getTime()));
        password1.setEmployeeId(employee.getId());
        password1.setCreatedBy(request.getUserName());
        password1.setPasswordCycle(1);
        password1.setStatus("A");
        password1.setEncryptedPswd(newPassword);
        pwdRepo.createPassword(password1);
        repo.updatePasswordChangeFlag(employee.getId());
        return TxnResult.builder().message("approved").
                code("00").build();
    }


}
