package com.micropay.webcash.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "sys_user")
@Entity
@Data
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sys_userid", nullable = false)
    private Integer id;

    @Column(name = "email_address", length = 50)
    private String emailAddress;

    @Column(name = "employee_number", length = 30)
    private String employeeNumber;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "security_role_id")
    private Integer securityRoleId;

    @Column(name = "start_dt")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_dt")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @Column(name = "failed_login_attempt")
    private Integer failedLoginAttempt;

    @Column(name = "last_logon_date")
    private Date lastLogonDate;

    @Column(name = "password_changed_flag", length = 1)
    private String passwordChangedFlag;

    @Column(name = "password_expiry_date")
    private Date passwordExpiryDate;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_pwd", updatable = false)
    private String userPwd;

    @Column(name = "password_change_flag")
    private String passwordChangeFlag;

    @Column(name = "create_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;

}