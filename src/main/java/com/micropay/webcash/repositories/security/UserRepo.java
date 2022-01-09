package com.micropay.webcash.repositories.security;

import com.micropay.webcash.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {

    @Query(value = "select u.* from user_ref u order by first_name", nativeQuery = true)
    List<User> findUsers();

    @Query(value = "select u.* from user_ref u where u.login_id = :login_id order by first_name", nativeQuery = true)
    List<User> findUserByLoginId(
            @Param("login_id") String loginId
    );

    @Modifying
    @Query(value = "update user_ref set last_logon_date = :last_logon_date where user_id = :user_id", nativeQuery = true)
    void updateLoginActivities(
            @Param("last_logon_date") Timestamp timestamp,
            @Param("user_id") Integer userId
    );

    @Modifying
    @Query(value = "update user_ref set password_changed_flag = 'Y' where user_id = :user_id", nativeQuery = true)
    void updatePasswordChangeFlag(
            @Param("user_id") Integer userId
    );

 }
