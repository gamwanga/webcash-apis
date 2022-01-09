package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.Customer;
import com.micropay.webcash.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepo extends CrudRepository<Customer, Integer> {
    @Query(value = "select * from mbl_customer u", nativeQuery = true)
    List<Customer> findAll();

    @Query(value = "select * from mbl_customer u where u.cust_id = :cust_id", nativeQuery = true)
    Customer findCustById(@Param("cust_id") Integer custId);

    @Query(value = "select u.* from mbl_customer u where u.phone_no = :phone_no and u.password = :password order by first_name", nativeQuery = true)
    Customer findCustomerByPhoneAndPin(
            @Param("phone_no") String phoneNo, @Param("password") String pinNumber
    );

    @Query(value = "select u.* from mbl_customer u where u.phone_no = :phone_no order by first_name", nativeQuery = true)
    Customer  findCustomerByPhone(
            @Param("phone_no") String phoneNo);


}