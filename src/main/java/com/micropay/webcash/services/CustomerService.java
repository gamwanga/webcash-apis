package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.entity.Password;
import com.micropay.webcash.entity.User;
import com.micropay.webcash.model.AuthRequest;
import com.micropay.webcash.model.AuthResponse;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CustomerRepo;
import com.micropay.webcash.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public TxnResult findAll(Customer request) {
        List<Customer> charges = customerRepo.findAll();
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }


    @Transactional
    public TxnResult authentication(Customer request) throws Exception {

        Customer data = customerRepo.findCustomerByPhoneAndPin(request.getPhoneNo(), request.getPassword());
        if (data == null) {
            return TxnResult.builder().message("Unauthorized").
                    code("403").build();
        }

        return TxnResult.builder().message("approved").
                code("00").data(data).build();
    }

    public TxnResult findCustById(Customer request) {
        Customer charges = customerRepo.findCustById(request.getId());
        if (charges == null)
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }

    public TxnResult save(Customer request) {
        Customer data = customerRepo.findCustomerByPhone(request.getPhoneNo());
        if (data != null){
            return TxnResult.builder().message("Phone number is already registered for Webcash").
                    code("403").build();
        }
        customerRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }

    public TxnResult update(Customer request) {
        customerRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }
}
