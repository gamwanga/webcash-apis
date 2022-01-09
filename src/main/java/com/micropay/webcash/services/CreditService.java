package com.micropay.webcash.services;

import com.micropay.webcash.entity.CreditApp;
import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.model.TxnResult;
import com.micropay.webcash.repositories.CreditAppRepo;
import com.micropay.webcash.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreditService {

    @Autowired
    private CreditAppRepo customerRepo;

    public TxnResult findAll(CreditApp request) {
        List<CreditApp> charges = customerRepo.findAll(request.getCustId());
        if (charges == null || charges.isEmpty())
            return TxnResult.builder().code("404")
                    .message("No records found")
                    .build();
        return TxnResult.builder().message("approved").
                code("00").data(charges).build();
    }


    public TxnResult save(CreditApp request) {
        customerRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }

    public TxnResult update(CreditApp request) {
        customerRepo.save(request);
        return TxnResult.builder().message("approved").
                code("00").data(request).build();
    }
}
