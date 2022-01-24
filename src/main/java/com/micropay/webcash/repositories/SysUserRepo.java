package com.micropay.webcash.repositories;

import com.micropay.webcash.entity.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SysUserRepo extends CrudRepository<SysUser, Integer> {

    @Query(value = "select u.* from sys_user u order by first_name", nativeQuery = true)
    List<SysUser> findUsers();

}