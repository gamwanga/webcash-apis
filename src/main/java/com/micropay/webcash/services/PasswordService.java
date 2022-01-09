package com.micropay.webcash.services;

import com.micropay.webcash.entity.Password;
import com.micropay.webcash.repositories.security.AuthenticationRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class PasswordService {
    @Autowired
    private AuthenticationRepoImpl repo;
    public void savePassword(Password request) throws SQLException {
        repo.createPassword(request);
    }
}
