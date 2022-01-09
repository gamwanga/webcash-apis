package com.micropay.webcash.repositories.security;


import com.micropay.webcash.entity.Password;
import com.micropay.webcash.model.AuthRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepo {
    void createPassword(Password request);
    String logAccessTrail(AuthRequest request);
    void logOutUser(AuthRequest request);
    Password findActivePasswordByEmployeeId(Integer employeeId);
    void deactivateCurrentPassword(Integer employeeId);
    void reactivateOlPassword(Integer employeeId, String encryptedPassword);
    Password findPasswordByEncryptedPassword(String encryptedPassword);
}
