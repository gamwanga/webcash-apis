package com.micropay.webcash.services;

import com.micropay.webcash.config.SchemaConfig;
import com.micropay.webcash.entity.Password;
import com.micropay.webcash.entity.User;
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
import java.util.List;

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
        List<User> data = repo.findUserByLoginId(request.getUsername());
        if (data.isEmpty()) {
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }

        request.setStatus("success");
        request.setEmployeeId(data.get(0).getId());
        pwdRepo.logOutUser(request);
        return TxnResult.builder().message("approved").
                code("00").build();
    }


    @Transactional
    public TxnResult authentication(AuthRequest request) throws Exception {

        List<User> data = repo.findUserByLoginId(request.getUsername());
        if (data.isEmpty()) {
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }

        Password password = pwdRepo.findActivePasswordByEmployeeId(data.get(0).getId());
        if (password.getEncryptedPswd() == null) {
            request.setStatus("failed");
            request.setEmployeeId(data.get(0).getId());
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
        response.setEmployeeId(data.get(0).getId());
        response.setGender(data.get(0).getGender());
        response.setAllowMultipleSessions(data.get(0).getAllowMultipleSessions());
        response.setStartDate(data.get(0).getStartDate());
        response.setEndDate(data.get(0).getEndDate());
        response.setFailedLoginAttempts(data.get(0).getFailedLoginAttempts());
        response.setBranchId(data.get(0).getBranchId());
        response.setLoginId(data.get(0).getLoginId());
        response.setEmployeeNumber(data.get(0).getEmployeeNumber());
        response.setCustomerNo(data.get(0).getCustomerNo());
        response.setFirstName(data.get(0).getFirstName());
        response.setMiddleName(data.get(0).getMiddleName());
        response.setLastName(data.get(0).getLastName());
        response.setEmailAddress(data.get(0).getEmailAddress());
        response.setSecurityRoleId(data.get(0).getSecurityRoleId());
        response.setStatus(data.get(0).getStatus());

        response.setProcessDate(DateUtils.toString(DateUtils.getCurrentDate()));
        response.setSecurityRoleDesc(data.get(0).getFirstName());
        response.setBranchDesc(data.get(0).getFirstName());
        response.setEmployeeName(data.get(0).getFirstName());
        response.setPasswordChangedFlag(data.get(0).getPasswordChangedFlag());
        response.setPasswordExpiryFlag(data.get(0).getFirstName());
        response.setPasswordChanged(data.get(0).getFirstName());
        response.setToken(data.get(0).getFirstName());
        response.setLicenseDays(0);
        response.setFailedLogins(data.get(0).getFirstName());
        response.setMinimumPasswordLength(data.get(0).getFirstName());
        response.setAllowBranchChange(data.get(0).getAllowBranchChange());
        response.setLastLogonDate(data.get(0).getLastLogonDate());
        response.setUserCurrencyId(0);
        repo.updateLoginActivities(new Timestamp(new Date().getTime()), data.get(0).getId());

        request.setStatus("success");
        request.setEmployeeId(data.get(0).getId());
        String sessionToken = pwdRepo.logAccessTrail(request);
        response.setToken(sessionToken);
        return TxnResult.builder().message("approved").
                code("00").data(response).build();
    }

    @Transactional
    public TxnResult changePassword(PasswordChangeRequest request) throws Exception {

        List<User> employee = repo.findUserByLoginId(request.getUserName());
        if (employee.isEmpty())
            return TxnResult.builder().message("Invalid username specified").
                    code("403").build();

        Password password = pwdRepo.findActivePasswordByEmployeeId(employee.get(0).getId());
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
                pwdRepo.deactivateCurrentPassword(employee.get(0).getId());
                pwdRepo.reactivateOlPassword(employee.get(0).getId(), newPassword);
                return TxnResult.builder().message("approved").
                        code("00").build();
            }
        }

        pwdRepo.deactivateCurrentPassword(employee.get(0).getId());
        Password password1 = new Password();
        password1.setCreateDate(new Timestamp(new Date().getTime()));
        password1.setEmployeeId(employee.get(0).getId());
        password1.setCreatedBy(request.getUserName());
        password1.setPasswordCycle(1);
        password1.setStatus("A");
        password1.setEncryptedPswd(newPassword);
        pwdRepo.createPassword(password1);
        repo.updatePasswordChangeFlag(employee.get(0).getId());
        return TxnResult.builder().message("approved").
                code("00").build();
    }


}
