package com.micropay.webcash.repositories.security;

import com.micropay.webcash.entity.SysUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<SysUser, Integer> {

    @Query(value = "select u.* from sys_user u order by first_name", nativeQuery = true)
    List<SysUser> findUsers();

    @Query(value = "select u.* from sys_user u where u.user_name = :user_name order by first_name", nativeQuery = true)
    SysUser findUserByLoginId(
            @Param("user_name") String loginId
    );

    @Modifying
    @Query(value = "update sys_user set last_logon_date = :last_logon_date where sys_userid = :sys_userid", nativeQuery = true)
    void updateLoginActivities(
            @Param("last_logon_date") Timestamp timestamp,
            @Param("sys_userid") Integer userId
    );

    @Modifying
    @Query(value = "update sys_user set password_changed_flag = 'Y' where sys_userid = :sys_userid", nativeQuery = true)
    void updatePasswordChangeFlag(
            @Param("sys_userid") Integer userId
    );

 }
