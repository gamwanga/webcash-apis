package com.micropay.webcash.services;

import com.micropay.webcash.config.SchemaConfig;
import com.micropay.webcash.entity.Password;
import com.micropay.webcash.entity.SysUser;
import com.micropay.webcash.model.*;
import com.micropay.webcash.repositories.security.AuthenticationRepoImpl;
import com.micropay.webcash.repositories.security.UserRepo;
import com.micropay.webcash.security.Encryptor;
import com.micropay.webcash.utils.DateUtils;
import com.micropay.webcash.utils.StringUtil;
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
    private EmailService emailService;

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

        String suppliedPassword = encryptor.encrypt(request.getPassword().trim());
        String savedPassword = data.getUserPwd();
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
        return TxnResult.builder().message("approved").
                code("00").data(response).build();
    }

    @Transactional
    public TxnResult changePassword(PasswordChangeRequest request) throws Exception {

        SysUser employee = repo.findUserByLoginId(request.getUserName());
        if (employee == null)
            return TxnResult.builder().message("Invalid username specified").
                    code("403").build();

        String oldPassword = encryptor.encrypt(request.getOldPassword().trim());
        String newPassword = encryptor.encrypt(request.getNewPassword().trim());
        String confirmPassword = encryptor.encrypt(request.getConfirmPassword().trim());
        String savedPassword = employee.getUserPwd().trim();
        if (!oldPassword.trim().equals(savedPassword))
            return TxnResult.builder().message("Invalid old password specified").
                    code("403").build();

        if (!confirmPassword.trim().equals(newPassword))
            return TxnResult.builder().message("Passwords do not match").
                    code("403").build();

        //pwdRepo.deactivateCurrentPassword(employee.get(0).getEmployeeId());
        Password password1 = new Password();
        password1.setCreateDate(new Timestamp(new Date().getTime()));
        password1.setEmployeeId(employee.getId());
        password1.setCreatedBy(request.getUserName());
        password1.setPasswordCycle(1);
        password1.setStatus("A");
        password1.setEncryptedPswd(newPassword);
        //pwdRepo.createPassword(password1);
        repo.updatePasswordChangeFlag(employee.getId(), password1.getEncryptedPswd(), "Y");
        return TxnResult.builder().message("approved").
                code("00").build();
    }

    @Transactional
    public TxnResult resetUserPassword(PasswordChangeRequest request) throws Exception {
        SysUser employee = repo.findUserByLoginEmailAddress(request.getEmailAddress());
        if (employee == null)
            return TxnResult.builder().message("No match was found for the specified email address").
                    code("-99").build();
        String newPassword = StringUtil.generateRandomString(6);

        String messageBody = "Dear " + employee.getFirstName().trim() + " " + employee.getLastName().trim()  + "\n";
        messageBody += "\n";
        messageBody += "Your user account password has been reset in Webcash system. Your new password is " + newPassword  + "\n";
        messageBody += "Please remember to change your password to proceed " + "\n";
        messageBody += "This is a system generated email, do not reply to this email id. " + "\n";
        messageBody += " " + "\n";
        messageBody += "Regards" + "\n";
        messageBody += "____________" + "\n";
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setMessageBody(messageBody);
        emailRequest.setEmailReceipient(request.getEmailAddress());
        emailRequest.setEmailSubject("Webcash User account");
        emailRequest.setEmailSender(schemaConfig.getEmailUserName());
        emailService.sendEmail(emailRequest);
        newPassword = encryptor.encrypt(newPassword);
        repo.updatePasswordChangeFlag(employee.getId(), newPassword, "N");
        return TxnResult.builder().message("New Password has been sent to the specified email address").
                code("00").build();
    }


}
